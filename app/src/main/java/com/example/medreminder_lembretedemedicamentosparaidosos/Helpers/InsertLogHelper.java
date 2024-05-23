package com.example.medreminder_lembretedemedicamentosparaidosos.Helpers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InsertLogHelper {
    private static void writeLogToFile(String tag, String message) {
        String log = tag + ": " + message;
        File documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File logDirectory = new File(documentsDirectory, "LogMedReminders");
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        File logFile = new File(logDirectory, "log_med_reminder.txt");
        Log.d("InsertLogHelper", "Log file path: " + logFile.getAbsolutePath());

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(log);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void i(String tag, String message) {
        writeLogToFile(tag, message);
    }

    public static void e(String tag, String message) {
        writeLogToFile(tag, message);
    }

}
