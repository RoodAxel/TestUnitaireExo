package com.exemple;

import java.util.List;

public class ServiceCatalogue {

    private final CatalogueProduit catalogue;

    public ServiceCatalogue(CatalogueProduit catalogue) {
        this.catalogue = catalogue;
    }

    public List<Produit> rechercher(String motCle) {
        return catalogue.rechercherParMotCle(motCle);
    }

    public List<Produit> rechercherParPrixMaximum(double prixMax) {
        return catalogue.rechercherParPrixMax(prixMax);
    }

    public List<Produit> parCategorie(String categorie) {
        return catalogue.trouverParCategorie(categorie);
    }
}
