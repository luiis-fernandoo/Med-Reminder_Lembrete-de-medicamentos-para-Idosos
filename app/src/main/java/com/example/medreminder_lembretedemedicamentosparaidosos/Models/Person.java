package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

public class Person {

    private int _id;
    private String name;
    private String profile_photo;
    private String password;
    private String email;

    public Person(String name, String email, String currentPhotoPath, String password) {
        this.name = name;
        this.email = email;
        this.profile_photo = currentPhotoPath;
        this.password = password;
    }

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
