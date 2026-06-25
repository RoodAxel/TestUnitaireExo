package com.exemple.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.exemple.exception.DonneesInvalidesException;
import com.exemple.model.Salle;
import com.exemple.repository.RepertoireSalle;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceSalleTest {

    @Mock
    private RepertoireSalle repertoireSalle;

    @Test
    @DisplayName("La création d'une salle valide renvoie la salle avec son nom et sa capacité")
    void shouldCreateRoomWhenDataIsValid() {
        // Given
        ServiceSalle service = new ServiceSalle(repertoireSalle);
        when(repertoireSalle.sauvegarder(any(Salle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Salle salle = service.creerSalle("Salle Bleue", 8);

        // Then
        assertEquals("Salle Bleue", salle.getNom());
        assertEquals(8, salle.getCapacite());
    }

    @Test
    @DisplayName("La création est refusée quand le nom est absent")
    void shouldRejectCreationWhenNameIsBlank() {
        // Given
        ServiceSalle service = new ServiceSalle(repertoireSalle);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.creerSalle("   ", 8));
    }

    @Test
    @DisplayName("La création est refusée quand la capacité est inférieure à 1")
    void shouldRejectCreationWhenCapacityIsBelowOne() {
        // Given
        ServiceSalle service = new ServiceSalle(repertoireSalle);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.creerSalle("Salle Bleue", 0));
    }

    @Test
    @DisplayName("La liste des salles renvoie toutes les salles du répertoire")
    void shouldReturnAllRooms() {
        // Given
        ServiceSalle service = new ServiceSalle(repertoireSalle);
        Salle premiere = new Salle(1L, "Salle Bleue", 8);
        Salle seconde = new Salle(2L, "Salle Rouge", 12);
        when(repertoireSalle.trouverTous()).thenReturn(List.of(premiere, seconde));

        // When
        List<Salle> salles = service.listerSalles();

        // Then
        assertEquals(List.of(premiere, seconde), salles);
    }
}
