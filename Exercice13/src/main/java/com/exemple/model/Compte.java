package com.exemple.model;

import java.math.BigDecimal;

public class Compte {

    private final String numero;
    private final String titulaire;
    private BigDecimal solde;

    public Compte(String numero, String titulaire) {
        this.numero = numero;
        this.titulaire = titulaire;
        this.solde = BigDecimal.ZERO;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }
}
