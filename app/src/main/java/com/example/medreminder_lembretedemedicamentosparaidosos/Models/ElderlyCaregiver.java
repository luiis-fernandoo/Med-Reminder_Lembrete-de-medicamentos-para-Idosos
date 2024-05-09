package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

import com.example.medreminder_lembretedemedicamentosparaidosos.Interface.PopupInterface;

public class ElderlyCaregiver extends Person implements PopupInterface {

    private String age;

    public ElderlyCaregiver(String name, String email, String currentPhotoPath, String password, String age) {
        super(name, email, currentPhotoPath, password);
        this.age = age;
    }

    public ElderlyCaregiver(String name, String email, String currentPhotoPath, String password) {
        super(name, email, currentPhotoPath, password);
    }

    public ElderlyCaregiver(String email, String password) {
        super(email, password);
    }

    public ElderlyCaregiver(String name, String email, String password) {
        super(name, email, password);
    }


    public ElderlyCaregiver(String email) {
        super(email);
    }

    public ElderlyCaregiver() {
        super();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
