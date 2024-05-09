package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.app.Application;

import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WorkerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.enqueueUniquePeriodicWork("workAlarmManager", ExistingPeriodicWorkPolicy.KEEP,
                new PeriodicWorkRequest.Builder(MyWorker.class, 6, TimeUnit.HOURS).setBackoffCriteria(BackoffPolicy.LINEAR, 60000, TimeUnit.MILLISECONDS)
                        .build());
    }

}
