package com.exemple;

import java.util.Optional;

public interface RepertoireUtilisateur {

    boolean existeParNomUtilisateur(String nomUtilisateur);

    void enregistrer(Utilisateur utilisateur);

    Optional<Utilisateur> trouverParNomUtilisateur(String nomUtilisateur);
}
