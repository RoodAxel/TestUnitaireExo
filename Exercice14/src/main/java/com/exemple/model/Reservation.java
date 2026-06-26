package com.exemple.model;

public class Reservation {

    private final Adherent adherent;
    private final Ouvrage ouvrage;

    public Reservation(Adherent adherent, Ouvrage ouvrage) {
        this.adherent = adherent;
        this.ouvrage = ouvrage;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public Ouvrage getOuvrage() {
        return ouvrage;
    }
}
