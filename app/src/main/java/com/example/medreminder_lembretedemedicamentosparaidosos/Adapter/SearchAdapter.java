package com.example.medreminder_lembretedemedicamentosparaidosos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.TypeMedicineActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    private final Activity activity;
    private final JSONArray medicineList;

    public SearchAdapter(Activity activity, JSONArray medicineList) {
        this.activity = activity;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.medicine_item_layout, parent, false);
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        try {
            if (position < medicineList.length()) {
                JSONObject medicine = medicineList.getJSONObject(position);
                holder.bind(medicine);
            } else {
                Log.e("Adapter", "Índice fora dos limites: " + position);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return medicineList.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView medicineTextId, secondInform;
        LinearLayout linearId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineTextId = itemView.findViewById(R.id.medicineTextId);
            secondInform = itemView.findViewById(R.id.secondInform);
            linearId = itemView.findViewById(R.id.linearId);
        }

        public void bind(JSONObject medicine) {
            try {
                String nomeProduto = medicine.getString("nomeProduto");
                String numProcesso = medicine.getString("numProcesso");
                String razaoSocial = medicine.getString("razaoSocial");
                medicineTextId.setText(nomeProduto);
                secondInform.setText(razaoSocial);
                linearId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Medicine medicineSelected = new Medicine(numProcesso);
                        MedicineDao medicineDao = new MedicineDao(activity, medicineSelected);
                        Medicine medicineVerify = medicineDao.getMedicineByProcessNumber();
                        if(medicineDao.insertNewReminder(medicine)){
                            Intent it = new Intent(activity, TypeMedicineActivity.class);
                            it.putExtra("medicine", numProcesso);
                            activity.startActivity(it);
                        }else{
                            Toast.makeText(activity, "Não foi possível cadastrar medicamento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
