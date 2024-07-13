package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.AlarmManagerCompat;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.InsertLogHelper;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class WorkerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkCanScheduleExactAlarms(this);
        }

        worker();
        setAlarmRepeting();

    }

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarmRepeting(){
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), BroadCastWorker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        InsertLogHelper.i("AlarmManager", "alarm time success set: " + calendar.getTime());
    }

    public void worker()
    {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.enqueueUniquePeriodicWork("workAlarmManager", ExistingPeriodicWorkPolicy.KEEP,
                new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).setBackoffCriteria(BackoffPolicy.LINEAR, 60000, TimeUnit.MILLISECONDS)
                        .build());

        InsertLogHelper.i("WorkerManager", "workerManager started with success!!");
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private void checkCanScheduleExactAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
