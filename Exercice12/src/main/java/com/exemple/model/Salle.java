package com.exemple.model;

public class Salle {

    private Long id;
    private String nom;
    private int capacite;

    public Salle(Long id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }
}
