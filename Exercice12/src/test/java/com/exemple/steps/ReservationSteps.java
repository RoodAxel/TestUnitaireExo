package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.exemple.model.Reservation;
import com.exemple.model.StatutReservation;
import com.exemple.service.ServiceReservation;
import com.exemple.service.ServiceSalle;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservationSteps {

    private static final LocalDate JOUR = LocalDate.of(2026, 6, 25);
    private static final long SALLE_INEXISTANTE = 999999L;

    @Autowired
    private ServiceSalle serviceSalle;

    @Autowired
    private ServiceReservation serviceReservation;

    private Long salleId;
    private Reservation reservation;
    private RuntimeException erreur;

    @Given("une salle {string} d'une capacité de {int}")
    public void une_salle(String nom, int capacite) {
        salleId = serviceSalle.creerSalle(nom, capacite).getId();
    }

    @Given("une réservation confirmée de {string} sur cette salle de {string} à {string}")
    public void une_reservation_confirmee(String nom, String debut, String fin) {
        serviceReservation.creerReservation(salleId, nom, heure(debut), heure(fin));
    }

    @When("une personne {string} réserve la salle de {string} à {string}")
    public void une_personne_reserve(String nom, String debut, String fin) {
        reserver(salleId, nom, debut, fin);
    }

    @When("une personne {string} réserve une salle inexistante de {string} à {string}")
    public void une_personne_reserve_salle_inexistante(String nom, String debut, String fin) {
        reserver(SALLE_INEXISTANTE, nom, debut, fin);
    }

    @Then("la réservation est confirmée")
    public void la_reservation_est_confirmee() {
        assertNull(erreur);
        assertNotNull(reservation);
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
    }

    @Then("la réservation est refusée")
    public void la_reservation_est_refusee() {
        assertNotNull(erreur);
    }

    private void reserver(Long salleId, String nom, String debut, String fin) {
        try {
            reservation = serviceReservation.creerReservation(salleId, nom, heure(debut), heure(fin));
        } catch (RuntimeException e) {
            erreur = e;
        }
    }

    private LocalDateTime heure(String heure) {
        return LocalDateTime.of(JOUR, LocalTime.parse(heure));
    }
}
