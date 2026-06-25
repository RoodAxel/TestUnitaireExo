package com.exemple.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exemple.exception.TicketIntrouvableException;
import com.exemple.exception.TicketInvalideException;
import com.exemple.exception.TransitionInterditeException;
import com.exemple.model.Priorite;
import com.exemple.model.Statut;
import com.exemple.model.Ticket;
import com.exemple.service.ServiceTicket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceTicket serviceTicket;

    @Test
    @DisplayName("POST /api/tickets renvoie 201 et le ticket créé")
    void shouldReturn201WhenTicketIsCreated() throws Exception {
        // Given
        when(serviceTicket.creerTicket("Bug sur la page de paiement", Priorite.HIGH))
                .thenReturn(new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.OPEN));

        // When // Then
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\":\"Bug sur la page de paiement\",\"priorite\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Bug sur la page de paiement"))
                .andExpect(jsonPath("$.priorite").value("HIGH"))
                .andExpect(jsonPath("$.statut").value("OPEN"));
    }

    @Test
    @DisplayName("POST /api/tickets renvoie 400 quand la validation échoue")
    void shouldReturn400WhenCreationIsInvalid() throws Exception {
        // Given
        when(serviceTicket.creerTicket(any(), any()))
                .thenThrow(new TicketInvalideException("Le titre est obligatoire et doit contenir au moins 3 caractères utiles"));

        // When // Then
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\":\"ab\",\"priorite\":\"LOW\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /api/tickets/{id} renvoie 200 et le ticket demandé")
    void shouldReturn200WhenTicketExists() throws Exception {
        // Given
        when(serviceTicket.trouverParId(1L))
                .thenReturn(new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.OPEN));

        // When // Then
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("OPEN"));
    }

    @Test
    @DisplayName("GET /api/tickets/{id} renvoie 404 quand le ticket n'existe pas")
    void shouldReturn404WhenTicketDoesNotExist() throws Exception {
        // Given
        when(serviceTicket.trouverParId(99L))
                .thenThrow(new TicketIntrouvableException("Ticket introuvable : 99"));

        // When // Then
        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("PATCH /api/tickets/{id}/statut renvoie 200 et le ticket mis à jour")
    void shouldReturn200WhenStatusIsChanged() throws Exception {
        // Given
        when(serviceTicket.changerStatut(1L, Statut.IN_PROGRESS))
                .thenReturn(new Ticket(1L, "Bug sur la page de paiement", Priorite.HIGH, Statut.IN_PROGRESS));

        // When // Then
        mockMvc.perform(patch("/api/tickets/1/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("PATCH /api/tickets/{id}/statut renvoie 409 lors d'un conflit métier")
    void shouldReturn409WhenTransitionIsForbidden() throws Exception {
        // Given
        when(serviceTicket.changerStatut(eq(1L), any()))
                .thenThrow(new TransitionInterditeException("Transition de statut interdite : RESOLVED vers IN_PROGRESS"));

        // When // Then
        mockMvc.perform(patch("/api/tickets/1/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}
