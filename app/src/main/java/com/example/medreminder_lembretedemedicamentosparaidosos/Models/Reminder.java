package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

import java.util.Date;

public class Reminder {

    private int _id;
    private int idoso_id;
    private int medicamento_id;
    private String frequency;
    private String dose;
    private Date date;
    private String duration;
    private String replacement;
    private String type_medicine;
    private int status;
    private String photo_medicine_box;
    private String photo_medicine_pill;

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

    public int getMedicamento_id() {
        return medicamento_id;
    }

    public void setMedicamento_id(int medicamento_id) {
        this.medicamento_id = medicamento_id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
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
}
