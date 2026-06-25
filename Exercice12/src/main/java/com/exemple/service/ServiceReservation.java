package com.exemple.service;

import com.exemple.exception.ConflitMetierException;
import com.exemple.exception.DonneesInvalidesException;
import com.exemple.exception.RessourceIntrouvableException;
import com.exemple.model.Reservation;
import com.exemple.model.StatutReservation;
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
        repertoireSalle.trouverParId(salleId)
                .orElseThrow(() -> new RessourceIntrouvableException("Salle introuvable : " + salleId));

        if (nomPersonne == null || nomPersonne.isBlank()) {
            throw new DonneesInvalidesException("Le nom de la personne qui réserve est obligatoire");
        }
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            throw new DonneesInvalidesException("La date de fin doit être strictement après la date de début");
        }
        if (chevauche(salleId, debut, fin)) {
            throw new ConflitMetierException("Le créneau chevauche une réservation confirmée existante");
        }

        Reservation reservation = new Reservation(null, salleId, nomPersonne, debut, fin, StatutReservation.CONFIRMEE);
        return repertoireReservation.sauvegarder(reservation);
    }

    public Reservation trouverParId(Long id) {
        return repertoireReservation.trouverParId(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Réservation introuvable : " + id));
    }

    public Reservation annuler(Long id) {
        Reservation reservation = trouverParId(id);
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new ConflitMetierException("La réservation est déjà annulée : " + id);
        }
        reservation.setStatut(StatutReservation.ANNULEE);
        return repertoireReservation.sauvegarder(reservation);
    }

    private boolean chevauche(Long salleId, LocalDateTime debut, LocalDateTime fin) {
        return repertoireReservation.trouverParSalle(salleId).stream()
                .filter(reservation -> reservation.getStatut() == StatutReservation.CONFIRMEE)
                .anyMatch(reservation -> debut.isBefore(reservation.getFin()) && reservation.getDebut().isBefore(fin));
    }
}
