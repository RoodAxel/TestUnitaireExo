package com.exemple.service;

import com.exemple.exception.ReservationImpossibleException;
import com.exemple.model.Adherent;
import com.exemple.model.Ouvrage;
import com.exemple.model.Reservation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceReservation {

    private final Map<String, Deque<Reservation>> filesParOuvrage = new HashMap<>();

    public Reservation reserver(Adherent adherent, Ouvrage ouvrage) {
        if (adherent.estSuspendu()) {
            throw new ReservationImpossibleException("Adhérent suspendu");
        }
        if (ouvrage.estDisponible()) {
            throw new ReservationImpossibleException("Ouvrage disponible, réservation inutile");
        }
        Reservation reservation = new Reservation(adherent, ouvrage);
        filesParOuvrage.computeIfAbsent(ouvrage.getId(), cle -> new ArrayDeque<>()).addLast(reservation);
        return reservation;
    }

    public List<Reservation> fileDAttente(Ouvrage ouvrage) {
        return new ArrayList<>(filesParOuvrage.getOrDefault(ouvrage.getId(), new ArrayDeque<>()));
    }

    public Reservation honorerProchaine(Ouvrage ouvrage) {
        return filesParOuvrage.getOrDefault(ouvrage.getId(), new ArrayDeque<>()).pollFirst();
    }
}
