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

import java.util.ArrayList;
import java.util.List;

public class ElderlyDao {
    private final Elderly elderly;
    private final FeedEntry.DBHelpers db;
    private HelperElderly helperElderly;
    private static final String TAG = "FilmLog";

    public ElderlyDao(Context ctx, Elderly elderly) {
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
}
