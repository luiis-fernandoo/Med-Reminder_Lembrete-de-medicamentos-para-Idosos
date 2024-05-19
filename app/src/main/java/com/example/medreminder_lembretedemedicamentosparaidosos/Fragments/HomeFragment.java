package com.example.medreminder_lembretedemedicamentosparaidosos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.ChoiceElderlyActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.SearchMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.HomeAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.HomeCaregiverAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast.MyWorker;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ElderlyCaregiver elderlyCaregiver;
    private ReminderDao reminderDao;
    private Elderly elderly, guestElderly;
    private ElderlyDao elderlyDao;
    private TextView currentDay, textMedicinesForToday;
    private SharedPreferences sp;
    private List<Reminder> reminders;
    private Button buttonAddMedicine;
    private String selectedUserType;
    private RecyclerView recycleHome;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = requireContext().getPackageName();
            PowerManager pm = (PowerManager) requireContext().getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                requireContext().startActivity(intent);
            }
        }

        currentDay = view.findViewById(R.id.currentDay);
        textMedicinesForToday = view.findViewById(R.id.textMedicinesForToday);
        recycleHome = view.findViewById(R.id.recycleReminder);

        SimpleDateFormat currentDayBar = new SimpleDateFormat("EEEE, dd 'de' MMMM", new Locale("pt", "BR"));
        Date currentDate = new Date();
        String formattedDate = currentDayBar.format(currentDate);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", new Locale("pt", "BR"));
        String dayOfWeek = dayOfWeekFormat.format(calendar.getTime());

        currentDay.setText(formattedDate);
        textMedicinesForToday.setText(R.string.medicines_today);

        sp = requireContext().getSharedPreferences("app", Context.MODE_PRIVATE);

        selectedUserType = sp.getString("selectedUserType", "");
        reminderDao = new ReminderDao(requireContext(), new Reminder());
        if(selectedUserType.equals("Idoso")){
            elderlyDao = new ElderlyDao(requireContext(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                guestElderly = elderlyDao.getElderlyByName("Convidado");
                reminders = reminderDao.getAllRemindersByHome(guestElderly.get_id(), dayOfWeek);
            }else{
                elderly = elderlyDao.getElderlyByEmail(sp.getString("email", ""));
                reminders = reminderDao.getAllRemindersByHome(elderly.get_id(), dayOfWeek);
            }

            if(reminders.size() != 0){
                HomeAdapter homeAdapter = new HomeAdapter(reminders, requireContext(), sp);
                recycleHome.setLayoutManager(new LinearLayoutManager(requireContext()));
                recycleHome.setAdapter(homeAdapter);
            }else{
                View viewEmpty = inflater.inflate(R.layout.fragment_home_empty, container, false);
                currentDay = viewEmpty.findViewById(R.id.currentDay);
                currentDay.setText(formattedDate);
                textMedicinesForToday = viewEmpty.findViewById(R.id.textMedicinesForToday);
                textMedicinesForToday.setText(R.string.medicines_today);
                buttonAddMedicine = viewEmpty.findViewById(R.id.buttonAddMedicine);
                buttonAddMedicine.setText(R.string.add_medicine);

                buttonAddMedicine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SearchMedicineActivity.class);
                        startActivity(intent);
                    }
                });
                return viewEmpty;
            }
        }else{
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(requireContext(), new ElderlyCaregiver());
            elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(sp.getString("email", ""));
            reminders = reminderDao.getAllRemindersForHomeByCaregiver(elderlyCaregiver.get_id(), dayOfWeek);
            if(reminders.size() != 0){
                HomeCaregiverAdapter homeCaregiverAdapter = new HomeCaregiverAdapter(reminders, requireContext(), sp);
                recycleHome.setLayoutManager(new LinearLayoutManager(requireContext()));
                recycleHome.setAdapter(homeCaregiverAdapter);
            }else{
                View viewEmpty = inflater.inflate(R.layout.fragment_home_empty, container, false);
                currentDay = viewEmpty.findViewById(R.id.currentDay);
                currentDay.setText(formattedDate);

                buttonAddMedicine = viewEmpty.findViewById(R.id.buttonAddMedicine);

                buttonAddMedicine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ChoiceElderlyActivity.class);
                        startActivity(intent);
                    }
                });
                return viewEmpty;
            }
        }

        return view;
    }
}