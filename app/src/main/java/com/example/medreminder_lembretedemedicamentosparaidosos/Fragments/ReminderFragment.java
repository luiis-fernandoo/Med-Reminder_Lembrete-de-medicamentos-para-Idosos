package com.example.medreminder_lembretedemedicamentosparaidosos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.ChoiceElderlyActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.SearchMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.HomeAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.HomeCaregiverAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.ReminderAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sp;
    private String selectedUserType;
    private ReminderDao reminderDao;
    private ElderlyDao elderlyDao;
    private Elderly elderly, guestElderly;
    private List<Reminder> reminders;
    private RecyclerView recycleReminder;
    private ElderlyCaregiver elderlyCaregiver;
    private ImageView iconAddMedicine;
    private TextView all_you_reminders;
    public ReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderFragment newInstance(String param1, String param2) {
        ReminderFragment fragment = new ReminderFragment();
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
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        recycleReminder = view.findViewById(R.id.recycleReminder);
        iconAddMedicine = view.findViewById(R.id.iconAddMedicine);
        all_you_reminders = view.findViewById(R.id.all_you_reminders);
        all_you_reminders.setText(R.string.all_your_reminders);

        sp = requireContext().getSharedPreferences("app", Context.MODE_PRIVATE);

        selectedUserType = sp.getString("selectedUserType", "");
        reminderDao = new ReminderDao(requireContext(), new Reminder());
        if(selectedUserType.equals("Idoso")){
            elderlyDao = new ElderlyDao(requireContext(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                guestElderly = elderlyDao.getElderlyByName("Convidado");
                reminders = reminderDao.getAllRemindersByReminder(guestElderly.get_id());
            }else{
                elderly = elderlyDao.getElderlyByEmail(sp.getString("email", ""));
                reminders = reminderDao.getAllRemindersByReminder(elderly.get_id());
            }

            if(reminders != null){
                MenuActivity menuActivity = (MenuActivity) getActivity();
                ReminderAdapter reminderAdapter = new ReminderAdapter(reminders, requireContext(), sp, menuActivity);
                recycleReminder.setLayoutManager(new LinearLayoutManager(requireContext()));
                recycleReminder.setAdapter(reminderAdapter);
            }

            iconAddMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireContext(), SearchMedicineActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(requireContext(), new ElderlyCaregiver());
            elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(sp.getString("email", ""));
            reminders = reminderDao.getAllRemindersForReminderByCaregiver(elderlyCaregiver.get_id());

            if(reminders != null){
                MenuActivity menuActivity = (MenuActivity) getActivity();
                ReminderAdapter reminderAdapter = new ReminderAdapter(reminders, requireContext(), sp, menuActivity);
                recycleReminder.setLayoutManager(new LinearLayoutManager(requireContext()));
                recycleReminder.setAdapter(reminderAdapter);
            }

            iconAddMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireContext(), ChoiceElderlyActivity.class);
                    startActivity(intent);
                }
            });
        }


        return view;
    }

}