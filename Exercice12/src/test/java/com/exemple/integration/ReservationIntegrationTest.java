package com.exemple.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Parcours complet : créer une salle, créer une réservation, la consulter puis l'annuler")
    void shouldCreateRoomThenReserveThenReadThenCancel() throws Exception {
        // Given : création d'une salle
        String reponseSalle = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Salle Bleue\",\"capacite\":8}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long salleId = objectMapper.readTree(reponseSalle).get("id").asLong();

        // When : création d'une réservation sur cette salle
        String reponseReservation = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"salleId\":" + salleId + ",\"nomPersonne\":\"Alice\","
                                + "\"debut\":\"2026-06-25T09:00:00\",\"fin\":\"2026-06-25T10:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode reservation = objectMapper.readTree(reponseReservation);
        long reservationId = reservation.get("id").asLong();

        // Then : consultation de la réservation créée
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.nomPersonne").value("Alice"))
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));

        // Then : annulation de la réservation
        mockMvc.perform(patch("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("ANNULEE"));
    }
}
