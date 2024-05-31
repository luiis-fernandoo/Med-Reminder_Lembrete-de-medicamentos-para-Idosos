package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.content.Context;
import android.content.Intent;

import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.InsertLogHelper;

import java.util.concurrent.TimeUnit;

public class BroadCastWorker extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork("workAlarmManager", ExistingPeriodicWorkPolicy.KEEP,
                new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).setBackoffCriteria(BackoffPolicy.LINEAR, 60000, TimeUnit.MILLISECONDS)
                        .build());

        InsertLogHelper.i("WorkerManager", "workerManager started with success!!");
    }
}
