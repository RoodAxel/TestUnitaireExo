package com.exemple.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.exemple.exception.ConflitMetierException;
import com.exemple.exception.DonneesInvalidesException;
import com.exemple.exception.RessourceIntrouvableException;
import com.exemple.model.Compte;
import com.exemple.service.ServiceCompte;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CompteController.class)
class CompteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceCompte serviceCompte;

    private Compte compteAvecSolde(String numero, String titulaire, String solde) {
        Compte compte = new Compte(numero, titulaire);
        compte.setSolde(new BigDecimal(solde));
        return compte;
    }

    @Test
    @DisplayName("POST /accounts renvoie 201 et le compte créé")
    void shouldReturn201WhenAccountIsCreated() throws Exception {
        // Given
        when(serviceCompte.creerCompte("C1", "Alice")).thenReturn(new Compte("C1", "Alice"));

        // When // Then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\":\"C1\",\"titulaire\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("C1"))
                .andExpect(jsonPath("$.titulaire").value("Alice"))
                .andExpect(jsonPath("$.solde").value(0));
    }

    @Test
    @DisplayName("POST /accounts renvoie 409 quand le numéro existe déjà")
    void shouldReturn409WhenNumberAlreadyExists() throws Exception {
        // Given
        when(serviceCompte.creerCompte(any(), any()))
                .thenThrow(new ConflitMetierException("Le numéro de compte existe déjà"));

        // When // Then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\":\"C1\",\"titulaire\":\"Alice\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /accounts renvoie 200 et la liste des comptes")
    void shouldReturn200WithAccountList() throws Exception {
        // Given
        when(serviceCompte.listerComptes())
                .thenReturn(List.of(new Compte("C1", "Alice"), new Compte("C2", "Bob")));

        // When // Then
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].numero").value("C1"))
                .andExpect(jsonPath("$[1].numero").value("C2"));
    }

    @Test
    @DisplayName("GET /accounts/{numero} renvoie 200 et le compte demandé")
    void shouldReturn200WhenAccountExists() throws Exception {
        // Given
        when(serviceCompte.consulterCompte("C1")).thenReturn(compteAvecSolde("C1", "Alice", "100"));

        // When // Then
        mockMvc.perform(get("/accounts/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("C1"))
                .andExpect(jsonPath("$.solde").value(100));
    }

    @Test
    @DisplayName("GET /accounts/{numero} renvoie 404 quand le compte est introuvable")
    void shouldReturn404WhenAccountIsNotFound() throws Exception {
        // Given
        when(serviceCompte.consulterCompte("X"))
                .thenThrow(new RessourceIntrouvableException("Compte introuvable"));

        // When // Then
        mockMvc.perform(get("/accounts/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /accounts/{numero}/deposit renvoie 200 et le compte mis à jour")
    void shouldReturn200WhenDepositIsValid() throws Exception {
        // Given
        when(serviceCompte.deposer(any(), any())).thenReturn(compteAvecSolde("C1", "Alice", "100"));

        // When // Then
        mockMvc.perform(post("/accounts/C1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(100));
    }

    @Test
    @DisplayName("POST /accounts/{numero}/deposit renvoie 400 quand le montant est invalide")
    void shouldReturn400WhenDepositIsInvalid() throws Exception {
        // Given
        when(serviceCompte.deposer(any(), any()))
                .thenThrow(new DonneesInvalidesException("Le montant doit être strictement positif"));

        // When // Then
        mockMvc.perform(post("/accounts/C1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /accounts/{numero}/withdraw renvoie 409 quand les fonds sont insuffisants")
    void shouldReturn409WhenFundsAreInsufficient() throws Exception {
        // Given
        when(serviceCompte.retirer(any(), any()))
                .thenThrow(new ConflitMetierException("Solde insuffisant"));

        // When // Then
        mockMvc.perform(post("/accounts/C1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\":100}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /accounts/transfer renvoie 200 quand le virement réussit")
    void shouldReturn200WhenTransferSucceeds() throws Exception {
        // When // Then
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"source\":\"C1\",\"destination\":\"C2\",\"montant\":30}"))
                .andExpect(status().isOk());
    }
}
