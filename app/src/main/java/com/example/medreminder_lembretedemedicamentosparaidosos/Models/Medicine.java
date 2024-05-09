package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

import java.util.Date;

public class Medicine {

    private int _id;
    private int product_id;
    private int register_number;
    private String product_name;
    private String office_hour;
    private String corporate_reason;
    private String cnpj;
    private int transaction_number;
    private Date date;
    private String process_number;
    private String id_protected_patient_leaflet;
    private String id_protected_professional_leaflet;
    private Date update_date;

    public Medicine (String process_number){
        this.process_number = process_number;
    }
    public Medicine(){}
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getRegister_number() {
        return register_number;
    }

    public void setRegister_number(int register_number) {
        this.register_number = register_number;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOffice_hour() {
        return office_hour;
    }

    public void setOffice_hour(String office_hour) {
        this.office_hour = office_hour;
    }

    public String getCorporate_reason() {
        return corporate_reason;
    }

    public void setCorporate_reason(String corporate_reason) {
        this.corporate_reason = corporate_reason;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public int getTransaction_number() {
        return transaction_number;
    }

    public void setTransaction_number(int transaction_number) {
        this.transaction_number = transaction_number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProcess_number() {
        return process_number;
    }

    public void setProcess_number(String process_number) {
        this.process_number = process_number;
    }

    public String getId_protected_patient_leaflet() {
        return id_protected_patient_leaflet;
    }

    public void setId_protected_patient_leaflet(String id_protected_patient_leaflet) {
        this.id_protected_patient_leaflet = id_protected_patient_leaflet;
    }

    public String getId_protected_professional_leaflet() {
        return id_protected_professional_leaflet;
    }

    public void setId_protected_professional_leaflet(String id_protected_professional_leaflet) {
        this.id_protected_professional_leaflet = id_protected_professional_leaflet;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
}
