package com.exemple;

import java.util.LinkedHashMap;
import java.util.Map;

public class Commande {

    private final String id;
    private final Map<String, Integer> quantitesParReference = new LinkedHashMap<>();

    public Commande(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public int ajouter(String reference) {
        int nouvelleQuantite = quantitesParReference.getOrDefault(reference, 0) + 1;
        quantitesParReference.put(reference, nouvelleQuantite);
        return nouvelleQuantite;
    }

    public int retirer(String reference) {
        int quantiteActuelle = quantitesParReference.getOrDefault(reference, 0);
        if (quantiteActuelle <= 1) {
            quantitesParReference.remove(reference);
            return 0;
        }
        int nouvelleQuantite = quantiteActuelle - 1;
        quantitesParReference.put(reference, nouvelleQuantite);
        return nouvelleQuantite;
    }

    public boolean contient(String reference) {
        return quantitesParReference.containsKey(reference);
    }

    public int quantite(String reference) {
        return quantitesParReference.getOrDefault(reference, 0);
    }
}
