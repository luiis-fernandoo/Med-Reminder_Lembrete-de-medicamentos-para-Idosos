package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

import java.util.Date;

public class Reminder {

    private int _id;
    private int idoso_id;
    private String medicamento_id;
    private String everyday;
    private String time;
    private Date date;
    private String quantity;
    private String warning;
    private String remaining;
    private String type_medicine;
    private int status;
    private String photo_medicine_box;
    private String photo_medicine_pill;

    public Reminder(){}

    public Reminder(int idoso_id, String medicamento_id, String typeMedicine, String everyday, String time, Date date, String quantity, String remaining, String warning, String photo_medicine_box, String photo_medicine_pill) {
        this.idoso_id = idoso_id;
        this.medicamento_id = medicamento_id;
        this.type_medicine = typeMedicine;
        this.everyday = everyday;
        this.time = time;
        this.date = date;
        this.quantity = quantity;
        this.remaining = remaining;
        this.warning = warning;
        this.photo_medicine_box = photo_medicine_box;
        this.photo_medicine_pill = photo_medicine_pill;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getIdoso_id() {
        return idoso_id;
    }

    public void setIdoso_id(int idoso_id) {
        this.idoso_id = idoso_id;
    }

    public String getMedicamento_id() {
        return medicamento_id;
    }

    public void setMedicamento_id(String medicamento_id) {
        this.medicamento_id = medicamento_id;
    }

    public String getEveryday() {
        return everyday;
    }

    public void setEveryday(String everyday) {
        this.everyday = everyday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getType_medicine() {
        return type_medicine;
    }

    public void setType_medicine(String type_medicine) {
        this.type_medicine = type_medicine;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoto_medicine_box() {
        return photo_medicine_box;
    }

    public void setPhoto_medicine_box(String photo_medicine_box) {
        this.photo_medicine_box = photo_medicine_box;
    }

    public String getPhoto_medicine_pill() {
        return photo_medicine_pill;
    }

    public void setPhoto_medicine_pill(String photo_medicine_pill) {
        this.photo_medicine_pill = photo_medicine_pill;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
