package com.exemple.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.exemple.exception.ConflitMetierException;
import com.exemple.exception.DonneesInvalidesException;
import com.exemple.exception.RessourceIntrouvableException;
import com.exemple.model.Compte;
import com.exemple.repository.RepertoireCompte;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceCompteTest {

    @Mock
    private RepertoireCompte repertoireCompte;

    private Compte compteAvecSolde(String numero, String titulaire, String solde) {
        Compte compte = new Compte(numero, titulaire);
        compte.setSolde(new BigDecimal(solde));
        return compte;
    }

    @Test
    @DisplayName("La création d'un compte avec un numéro libre renvoie un compte au solde nul")
    void shouldCreateAccountWhenNumberIsFree() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        when(repertoireCompte.existeParNumero("C1")).thenReturn(false);
        when(repertoireCompte.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Compte compte = service.creerCompte("C1", "Alice");

        // Then
        assertEquals("C1", compte.getNumero());
        assertEquals("Alice", compte.getTitulaire());
        assertEquals(0, compte.getSolde().compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("La création est refusée quand le numéro existe déjà")
    void shouldRejectCreationWhenNumberAlreadyExists() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        when(repertoireCompte.existeParNumero("C1")).thenReturn(true);

        // When // Then
        assertThrows(ConflitMetierException.class, () -> service.creerCompte("C1", "Alice"));
    }

    @Test
    @DisplayName("La consultation d'un compte existant renvoie ce compte")
    void shouldReturnAccountWhenItExists() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte compte = new Compte("C1", "Alice");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(compte));

        // When
        Compte resultat = service.consulterCompte("C1");

        // Then
        assertEquals(compte, resultat);
    }

    @Test
    @DisplayName("La consultation d'un compte inexistant lève une erreur")
    void shouldThrowWhenAccountIsNotFound() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        when(repertoireCompte.trouverParNumero("X")).thenReturn(Optional.empty());

        // When // Then
        assertThrows(RessourceIntrouvableException.class, () -> service.consulterCompte("X"));
    }

    @Test
    @DisplayName("Le listing renvoie tous les comptes du répertoire")
    void shouldReturnAllAccounts() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte premier = new Compte("C1", "Alice");
        Compte second = new Compte("C2", "Bob");
        when(repertoireCompte.trouverTous()).thenReturn(List.of(premier, second));

        // When
        List<Compte> comptes = service.listerComptes();

        // Then
        assertEquals(List.of(premier, second), comptes);
    }

    @Test
    @DisplayName("Un dépôt valide augmente le solde du compte")
    void shouldIncreaseBalanceOnValidDeposit() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte compte = new Compte("C1", "Alice");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(compte));
        when(repertoireCompte.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Compte resultat = service.deposer("C1", new BigDecimal("100"));

        // Then
        assertEquals(0, resultat.getSolde().compareTo(new BigDecimal("100")));
    }

    @Test
    @DisplayName("Un dépôt nul est refusé")
    void shouldRejectDepositOfZero() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.deposer("C1", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Un dépôt négatif est refusé")
    void shouldRejectNegativeDeposit() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.deposer("C1", new BigDecimal("-10")));
    }

    @Test
    @DisplayName("Un retrait valide diminue le solde du compte")
    void shouldDecreaseBalanceOnValidWithdrawal() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte compte = compteAvecSolde("C1", "Alice", "100");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(compte));
        when(repertoireCompte.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Compte resultat = service.retirer("C1", new BigDecimal("40"));

        // Then
        assertEquals(0, resultat.getSolde().compareTo(new BigDecimal("60")));
    }

    @Test
    @DisplayName("Un retrait nul est refusé")
    void shouldRejectWithdrawalOfZero() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.retirer("C1", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Un retrait négatif est refusé")
    void shouldRejectNegativeWithdrawal() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.retirer("C1", new BigDecimal("-10")));
    }

    @Test
    @DisplayName("Un retrait avec des fonds insuffisants est refusé")
    void shouldRejectWithdrawalWhenFundsAreInsufficient() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte compte = compteAvecSolde("C1", "Alice", "50");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(compte));

        // When // Then
        assertThrows(ConflitMetierException.class, () -> service.retirer("C1", new BigDecimal("100")));
    }

    @Test
    @DisplayName("Un virement valide débite l'émetteur et crédite le destinataire")
    void shouldTransferBetweenTwoAccounts() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte source = compteAvecSolde("C1", "Alice", "100");
        Compte destination = new Compte("C2", "Bob");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(source));
        when(repertoireCompte.trouverParNumero("C2")).thenReturn(Optional.of(destination));
        when(repertoireCompte.sauvegarder(any(Compte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        service.virer("C1", "C2", new BigDecimal("30"));

        // Then
        assertEquals(0, source.getSolde().compareTo(new BigDecimal("70")));
        assertEquals(0, destination.getSolde().compareTo(new BigDecimal("30")));
    }

    @Test
    @DisplayName("Un virement nul est refusé")
    void shouldRejectTransferOfZero() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.virer("C1", "C2", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Un virement négatif est refusé")
    void shouldRejectNegativeTransfer() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);

        // When // Then
        assertThrows(DonneesInvalidesException.class, () -> service.virer("C1", "C2", new BigDecimal("-10")));
    }

    @Test
    @DisplayName("Un virement avec des fonds insuffisants est refusé")
    void shouldRejectTransferWhenFundsAreInsufficient() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte source = compteAvecSolde("C1", "Alice", "20");
        Compte destination = new Compte("C2", "Bob");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(source));
        when(repertoireCompte.trouverParNumero("C2")).thenReturn(Optional.of(destination));

        // When // Then
        assertThrows(ConflitMetierException.class, () -> service.virer("C1", "C2", new BigDecimal("100")));
    }

    @Test
    @DisplayName("Un virement vers un compte inexistant est refusé")
    void shouldRejectTransferToUnknownAccount() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        Compte source = compteAvecSolde("C1", "Alice", "100");
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.of(source));
        when(repertoireCompte.trouverParNumero("C2")).thenReturn(Optional.empty());

        // When // Then
        assertThrows(RessourceIntrouvableException.class, () -> service.virer("C1", "C2", new BigDecimal("30")));
    }

    @Test
    @DisplayName("Un virement depuis un compte inexistant est refusé")
    void shouldRejectTransferFromUnknownAccount() {
        // Given
        ServiceCompte service = new ServiceCompte(repertoireCompte);
        when(repertoireCompte.trouverParNumero("C1")).thenReturn(Optional.empty());

        // When // Then
        assertThrows(RessourceIntrouvableException.class, () -> service.virer("C1", "C2", new BigDecimal("30")));
    }
}
