package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

public class ElderlyCaregiver extends Person{

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
}
