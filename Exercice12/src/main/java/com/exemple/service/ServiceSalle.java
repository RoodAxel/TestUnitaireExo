package com.exemple.service;

import com.exemple.exception.DonneesInvalidesException;
import com.exemple.model.Salle;
import com.exemple.repository.RepertoireSalle;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceSalle {

    private static final int CAPACITE_MINIMALE = 1;

    private final RepertoireSalle repertoireSalle;

    public ServiceSalle(RepertoireSalle repertoireSalle) {
        this.repertoireSalle = repertoireSalle;
    }

    public Salle creerSalle(String nom, int capacite) {
        if (nom == null || nom.isBlank()) {
            throw new DonneesInvalidesException("Le nom de la salle est obligatoire");
        }
        if (capacite < CAPACITE_MINIMALE) {
            throw new DonneesInvalidesException("La capacité doit être supérieure ou égale à 1");
        }
        Salle salle = new Salle(null, nom, capacite);
        return repertoireSalle.sauvegarder(salle);
    }

    public List<Salle> listerSalles() {
        return repertoireSalle.trouverTous();
    }
}
