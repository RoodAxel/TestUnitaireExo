package com.exemple.repository;

import com.exemple.model.Reservation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class RepertoireReservationEnMemoire implements RepertoireReservation {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Reservation sauvegarder(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(sequence.incrementAndGet());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> trouverParId(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> trouverParSalle(Long salleId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getSalleId().equals(salleId))
                .collect(Collectors.toList());
    }
}
