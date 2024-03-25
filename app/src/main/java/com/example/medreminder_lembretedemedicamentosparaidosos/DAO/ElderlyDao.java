package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperElderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;

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
        if(elderly.getCuidador_id() != 0){
            values.put("age", Math.min(elderly.getAge(), 0));
            values.put("cuidador_id", elderly.getCuidador_id() != 0 ? elderly.getCuidador_id() : null);
        }
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

}
