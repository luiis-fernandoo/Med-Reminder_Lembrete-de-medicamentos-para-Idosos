package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;

public class ElderlyCaregiverDao {
    private final ElderlyCaregiver elderlyCaregiver;
    private final FeedEntry.DBHelpers db;
    private HelperElderlyCaregiver helperElderlyCaregiver;
    private static final String TAG = "FilmLog";
    private Context context;

    public ElderlyCaregiverDao(Context ctx, ElderlyCaregiver elderlyCaregiver) {
        this.elderlyCaregiver = elderlyCaregiver;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertNewElderlyCaregiver(){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("email", elderlyCaregiver.getEmail());
        values.put("name", elderlyCaregiver.getName());
        values.put("password", elderlyCaregiver.getPassword());
        values.put("photo_profile", elderlyCaregiver.getProfile_photo());

        long resultado = dbLite.insert("elderlyCaregiver", null, values);

        return resultado != -1;
    }

    public boolean VerifyLogin(){
        SQLiteDatabase dbLite = this.db.getReadableDatabase();

        String sql = "SELECT * FROM elderlyCaregiver where email = ? AND password = ?";
        Cursor cursor = dbLite.rawQuery(sql, new String[]{elderlyCaregiver.getEmail(), elderlyCaregiver.getPassword()});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    @SuppressLint("Range")
    public ElderlyCaregiver getElderlyCaregiver(String email){
        SQLiteDatabase dbLite = this.db.getReadableDatabase();

        String sql = "Select * From elderlyCaregiver Where email = '"+ email +"';";
        ElderlyCaregiver caregiver = new ElderlyCaregiver();
        Cursor cursor = dbLite.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            caregiver.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            caregiver.setName(cursor.getString(cursor.getColumnIndex("name")));
            caregiver.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            caregiver.setAge(cursor.getString(cursor.getColumnIndex("age")));
            caregiver.setProfile_photo(cursor.getString(cursor.getColumnIndex("photo_profile")));

        }
        cursor.close();
        db.close();

        return caregiver;
    }

    public boolean deleteCaregiver(int cuidador_id){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            long resultado = dbLite.delete("elderlyCaregiver", "_id = ?", new String[]{String.valueOf(cuidador_id)});
            ElderlyDao elderlyDao = new ElderlyDao(context, new Elderly());
            elderlyDao.deleteElderlyByCaregiver(cuidador_id);
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela Caregiver: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCaregiver(ElderlyCaregiver elderlyCaregiver){
        SQLiteDatabase db = this.db.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("name", elderlyCaregiver.getName());
            values.put("age", elderlyCaregiver.getAge());
            String whereClause = "_id = ?";
            String[] whereArgs = {String.valueOf(elderlyCaregiver.get_id())};

            long resultado = db.update("elderlyCaregiver", values, whereClause, whereArgs);
            db.close();

            return resultado != -1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
