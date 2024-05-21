package com.example.medreminder_lembretedemedicamentosparaidosos.Broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.medreminder_lembretedemedicamentosparaidosos.Services.AlarmReminderService;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
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
