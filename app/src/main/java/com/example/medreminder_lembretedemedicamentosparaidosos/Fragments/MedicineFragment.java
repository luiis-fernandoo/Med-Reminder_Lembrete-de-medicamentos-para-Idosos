package com.example.medreminder_lembretedemedicamentosparaidosos.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.MedicineAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonHelp;
    private ElderlyCaregiver elderlyCaregiver;
    private ReminderDao reminderDao;
    private MedicineDao medicineDao;
    private Elderly elderly, guestElderly;
    private ElderlyDao elderlyDao;
    private Medicine medicine;
    private SharedPreferences sp;
    private List<Reminder> reminders;
    private List<Medicine> medicines = new ArrayList<>();
    private String selectedUserType;
    private RecyclerView recycleMedicine;
    private TextView textMedicines;

    public MedicineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineFragment newInstance(String param1, String param2) {
        MedicineFragment fragment = new MedicineFragment();
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
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);

        recycleMedicine = view.findViewById(R.id.recycleReminder);
        textMedicines = view.findViewById(R.id.textMedicines);
        textMedicines.setText(R.string.medicines);
        buttonHelp = view.findViewById(R.id.buttonHelp);

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelp();
            }
        });

        sp = requireContext().getSharedPreferences("app", Context.MODE_PRIVATE);

        selectedUserType = sp.getString("selectedUserType", "");
        reminderDao = new ReminderDao(requireContext(), new Reminder());
        if(selectedUserType.equals("Idoso")){
            elderlyDao = new ElderlyDao(requireContext(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                guestElderly = elderlyDao.getElderlyByName("Convidado");
                reminders = reminderDao.getAllRemindersByElderlyForMedicine(guestElderly.get_id());
                if(reminders != null){
                    for(int i=0; i<reminders.size(); i++){
                        medicineDao = new MedicineDao(requireContext(), new Medicine(reminders.get(i).getMedicamento_id()));
                        medicine = medicineDao.getMedicineByProcessNumber();
                        medicines.add(medicine);
                    }
                }
            }else{
                elderly = elderlyDao.getElderlyByEmail(sp.getString("email", ""));
                reminders = reminderDao.getAllRemindersByElderlyForMedicine(elderly.get_id());
                if(reminders != null){
                    for(int i=0; i<reminders.size(); i++){
                        medicineDao = new MedicineDao(requireContext(), new Medicine(reminders.get(i).getMedicamento_id()));
                        medicine = medicineDao.getMedicineByProcessNumber();
                        medicines.add(medicine);
                    }
                }
            }

            if(medicines != null){
                MenuActivity menuActivity = (MenuActivity) getActivity();
                MedicineAdapter medicineAdapter = new MedicineAdapter(medicines, requireContext(), sp, menuActivity);
                recycleMedicine.setLayoutManager(new LinearLayoutManager(requireContext()));
                recycleMedicine.setAdapter(medicineAdapter);
            }
        }else{
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(requireContext(), new ElderlyCaregiver());
            elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(sp.getString("email", ""));
            reminders = reminderDao.getAllRemindersByCaregiverForMedicine(elderlyCaregiver.get_id());
            if(reminders != null){
                for(int i=0; i<reminders.size(); i++){
                    medicineDao = new MedicineDao(requireContext(), new Medicine(reminders.get(i).getMedicamento_id()));
                    medicine = medicineDao.getMedicineByProcessNumber();
                    medicines.add(medicine);
                }
            }

            if(medicines != null){
                MenuActivity menuActivity = (MenuActivity) getActivity();
                MedicineAdapter medicineAdapter = new MedicineAdapter(medicines, requireContext(), sp, menuActivity);
                recycleMedicine.setLayoutManager(new LinearLayoutManager(requireContext()));
                recycleMedicine.setAdapter(medicineAdapter);
            }
        }
        return view;
    }

    public void popupHelp(){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.popup_help, null);

        TextView textHelp = popupView.findViewById(R.id.textHelp);
        textHelp.setText(R.string.textHelpMedicine);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button buttonOk = popupView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}