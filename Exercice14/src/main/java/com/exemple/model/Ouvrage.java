package com.exemple.model;

public class Ouvrage {

    private final String id;
    private final String titre;
    private boolean disponible = true;

    public Ouvrage(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public boolean estDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
