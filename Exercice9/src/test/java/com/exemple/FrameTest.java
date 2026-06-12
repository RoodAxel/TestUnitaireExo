package com.exemple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FrameTest {

    @Mock
    private IGenerateur generateur;

    // ----- Série standard -----

    @Test
    @DisplayName("Le premier lancer d'une série standard augmente le score")
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        // Given
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(anyInt())).thenReturn(4);

        // When
        boolean lance = frame.makeRoll();

        // Then
        assertTrue(lance);
        assertEquals(4, frame.getScore());
    }

    @Test
    @DisplayName("Le second lancer d'une série standard augmente le score")
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        // Given
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);

        // When
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertTrue(lance);
        assertEquals(7, frame.getScore());
    }

    @Test
    @DisplayName("Un strike interdit un second lancer dans une série standard")
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        // Given
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(anyInt())).thenReturn(10);

        // When
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertFalse(lance);
        assertEquals(10, frame.getScore());
    }

    @Test
    @DisplayName("Une série standard ne permet pas un troisième lancer")
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        // Given
        Frame frame = new Frame(generateur, false);
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);

        // When
        frame.makeRoll();
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertFalse(lance);
        assertEquals(7, frame.getScore());
    }

    // ----- Série finale (dernier round) -----

    @Test
    @DisplayName("Dans la série finale, un strike autorise un second lancer")
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);

        // When
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertTrue(lance);
    }

    @Test
    @DisplayName("Dans la série finale, le lancer suivant un strike augmente le score")
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);

        // When
        frame.makeRoll();
        frame.makeRoll();

        // Then
        assertEquals(15, frame.getScore());
    }

    @Test
    @DisplayName("Dans la série finale, un strike autorise un troisième lancer")
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 3);

        // When
        frame.makeRoll();
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertTrue(lance);
    }

    @Test
    @DisplayName("Dans la série finale, le troisième lancer après un strike augmente le score")
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 3);

        // When
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        // Then
        assertEquals(18, frame.getScore());
    }

    @Test
    @DisplayName("Dans la série finale, un spare autorise un troisième lancer")
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(4, 6, 3);

        // When
        frame.makeRoll();
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertTrue(lance);
    }

    @Test
    @DisplayName("Dans la série finale, le troisième lancer après un spare augmente le score")
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(4, 6, 3);

        // When
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        // Then
        assertEquals(13, frame.getScore());
    }

    @Test
    @DisplayName("Dans la série finale sans strike ni spare, le troisième lancer est interdit")
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(4, 3);

        // When
        frame.makeRoll();
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertFalse(lance);
        assertEquals(7, frame.getScore());
    }

    @Test
    @DisplayName("La série finale ne permet pas un quatrième lancer")
    void shouldRejectFourthRollInLastFrame() {
        // Given
        Frame frame = new Frame(generateur, true);
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 3);

        // When
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();
        boolean lance = frame.makeRoll();

        // Then
        assertFalse(lance);
    }
}
