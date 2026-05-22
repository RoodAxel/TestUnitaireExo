package com.exemple;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiceScoreTest {

    @Mock
    private Ide de;

    @InjectMocks
    private DiceScore diceScore;

    @Test
    @DisplayName("Deux des identiques (differents de 6) renvoient valeur * 2 + 10")
    void getScore_avecDeuxDesIdentiquesDifferentsDe6_retourneValeurFois2Plus10() {
        // Given
        when(de.getRoll()).thenReturn(3, 3);

        // When
        int score = diceScore.getScore();

        // Then
        assertEquals(16, score);
        verify(de, times(2)).getRoll();
    }

    @Test
    @DisplayName("Deux des identiques egaux a 6 renvoient 30")
    void getScore_avecDeuxDesIdentiquesEgauxA6_retourne30() {
        // Given
        when(de.getRoll()).thenReturn(6, 6);

        // When
        int score = diceScore.getScore();

        // Then
        assertEquals(30, score);
    }

    @Test
    @DisplayName("Des differents : le premier est le plus petit, on renvoie le plus grand")
    void getScore_avecPremierDePlusPetit_retourneLePlusGrand() {
        // Given
        when(de.getRoll()).thenReturn(2, 5);

        // When
        int score = diceScore.getScore();

        // Then
        assertEquals(5, score);
    }

    @Test
    @DisplayName("Des differents : le premier est le plus grand, on renvoie le plus grand")
    void getScore_avecPremierDePlusGrand_retourneLePlusGrand() {
        // Given
        when(de.getRoll()).thenReturn(5, 2);

        // When
        int score = diceScore.getScore();

        // Then
        assertEquals(5, score);
    }
}
