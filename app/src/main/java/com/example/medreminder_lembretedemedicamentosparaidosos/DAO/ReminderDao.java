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
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
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
    private Context context;

    public ReminderDao(Context ctx, Reminder reminder) {
        this.reminder = reminder;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertNewReminder(){
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idoso_id", reminder.getIdoso_id());
        values.put("cuidador_id", reminder.getCuidador_id());
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
        String sql = "Select * From reminder Where idoso_id = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idoso_id)});

        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
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

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByCaregiver(int cuidador_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "Select * From reminder Where cuidador_id = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cuidador_id)});

        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
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

    public boolean setStatusAlarm(int status, int reminder_id){
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);

        int rowsAffected = db.update("reminder", values, "_id = ?", new String[]{String.valueOf(reminder_id)});
        db.close();

        return rowsAffected > 0;
    }

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByHome(int idoso_id, String dayOfWeek){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE idoso_id = ? AND (everyday = 'S' OR date(date) = date(datetime('now', 'localtime')) OR day_of_week = ?) ORDER BY time ASC;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idoso_id), dayOfWeek});
        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            if (dateString != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersForHomeByCaregiver(int cuidador_id, String dayOfWeek){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE cuidador_id = ? AND (everyday = 'S' OR date(date) = date('now') OR day_of_week = ?) ORDER BY time ASC;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cuidador_id), dayOfWeek});
        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            if (dateString != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

    @SuppressLint("Range")
    public Reminder getPhotoByMedicine(String processNumber){
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From reminder Where medicamento_id = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{processNumber});
        Reminder reminder = new Reminder();

        if(cursor.moveToFirst()){
            reminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            reminder.setPhoto_medicine_box(cursor.getString(cursor.getColumnIndex("photo_medicine_box")));
        }

        cursor.close();
        db.close();
        return reminder;
    }

    @SuppressLint("Range")
    public Reminder getReminderById(int id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        String sql = "Select * From reminder Where _id = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        Reminder reminder = new Reminder();

        if(cursor.moveToFirst()){
            reminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            reminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            reminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            reminder.setType_medicine(cursor.getString(cursor.getColumnIndex("type_medicine")));
            reminder.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
            reminder.setRemaining(cursor.getString(cursor.getColumnIndex("remaining")));
            reminder.setPhoto_medicine_pill(cursor.getString(cursor.getColumnIndex("photo_medicine_pill")));
            reminder.setPhoto_medicine_box(cursor.getString(cursor.getColumnIndex("photo_medicine_box")));
        }

        cursor.close();
        db.close();
        return reminder;
    }

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByElderlyForMedicine(int idoso_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "Select * From reminder Where idoso_id = ? GROUP BY medicamento_id ;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idoso_id)});

        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
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

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByCaregiverForMedicine(int cuidador_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "Select * From reminder Where cuidador_id = ? GROUP BY medicamento_id ;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cuidador_id)});

        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
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

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByReminder(int idoso_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE idoso_id = ? ORDER BY time ASC;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idoso_id)});
        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            if (dateString != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

    @SuppressLint("Range")
    public List<Reminder> getAllRemindersForReminderByCaregiver(int cuidador_id){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE cuidador_id = ? ORDER BY time ASC;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cuidador_id)});
        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setCuidador_id(cursor.getInt(cursor.getColumnIndex("cuidador_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            if (dateString != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

    public boolean updateReminder(Reminder reminder){
        SQLiteDatabase db = this.db.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("time", reminder.getTime());
            values.put("quantity", reminder.getQuantity());
            String whereClause = "medicamento_id = ?";
            String[] whereArgs = {String.valueOf(reminder.getMedicamento_id())};

            long resultado = db.update("reminder", values, whereClause, whereArgs);
            db.close();

            return resultado != -1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateReminderById(Reminder reminder){
        SQLiteDatabase db = this.db.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("time", reminder.getTime());
            values.put("quantity", reminder.getQuantity());

            String whereClause = "_id = ?";
            String[] whereArgs = {String.valueOf(reminder.get_id())};

            long resultado = db.update("reminder", values, whereClause, whereArgs);
            db.close();

            return resultado != -1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setRemaining(int remaining, int reminder_id){
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("remaining", remaining);

        int rowsAffected = db.update("reminder", values, "_id = ?", new String[]{String.valueOf(reminder_id)});
        db.close();

        return rowsAffected > 0;
    }

    @SuppressLint("Range")
    public List<Reminder> getReminderByMedicamentoIdAndElderlyId(Reminder reminder){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE idoso_id = ? AND medicamento_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(reminder.getIdoso_id()), reminder.getMedicamento_id()});
        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            specificReminder.setTime(cursor.getString(cursor.getColumnIndex("time")));

            reminders.add(specificReminder);
        }

        cursor.close();
        db.close();

        return reminders;
    }

    @SuppressLint("Range")
    public List<Reminder> getReminderByMedicamentoIdAndCaregiverId(Reminder reminder){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE cuidador_id = ? AND medicamento_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(reminder.getCuidador_id()), reminder.getMedicamento_id()});
        while (cursor.moveToNext()) {
            Reminder specificReminder = new Reminder();

            specificReminder.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            specificReminder.setIdoso_id(cursor.getInt(cursor.getColumnIndex("idoso_id")));
            specificReminder.setMedicamento_id(cursor.getString(cursor.getColumnIndex("medicamento_id")));
            specificReminder.setTime(cursor.getString(cursor.getColumnIndex("time")));

            reminders.add(specificReminder);
        }

        cursor.close();
        db.close();

        return reminders;
    }

    public boolean deleteReminderByMedicineId(String medicineId){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            long resultado = dbLite.delete("reminder", "medicamento_id = ?", new String[]{String.valueOf(medicineId)});
            MedicineDao medicineDao = new MedicineDao(context, new Medicine());
            medicineDao.deleteMedicine(medicineId);
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela Caregiver: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReminderByElderly(int idoso_id){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            long resultado = dbLite.delete("reminder", "idoso_id = ?", new String[]{String.valueOf(idoso_id)});
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela Reminder: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReminderByCaregiver(int cuidador_id){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            long resultado = dbLite.delete("reminder", "cuidador_id = ?", new String[]{String.valueOf(cuidador_id)});
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Delete", "Erro ao deletar na tabela Reminder: " + e.getMessage());
            return false;
        }
    }
}
