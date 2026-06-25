package com.exemple.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exemple.exception.ConflitMetierException;
import com.exemple.exception.RessourceIntrouvableException;
import com.exemple.model.Reservation;
import com.exemple.model.StatutReservation;
import com.exemple.service.ServiceReservation;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    private static final LocalDateTime DEBUT = LocalDateTime.of(2026, 6, 25, 9, 0);
    private static final LocalDateTime FIN = LocalDateTime.of(2026, 6, 25, 10, 0);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceReservation serviceReservation;

    @Test
    @DisplayName("POST /api/reservations renvoie 201 et la réservation créée")
    void shouldReturn201WhenReservationIsCreated() throws Exception {
        // Given
        when(serviceReservation.creerReservation(any(), any(), any(), any()))
                .thenReturn(new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMEE));

        // When // Then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"salleId\":1,\"nomPersonne\":\"Alice\","
                                + "\"debut\":\"2026-06-25T09:00:00\",\"fin\":\"2026-06-25T10:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomPersonne").value("Alice"))
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));
    }

    @Test
    @DisplayName("POST /api/reservations renvoie 409 lors d'un conflit de créneau")
    void shouldReturn409WhenSlotConflicts() throws Exception {
        // Given
        when(serviceReservation.creerReservation(any(), any(), any(), any()))
                .thenThrow(new ConflitMetierException("Le créneau chevauche une réservation confirmée existante"));

        // When // Then
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"salleId\":1,\"nomPersonne\":\"Alice\","
                                + "\"debut\":\"2026-06-25T09:00:00\",\"fin\":\"2026-06-25T10:00:00\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /api/reservations/{id} renvoie 200 et la réservation demandée")
    void shouldReturn200WhenReservationExists() throws Exception {
        // Given
        when(serviceReservation.trouverParId(1L))
                .thenReturn(new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMEE));

        // When // Then
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));
    }

    @Test
    @DisplayName("GET /api/reservations/{id} renvoie 404 quand la réservation n'existe pas")
    void shouldReturn404WhenReservationDoesNotExist() throws Exception {
        // Given
        when(serviceReservation.trouverParId(99L))
                .thenThrow(new RessourceIntrouvableException("Réservation introuvable : 99"));

        // When // Then
        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("PATCH /api/reservations/{id}/cancel renvoie 200 et la réservation annulée")
    void shouldReturn200WhenReservationIsCancelled() throws Exception {
        // Given
        when(serviceReservation.annuler(1L))
                .thenReturn(new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.ANNULEE));

        // When // Then
        mockMvc.perform(patch("/api/reservations/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("ANNULEE"));
    }

    @Test
    @DisplayName("PATCH /api/reservations/{id}/cancel renvoie 409 quand la réservation est déjà annulée")
    void shouldReturn409WhenReservationAlreadyCancelled() throws Exception {
        // Given
        when(serviceReservation.annuler(eq(1L)))
                .thenThrow(new ConflitMetierException("La réservation est déjà annulée : 1"));

        // When // Then
        mockMvc.perform(patch("/api/reservations/1/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}
