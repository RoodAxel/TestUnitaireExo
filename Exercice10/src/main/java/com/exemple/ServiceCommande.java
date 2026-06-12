package com.exemple;

public class ServiceCommande {

    private final RepertoireCommande repertoireCommande;

    public ServiceCommande(RepertoireCommande repertoireCommande) {
        this.repertoireCommande = repertoireCommande;
    }

    public int ajouterProduit(String commandeId, String reference) {
        Commande commande = chargerCommande(commandeId);
        return commande.ajouter(reference);
    }

    public int supprimerProduit(String commandeId, String reference) {
        Commande commande = chargerCommande(commandeId);
        if (!commande.contient(reference)) {
            throw new CommandeRefuseeException("Produit absent de la commande : " + reference);
        }
        return commande.retirer(reference);
    }

    public ConfirmationCommande validerCommande(String commandeId) {
        Commande commande = chargerCommande(commandeId);
        return new ConfirmationCommande(commande.id(), "Commande validée : " + commande.id());
    }

    private Commande chargerCommande(String commandeId) {
        return repertoireCommande.trouverParId(commandeId)
                .orElseThrow(() -> new CommandeRefuseeException("Commande introuvable : " + commandeId));
    }
}
