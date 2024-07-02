package com.example.medreminder_lembretedemedicamentosparaidosos.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
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
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private LinearLayout buttonOk;
    private SharedPreferences sp;
    private TextView nameProfile, ageProfile, qtdMedicines, qtdReminders, typeUser,youElderlys, nameEditPop, ageEditPop, textWarning;
    private Button buttonLogout, buttonDeleteProfile, buttonConfirm, buttonCancel, imageEdit;
    private ImageView imageProfile, iconAddElderly;
    private RecyclerView recycleElderly;
    private String selectedUserType, currentPhotoPath;
    private Uri selectedImageUri;
    private ElderlyDao elderlyDao;
    private ElderlyCaregiverDao elderlyCaregiverDao;
    private ElderlyCaregiver elderlyCaregiver;
    private ReminderDao reminderDao;
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

        elderlyCaregiverDao = new ElderlyCaregiverDao(requireContext(), new ElderlyCaregiver());
        elderlyDao = new ElderlyDao(requireContext(), new Elderly());
        reminderDao = new ReminderDao(requireContext(), new Reminder());

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
            qtdMedicines = viewElderly.findViewById(R.id.qtdMedicines);
            qtdReminders = viewElderly.findViewById(R.id.qtdReminders);

            buttonDeleteProfile = viewElderly.findViewById(R.id.buttonDeleteProfile);
            buttonDeleteProfile.setText(R.string.account_delete);

            if(sp.getString("Guest", "").equals("Convidado")){
                elderly = elderlyDao.getElderlyByName("Convidado");
            }else{
                String email = sp.getString("email", "");
                elderly = elderlyDao.getElderlyByEmail(email);
            }
            nameProfile.setText(elderly.getName());
            if(elderly.getAge() != null){
                ageProfile.setText(requireContext().getString(R.string.age) + ": " + elderly.getAge());
            }else{
                ageProfile.setText(requireContext().getString(R.string.age) + ": ");
            }

            if(elderly.getProfile_photo()!=null){
                Glide.with(viewElderly.getContext())
                        .load(elderly.getProfile_photo())
                        .into(imageProfile);
            }

            String qtdTreatment = reminderDao.getQtdTreatment(elderly.get_id());
            String qtdReminder = reminderDao.getQtdReminders(elderly.get_id());

            qtdReminders.setText(qtdReminder);
            qtdMedicines.setText(qtdTreatment);

            imageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            });

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewElderly) {
                    popupWarningLogoutAccount();
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

            elderlyList = elderlyDao.getElderlyByCareviger(elderlyCaregiver.get_id());
            MenuActivity menuActivity = (MenuActivity) getActivity();
            ChoiceElderlyAdapter choiceElderlyAdapter = new ChoiceElderlyAdapter(elderlyList, requireActivity(), sp, menuActivity);
            recycleElderly.setLayoutManager(new LinearLayoutManager(requireContext()));
            recycleElderly.setAdapter(choiceElderlyAdapter);

            imageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            });

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewElderly) {
                    popupWarningLogoutAccount();
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
                    if(inputNameEdit.getText().toString().equals("Convidado")){
                        Toast.makeText(requireContext(), "Usuários sem cadastro não podem editar suas informações!", Toast.LENGTH_SHORT).show();
                    }else{
                        elderly.setName(inputNameEdit.getText().toString());
                        elderly.setAge(inputAgeEdit.getText().toString());

                        if (elderlyDao.updateElderly(elderly)) {
                            Toast.makeText(requireContext(), "Usuário editado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Erro ao editar usuário.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    elderlyCaregiver.setName(inputNameEdit.getText().toString());
                    elderlyCaregiver.setAge(inputAgeEdit.getText().toString());

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

        SharedPreferences sp = requireContext().getSharedPreferences("app", Context.MODE_PRIVATE);
        String guest = sp.getString("Guest", "");

        if(!guest.equals("")){
            ElderlyDao elderlyDao = new ElderlyDao(requireContext(), new Elderly());
            Elderly elderly = elderlyDao.getElderlyByName("Convidado");
            elderlyDao.deleteElderly(elderly.get_id());
        }
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
                    if(elderlyCaregiverDao.deleteCaregiver(elderlyCaregiver.get_id())){
                        deleteAccountFirebase();
                        logoutUser();
                        Toast.makeText(getActivity(), "Usuário apagado com sucesso", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Erro ao apagar usuário", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(elderlyDao.deleteElderly(elderly.get_id())){
                        deleteAccountFirebase();
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imageProfile.setVisibility(View.VISIBLE);
                    imageProfile.setImageURI(selectedImageUri);
                    saveImageToInternalStorage(selectedImageUri);
                } else {
                    Toast.makeText(requireContext(), "Selecione uma imagem válida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Seleção de imagem cancelada ou falhou", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToInternalStorage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            File directory = new File(getContext().getFilesDir() + "/MedReminder");
            if (!directory.exists()) {
                directory.mkdir();
            }
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timeStamp + ".jpg";
            File file = new File(directory, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
            currentPhotoPath = file.getAbsolutePath();

            if(selectedUserType.equals("Idoso")) {
                if (elderlyDao.updatePhoto(elderly, currentPhotoPath)) {
                    Toast.makeText(requireContext(), "Foto atualizada com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(elderlyCaregiverDao.updatePhoto(elderlyCaregiver, currentPhotoPath)){
                    Toast.makeText(requireContext(), "Foto atualizada com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAccountFirebase()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("User delete", "User data deleted from Realtime Database.");
                                }else{
                                    Log.d("User delete", "Failed to delete user data from Realtime Database.");
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    public void popupWarningLogoutAccount(){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.popup_warnings_delete, null);

        textWarning = popupView.findViewById(R.id.textWarning);
        textWarning.setText(R.string.textWarningLogout);
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
                logoutUser();
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