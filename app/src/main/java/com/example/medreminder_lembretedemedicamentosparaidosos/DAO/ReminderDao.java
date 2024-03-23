package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.Context;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperReminder;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Reminder;

public class ReminderDao {
    private final Reminder reminder;
    private final FeedEntry.DBHelpers db;
    private HelperReminder helperReminder;
    private static final String TAG = "FilmLog";

    public ReminderDao(Context ctx, Reminder reminder) {
        this.reminder = reminder;
        this.db = new FeedEntry.DBHelpers(ctx);
    }
}
