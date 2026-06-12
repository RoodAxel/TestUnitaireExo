package com.exemple.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.exemple.CatalogueProduit;
import com.exemple.Produit;
import com.exemple.ServiceCatalogue;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;

public class CatalogueSteps {

    private final CatalogueProduit catalogue = mock(CatalogueProduit.class);
    private final ServiceCatalogue service = new ServiceCatalogue(catalogue);

    private List<Produit> resultats;

    @Given("la recherche par mot-clé {string} renvoie les produits:")
    public void la_recherche_par_mot_cle_renvoie(String motCle, DataTable table) {
        when(catalogue.rechercherParMotCle(motCle)).thenReturn(versProduits(table));
    }

    @Given("la recherche par prix maximum {double} renvoie les produits:")
    public void la_recherche_par_prix_max_renvoie(double prixMax, DataTable table) {
        when(catalogue.rechercherParPrixMax(prixMax)).thenReturn(versProduits(table));
    }

    @Given("la catégorie {string} contient les produits:")
    public void la_categorie_contient(String categorie, DataTable table) {
        when(catalogue.trouverParCategorie(categorie)).thenReturn(versProduits(table));
    }

    @When("l'utilisateur recherche les produits avec le mot-clé {string}")
    public void l_utilisateur_recherche_mot_cle(String motCle) {
        resultats = service.rechercher(motCle);
    }

    @When("l'utilisateur recherche les produits jusqu'au prix {double}")
    public void l_utilisateur_recherche_prix_max(double prixMax) {
        resultats = service.rechercherParPrixMaximum(prixMax);
    }

    @When("l'utilisateur sélectionne la catégorie {string}")
    public void l_utilisateur_selectionne_categorie(String categorie) {
        resultats = service.parCategorie(categorie);
    }

    @Then("le nombre de produits retournés est {int}")
    public void le_nombre_de_produits_retournes_est(int nombre) {
        assertEquals(nombre, resultats.size());
    }

    @Then("le produit {string} fait partie des résultats")
    public void le_produit_fait_partie_des_resultats(String reference) {
        assertTrue(resultats.stream().anyMatch(produit -> produit.reference().equals(reference)));
    }

    private List<Produit> versProduits(DataTable table) {
        return table.asMaps().stream()
                .map(this::versProduit)
                .toList();
    }

    private Produit versProduit(Map<String, String> ligne) {
        double prix = ligne.containsKey("prix") ? Double.parseDouble(ligne.get("prix")) : 0.0;
        String categorie = ligne.getOrDefault("categorie", "");
        return new Produit(ligne.get("reference"), ligne.get("nom"), prix, categorie);
    }
}
