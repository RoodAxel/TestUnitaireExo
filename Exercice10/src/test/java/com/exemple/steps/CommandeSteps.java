package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.exemple.Commande;
import com.exemple.CommandeRefuseeException;
import com.exemple.ConfirmationCommande;
import com.exemple.RepertoireCommande;
import com.exemple.ServiceCommande;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;

public class CommandeSteps {

    private final RepertoireCommande repertoireCommande = mock(RepertoireCommande.class);
    private final ServiceCommande service = new ServiceCommande(repertoireCommande);

    private Commande commande;
    private ConfirmationCommande confirmation;
    private CommandeRefuseeException erreur;

    @Given("une commande {string} vide")
    public void une_commande_vide(String commandeId) {
        commande = new Commande(commandeId);
        when(repertoireCommande.trouverParId(commandeId)).thenReturn(Optional.of(commande));
    }

    @Given("une commande {string} contenant {int} unité(s) du produit {string}")
    public void une_commande_contenant(String commandeId, int quantite, String reference) {
        commande = new Commande(commandeId);
        for (int i = 0; i < quantite; i++) {
            commande.ajouter(reference);
        }
        when(repertoireCommande.trouverParId(commandeId)).thenReturn(Optional.of(commande));
    }

    @Given("la commande {string} n'existe pas")
    public void la_commande_n_existe_pas(String commandeId) {
        when(repertoireCommande.trouverParId(commandeId)).thenReturn(Optional.empty());
    }

    @When("l'utilisateur ajoute le produit {string} à la commande {string}")
    public void l_utilisateur_ajoute_produit(String reference, String commandeId) {
        try {
            service.ajouterProduit(commandeId, reference);
        } catch (CommandeRefuseeException e) {
            erreur = e;
        }
    }

    @When("l'utilisateur supprime le produit {string} de la commande {string}")
    public void l_utilisateur_supprime_produit(String reference, String commandeId) {
        try {
            service.supprimerProduit(commandeId, reference);
        } catch (CommandeRefuseeException e) {
            erreur = e;
        }
    }

    @When("l'utilisateur valide la commande {string}")
    public void l_utilisateur_valide_commande(String commandeId) {
        try {
            confirmation = service.validerCommande(commandeId);
        } catch (CommandeRefuseeException e) {
            erreur = e;
        }
    }

    @Then("la quantité du produit {string} dans la commande est {int}")
    public void la_quantite_du_produit_est(String reference, int quantite) {
        assertEquals(quantite, commande.quantite(reference));
    }

    @Then("le produit {string} n'est plus présent dans la commande")
    public void le_produit_n_est_plus_present(String reference) {
        assertFalse(commande.contient(reference));
    }

    @Then("la commande est validée")
    public void la_commande_est_validee() {
        assertNull(erreur);
        assertNotNull(confirmation);
    }

    @Then("une erreur de commande est renvoyée")
    public void une_erreur_de_commande_est_renvoyee() {
        assertNotNull(erreur);
    }
}
