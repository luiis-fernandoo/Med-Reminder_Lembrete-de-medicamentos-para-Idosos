package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.medreminder_lembretedemedicamentosparaidosos.Services.AlarmReminderService;

import java.util.concurrent.TimeUnit;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            WorkManager workManager = WorkManager.getInstance(context);
            workManager.enqueueUniquePeriodicWork("workAlarmManager", ExistingPeriodicWorkPolicy.KEEP,
                    new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).setBackoffCriteria(BackoffPolicy.LINEAR, 60000, TimeUnit.MILLISECONDS)
                            .build());
        }else{
            int reminderId = intent.getIntExtra("reminderId", -1);

            Intent serviceIntent = new Intent(context, AlarmReminderService.class);
            serviceIntent.putExtra("reminderId", reminderId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }

    }
}
