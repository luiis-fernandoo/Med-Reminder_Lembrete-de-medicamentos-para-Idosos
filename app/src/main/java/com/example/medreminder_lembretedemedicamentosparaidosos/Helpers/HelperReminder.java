package com.example.medreminder_lembretedemedicamentosparaidosos.Helpers;

import android.provider.BaseColumns;

public class HelperReminder implements BaseColumns {
    public String TABLE_NAME = "reminder";
    public String COLUMN_NAME_IDOSO_ID = "idoso_id";
    public String COLUMN_NAME_MEDICAMENTO_ID = "medicamento_id";
    public String COLUMN_NAME_EVERYDAY = "everyday";
    public String COLUMN_NAME_DATE = "date";
    public String COLUMN_NAME_DAY_OF_WEEK = "day_of_week";
    public String COLUMN_NAME_TYPE_MEDICINE = "type_medicine";
    public String COLUMN_NAME_TIME = "time";
    public String COLUMN_NAME_STATUS = "status";
    public String COLUMN_NAME_QUANTITY = "quantity";
    public String COLUMN_NAME_REMAINING = "remaining";
    public String COLUMN_NAME_WARNING = "warning";
    public String COLUMN_NAME_PHOTO_MEDICINE_BOX = "photo_medicine_box";
    public String COLUMN_NAME_PHOTO_MEDICINE_PILL = "photo_medicine_pill";
}
