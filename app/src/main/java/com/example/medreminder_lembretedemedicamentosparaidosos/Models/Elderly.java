package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

public class Elderly extends Person{

    private int medicamento_id;
    private int idade;
    private Person person;

    public int getMedicamento_id() {
        return medicamento_id;
    }

    public void setMedicamento_id(int medicamento_id) {
        this.medicamento_id = medicamento_id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
