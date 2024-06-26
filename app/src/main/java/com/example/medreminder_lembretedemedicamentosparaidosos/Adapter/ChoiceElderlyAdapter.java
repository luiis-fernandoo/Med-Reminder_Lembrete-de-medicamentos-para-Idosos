package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.SearchMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ProfileFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ReminderFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.List;

public class ChoiceElderlyAdapter extends RecyclerView.Adapter<ChoiceElderlyAdapter.MyViewHolder> {

    private Activity activity;
    private List<Elderly> elderlies;
    private SharedPreferences sp;
    private MenuActivity menuActivity;
    public ChoiceElderlyAdapter(List<Elderly> elderlies, Activity activity, SharedPreferences sp, MenuActivity menuActivity){
        this.elderlies = elderlies;
        this.activity = activity;
        this.sp = sp;
        this.menuActivity = menuActivity;
    }

    @NonNull
    @Override
    public ChoiceElderlyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.recycle_elderlys_layout, parent, false);
        return new ChoiceElderlyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position < elderlies.size()) {
            Log.d("", "Size: " + elderlies.size());
            Elderly elderly = elderlies.get(position);
            holder.bind(elderly, this.activity);
        } else {
            Log.e("Adapter", "Índice fora dos limites: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return elderlies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout clickToNext;
        ImageView imageElderlyChoice;
        TextView textNameElderly, textAgeElderly;
        Button buttonDeleteElderly;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageElderlyChoice = itemView.findViewById(R.id.imageBox);
            textNameElderly = itemView.findViewById(R.id.textNameElderly);
            textAgeElderly = itemView.findViewById(R.id.textAgeElderly);
            clickToNext = itemView.findViewById(R.id.clickToNext);
            buttonDeleteElderly = itemView.findViewById(R.id.buttonDeleteElderly);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Elderly elderly, Context context) {
            textNameElderly.setText(context.getString(R.string.name) + ": " + elderly.getName());
            textAgeElderly.setText(context.getString(R.string.age) + ": " + elderly.getAge() + " " + context.getString(R.string.years_old));
            if (elderly.getProfile_photo() != null) {
                Glide.with(itemView.getContext())
                        .load(elderly.getProfile_photo())
                        .into(imageElderlyChoice);
            }
            clickToNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("chosenElderlyById", elderly.get_id());
                    editor.apply();
                    Intent it = new Intent(context, SearchMedicineActivity.class);
                    context.startActivity(it);
                }
            });

            buttonDeleteElderly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWarningDelete(elderly, context);
                }
            });
        }
    }

    public void popupWarningDelete(Elderly elderly, Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_warnings_delete, null);

        TextView textWarning = popupView.findViewById(R.id.textWarning);
        textWarning.setText(R.string.deleteElderly);
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button buttonConfirm = popupView.findViewById(R.id.buttonConfirm);
        buttonConfirm.setText(R.string.confirm);

        Button buttonCancel = popupView.findViewById(R.id.buttonCancel);
        buttonCancel.setText(R.string.cancel);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ElderlyDao elderlyDao = new ElderlyDao(context, new Elderly());
                elderlyDao.deleteElderly(elderly.get_id());
                alertDialog.dismiss();
                menuActivity.replaceFragment(new ProfileFragment());
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
