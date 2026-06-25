package com.exemple.repository;

import com.exemple.model.Salle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class RepertoireSalleEnMemoire implements RepertoireSalle {

    private final Map<Long, Salle> salles = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Salle sauvegarder(Salle salle) {
        if (salle.getId() == null) {
            salle.setId(sequence.incrementAndGet());
        }
        salles.put(salle.getId(), salle);
        return salle;
    }

    @Override
    public Optional<Salle> trouverParId(Long id) {
        return Optional.ofNullable(salles.get(id));
    }

    @Override
    public List<Salle> trouverTous() {
        return new ArrayList<>(salles.values());
    }
}
