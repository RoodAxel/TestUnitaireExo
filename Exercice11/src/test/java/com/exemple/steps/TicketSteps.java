package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.exemple.exception.TicketIntrouvableException;
import com.exemple.exception.TransitionInterditeException;
import com.exemple.model.Priorite;
import com.exemple.model.Statut;
import com.exemple.model.Ticket;
import com.exemple.service.ServiceTicket;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class TicketSteps {

    @Autowired
    private ServiceTicket serviceTicket;

    private String titre;
    private Priorite priorite;
    private Ticket ticket;
    private TransitionInterditeException erreurTransition;
    private TicketIntrouvableException erreurIntrouvable;

    @Given("un titre {string} et une priorité {string}")
    public void un_titre_et_une_priorite(String titre, String priorite) {
        this.titre = titre;
        this.priorite = Priorite.valueOf(priorite);
    }

    @Given("un ticket ouvert intitulé {string} de priorité {string}")
    public void un_ticket_ouvert(String titre, String priorite) {
        ticket = serviceTicket.creerTicket(titre, Priorite.valueOf(priorite));
    }

    @Given("un ticket résolu intitulé {string} de priorité {string}")
    public void un_ticket_resolu(String titre, String priorite) {
        ticket = serviceTicket.creerTicket(titre, Priorite.valueOf(priorite));
        ticket = serviceTicket.changerStatut(ticket.getId(), Statut.RESOLVED);
    }

    @When("l'utilisateur crée le ticket")
    public void l_utilisateur_cree_le_ticket() {
        ticket = serviceTicket.creerTicket(titre, priorite);
    }

    @When("l'utilisateur change le statut du ticket en {string}")
    public void l_utilisateur_change_le_statut(String statut) {
        try {
            ticket = serviceTicket.changerStatut(ticket.getId(), Statut.valueOf(statut));
        } catch (TransitionInterditeException e) {
            erreurTransition = e;
        }
    }

    @When("l'utilisateur consulte le ticket numéro {long}")
    public void l_utilisateur_consulte_le_ticket(long id) {
        try {
            ticket = serviceTicket.trouverParId(id);
        } catch (TicketIntrouvableException e) {
            erreurIntrouvable = e;
        }
    }

    @Then("le ticket est créé avec le statut {string}")
    public void le_ticket_est_cree_avec_le_statut(String statut) {
        assertNotNull(ticket);
        assertNotNull(ticket.getId());
        assertEquals(Statut.valueOf(statut), ticket.getStatut());
    }

    @Then("le ticket a le statut {string}")
    public void le_ticket_a_le_statut(String statut) {
        assertEquals(Statut.valueOf(statut), ticket.getStatut());
    }

    @Then("une erreur de conflit de statut est renvoyée")
    public void une_erreur_de_conflit_de_statut_est_renvoyee() {
        assertNotNull(erreurTransition);
    }

    @Then("une erreur de ticket introuvable est renvoyée")
    public void une_erreur_de_ticket_introuvable_est_renvoyee() {
        assertNotNull(erreurIntrouvable);
    }
}
