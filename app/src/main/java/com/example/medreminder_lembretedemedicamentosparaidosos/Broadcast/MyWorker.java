package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class MyWorker extends Worker {
    private static final int SCHEDULE_ALARM_REQUEST_CODE = 1;
    private static final String TAG = "VerificarAlarmeJobService";
    private SharedPreferences sp;
    private String selectedUserType;
    private Elderly guestElderly, elderly;
    private ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
    private List<Elderly> elderlies;
    private List<Reminder> reminders;
    private ElderlyCaregiver elderlyCaregiver ;
    private ReminderDao reminderDao = new ReminderDao(getApplicationContext(), new Reminder());

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressLint("ScheduleExactAlarm")
    @NonNull
    @Override
    public Result doWork() {

        sp = getApplicationContext().getSharedPreferences("app", Context.MODE_PRIVATE);

        selectedUserType = sp.getString("selectedUserType", "");
        if(selectedUserType.equals("Idoso")){
            elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
            if(sp.getString("Guest", "").equals("Convidado")){
                guestElderly = elderlyDao.getElderlyByName("Convidado");
                reminders = reminderDao.getAllRemindersByElderly(guestElderly.get_id());
                Log.d("", "Reminders de convidado: " + reminders.get(0).getType_medicine());
            }else{
                elderly = elderlyDao.getElderlyByEmail(sp.getString("email", ""));
                reminders = reminderDao.getAllRemindersByElderly(elderly.get_id());
                Log.d("", "Reminders de idoso: " + reminders);
            }
        }else{
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver());
            elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(sp.getString("email", ""));
            elderlies = new ArrayList<>();
            elderlies = elderlyDao.getElderlyByCareviger(elderlyCaregiver.get_id());
            for(int i=0; i<elderlies.size(); i++){

            }
        }

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BroadcastReceiver.class); // Substitua MyAlarmReceiver pela sua classe BroadcastReceiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);
        }

        return Result.success(); // Retorna Result.success() se o trabalho for concluído com sucesso
    }
}
