package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.ConfirmationReservation;
import com.exemple.PlanningReservation;
import com.exemple.RepertoireSalle;
import com.exemple.Reservation;
import com.exemple.ReservationRefuseeException;
import com.exemple.Salle;
import com.exemple.ServiceNotification;
import com.exemple.ServiceReservation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationSteps {

    private final RepertoireSalle repertoire = mock(RepertoireSalle.class);
    private final PlanningReservation planning = mock(PlanningReservation.class);
    private final ServiceNotification notification = mock(ServiceNotification.class);
    private final ServiceReservation service = new ServiceReservation(repertoire, planning, notification);

    private final List<Reservation> reservationsExistantes = new ArrayList<>();
    private ConfirmationReservation confirmation;
    private ReservationRefuseeException erreur;

    @Given("une salle {string} nommée {string} avec une capacité de {int}")
    public void une_salle_existe(String code, String nom, int capacite) {
        when(repertoire.trouverParCode(code)).thenReturn(Optional.of(new Salle(code, nom, capacite)));
    }

    @Given("la salle {string} est déjà réservée de {string} à {string}")
    public void la_salle_est_deja_reservee(String code, String debut, String fin) {
        reservationsExistantes.add(new Reservation("autre@entreprise.com", code, 1,
                LocalDateTime.parse(debut), LocalDateTime.parse(fin)));
        when(planning.reservationsPour(code)).thenReturn(reservationsExistantes);
    }

    @When("l'utilisateur réserve la salle {string} pour {int} participants de {string} à {string}")
    public void l_utilisateur_reserve(String code, int participants, String debut, String fin) {
        Reservation reservation = new Reservation("user@entreprise.com", code, participants,
                LocalDateTime.parse(debut), LocalDateTime.parse(fin));
        try {
            confirmation = service.reserver(reservation);
        } catch (ReservationRefuseeException e) {
            erreur = e;
        }
    }

    @Then("la réservation est acceptée")
    public void la_reservation_est_acceptee() {
        assertNull(erreur);
        assertNotNull(confirmation);
    }

    @Then("la réservation est refusée")
    public void la_reservation_est_refusee() {
        assertNull(confirmation);
        assertNotNull(erreur);
    }

    @Then("une confirmation est envoyée")
    public void une_confirmation_est_envoyee() {
        verify(notification).envoyerConfirmation(any(Reservation.class));
    }

    @Then("aucune confirmation n'est envoyée")
    public void aucune_confirmation_n_est_envoyee() {
        verify(notification, never()).envoyerConfirmation(any(Reservation.class));
    }
}
