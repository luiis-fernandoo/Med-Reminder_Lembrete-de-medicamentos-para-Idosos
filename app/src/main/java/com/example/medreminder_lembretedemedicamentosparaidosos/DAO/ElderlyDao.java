package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperElderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ElderlyDao {
    private final Elderly elderly;
    private final FeedEntry.DBHelpers db;
    private HelperElderly helperElderly;
    private static final String TAG = "FilmLog";
    private Context ctx;

    public ElderlyDao(Context ctx, Elderly elderly) {
        this.ctx = ctx;
        this.elderly = elderly;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertNewElderly(){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("email", elderly.getEmail());
        values.put("name", elderly.getName());
        values.put("password", elderly.getPassword());
        values.put("photo_profile", elderly.getProfile_photo());
        values.put("age", elderly.getAge());
        values.put("cuidador_id", elderly.getCuidador_id() != 0 ? elderly.getCuidador_id() : null);
        long resultado = dbLite.insert("elderly", null, values);

        return resultado != -1;
    }

    public boolean VerifyLogin(){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();

        String sql = "SELECT * FROM elderly where email = ? AND password = ?";
        Cursor cursor = dbLite.rawQuery(sql, new String[]{elderly.getEmail(), elderly.getPassword()});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean VerifyGuest(){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();

        String sql = "SELECT * FROM elderly where name = ?";
        Cursor cursor = dbLite.rawQuery(sql, new String[]{elderly.getName()});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    @SuppressLint("Range")
    public List<Elderly> getElderlyByCareviger(int cuidador_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Elderly> elderlies = new ArrayList<>();
        String sql = "Select * From elderly Where cuidador_id = ? ;";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cuidador_id)});

        while (cursor.moveToNext()) {
            Elderly elderly = new Elderly();
            elderly.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            elderly.setName(cursor.getString(cursor.getColumnIndex("name")));
            elderly.setProfile_photo(cursor.getString(cursor.getColumnIndex("photo_profile")));
            elderly.setAge(cursor.getString(cursor.getColumnIndex("age")));
            elderly.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
            elderlies.add(elderly);
        }

        cursor.close();
        db.close();
        return elderlies;
    }

    @SuppressLint("Range")
    public boolean verifyElderlyExists(String name, int cuidador_id) {
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From elderly Where name = '"+ name +"' AND cuidador_id = '"+ cuidador_id +"';";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    @SuppressLint("Range")
    public Elderly getElderlyByEmail(String email){
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From elderly Where email = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{email});
        Elderly elderly = new Elderly();

        if(cursor.moveToFirst()){
            elderly.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            elderly.setName(cursor.getString(cursor.getColumnIndex("name")));
            elderly.setProfile_photo(cursor.getString(cursor.getColumnIndex("photo_profile")));
            elderly.setAge(cursor.getString(cursor.getColumnIndex("age")));
            elderly.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
        }

        cursor.close();
        db.close();
        return elderly;
    }

    @SuppressLint("Range")
    public Elderly getElderlyByName(String name){
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From elderly Where name = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        Elderly elderly = new Elderly();

        if(cursor.moveToFirst()){
            elderly.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            elderly.setName(cursor.getString(cursor.getColumnIndex("name")));
            elderly.setProfile_photo(cursor.getString(cursor.getColumnIndex("photo_profile")));
            elderly.setAge(cursor.getString(cursor.getColumnIndex("age")));
            elderly.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
        }

        cursor.close();
        db.close();
        return elderly;
    }

    @SuppressLint("Range")
    public Elderly getElderlyById(int _id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From elderly Where _id = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(_id)});
        Elderly elderly = new Elderly();

        if(cursor.moveToFirst()){
            elderly.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            elderly.setName(cursor.getString(cursor.getColumnIndex("name")));
            elderly.setProfile_photo(cursor.getString(cursor.getColumnIndex("photo_profile")));
            elderly.setAge(cursor.getString(cursor.getColumnIndex("age")));
            elderly.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
        }

        cursor.close();
        db.close();
        return elderly;
    }


    public boolean deleteElderly(int idoso_id){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            long resultado = dbLite.delete("elderly", "_id = ?", new String[]{String.valueOf(idoso_id)});
            ReminderDao reminderDao = new ReminderDao(this.ctx, new Reminder());
            reminderDao.deleteReminderByElderly(idoso_id);
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela elderly: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteElderlyByCaregiver(int cuidador_id){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();

            long resultado = dbLite.delete("elderly", "cuidador_id = ?", new String[]{String.valueOf(cuidador_id)});
            ReminderDao reminderDao = new ReminderDao(this.ctx, new Reminder());
            reminderDao.deleteReminderByCaregiver(cuidador_id);
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela elderly: " + e.getMessage());
            return false;
        }
    }

    public boolean updateElderly(Elderly elderly){
        SQLiteDatabase db = this.db.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("name", elderly.getName());
            values.put("age", elderly.getAge());
            String whereClause = "_id = ?";
            String[] whereArgs = {String.valueOf(elderly.get_id())};

            long resultado = db.update("elderly", values, whereClause, whereArgs);
            db.close();

            return resultado != -1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePhoto(Elderly elderly, String photo){
        SQLiteDatabase db = this.db.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("photo_profile", photo);
            String whereClause = "_id = ?";
            String[] whereArgs = {String.valueOf(elderly.get_id())};

            long resultado = db.update("elderly", values, whereClause, whereArgs);
            db.close();

            return resultado != -1;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Error", e.getMessage());
            return false;
        }
    }
}
