package com.exemple;

public class ServiceAuthentification {

    private static final String PAGE_ACCUEIL = "ACCUEIL";

    private final RepertoireUtilisateur repertoire;

    public ServiceAuthentification(RepertoireUtilisateur repertoire) {
        this.repertoire = repertoire;
    }

    public ResultatConnexion connecter(String nomUtilisateur, String motDePasse) {
        Utilisateur utilisateur = repertoire.trouverParNomUtilisateur(nomUtilisateur)
                .orElseThrow(() -> new ConnexionRefuseeException("Identifiants invalides"));

        if (!utilisateur.motDePasse().equals(motDePasse)) {
            throw new ConnexionRefuseeException("Identifiants invalides");
        }

        return new ResultatConnexion(nomUtilisateur, PAGE_ACCUEIL);
    }
}
