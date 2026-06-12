package com.exemple;

import java.util.List;

public interface CatalogueProduit {

    List<Produit> rechercherParMotCle(String motCle);

    List<Produit> rechercherParPrixMax(double prixMax);

    List<Produit> trouverParCategorie(String categorie);
}
