package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperMedicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicineDao {
    private final Medicine medicine;
    private final FeedEntry.DBHelpers db;
    private HelperMedicine helperMedicine;
    private static final String TAG = "FilmLog";

    public MedicineDao(Context ctx, Medicine medicine) {
        this.medicine = medicine;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertNewReminder(JSONObject medicine){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("product_id", medicine.getString("idProduto"));
            values.put("number_register", medicine.getString("numeroRegistro"));
            values.put("product_name", medicine.getString("nomeProduto"));
            values.put("office_hour", medicine.getString("expediente"));
            values.put("corporation_reason", medicine.getString("razaoSocial"));
            values.put("cnpj", medicine.getString("cnpj"));
            values.put("trasaction_number", medicine.getString("numeroTransacao"));
            values.put("date", medicine.getString("data"));
            values.put("process_number", medicine.getString("numProcesso"));
            values.put("id_protected_patient_leaflet", medicine.getString("idBulaPacienteProtegido"));
            values.put("id_protected_professional_leaflet", medicine.getString("idBulaProfissionalProtegido"));

            long resultado = dbLite.insert("medicine", null, values);

            return resultado != -1;
        }catch (Exception error){
            Log.d("Error", "Error: " + error);
            return false;
        }
    }

    @SuppressLint("Range")
    public Medicine getMedicineByProcessNumber(){
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From medicine Where process_number = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(medicine.getProcess_number())});
        Medicine medicine = new Medicine();

        if(cursor.moveToFirst()){
            medicine.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            medicine.setProcess_number(cursor.getString(cursor.getColumnIndex("process_number")));
            medicine.setProduct_name(cursor.getString(cursor.getColumnIndex("product_name")));
        }

        cursor.close();
        db.close();
        return medicine;
    }

    @SuppressLint("Range")
    public List<Medicine> getAllMedicinesByProcessNumber(String processNumber){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Medicine> medicines = new ArrayList<>();
        String sql = "Select * From medicine Where number_process = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{processNumber});

        while (cursor.moveToNext()) {
            Medicine medicine = new Medicine();

            medicine.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            medicine.setProcess_number(cursor.getString(cursor.getColumnIndex("idoso_id")));
            medicine.setProduct_name(cursor.getString(cursor.getColumnIndex("cuidador_id")));

            medicines.add(medicine);
        }

        cursor.close();
        db.close();

        return medicines;
    }

    public boolean deleteMedicine(String processNumber){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            long resultado = dbLite.delete("medicine", "number_process = ?", new String[]{processNumber});
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela Medicine: " + e.getMessage());
            return false;
        }
    }
}
