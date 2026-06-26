package com.exemple.repository;

import com.exemple.model.Compte;
import java.util.List;
import java.util.Optional;

public interface RepertoireCompte {

    Compte sauvegarder(Compte compte);

    Optional<Compte> trouverParNumero(String numero);

    boolean existeParNumero(String numero);

    List<Compte> trouverTous();
}
