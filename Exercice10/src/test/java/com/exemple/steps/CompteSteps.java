package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.exemple.CompteExistantException;
import com.exemple.ConfirmationInscription;
import com.exemple.RepertoireUtilisateur;
import com.exemple.ServiceCompte;
import com.exemple.Utilisateur;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CompteSteps {

    private final RepertoireUtilisateur repertoire = mock(RepertoireUtilisateur.class);
    private final ServiceCompte service = new ServiceCompte(repertoire);

    private ConfirmationInscription confirmation;
    private CompteExistantException erreur;

    @Given("aucun compte n'existe pour le nom d'utilisateur {string}")
    public void aucun_compte_n_existe(String nomUtilisateur) {
        when(repertoire.existeParNomUtilisateur(nomUtilisateur)).thenReturn(false);
    }

    @Given("un compte existe déjà pour le nom d'utilisateur {string}")
    public void un_compte_existe_deja(String nomUtilisateur) {
        when(repertoire.existeParNomUtilisateur(nomUtilisateur)).thenReturn(true);
    }

    @When("l'utilisateur s'inscrit avec l'email {string}, le nom d'utilisateur {string} et le mot de passe {string}")
    public void l_utilisateur_s_inscrit(String email, String nomUtilisateur, String motDePasse) {
        try {
            confirmation = service.inscrire(new Utilisateur(email, nomUtilisateur, motDePasse));
        } catch (CompteExistantException e) {
            erreur = e;
        }
    }

    @Then("l'inscription est confirmée")
    public void l_inscription_est_confirmee() {
        assertNull(erreur);
        assertNotNull(confirmation);
    }

    @Then("le compte {string} est enregistré")
    public void le_compte_est_enregistre(String nomUtilisateur) {
        verify(repertoire).enregistrer(argThat(utilisateur -> utilisateur.nomUtilisateur().equals(nomUtilisateur)));
    }

    @Then("l'inscription est refusée car le compte existe déjà")
    public void l_inscription_est_refusee() {
        assertNull(confirmation);
        assertNotNull(erreur);
        verify(repertoire, never()).enregistrer(any(Utilisateur.class));
    }
}
