package com.example.medreminder_lembretedemedicamentosparaidosos.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.LoginActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.RegisterElderlyActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Adapter.ChoiceElderlyAdapter;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Interface.PopupInterface;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Elderly elderly;
    private List<Elderly> elderlyList;
    private ElderlyCaregiver elderlyCaregiver;
    private LinearLayout buttonOk;
    private SharedPreferences sp;
    private TextView nameProfile, ageProfile, textInfoMedicine, textInfoReminder,typeUser,youElderlys, nameEditPop, ageEditPop, textWarning;
    private Button buttonLogout, buttonDeleteProfile, buttonConfirm, buttonCancel;
    private ImageView imageProfile, imageEdit, iconAddElderly;
    private RecyclerView recycleElderly;
    private String selectedUserType;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        sp = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        selectedUserType = sp.getString("selectedUserType", "");

        if(selectedUserType.equals("Idoso")) {
            View viewElderly = inflater.inflate(R.layout.fragment_profile_elderly, container, false);
            typeUser = viewElderly.findViewById(R.id.typeUser);
            typeUser.setText(R.string.elderly);

            buttonLogout = viewElderly.findViewById(R.id.buttonLogout);
            buttonLogout.setText(R.string.exit);

            nameProfile = viewElderly.findViewById(R.id.nameProfile);
            ageProfile = viewElderly.findViewById(R.id.ageProfile);
            imageProfile = viewElderly.findViewById(R.id.imageProfile);
            imageEdit = viewElderly.findViewById(R.id.imageEdit);
            textInfoMedicine = viewElderly.findViewById(R.id.textInfoMedicine);
            textInfoReminder = viewElderly.findViewById(R.id.textInfoReminder);

            buttonDeleteProfile = viewElderly.findViewById(R.id.buttonDeleteProfile);
            buttonDeleteProfile.setText(R.string.account_delete);

            ElderlyDao elderlyDao = new ElderlyDao(getActivity(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                elderly = elderlyDao.getElderlyByName("Convidado");
            }else{
                String email = sp.getString("email", "");
                elderly = elderlyDao.getElderlyByEmail(email);
            }
            nameProfile.setText(elderly.getName());
            ageProfile.setText(requireContext().getString(R.string.age) + " " + elderly.getAge());
            if(elderly.getProfile_photo()!=null){
                Glide.with(viewElderly.getContext())
                        .load(elderly.getProfile_photo())
                        .into(imageProfile);
            }

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewElderly) {
                    logoutUser();
                }
            });

            buttonDeleteProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWarningDeleteAccount("idoso");
                }
            });

            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup_edit(elderly);
                }
            });
            return viewElderly;

        }else{
            View viewCaregiver = inflater.inflate(R.layout.fragment_profile_caregiver, container, false);

            buttonLogout = viewCaregiver.findViewById(R.id.buttonLogout);
            buttonLogout.setText(R.string.exit);

            nameProfile = viewCaregiver.findViewById(R.id.nameProfile);
            ageProfile = viewCaregiver.findViewById(R.id.ageProfile);
            imageProfile = viewCaregiver.findViewById(R.id.imageProfile);
            imageEdit = viewCaregiver.findViewById(R.id.imageEdit);

            buttonDeleteProfile = viewCaregiver.findViewById(R.id.buttonDeleteProfile);
            buttonDeleteProfile.setText(R.string.account_delete);

            iconAddElderly = viewCaregiver.findViewById(R.id.iconAddElderly);
            recycleElderly = viewCaregiver.findViewById(R.id.recycleElderly);

            youElderlys = viewCaregiver.findViewById(R.id.youElderlys);
            youElderlys.setText(R.string.your_elderly);

            sp = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getActivity(), new ElderlyCaregiver());
            elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(sp.getString("email", ""));

            nameProfile.setText(elderlyCaregiver.getName());
            if(elderlyCaregiver.getAge() != null){
                ageProfile.setText(requireContext().getString(R.string.age) + ": " + elderlyCaregiver.getAge());
            }else{
                ageProfile.setText(requireContext().getString(R.string.age) + ": ");
            }
            if(elderlyCaregiver.getProfile_photo()!=null){
                Glide.with(viewCaregiver.getContext())
                        .load(elderlyCaregiver.getProfile_photo())
                        .into(imageProfile);
            }

            ElderlyDao elderlyDao = new ElderlyDao(requireContext(), new Elderly());
            elderlyList = elderlyDao.getElderlyByCareviger(elderlyCaregiver.get_id());

            ChoiceElderlyAdapter choiceElderlyAdapter = new ChoiceElderlyAdapter(elderlyList, requireActivity(), sp);
            recycleElderly.setLayoutManager(new LinearLayoutManager(requireContext()));
            recycleElderly.setAdapter(choiceElderlyAdapter);

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewElderly) {
                    logoutUser();
                }
            });

            buttonDeleteProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWarningDeleteAccount("Cuidador");
                }
            });

            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup_edit(elderlyCaregiver);
                }
            });

            iconAddElderly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(requireActivity(), RegisterElderlyActivity.class);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove("isFirstTime");
                    editor.apply();
                    startActivity(it);
                }
            });

            return viewCaregiver;
        }
    }

    public void popup_edit(PopupInterface popupInterface){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.pop_up_edit_profile, null);

        EditText inputNameEdit = popupView.findViewById(R.id.inputNameEdit);
        inputNameEdit.setHint(R.string.type_here);
        inputNameEdit.setText(popupInterface.getName());

        EditText inputAgeEdit = popupView.findViewById(R.id.inputAgeEdit);
        inputAgeEdit.setHint(R.string.type_here);
        inputAgeEdit.setText(popupInterface.getAge());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(popupView);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputNameEdit = popupView.findViewById(R.id.inputNameEdit);
                EditText inputAgeEdit = popupView.findViewById(R.id.inputAgeEdit);

                if(selectedUserType.equals("Idoso")){
                    elderly.setName(inputNameEdit.getText().toString());
                    elderly.setAge(inputAgeEdit.getText().toString());

                    ElderlyDao elderlyDao = new ElderlyDao(requireContext(), elderly);
                    if (elderlyDao.updateElderly(elderly)) {
                        Toast.makeText(requireContext(), "Usuário editado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Erro ao editar usuário.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    elderlyCaregiver.setName(inputNameEdit.getText().toString());
                    elderlyCaregiver.setAge(inputAgeEdit.getText().toString());

                    ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(requireContext(), elderlyCaregiver);
                    if (elderlyCaregiverDao.updateCaregiver(elderlyCaregiver)) {
                        Toast.makeText(requireContext(), "Usuário editado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Erro ao editar usuário.", Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
                replaceFragment(new ProfileFragment());
            }
        });
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sp.edit();

        editor.remove("email");
        editor.remove("profile_image_url");
        editor.remove("Guest");
        editor.remove("selectedUserType");
        editor.remove("name");
        editor.remove("isFirstTime");
        editor.remove("chosenElderly");
        editor.remove("chosenElderlyById");

        editor.apply();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);

        startActivity(intent);
        requireActivity().finish();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void popupWarningDeleteAccount(String userType){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.popup_warnings_delete, null);

        textWarning = popupView.findViewById(R.id.textWarning);
        textWarning.setText(R.string.textWarningDeleteAccount);
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonConfirm = popupView.findViewById(R.id.buttonConfirm);
        buttonConfirm.setText(R.string.confirm);

        buttonCancel = popupView.findViewById(R.id.buttonCancel);
        buttonCancel.setText(R.string.cancel);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals("Cuidador")){
                    ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(requireContext(), new ElderlyCaregiver());
                    if(elderlyCaregiverDao.deleteCaregiver(elderlyCaregiver.get_id())){
                        logoutUser();
                        Toast.makeText(getActivity(), "Usuário apagado com sucesso", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Erro ao apagar usuário", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    ElderlyDao elderlyDao = new ElderlyDao(requireContext(), new Elderly());
                    if(elderlyDao.deleteElderly(elderly.get_id())){
                        logoutUser();
                        Toast.makeText(getActivity(), "Usuário apagado com sucesso", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Erro ao apagar usuário", Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}