package com.exemple.service;

import com.exemple.model.Salle;
import com.exemple.repository.RepertoireSalle;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceSalle {

    private final RepertoireSalle repertoireSalle;

    public ServiceSalle(RepertoireSalle repertoireSalle) {
        this.repertoireSalle = repertoireSalle;
    }

    public Salle creerSalle(String nom, int capacite) {
        throw new UnsupportedOperationException("creerSalle n'est pas encore implémentée");
    }

    public List<Salle> listerSalles() {
        throw new UnsupportedOperationException("listerSalles n'est pas encore implémentée");
    }
}
