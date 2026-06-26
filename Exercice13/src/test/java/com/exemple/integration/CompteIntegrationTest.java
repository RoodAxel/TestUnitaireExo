package com.exemple.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exemple.repository.RepertoireCompteEnMemoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CompteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepertoireCompteEnMemoire repertoire;

    @BeforeEach
    void reset() {
        repertoire.vider();
    }

    @Test
    @DisplayName("Parcours complet : créer deux comptes, déposer, retirer, virer puis consulter les soldes")
    void shouldCreateDepositWithdrawTransferThenConsult() throws Exception {
        // Given : création de deux comptes
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\":\"C1\",\"titulaire\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.solde").value(0));
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\":\"C2\",\"titulaire\":\"Bob\"}"))
                .andExpect(status().isCreated());

        // When : dépôt, retrait puis virement
        mockMvc.perform(post("/accounts/C1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(100));
        mockMvc.perform(post("/accounts/C1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(70));
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"source\":\"C1\",\"destination\":\"C2\",\"montant\":20}"))
                .andExpect(status().isOk());

        // Then : consultation des soldes finaux
        mockMvc.perform(get("/accounts/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(50));
        mockMvc.perform(get("/accounts/C2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(20));
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
