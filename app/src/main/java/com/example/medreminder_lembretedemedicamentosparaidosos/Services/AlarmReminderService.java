package com.example.medreminder_lembretedemedicamentosparaidosos.Services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.medreminder_lembretedemedicamentosparaidosos.Activities.MenuActivity;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyCaregiverDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ElderlyDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.MedicineDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.DAO.ReminderDao;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.InsertLogHelper;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.R;

import java.util.LinkedList;
import java.util.Queue;

public class AlarmReminderService extends Service {

    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "Channel Name";
    private static final String ACTION_STOP = "STOP";
    private static final int NOTIFICATION_ID = 1;
    private TextView nameMedicine, nameElderly, typeMedicine, quantityMedicine;
    private Button buttonConfirm, buttonCancel;
    private ImageView imageMedicine;

    private ReminderDao reminderDao;
    private MedicineDao medicineDao;
    private ElderlyDao elderlyDao;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private Queue<Reminder> reminderQueue;
    private boolean isPopupOpen = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();

        reminderQueue = new LinkedList<>();

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");
        String guest = sp.getString("Guest", "");

        ElderlyDao elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
        Elderly elderly = elderlyDao.getElderlyByEmail(savedEmail);
        ElderlyCaregiverDao elderlyCaregiverDao = new ElderlyCaregiverDao(getApplicationContext(), new ElderlyCaregiver());
        ElderlyCaregiver elderlyCaregiver = elderlyCaregiverDao.getElderlyCaregiver(savedEmail);

        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        int reminderId = intent.getIntExtra("reminderId", -1);

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.clock_alarm);

        createNotificationChannel();
        reminderDao = new ReminderDao(getApplicationContext(), new Reminder(reminderId));
        Reminder reminder = reminderDao.getReminderById(reminderId);
        MedicineDao medicineDao = new MedicineDao(getApplicationContext(), new Medicine(reminder.getMedicamento_id()));
        Medicine medicine = medicineDao.getMedicineByProcessNumber();
        Notification notification = createNotification(medicine);

        if(reminder.getCuidador_id() > 0){
            if(reminder.getCuidador_id() == elderlyCaregiver.get_id()) {
                if (reminder.getStatus() == 1) {
                    vibrate();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    reminderQueue.add(reminder);
                    processNextReminder();

                    InsertLogHelper.i("AlarmReminderService", "Alarm for the time: " + reminder.getTime() + " success dispatch!!");

                    if(reminder.getWarning() != null && reminder.getRemaining() != null){
                        if (Integer.parseInt(reminder.getWarning()) >= Integer.parseInt(reminder.getRemaining())) {
                            startForeground(NOTIFICATION_ID, notification);
                        }
                    }
                }
            }
        }else{
            if(reminder.getIdoso_id() == elderly.get_id()) {
                if (reminder.getStatus() == 1) {
                    vibrate();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    reminderQueue.add(reminder);
                    processNextReminder();

                    InsertLogHelper.i("AlarmReminderService", "Alarm for the time: " + reminder.getTime() + " success dispatch!!");

                    if (Integer.parseInt(reminder.getWarning()) >= Integer.parseInt(reminder.getRemaining())) {
                        startForeground(NOTIFICATION_ID, notification);
                    }
                }
            }else if(!guest.equals("")){
                if (reminder.getStatus() == 1) {
                    vibrate();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    reminderQueue.add(reminder);
                    processNextReminder();

                    InsertLogHelper.i("AlarmReminderService", "Alarm for the time: " + reminder.getTime() + " success dispatch!!");

                    if(reminder.getWarning() != null && reminder.getRemaining() != null){
                        if (Integer.parseInt(reminder.getWarning()) >= Integer.parseInt(reminder.getRemaining())) {
                            startForeground(NOTIFICATION_ID, notification);
                        }
                    }
                }
            }
        }

        return START_STICKY;
    }

    private void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire(3000000);
    }

    private void vibrate() {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
            } else {
                vibrator.vibrate(pattern, 0);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification(Medicine medicine) {
        Intent stopIntent = new Intent(this, AlarmReminderService.class);
        stopIntent.setAction(ACTION_STOP);

        Intent notificationIntent = new Intent(this, MenuActivity.class);
        notificationIntent.putExtra("notification", "Notification_message");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Aviso para reposição")
                .setContentText("O seu medicamento: " + medicine.getProduct_name() + " está acabando!")
                .setSmallIcon(R.drawable.logo_app)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.bg_list, "OK", stopPendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showAlertDialog(Reminder reminder) {
        isPopupOpen = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_reminder_alarm, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(popupView);

        nameMedicine = popupView.findViewById(R.id.nameMedicine);
        nameElderly = popupView.findViewById(R.id.nameElderly);
        typeMedicine = popupView.findViewById(R.id.typeMedicine);
        quantityMedicine = popupView.findViewById(R.id.quantityMedicine);
        buttonConfirm = popupView.findViewById(R.id.buttonConfirm);
        buttonCancel = popupView.findViewById(R.id.buttonCancel);
        ImageView imageViewMedicine = popupView.findViewById(R.id.imageViewMedicine);

        AlertDialog dialog = alertDialogBuilder.create();

        medicineDao = new MedicineDao(getApplicationContext(), new Medicine(reminder.getMedicamento_id()));
        Medicine medicine = medicineDao.getMedicineByProcessNumber();

        elderlyDao = new ElderlyDao(getApplicationContext(), new Elderly());
        Elderly elderly = elderlyDao.getElderlyById(reminder.getIdoso_id());

        Typeface typefaceBaloon = ResourcesCompat.getFont(this, R.font.baloo_chettan);
        Typeface typefaceAldrich = ResourcesCompat.getFont(this, R.font.aldrich);

        nameMedicine.setText(medicine.getProduct_name());
        nameMedicine.setTypeface(typefaceBaloon);

        nameElderly.setText(getApplicationContext().getString(R.string.elderly) + ": " + elderly.getName());
        nameElderly.setTypeface(typefaceAldrich);

        if(reminder.getType_medicine().equals("pill")){
            typeMedicine.setText("Tipo: " + getApplicationContext().getString(R.string.pill));
        }else if(reminder.getType_medicine().equals("drops")){
            typeMedicine.setText("Tipo: " + getApplicationContext().getString(R.string.drops));
        }else if(reminder.getType_medicine().equals("dust")){
            typeMedicine.setText("Tipo: " + getApplicationContext().getString(R.string.dust));
        }
        typeMedicine.setTypeface(typefaceAldrich);

        if(reminder.getType_medicine().equals("pill")){
            quantityMedicine.setText(getApplicationContext().getString(R.string.dose) + ": " + reminder.getQuantity() + " comprimido(s)");
        }else if(reminder.getType_medicine().equals("drops")){
            quantityMedicine.setText(getApplicationContext().getString(R.string.dose) + ": " + reminder.getQuantity() + " gota(s)");
        }else if(reminder.getType_medicine().equals("dust")){
            quantityMedicine.setText(getApplicationContext().getString(R.string.dose) + ": " + reminder.getQuantity() + " mg(s)");
        }
        quantityMedicine.setTypeface(typefaceAldrich);

        if(reminder.getPhoto_medicine_pill()!=null){
            Glide.with(getApplicationContext())
                    .load(reminder.getPhoto_medicine_pill())
                    .into(imageViewMedicine);
        }else if(reminder.getPhoto_medicine_box()!=null){
            Glide.with(getApplicationContext())
                    .load(reminder.getPhoto_medicine_box())
                    .into(imageViewMedicine);
        }else{
            imageViewMedicine.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.medicamento_home_2));
        }

        final Reminder finalReminder = reminder;
        buttonConfirm.setText(R.string.confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderDao.setStatusAlarm(10, finalReminder.get_id());
                if(finalReminder.getRemaining() != null){
                    int remaining = Integer.parseInt(finalReminder.getRemaining());
                    int quantity = Integer.parseInt(finalReminder.getQuantity());
                    remaining -= quantity;
                    reminderDao.setRemaining(remaining, finalReminder.get_id());
                }
                if (vibrator != null) {
                    vibrator.cancel();
                }
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                isPopupOpen = false;
                processNextReminder();
                dialog.dismiss();
            }
        });

        buttonCancel.setText(R.string.cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderDao.setStatusAlarm(11, finalReminder.get_id());
                if (vibrator != null) {
                    vibrator.cancel();
                }
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                isPopupOpen = false;
                processNextReminder();
                dialog.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        dialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void processNextReminder() {
        if (!isPopupOpen && !reminderQueue.isEmpty()) {
            showAlertDialog(reminderQueue.poll());
        }else if(reminderQueue.isEmpty()){
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }
}