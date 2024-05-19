package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.app.Application;

import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.InsertLogHelper;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class WorkerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.enqueueUniquePeriodicWork("workAlarmManager", ExistingPeriodicWorkPolicy.KEEP,
                new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).setBackoffCriteria(BackoffPolicy.LINEAR, 60000, TimeUnit.MILLISECONDS)
                        .build());

        InsertLogHelper.i("WorkerManager", "workerManager started with success!!");
    }

}
