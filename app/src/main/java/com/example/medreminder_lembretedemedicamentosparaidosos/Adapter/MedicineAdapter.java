package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.DetailsMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.MedicineFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Fragments.ReminderFragment;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {
    private Context context;
    private List<Medicine> medicines;
    private MenuActivity menuActivity;
    private Button buttonOK;
    private TextView textWarning;
    private SharedPreferences sp;
    private List<Reminder> reminders;
    public MedicineAdapter(List<Medicine> medicines, Context context, SharedPreferences sp, MenuActivity menuActivity, List<Reminder> reminders) {
        this.medicines = medicines;
        this.context = context;
        this.sp = sp;
        this.menuActivity = menuActivity;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public MedicineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medice_layout, parent, false);
        return new MedicineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.MyViewHolder holder, int position) {
        if (position < medicines.size()) {
            Medicine medicine = medicines.get(position);
            holder.bind(medicine, this.context);
        } else {
            Log.e("Adapter", "Índice fora dos limites: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBox, imagePill;
        TextView textNameMedicine, qtdForWarning, remainingPills;
        LinearLayout clickToNext;
        Button buttonRepository, buttonDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBox = itemView.findViewById(R.id.imageBox);
            imagePill = itemView.findViewById(R.id.imagePill);
            textNameMedicine = itemView.findViewById(R.id.textNameMedicine);
            clickToNext = itemView.findViewById(R.id.clickToNext);
            buttonRepository = itemView.findViewById(R.id.buttonRepository);
            buttonDetails = itemView.findViewById(R.id.buttonDetails);
            qtdForWarning = itemView.findViewById(R.id.qtdForWarning);
            remainingPills = itemView.findViewById(R.id.remainingPills);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Medicine medicine, Context context) {
            for (int i = 0; i < reminders.size(); i++){
                if(Objects.equals(reminders.get(i).getMedicamento_id(), medicine.getProcess_number())){
                    ReminderDao reminderDao = new ReminderDao(context, new Reminder());
                    Reminder reminder = reminderDao.getReminderById(reminders.get(i).get_id());

                    textNameMedicine.setText(medicine.getProduct_name());
                    if(reminder.getType_medicine().equals("pill")){
                        if(reminder.getRemaining()!= null){
                            if(Integer.parseInt(reminder.getRemaining()) <= Integer.parseInt(reminder.getWarning())){
                                popupWarningRemaining(medicine);
                            }
                            if(reminder.getWarning()!= null){
                                qtdForWarning.setText(context.getString(R.string.quantity_for_notice)+": " + reminder.getWarning());
                            }
                            if(reminder.getRemaining()!= null){
                                remainingPills.setText(context.getString(R.string.remaining_pills)+": " + reminder.getRemaining());
                            }
                        }
                    }else{
                        remainingPills.setText("Sem dados.");
                        qtdForWarning.setText("Não é possível contabilizar.");
                    }

                    if(reminder.getPhoto_medicine_box()!=null){
                        Glide.with(itemView.getContext())
                                .load(reminder.getPhoto_medicine_box())
                                .into(imageBox);
                    }
                    if(reminder.getPhoto_medicine_pill()!=null){
                        Glide.with(itemView.getContext())
                                .load(reminder.getPhoto_medicine_pill())
                                .into(imagePill);
                    }
                    buttonDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it = new Intent(context, DetailsMedicineActivity.class);
                            it.putExtra("numProcesso", medicine.getProcess_number());
                            context.startActivity(it);
                        }
                    });


                    buttonRepository.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_edit(reminder);
                        }
                    });
                }
            }
        }
    }

    public void popup_edit(Reminder reminder){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.pop_up_edit_qtd_pills, null);

        EditText remainingInput = popupView.findViewById(R.id.remainingInput);
        EditText warningInput = popupView.findViewById(R.id.warningInput);
        warningInput.setText(reminder.getWarning());
        remainingInput.setText(reminder.getRemaining());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(popupView);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        LinearLayout buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reminder.getType_medicine().equals("pill")){
                    String remaining = remainingInput.getText().toString();
                    reminder.setRemaining(remaining);
                    String warning = warningInput.getText().toString();
                    reminder.setWarning(warning);

                    ReminderDao reminderDao = new ReminderDao(context, reminder);
                    if(reminder.getEveryday().equals("S")){
                        if (reminderDao.updateReminderRemainingById(reminder)) {
                            Toast.makeText(context, "Lembrete atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            menuActivity.replaceFragment(new MedicineFragment());
                        }else{
                            Toast.makeText(context, "Erro ao atualizar lembrete!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (reminderDao.updateReminderRemaining(reminder)) {
                            Toast.makeText(context, "Lembrete atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            menuActivity.replaceFragment(new MedicineFragment());
                        }else{
                            Toast.makeText(context, "Erro ao atualizar lembrete!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    alertDialog.dismiss();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void popupWarningRemaining(Medicine medicine){
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_warnings_remainings, null);

        textWarning = popupView.findViewById(R.id.textWarning);
        textWarning.setText(medicine.getProduct_name() + " " + context.getString(R.string.is_running_out));
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(popupView);

        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOK = popupView.findViewById(R.id.buttonOk);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
