package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperReminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReminderDao {
    private final Reminder reminder;
    private final FeedEntry.DBHelpers db;
    private HelperReminder helperReminder;
    private static final String TAG = "FilmLog";

    public ReminderDao(Context ctx, Reminder reminder) {
        this.reminder = reminder;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertNewReminder(){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idoso_id", reminder.getIdoso_id());
        values.put("medicamento_id", reminder.getMedicamento_id());
        values.put("everyday", reminder.getEveryday());
        if(reminder.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateString = dateFormat.format(reminder.getDate());
            values.put("date", dateString);
        }
        values.put("type_medicine", reminder.getType_medicine());
        values.put("time", reminder.getTime());
        values.put("quantity", reminder.getQuantity());
        values.put("day_of_week", reminder.getDayOfWeek());
        values.put("remaining", reminder.getRemaining());
        values.put("warning", reminder.getWarning());
        values.put("photo_medicine_box", reminder.getPhoto_medicine_box());
        values.put("photo_medicine_pill", reminder.getPhoto_medicine_pill());

        long resultado = dbLite.insert("reminder", null, values);

        return resultado != -1;
    }
}
