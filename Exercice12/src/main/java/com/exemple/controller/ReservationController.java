package com.exemple.controller;

import com.exemple.dto.CreerReservationRequest;
import com.exemple.model.Reservation;
import com.exemple.service.ServiceReservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class
ReservationController {

    private final ServiceReservation serviceReservation;

    public ReservationController(ServiceReservation serviceReservation) {
        this.serviceReservation = serviceReservation;
    }

    @PostMapping
    public ResponseEntity<Reservation> creer(@RequestBody CreerReservationRequest request) {
        Reservation reservation = serviceReservation.creerReservation(
                request.salleId(), request.nomPersonne(), request.debut(), request.fin());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/{id}")
    public Reservation consulter(@PathVariable Long id) {
        return serviceReservation.trouverParId(id);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation annuler(@PathVariable Long id) {
        return serviceReservation.annuler(id);
    }
}
