package com.exemple;

public class ServiceCommande {

    private final CatalogueProduit catalogue;

    public ServiceCommande(CatalogueProduit catalogue) {
        this.catalogue = catalogue;
    }

    public RecuCommande passerCommande(Commande commande, ProfilClient profil) {
        Produit produit = catalogue.trouverParReference(commande.reference())
                .orElseThrow(() -> new CommandeRefuseeException(
                        "Produit inconnu : " + commande.reference()));

        if (commande.quantite() > produit.stockDisponible()) {
            throw new CommandeRefuseeException(
                    "Stock insuffisant pour le produit : " + commande.reference());
        }

        double montantBrut = produit.prixUnitaire() * commande.quantite();
        double montantTotal = montantBrut * (1 - profil.tauxRemise());

        return new RecuCommande(produit.reference(), commande.quantite(), montantTotal,
                "Commande confirmee pour " + commande.quantite() + " x " + produit.nom());
    }
}
