package com.exemple.model;

public class Adherent {

    private static final int SEUIL_SUSPENSION = 3;

    private final String nom;
    private StatutAdherent statut = StatutAdherent.ACTIF;
    private int retardsImportants = 0;

    public Adherent(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public StatutAdherent getStatut() {
        return statut;
    }

    public boolean estSuspendu() {
        return statut == StatutAdherent.SUSPENDU;
    }

    public int getRetardsImportants() {
        return retardsImportants;
    }

    public void enregistrerRetardImportant() {
        retardsImportants++;
        if (retardsImportants >= SEUIL_SUSPENSION) {
            statut = StatutAdherent.SUSPENDU;
        }
    }
}
