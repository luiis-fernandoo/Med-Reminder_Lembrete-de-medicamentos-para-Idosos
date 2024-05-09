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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            }else{
                elderly = elderlyDao.getElderlyByEmail(sp.getString("email", ""));
                reminders = reminderDao.getAllRemindersByElderly(elderly.get_id());
            }
        }else{
            ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver());
            elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(sp.getString("email", ""));
            reminders = reminderDao.getAllRemindersByCaregiver(elderlyCaregiver.get_id());
        }
        if(reminders.size() != 0){
            for (int i=0; i<reminders.size(); i++) {
                if (reminders.get(i).getEveryday().equals("S")) {
                    String time = reminders.get(i).getTime();
                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    try {
                        Date date = sdf.parse(time);
                        if (date != null) {
                            Calendar alarmTime = Calendar.getInstance();
                            alarmTime.setTime(date);

                            calendar.set(Calendar.HOUR_OF_DAY, alarmTime.get(Calendar.HOUR_OF_DAY));
                            calendar.set(Calendar.MINUTE, alarmTime.get(Calendar.MINUTE));
                            calendar.set(Calendar.SECOND, 0);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), BroadcastReceiver.class);
                    intent.putExtra("reminderId", reminders.get(i).get_id());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),  reminders.get(i).get_id(), intent, PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Log.d("", "Horário do alarme setado: " + calendar.getTime());
                }
            }
        }
        return Result.success(); // Retorna Result.success() se o trabalho for concluído com sucesso
    }

}
