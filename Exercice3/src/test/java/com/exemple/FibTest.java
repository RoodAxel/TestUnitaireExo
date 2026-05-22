package com.exemple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FibTest {

    @Test
    @DisplayName("getFibSeries avec un range de 1 ne retourne pas une liste vide")
    void getFibSeries_avecRange1_neRetournePasUneListeVide() {
        // Given
        Fib fib = new Fib(1);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("getFibSeries avec un range de 1 retourne la liste {0}")
    void getFibSeries_avecRange1_retourneLaListeContenantUniquementZero() {
        // Given
        Fib fib = new Fib(1);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        assertEquals(List.of(0), result);
    }

    @Test
    @DisplayName("getFibSeries avec un range de 6 contient le chiffre 3")
    void getFibSeries_avecRange6_contientLeChiffre3() {
        // Given
        Fib fib = new Fib(6);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        assertTrue(result.contains(3));
    }

    @Test
    @DisplayName("getFibSeries avec un range de 6 contient 6 elements")
    void getFibSeries_avecRange6_contient6Elements() {
        // Given
        Fib fib = new Fib(6);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        assertEquals(6, result.size());
    }

    @Test
    @DisplayName("getFibSeries avec un range de 6 ne contient pas le chiffre 4")
    void getFibSeries_avecRange6_neContientPasLeChiffre4() {
        // Given
        Fib fib = new Fib(6);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        assertFalse(result.contains(4));
    }

    @Test
    @DisplayName("getFibSeries avec un range de 6 retourne la liste {0, 1, 1, 2, 3, 5}")
    void getFibSeries_avecRange6_retourneLaListeAttendue() {
        // Given
        Fib fib = new Fib(6);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        assertEquals(List.of(0, 1, 1, 2, 3, 5), result);
    }

    @Test
    @DisplayName("getFibSeries avec un range de 6 retourne une liste triee de facon ascendante")
    void getFibSeries_avecRange6_retourneUneListeTrieeAscendante() {
        // Given
        Fib fib = new Fib(6);

        // When
        List<Integer> result = fib.getFibSeries();

        // Then
        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i - 1) <= result.get(i),
                    "La liste doit etre triee de facon ascendante");
        }
    }
}
