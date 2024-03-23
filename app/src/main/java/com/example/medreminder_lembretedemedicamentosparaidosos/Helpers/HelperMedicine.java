package com.example.medreminder_lembretedemedicamentosparaidosos.Helpers;

import android.provider.BaseColumns;

public class HelperMedicine implements BaseColumns {
    public String TABLE_NAME = "medicine";
    public String COLUMN_NAME_PRODUCT_ID = "product_id";
    public String COLUMN_NAME_NUMBER_REGISTER = "number_register";
    public String COLUMN_NAME_PRODUCT_NAME = "product_name";
    public String COLUMN_NAME_OFFICE_HOUR = "office_hour";
    public String COLUMN_NAME_CORPORATION_REASON = "corporation_reason";
    public String COLUMN_NAME_CNPJ = "cnpj";
    public String COLUMN_NAME_TRANSACTION_NUMBER = "trasaction_number";
    public String COLUMN_NAME_DATE = "date";
    public String COLUMN_NAME_PROCESS_NUMBER = "process_number";
    public String COLUMN_NAME_ID_PROTECTED_PATIENT_LEAFLET = "id_protected_patient_leaflet";
    public String COLUMN_NAME_ID_PROTECTED_PROFESSIONAL_LEAFLET = "id_protected_professional_leaflet";

}
