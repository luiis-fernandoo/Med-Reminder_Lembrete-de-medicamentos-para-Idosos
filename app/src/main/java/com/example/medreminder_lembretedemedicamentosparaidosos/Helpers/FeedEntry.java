package com.example.medreminder_lembretedemedicamentosparaidosos.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FeedEntry implements BaseColumns {
    static HelperElderly helperElderly = new HelperElderly();
    static HelperElderlyCaregiver helperElderlyCaregiver = new HelperElderlyCaregiver();
    static HelperMedicine helperMedicine = new HelperMedicine();
    static HelperReminder helperReminder = new HelperReminder();

    public static class DBHelpers extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MedReminder.db";

        private static final String SQL_CREATE_ENTRIES_Medicine =
                "CREATE TABLE " + helperMedicine.TABLE_NAME + " (" +
                        helperMedicine._ID + " INTEGER PRIMARY KEY, " +
                        helperMedicine.COLUMN_NAME_PRODUCT_ID + " TEXT," +
                        helperMedicine.COLUMN_NAME_PROCESS_NUMBER + " TEXT," +
                        helperMedicine.COLUMN_NAME_NUMBER_REGISTER + " TEXT," +
                        helperMedicine.COLUMN_NAME_PRODUCT_NAME + " TEXT," +
                        helperMedicine.COLUMN_NAME_CNPJ + " TEXT," +
                        helperMedicine.COLUMN_NAME_DATE + " DATE," +
                        helperMedicine.COLUMN_NAME_CORPORATION_REASON + " TEXT," +
                        helperMedicine.COLUMN_NAME_TRANSACTION_NUMBER + " TEXT," +
                        helperMedicine.COLUMN_NAME_ID_PROTECTED_PATIENT_LEAFLET + " TEXT," +
                        helperMedicine.COLUMN_NAME_ID_PROTECTED_PROFESSIONAL_LEAFLET + " TEXT," +
                        helperMedicine.COLUMN_NAME_OFFICE_HOUR + " TEXT)";
        private static final String SQL_CREATE_ENTRIES_Elderly =
                "CREATE TABLE " + helperElderly.TABLE_NAME + " (" +
                        helperElderly._ID + " INTEGER PRIMARY KEY, " +
                        helperElderly.COLUMN_NAME_NAME + " TEXT," +
                        helperElderly.COLUMN_NAME_EMAIL + " TEXT," +
                        helperElderly.COLUMN_NAME_PASSWORD + " TEXT," +
                        helperElderly.COLUMN_NAME_PHOTO_PROFILE + " TEXT," +
                        helperElderly.COLUMN_NAME_CUIDADOR_ID + " INTEGER REFERENCES " +
                        helperElderly.TABLE_NAME + "(" + helperElderlyCaregiver._ID + ")," +
                        helperElderly.COLUMN_NAME_AGE + " INTEGER)";

        private static final String SQL_CREATE_ENTRIES_ElderlyCaregiver =
                "CREATE TABLE " + helperElderlyCaregiver.TABLE_NAME + " (" +
                        helperElderlyCaregiver._ID + " INTEGER PRIMARY KEY, " +
                        helperElderlyCaregiver.COLUMN_NAME_EMAIL + " TEXT," +
                        helperElderlyCaregiver.COLUMN_NAME_NAME + " TEXT," +
                        helperElderlyCaregiver.COLUMN_NAME_PASSWORD + " TEXT," +
                        helperElderlyCaregiver.COLUMN_NAME_PHOTO_PROFILE + " TEXT)";

        private static final String SQL_CREATE_ENTRIES_Reminder =
                "CREATE TABLE " + helperReminder.TABLE_NAME + " (" +
                        helperReminder._ID + " INTEGER PRIMARY KEY, " +
                        helperReminder.COLUMN_NAME_MEDICAMENTO_ID + " INTEGER REFERENCES " +
                        helperMedicine.TABLE_NAME + "(" + helperMedicine._ID + ")," +
                        helperReminder.COLUMN_NAME_IDOSO_ID + " INTEGER REFERENCES " +
                        helperElderly.TABLE_NAME + "(" + helperElderly._ID + ")," +
                        helperReminder.COLUMN_NAME_DOSE + " TEXT," +
                        helperReminder.COLUMN_NAME_DURATION + " TEXT," +
                        helperReminder.COLUMN_NAME_FREQUENCY + " TEXT," +
                        helperReminder.COLUMN_NAME_DATE + " DATE," +
                        helperReminder.COLUMN_NAME_REPLACEMENT + " TEXT," +
                        helperReminder.COLUMN_NAME_TYPE_MEDICINE + " TEXT," +
                        helperReminder.COLUMN_NAME_STATUS + " TEXT," +
                        helperReminder.COLUMN_NAME_PHOTO_MEDICINE_BOX + " TEXT," +
                        helperReminder.COLUMN_NAME_PHOTO_MEDICINE_PILL + " TEXT)";


        public DBHelpers(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES_Medicine);
            db.execSQL(SQL_CREATE_ENTRIES_Elderly);
            db.execSQL(SQL_CREATE_ENTRIES_ElderlyCaregiver);
            db.execSQL(SQL_CREATE_ENTRIES_Reminder);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Aqui você colocaria código para atualizar as tabelas, se necessário
        }

        public void deleteDatabase(Context context) {
            context.deleteDatabase(DATABASE_NAME);
        }
    }
}
