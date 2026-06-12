package com.exemple;

public class ServiceCompte {

    private final RepertoireUtilisateur repertoire;

    public ServiceCompte(RepertoireUtilisateur repertoire) {
        this.repertoire = repertoire;
    }

    public ConfirmationInscription inscrire(Utilisateur utilisateur) {
        if (repertoire.existeParNomUtilisateur(utilisateur.nomUtilisateur())) {
            throw new CompteExistantException(
                    "Compte déjà existant : " + utilisateur.nomUtilisateur());
        }

        repertoire.enregistrer(utilisateur);

        return new ConfirmationInscription(utilisateur.nomUtilisateur(),
                "Compte créé pour " + utilisateur.nomUtilisateur());
    }
}
