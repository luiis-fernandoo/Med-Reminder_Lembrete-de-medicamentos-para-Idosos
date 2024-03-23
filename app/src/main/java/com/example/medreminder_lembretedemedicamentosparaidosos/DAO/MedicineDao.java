package com.example.medreminder_lembretedemedicamentosparaidosos.DAO;

import android.content.Context;

import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.FeedEntry;
import com.example.medreminder_lembretedemedicamentosparaidosos.Helpers.HelperMedicine;
import com.example.medreminder_lembretedemedicamentosparaidosos.Models.Medicine;

public class MedicineDao {
    private final Medicine medicine;
    private final FeedEntry.DBHelpers db;
    private HelperMedicine helperMedicine;
    private static final String TAG = "FilmLog";

    public MedicineDao(Context ctx, Medicine medicine) {
        this.medicine = medicine;
        this.db = new FeedEntry.DBHelpers(ctx);
    }
}
