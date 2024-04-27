package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperReminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        values.put("status", 0);
        values.put("remaining", reminder.getRemaining());
        values.put("warning", reminder.getWarning());
        values.put("photo_medicine_box", reminder.getPhoto_medicine_box());
        values.put("photo_medicine_pill", reminder.getPhoto_medicine_pill());

        long resultado = dbLite.insert("reminder", null, values);

        return resultado != -1;
    }

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByElderly(int idoso_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "Select * From reminder Where idoso_id = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idoso_id)});

        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            if (dateString != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(dateString);
                    specificReminder.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            specificReminder.setEveryday(cursor.getString(cursor.getColumnIndex("everyday")));
            specificReminder.setDayOfWeek(cursor.getString(cursor.getColumnIndex("day_of_week")));
            specificReminder.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
            specificReminder.setRemaining(cursor.getString(cursor.getColumnIndex("remaining")));
            specificReminder.setTime(cursor.getString(cursor.getColumnIndex("time")));
            specificReminder.setWarning(cursor.getString(cursor.getColumnIndex("warning")));
            specificReminder.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            specificReminder.setType_medicine(cursor.getString(cursor.getColumnIndex("type_medicine")));
            specificReminder.setPhoto_medicine_box(cursor.getString(cursor.getColumnIndex("photo_medicine_box")));
            specificReminder.setPhoto_medicine_pill(cursor.getString(cursor.getColumnIndex("photo_medicine_pill")));

            reminders.add(specificReminder);
        }

        cursor.close();
        db.close();

        return reminders;
    }
}
