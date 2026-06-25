package com.exemple.repository;

import com.exemple.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface RepertoireReservation {

    Reservation sauvegarder(Reservation reservation);

    Optional<Reservation> trouverParId(Long id);

    List<Reservation> trouverParSalle(Long salleId);
}
