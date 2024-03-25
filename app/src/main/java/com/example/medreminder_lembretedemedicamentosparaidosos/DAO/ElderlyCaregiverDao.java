package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;

public class ElderlyCaregiverDao {
    private final ElderlyCaregiver elderlyCaregiver;
    private final FeedEntry.DBHelpers db;
    private HelperElderlyCaregiver helperElderlyCaregiver;
    private static final String TAG = "FilmLog";

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
        SQLiteDatabase dbLite = this.db.getWritableDatabase();

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
}
