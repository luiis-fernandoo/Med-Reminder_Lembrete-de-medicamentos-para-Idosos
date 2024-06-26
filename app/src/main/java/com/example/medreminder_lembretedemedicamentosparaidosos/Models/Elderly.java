package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

import android.text.Editable;
import android.widget.TextView;

import com.example.medreminder_lembretedemedicamentosparaidosos.Interface.PopupInterface;

public class Elderly extends Person implements PopupInterface {

    private String age;
    private int cuidador_id;
    private Person person;

    public Elderly(String name, String email, String currentPhotoPath, String password) {
        super(name, email, currentPhotoPath, password); // Chama o construtor da classe pai
    }
    public Elderly(String name, String email, String currentPhotoPath, String password, int age, int cuidador_id) {
        super(name, email, currentPhotoPath, password); // Chama o construtor da classe pai
        this.age = String.valueOf(age);
        this.cuidador_id = cuidador_id;
    }

    public Elderly(String name) {
        super(name);
    }

    public Elderly(String email, String password) {
        super(email, password);
    }

    public Elderly(String name, String email, String password) {
        super(name, email, password);
    }
    public Elderly(){}

    public Elderly(Editable name, String email, String currentPhotoPath, String password, Editable age, int cuidador_id) {
        super(name, email, currentPhotoPath, password);
        this.age = String.valueOf(age);
        this.cuidador_id = cuidador_id;
    }

    public int getCuidador_id() {
        return cuidador_id;
    }

    public void setCuidador_id(int cuidador_id) {
        this.cuidador_id = cuidador_id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
