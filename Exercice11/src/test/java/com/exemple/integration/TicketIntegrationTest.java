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
class TicketIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Parcours complet : créer un ticket, le consulter puis modifier son statut")
    void shouldCreateThenReadThenUpdateTicket() throws Exception {
        // Given : création d'un ticket
        String reponseCreation = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\":\"Bug sur la page de paiement\",\"priorite\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("OPEN"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode ticketCree = objectMapper.readTree(reponseCreation);
        long id = ticketCree.get("id").asLong();

        // When // Then : consultation du ticket créé
        mockMvc.perform(get("/api/tickets/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titre").value("Bug sur la page de paiement"))
                .andExpect(jsonPath("$.statut").value("OPEN"));

        // When // Then : modification du statut
        mockMvc.perform(patch("/api/tickets/" + id + "/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("IN_PROGRESS"));
    }
}
