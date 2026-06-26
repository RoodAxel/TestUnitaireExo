package com.exemple.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.exemple.exception.ReservationImpossibleException;
import com.exemple.horloge.HorlogeSysteme;
import com.exemple.model.Adherent;
import com.exemple.model.Ouvrage;
import com.exemple.model.Pret;
import com.exemple.model.Reservation;
import com.exemple.service.ServicePret;
import com.exemple.service.ServiceReservation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationSteps {

    private final ServicePret servicePret = new ServicePret(new HorlogeSysteme());
    private final ServiceReservation serviceReservation = new ServiceReservation();
    private final Map<String, Adherent> adherents = new HashMap<>();
    private final Map<String, Ouvrage> ouvrages = new HashMap<>();
    private final Map<String, Pret> prets = new HashMap<>();

    private Reservation reservationHonoree;
    private RuntimeException erreur;

    private Adherent adherent(String nom) {
        return adherents.computeIfAbsent(nom, Adherent::new);
    }

    private Ouvrage ouvrage(String titre) {
        return ouvrages.computeIfAbsent(titre, titreOuvrage -> new Ouvrage(titreOuvrage, titreOuvrage));
    }

    @Given("un ouvrage {string} emprunté par {string}")
    public void un_ouvrage_emprunte_par(String titre, String nom) {
        prets.put(titre, servicePret.creerPret(adherent(nom), ouvrage(titre)));
    }

    @Given("un ouvrage disponible {string}")
    public void un_ouvrage_disponible(String titre) {
        ouvrage(titre);
    }

    @Given("un adhérent suspendu {string}")
    public void un_adherent_suspendu(String nom) {
        Adherent adherent = adherent(nom);
        adherent.enregistrerRetardImportant();
        adherent.enregistrerRetardImportant();
        adherent.enregistrerRetardImportant();
    }

    @When("{string} réserve l'ouvrage {string}")
    public void reserve_l_ouvrage(String nom, String titre) {
        serviceReservation.reserver(adherent(nom), ouvrage(titre));
    }

    @When("{string} tente de réserver l'ouvrage {string}")
    public void tente_de_reserver(String nom, String titre) {
        try {
            serviceReservation.reserver(adherent(nom), ouvrage(titre));
        } catch (RuntimeException exception) {
            erreur = exception;
        }
    }

    @When("{string} restitue l'ouvrage {string}")
    public void restitue_l_ouvrage(String nom, String titre) {
        servicePret.retourner(prets.get(titre));
        reservationHonoree = serviceReservation.honorerProchaine(ouvrage(titre));
    }

    @Then("{string} est en position {int} dans la file d'attente de {string}")
    public void est_en_position_dans_la_file(String nom, int position, String titre) {
        List<Reservation> file = serviceReservation.fileDAttente(ouvrage(titre));
        assertThat(file.get(position - 1).getAdherent().getNom()).isEqualTo(nom);
    }

    @Then("la réservation de {string} est honorée")
    public void la_reservation_est_honoree(String nom) {
        assertThat(reservationHonoree).isNotNull();
        assertThat(reservationHonoree.getAdherent().getNom()).isEqualTo(nom);
    }

    @Then("la file d'attente de {string} est vide")
    public void la_file_est_vide(String titre) {
        assertThat(serviceReservation.fileDAttente(ouvrage(titre))).isEmpty();
    }

    @Then("la réservation est refusée")
    public void la_reservation_est_refusee() {
        assertThat(erreur).isInstanceOf(ReservationImpossibleException.class);
    }
}
