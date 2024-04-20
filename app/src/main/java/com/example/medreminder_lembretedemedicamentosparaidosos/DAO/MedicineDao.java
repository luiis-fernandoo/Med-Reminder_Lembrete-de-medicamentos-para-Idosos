package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperMedicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
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
}
