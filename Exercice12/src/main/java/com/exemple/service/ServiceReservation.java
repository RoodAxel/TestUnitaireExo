package com.exemple.service;

import com.exemple.model.Reservation;
import com.exemple.repository.RepertoireReservation;
import com.exemple.repository.RepertoireSalle;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class ServiceReservation {

    private final RepertoireSalle repertoireSalle;
    private final RepertoireReservation repertoireReservation;

    public ServiceReservation(RepertoireSalle repertoireSalle, RepertoireReservation repertoireReservation) {
        this.repertoireSalle = repertoireSalle;
        this.repertoireReservation = repertoireReservation;
    }

    public Reservation creerReservation(Long salleId, String nomPersonne, LocalDateTime debut, LocalDateTime fin) {
        throw new UnsupportedOperationException("creerReservation n'est pas encore implémentée");
    }

    public Reservation trouverParId(Long id) {
        throw new UnsupportedOperationException("trouverParId n'est pas encore implémentée");
    }

    public Reservation annuler(Long id) {
        throw new UnsupportedOperationException("annuler n'est pas encore implémentée");
    }
}
