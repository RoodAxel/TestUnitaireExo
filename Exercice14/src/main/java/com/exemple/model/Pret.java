package com.exemple.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pret {

    private final Adherent adherent;
    private final Ouvrage ouvrage;
    private final LocalDate dateEmprunt;
    private final LocalDate dateRetourPrevue;
    private LocalDate dateRetourReelle;
    private BigDecimal penalite = BigDecimal.ZERO;

    public Pret(Adherent adherent, Ouvrage ouvrage, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.adherent = adherent;
        this.ouvrage = ouvrage;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public Ouvrage getOuvrage() {
        return ouvrage;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public LocalDate getDateRetourReelle() {
        return dateRetourReelle;
    }

    public void setDateRetourReelle(LocalDate dateRetourReelle) {
        this.dateRetourReelle = dateRetourReelle;
    }

    public BigDecimal getPenalite() {
        return penalite;
    }

    public void setPenalite(BigDecimal penalite) {
        this.penalite = penalite;
    }
}
