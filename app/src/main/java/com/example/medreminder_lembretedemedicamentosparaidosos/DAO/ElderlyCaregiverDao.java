package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.Context;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperElderlyCaregiver;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.ElderlyCaregiver;

public class ElderlyCaregiverDao {
    private final ElderlyCaregiver elderlyCaregiver;
    private final FeedEntry.DBHelpers db;
    private HelperElderlyCaregiver helperElderlyCaregiver;
    private static final String TAG = "FilmLog";

    public ElderlyCaregiverDao(Context ctx, ElderlyCaregiver elderlyCaregiver) {
        this.elderlyCaregiver = elderlyCaregiver;
        this.db = new FeedEntry.DBHelpers(ctx);
    }
}
