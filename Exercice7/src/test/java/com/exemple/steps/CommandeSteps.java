package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.exemple.CatalogueProduit;
import com.exemple.Commande;
import com.exemple.CommandeRefuseeException;
import com.exemple.Produit;
import com.exemple.ProfilClient;
import com.exemple.RecuCommande;
import com.exemple.ServiceCommande;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;

public class CommandeSteps {

    private final CatalogueProduit catalogue = mock(CatalogueProduit.class);
    private final ServiceCommande service = new ServiceCommande(catalogue);

    private RecuCommande recu;
    private CommandeRefuseeException erreur;

    @Given("un produit {string} nommé {string} au prix unitaire de {double} avec un stock de {int}")
    public void un_produit_existe_dans_le_catalogue(String reference, String nom, double prix, int stock) {
        when(catalogue.trouverParReference(reference))
                .thenReturn(Optional.of(new Produit(reference, nom, prix, stock)));
    }

    @When("le client {string} commande {int} unités du produit {string}")
    public void le_client_passe_une_commande(String profil, int quantite, String reference) {
        Commande commande = new Commande("client@boutique.com", reference, quantite);
        try {
            recu = service.passerCommande(commande, ProfilClient.valueOf(profil));
        } catch (CommandeRefuseeException e) {
            erreur = e;
        }
    }

    @Then("la commande est acceptée")
    public void la_commande_est_acceptee() {
        assertNull(erreur);
        assertNotNull(recu);
    }

    @Then("la commande est refusée")
    public void la_commande_est_refusee() {
        assertNull(recu);
        assertNotNull(erreur);
    }

    @Then("le montant total du reçu est {double}")
    public void le_montant_total_du_recu_est(double montantAttendu) {
        assertEquals(montantAttendu, recu.montantTotal(), 0.001);
    }
}
