package com.exemple.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.exemple.exception.TicketIntrouvableException;
import com.exemple.exception.TicketInvalideException;
import com.exemple.exception.TransitionInterditeException;
import com.exemple.model.Priorite;
import com.exemple.model.Statut;
import com.exemple.model.Ticket;
import com.exemple.repository.RepertoireTicket;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceTicketTest {

    @Mock
    private RepertoireTicket repertoireTicket;

    // ----- Création d'un ticket -----

    @Test
    @DisplayName("La création d'un ticket valide renvoie le ticket avec son titre et sa priorité")
    void shouldCreateTicketWhenDataIsValid() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        when(repertoireTicket.sauvegarder(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Ticket ticket = service.creerTicket("Bug sur la page de paiement", Priorite.HIGH);

        // Then
        assertEquals("Bug sur la page de paiement", ticket.getTitre());
        assertEquals(Priorite.HIGH, ticket.getPriorite());
    }

    @Test
    @DisplayName("Un ticket créé a toujours le statut OPEN par défaut")
    void shouldCreateTicketWithOpenStatusByDefault() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        when(repertoireTicket.sauvegarder(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Ticket ticket = service.creerTicket("Bug sur la page de paiement", Priorite.LOW);

        // Then
        assertEquals(Statut.OPEN, ticket.getStatut());
    }

    @Test
    @DisplayName("La création est refusée quand le titre est absent")
    void shouldRejectCreationWhenTitleIsNull() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);

        // When // Then
        assertThrows(TicketInvalideException.class, () -> service.creerTicket(null, Priorite.MEDIUM));
    }

    @Test
    @DisplayName("La création est refusée quand le titre a moins de 3 caractères utiles")
    void shouldRejectCreationWhenTitleHasLessThanThreeUsefulCharacters() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);

        // When // Then
        assertThrows(TicketInvalideException.class, () -> service.creerTicket("  ab  ", Priorite.MEDIUM));
    }

    @Test
    @DisplayName("La création est refusée quand la priorité est absente")
    void shouldRejectCreationWhenPriorityIsNull() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);

        // When // Then
        assertThrows(TicketInvalideException.class, () -> service.creerTicket("Bug sur la page de paiement", null));
    }

    // ----- Recherche de tickets -----

    @Test
    @DisplayName("La recherche d'un ticket existant renvoie le ticket")
    void shouldReturnTicketWhenIdExists() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket ticket = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.OPEN);
        when(repertoireTicket.trouverParId(1L)).thenReturn(Optional.of(ticket));

        // When
        Ticket resultat = service.trouverParId(1L);

        // Then
        assertEquals(ticket, resultat);
    }

    @Test
    @DisplayName("La recherche d'un ticket inexistant lève une erreur")
    void shouldThrowNotFoundWhenIdDoesNotExist() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        when(repertoireTicket.trouverParId(99L)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(TicketIntrouvableException.class, () -> service.trouverParId(99L));
    }

    @Test
    @DisplayName("La liste des tickets renvoie tous les tickets du répertoire")
    void shouldReturnAllTickets() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket premier = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.OPEN);
        Ticket second = new Ticket(2L, "Demande de nouvelle fonctionnalité", Priorite.LOW, Statut.RESOLVED);
        when(repertoireTicket.trouverTous()).thenReturn(List.of(premier, second));

        // When
        List<Ticket> tickets = service.listerTickets();

        // Then
        assertEquals(List.of(premier, second), tickets);
    }

    // ----- Changement de statut -----

    @Test
    @DisplayName("La transition OPEN vers IN_PROGRESS est autorisée")
    void shouldAllowTransitionFromOpenToInProgress() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket ticket = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.OPEN);
        when(repertoireTicket.trouverParId(1L)).thenReturn(Optional.of(ticket));
        when(repertoireTicket.sauvegarder(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Ticket resultat = service.changerStatut(1L, Statut.IN_PROGRESS);

        // Then
        assertEquals(Statut.IN_PROGRESS, resultat.getStatut());
    }

    @Test
    @DisplayName("La transition OPEN vers RESOLVED est autorisée")
    void shouldAllowTransitionFromOpenToResolved() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket ticket = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.OPEN);
        when(repertoireTicket.trouverParId(1L)).thenReturn(Optional.of(ticket));
        when(repertoireTicket.sauvegarder(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Ticket resultat = service.changerStatut(1L, Statut.RESOLVED);

        // Then
        assertEquals(Statut.RESOLVED, resultat.getStatut());
    }

    @Test
    @DisplayName("La transition IN_PROGRESS vers RESOLVED est autorisée")
    void shouldAllowTransitionFromInProgressToResolved() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket ticket = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.IN_PROGRESS);
        when(repertoireTicket.trouverParId(1L)).thenReturn(Optional.of(ticket));
        when(repertoireTicket.sauvegarder(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Ticket resultat = service.changerStatut(1L, Statut.RESOLVED);

        // Then
        assertEquals(Statut.RESOLVED, resultat.getStatut());
    }

    @Test
    @DisplayName("La transition IN_PROGRESS vers OPEN est interdite")
    void shouldRejectTransitionFromInProgressToOpen() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket ticket = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.IN_PROGRESS);
        when(repertoireTicket.trouverParId(1L)).thenReturn(Optional.of(ticket));

        // When // Then
        assertThrows(TransitionInterditeException.class, () -> service.changerStatut(1L, Statut.OPEN));
        assertEquals(Statut.IN_PROGRESS, ticket.getStatut());
    }

    @Test
    @DisplayName("Un ticket RESOLVED ne peut plus changer de statut")
    void shouldRejectAnyTransitionWhenTicketIsResolved() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        Ticket ticket = new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.RESOLVED);
        when(repertoireTicket.trouverParId(1L)).thenReturn(Optional.of(ticket));

        // When // Then
        assertThrows(TransitionInterditeException.class, () -> service.changerStatut(1L, Statut.IN_PROGRESS));
        assertEquals(Statut.RESOLVED, ticket.getStatut());
    }

    @Test
    @DisplayName("Le changement de statut d'un ticket inexistant lève une erreur")
    void shouldThrowNotFoundWhenChangingStatusOfUnknownTicket() {
        // Given
        ServiceTicket service = new ServiceTicket(repertoireTicket);
        when(repertoireTicket.trouverParId(99L)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(TicketIntrouvableException.class, () -> service.changerStatut(99L, Statut.IN_PROGRESS));
    }
}
