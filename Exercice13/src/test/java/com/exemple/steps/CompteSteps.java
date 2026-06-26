package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.exemple.exception.ConflitMetierException;
import com.exemple.repository.RepertoireCompteEnMemoire;
import com.exemple.service.ServiceCompte;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;

public class CompteSteps {

    @Autowired
    private ServiceCompte serviceCompte;

    @Autowired
    private RepertoireCompteEnMemoire repertoire;

    private RuntimeException erreur;

    @Before
    public void reinitialiser() {
        repertoire.vider();
        erreur = null;
    }

    @Given("un compte {string} au nom de {string}")
    public void un_compte(String numero, String titulaire) {
        serviceCompte.creerCompte(numero, titulaire);
    }

    @Given("un compte {string} au nom de {string} avec un solde de {int}")
    public void un_compte_avec_solde(String numero, String titulaire, int solde) {
        serviceCompte.creerCompte(numero, titulaire);
        serviceCompte.deposer(numero, BigDecimal.valueOf(solde));
    }

    @When("je crée un compte {string} au nom de {string}")
    public void je_cree_un_compte(String numero, String titulaire) {
        executer(() -> serviceCompte.creerCompte(numero, titulaire));
    }

    @When("je dépose {int} sur le compte {string}")
    public void je_depose(int montant, String numero) {
        executer(() -> serviceCompte.deposer(numero, BigDecimal.valueOf(montant)));
    }

    @When("je retire {int} du compte {string}")
    public void je_retire(int montant, String numero) {
        executer(() -> serviceCompte.retirer(numero, BigDecimal.valueOf(montant)));
    }

    @When("je vire {int} du compte {string} vers le compte {string}")
    public void je_vire(int montant, String source, String destination) {
        executer(() -> serviceCompte.virer(source, destination, BigDecimal.valueOf(montant)));
    }

    @Then("le compte {string} existe avec un solde de {int}")
    public void le_compte_existe_avec_un_solde(String numero, int solde) {
        assertEquals(0, serviceCompte.consulterCompte(numero).getSolde().compareTo(BigDecimal.valueOf(solde)));
    }

    @Then("le solde du compte {string} est {int}")
    public void le_solde_du_compte_est(String numero, int solde) {
        assertEquals(0, serviceCompte.consulterCompte(numero).getSolde().compareTo(BigDecimal.valueOf(solde)));
    }

    @Then("l'opération est refusée")
    public void l_operation_est_refusee() {
        assertInstanceOf(ConflitMetierException.class, erreur);
    }

    private void executer(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException exception) {
            erreur = exception;
        }
    }
}
