package com.exemple.repository;

import com.exemple.model.Salle;
import java.util.List;
import java.util.Optional;

public interface RepertoireSalle {

    Salle sauvegarder(Salle salle);

    Optional<Salle> trouverParId(Long id);

    List<Salle> trouverTous();
}
