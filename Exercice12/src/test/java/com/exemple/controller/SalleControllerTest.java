package com.exemple.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exemple.exception.DonneesInvalidesException;
import com.exemple.model.Salle;
import com.exemple.service.ServiceSalle;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SalleController.class)
class SalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceSalle serviceSalle;

    @Test
    @DisplayName("POST /api/rooms renvoie 201 et la salle créée")
    void shouldReturn201WhenRoomIsCreated() throws Exception {
        // Given
        when(serviceSalle.creerSalle("Salle Bleue", 8)).thenReturn(new Salle(1L, "Salle Bleue", 8));

        // When // Then
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Salle Bleue\",\"capacite\":8}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Salle Bleue"))
                .andExpect(jsonPath("$.capacite").value(8));
    }

    @Test
    @DisplayName("POST /api/rooms renvoie 400 quand la validation échoue")
    void shouldReturn400WhenRoomIsInvalid() throws Exception {
        // Given
        when(serviceSalle.creerSalle(any(), anyInt()))
                .thenThrow(new DonneesInvalidesException("Le nom de la salle est obligatoire"));

        // When // Then
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"\",\"capacite\":8}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /api/rooms renvoie 200 et la liste des salles")
    void shouldReturn200WithRoomList() throws Exception {
        // Given
        when(serviceSalle.listerSalles())
                .thenReturn(List.of(new Salle(1L, "Salle Bleue", 8), new Salle(2L, "Salle Rouge", 12)));

        // When // Then
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Salle Bleue"))
                .andExpect(jsonPath("$[1].nom").value("Salle Rouge"));
    }
}
