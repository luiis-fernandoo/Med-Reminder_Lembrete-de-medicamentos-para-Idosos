package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.Context;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperElderly;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Elderly;

public class ElderlyDao {
    private final Elderly elderly;
    private final FeedEntry.DBHelpers db;
    private HelperElderly helperElderly;
    private static final String TAG = "FilmLog";

    public ElderlyDao(Context ctx, Elderly elderly) {
        this.elderly = elderly;
        this.db = new FeedEntry.DBHelpers(ctx);
    }
}
