package com.exemple.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.exemple.exception.ConflitMetierException;
import com.exemple.exception.DonneesInvalidesException;
import com.exemple.exception.RessourceIntrouvableException;
import com.exemple.model.Reservation;
import com.exemple.model.Salle;
import com.exemple.model.StatutReservation;
import com.exemple.repository.RepertoireReservation;
import com.exemple.repository.RepertoireSalle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceReservationTest {

    private static final LocalDateTime DEBUT = LocalDateTime.of(2026, 6, 25, 9, 0);
    private static final LocalDateTime FIN = LocalDateTime.of(2026, 6, 25, 10, 0);

    @Mock
    private RepertoireSalle repertoireSalle;

    @Mock
    private RepertoireReservation repertoireReservation;

    // ----- Création d'une réservation -----

    @Test
    @DisplayName("Une réservation valide est créée avec le statut CONFIRMEE")
    void shouldCreateReservationWhenDataIsValid() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireSalle.trouverParId(1L)).thenReturn(Optional.of(new Salle(1L, "Salle Bleue", 8)));
        when(repertoireReservation.trouverParSalle(1L)).thenReturn(List.of());
        when(repertoireReservation.sauvegarder(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reservation reservation = service.creerReservation(1L, "Alice", DEBUT, FIN);

        // Then
        assertEquals("Alice", reservation.getNomPersonne());
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
    }

    @Test
    @DisplayName("La réservation est refusée quand la salle n'existe pas")
    void shouldRejectReservationWhenRoomDoesNotExist() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireSalle.trouverParId(99L)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(RessourceIntrouvableException.class,
                () -> service.creerReservation(99L, "Alice", DEBUT, FIN));
    }

    @Test
    @DisplayName("La réservation est refusée quand le nom de la personne est absent")
    void shouldRejectReservationWhenPersonNameIsBlank() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireSalle.trouverParId(1L)).thenReturn(Optional.of(new Salle(1L, "Salle Bleue", 8)));

        // When // Then
        assertThrows(DonneesInvalidesException.class,
                () -> service.creerReservation(1L, "   ", DEBUT, FIN));
    }

    @Test
    @DisplayName("La réservation est refusée quand la fin n'est pas strictement après le début")
    void shouldRejectReservationWhenSlotIsInvalid() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireSalle.trouverParId(1L)).thenReturn(Optional.of(new Salle(1L, "Salle Bleue", 8)));

        // When // Then
        assertThrows(DonneesInvalidesException.class,
                () -> service.creerReservation(1L, "Alice", FIN, DEBUT));
    }

    @Test
    @DisplayName("La réservation est refusée quand le créneau chevauche une réservation confirmée")
    void shouldRejectReservationWhenSlotOverlapsConfirmedReservation() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireSalle.trouverParId(1L)).thenReturn(Optional.of(new Salle(1L, "Salle Bleue", 8)));
        Reservation existante = new Reservation(1L, 1L, "Bob",
                LocalDateTime.of(2026, 6, 25, 9, 30),
                LocalDateTime.of(2026, 6, 25, 10, 30),
                StatutReservation.CONFIRMEE);
        when(repertoireReservation.trouverParSalle(1L)).thenReturn(List.of(existante));

        // When // Then
        assertThrows(ConflitMetierException.class,
                () -> service.creerReservation(1L, "Alice", DEBUT, FIN));
    }

    @Test
    @DisplayName("Une réservation est acceptée quand le créneau ne chevauche qu'une réservation annulée")
    void shouldAcceptReservationWhenOverlappingReservationIsCancelled() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireSalle.trouverParId(1L)).thenReturn(Optional.of(new Salle(1L, "Salle Bleue", 8)));
        Reservation annulee = new Reservation(1L, 1L, "Bob",
                LocalDateTime.of(2026, 6, 25, 9, 30),
                LocalDateTime.of(2026, 6, 25, 10, 30),
                StatutReservation.ANNULEE);
        when(repertoireReservation.trouverParSalle(1L)).thenReturn(List.of(annulee));
        when(repertoireReservation.sauvegarder(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reservation reservation = service.creerReservation(1L, "Alice", DEBUT, FIN);

        // Then
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
    }

    // ----- Consultation -----

    @Test
    @DisplayName("La consultation d'une réservation inexistante lève une erreur")
    void shouldThrowNotFoundWhenReservationDoesNotExist() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        when(repertoireReservation.trouverParId(99L)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(RessourceIntrouvableException.class, () -> service.trouverParId(99L));
    }

    // ----- Annulation -----

    @Test
    @DisplayName("Une réservation confirmée peut être annulée")
    void shouldCancelConfirmedReservation() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        Reservation reservation = new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMEE);
        when(repertoireReservation.trouverParId(1L)).thenReturn(Optional.of(reservation));
        when(repertoireReservation.sauvegarder(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reservation resultat = service.annuler(1L);

        // Then
        assertEquals(StatutReservation.ANNULEE, resultat.getStatut());
    }

    @Test
    @DisplayName("Une réservation déjà annulée ne peut pas être annulée une seconde fois")
    void shouldRejectCancellationWhenReservationAlreadyCancelled() {
        // Given
        ServiceReservation service = new ServiceReservation(repertoireSalle, repertoireReservation);
        Reservation reservation = new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.ANNULEE);
        when(repertoireReservation.trouverParId(1L)).thenReturn(Optional.of(reservation));

        // When // Then
        assertThrows(ConflitMetierException.class, () -> service.annuler(1L));
        assertEquals(StatutReservation.ANNULEE, reservation.getStatut());
    }
}
