package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.exemple.ConnexionRefuseeException;
import com.exemple.RepertoireUtilisateur;
import com.exemple.ResultatConnexion;
import com.exemple.ServiceAuthentification;
import com.exemple.Utilisateur;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;

public class ConnexionSteps {

    private final RepertoireUtilisateur repertoire = mock(RepertoireUtilisateur.class);
    private final ServiceAuthentification service = new ServiceAuthentification(repertoire);

    private ResultatConnexion resultat;
    private ConnexionRefuseeException erreur;

    @Given("un compte {string} avec le mot de passe {string}")
    public void un_compte_avec_mot_de_passe(String nomUtilisateur, String motDePasse) {
        when(repertoire.trouverParNomUtilisateur(nomUtilisateur))
                .thenReturn(Optional.of(new Utilisateur(nomUtilisateur + "@boutique.com", nomUtilisateur, motDePasse)));
    }

    @Given("il n'existe aucun compte au nom de {string}")
    public void il_n_existe_aucun_compte(String nomUtilisateur) {
        when(repertoire.trouverParNomUtilisateur(nomUtilisateur)).thenReturn(Optional.empty());
    }

    @When("l'utilisateur se connecte avec le nom d'utilisateur {string} et le mot de passe {string}")
    public void l_utilisateur_se_connecte(String nomUtilisateur, String motDePasse) {
        try {
            resultat = service.connecter(nomUtilisateur, motDePasse);
        } catch (ConnexionRefuseeException e) {
            erreur = e;
        }
    }

    @Then("la connexion réussit")
    public void la_connexion_reussit() {
        assertNull(erreur);
        assertNotNull(resultat);
    }

    @Then("l'utilisateur est redirigé vers la page {string}")
    public void l_utilisateur_est_redirige(String page) {
        assertEquals(page, resultat.pageRedirection());
    }

    @Then("la connexion échoue avec un message d'erreur")
    public void la_connexion_echoue() {
        assertNull(resultat);
        assertNotNull(erreur);
    }
}
