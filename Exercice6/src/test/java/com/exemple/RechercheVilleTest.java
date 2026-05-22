package com.exemple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RechercheVilleTest {

    private RechercheVille recherche;

    @BeforeEach
    void setUp() {
        recherche = new RechercheVille();
    }

    @Test
    @DisplayName("Une recherche de moins de 2 caracteres leve une NotImplementedException")
    void search_withLessThanTwoCharacters_throws_NotImplementedException() {
        // Given
        String mot = "V";

        // When / Then
        assertThrows(NotImplementedException.class, () -> recherche.Rechercher(mot));
    }

    @Test
    @DisplayName("Une recherche vide leve une NotImplementedException")
    void search_withEmptyString_throws_NotImplementedException() {
        // Given
        String mot = "";

        // When / Then
        assertThrows(NotImplementedException.class, () -> recherche.Rechercher(mot));
    }

    @Test
    @DisplayName("\"Va\" renvoie les villes commencant par Va : Valence et Vancouver")
    void search_withPrefixVa_returnsValenceAndVancouver() {
        // Given
        String mot = "Va";

        // When
        List<String> resultat = recherche.Rechercher(mot);

        // Then
        assertEquals(List.of("Valence", "Vancouver"), resultat);
    }

    @Test
    @DisplayName("La recherche est insensible a la casse : \"va\" renvoie Valence et Vancouver")
    void search_isCaseInsensitive() {
        // Given
        String mot = "va";

        // When
        List<String> resultat = recherche.Rechercher(mot);

        // Then
        assertEquals(List.of("Valence", "Vancouver"), resultat);
    }

    @Test
    @DisplayName("La recherche fonctionne sur une partie du nom : \"ape\" renvoie Budapest")
    void search_withTextInsideName_returnsBudapest() {
        // Given
        String mot = "ape";

        // When
        List<String> resultat = recherche.Rechercher(mot);

        // Then
        assertEquals(List.of("Budapest"), resultat);
    }

    @Test
    @DisplayName("\"*\" renvoie toutes les villes")
    void search_withAsterisk_returnsAllCities() {
        // Given
        String mot = "*";

        // When
        List<String> resultat = recherche.Rechercher(mot);

        // Then
        assertEquals(16, resultat.size());
    }
}
