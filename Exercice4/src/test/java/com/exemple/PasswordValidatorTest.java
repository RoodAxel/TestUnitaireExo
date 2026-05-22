package com.exemple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @Test
    @DisplayName("isValid retourne true pour un mot de passe respectant toutes les regles")
    void isValid_avecMotDePasseConforme_retourneTrue() {
        // Given
        String password = "Password1!";

        // When
        boolean result = validator.isValid(password);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("isValid retourne false pour un mot de passe null")
    void isValid_avecMotDePasseNull_retourneFalse() {
        // Given
        String password = null;

        // When
        boolean result = validator.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("isValid retourne false pour un mot de passe trop court")
    void isValid_avecMotDePasseTropCourt_retourneFalse() {
        // Given
        String password = "short1!";

        // When
        boolean result = validator.isValid(password);

        // Then
        assertFalse(result);
    }

    @ParameterizedTest(name = "\"{0}\" est attendu comme valide={1}")
    @CsvSource({
            "Password1!, true",
            "Admin2024@, true",
            "short1!,    false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!,  false",
            "Password1,  false"
    })
    @DisplayName("isValid respecte le tableau des resultats attendus")
    void isValid_respecteLeTableauDesResultatsAttendus(String password, boolean expected) {
        // Given - le mot de passe fourni par @CsvSource

        // When
        boolean result = validator.isValid(password);

        // Then
        assertEquals(expected, result);
    }

    @ParameterizedTest(name = "\"{0}\" est valide")
    @ValueSource(strings = {"Password1!", "Admin2024@", "Strong9#", "Secure42$"})
    @DisplayName("isValid retourne true pour plusieurs mots de passe valides")
    void isValid_avecMotsDePasseValides_retourneTrue(String password) {
        // Given - le mot de passe fourni par @ValueSource

        // When
        boolean result = validator.isValid(password);

        // Then
        assertTrue(result);
    }

    @ParameterizedTest(name = "\"{0}\" est invalide")
    @NullAndEmptySource
    @DisplayName("isValid retourne false pour un mot de passe null ou vide")
    void isValid_avecMotDePasseNullOuVide_retourneFalse(String password) {
        // Given - null ou "" fourni par @NullAndEmptySource

        // When
        boolean result = validator.isValid(password);

        // Then
        assertFalse(result);
    }

    @ParameterizedTest(name = "\"{0}\" => \"{1}\"")
    @MethodSource("passwordsAndExpectedMessages")
    @DisplayName("getErrorMessage retourne le message attendu pour chaque cas")
    void getErrorMessage_retourneLeMessageAttendu(String password, String expectedMessage) {
        // When
        String message = validator.getErrorMessage(password);

        // Then
        assertEquals(expectedMessage, message);
    }

    static Stream<Arguments> passwordsAndExpectedMessages() {
        return Stream.of(
                Arguments.of(null, "Password must not be null"),
                Arguments.of("short1!", "Password must contain at least 8 characters"),
                Arguments.of("PASSWORD1!", "Password must contain at least one lowercase letter"),
                Arguments.of("password1!", "Password must contain at least one uppercase letter"),
                Arguments.of("Password!", "Password must contain at least one digit"),
                Arguments.of("Password1", "Password must contain at least one special character"),
                Arguments.of("Password1!", "Password is valid")
        );
    }

    @Test
    @DisplayName("getErrorMessage retourne le message null pour un mot de passe null")
    void getErrorMessage_avecMotDePasseNull_retourneMessageNull() {
        // Given
        String password = null;

        // When
        String message = validator.getErrorMessage(password);

        // Then
        assertEquals("Password must not be null", message);
    }

    @Test
    @DisplayName("getErrorMessage retourne le message de validite pour un mot de passe conforme")
    void getErrorMessage_avecMotDePasseConforme_retourneMessageValide() {
        // Given
        String password = "Admin2024@";

        // When
        String message = validator.getErrorMessage(password);

        // Then
        assertEquals("Password is valid", message);
    }
}
