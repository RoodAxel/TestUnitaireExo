package com.exemple.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.exemple.exception.EmpruntImpossibleException;
import com.exemple.horloge.Horloge;
import com.exemple.model.Adherent;
import com.exemple.model.Ouvrage;
import com.exemple.model.Pret;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServicePretTest {

    @Mock
    private Horloge horloge;

    @Test
    @DisplayName("Un prêt fixe la date de retour à 21 jours après l'emprunt et rend l'ouvrage indisponible")
    void shouldComputeReturnDate21DaysAfterLoan() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Adherent adherent = new Adherent("Alice");
        Ouvrage ouvrage = new Ouvrage("O1", "L'Étranger");
        when(horloge.aujourdhui()).thenReturn(LocalDate.of(2026, 1, 5));

        // When
        Pret pret = service.creerPret(adherent, ouvrage);

        // Then
        assertThat(pret.getDateEmprunt()).isEqualTo(LocalDate.of(2026, 1, 5));
        assertThat(pret.getDateRetourPrevue()).isEqualTo(LocalDate.of(2026, 1, 26));
        assertThat(ouvrage.estDisponible()).isFalse();
    }

    @Test
    @DisplayName("Un ouvrage déjà emprunté ne peut pas être prêté une seconde fois")
    void shouldRejectLoanWhenBookIsNotAvailable() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Ouvrage ouvrage = new Ouvrage("O1", "L'Étranger");
        ouvrage.setDisponible(false);

        // When // Then
        assertThatThrownBy(() -> service.creerPret(new Adherent("Alice"), ouvrage))
                .isInstanceOf(EmpruntImpossibleException.class)
                .hasMessageContaining("indisponible");
    }

    @Test
    @DisplayName("Un adhérent suspendu ne peut pas emprunter")
    void shouldRejectLoanWhenMemberIsSuspended() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Adherent adherent = new Adherent("Alice");
        adherent.enregistrerRetardImportant();
        adherent.enregistrerRetardImportant();
        adherent.enregistrerRetardImportant();

        // When // Then
        assertThatThrownBy(() -> service.creerPret(adherent, new Ouvrage("O1", "L'Étranger")))
                .isInstanceOf(EmpruntImpossibleException.class)
                .hasMessageContaining("suspendu");
    }

    @Test
    @DisplayName("Aucune pénalité n'est appliquée pour un retour dans les délais")
    void shouldApplyNoPenaltyWhenReturnedOnTime() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Pret pret = pret(LocalDate.of(2026, 1, 26));
        when(horloge.aujourdhui()).thenReturn(LocalDate.of(2026, 1, 26));

        // When
        Pret retourne = service.retourner(pret);

        // Then
        assertThat(retourne.getPenalite()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("La pénalité est de 0,15 € par jour de retard")
    void shouldApplyPenaltyOf015PerLateDay() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Pret pret = pret(LocalDate.of(2026, 1, 26));
        when(horloge.aujourdhui()).thenReturn(LocalDate.of(2026, 2, 5));

        // When
        Pret retourne = service.retourner(pret);

        // Then
        assertThat(retourne.getPenalite()).isEqualByComparingTo("1.50");
    }

    @Test
    @DisplayName("Un troisième retard important entraîne la suspension de l'adhérent")
    void shouldSuspendMemberAfterThirdMajorDelay() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Adherent adherent = new Adherent("Alice");
        adherent.enregistrerRetardImportant();
        adherent.enregistrerRetardImportant();
        Pret pret = pret(adherent, LocalDate.of(2026, 1, 26));
        when(horloge.aujourdhui()).thenReturn(LocalDate.of(2026, 3, 10));

        // When
        service.retourner(pret);

        // Then
        assertThat(adherent.estSuspendu()).isTrue();
    }

    @Test
    @DisplayName("Un retard mineur n'entraîne pas de suspension")
    void shouldNotSuspendMemberForMinorDelay() {
        // Given
        ServicePret service = new ServicePret(horloge);
        Adherent adherent = new Adherent("Alice");
        Pret pret = pret(adherent, LocalDate.of(2026, 1, 26));
        when(horloge.aujourdhui()).thenReturn(LocalDate.of(2026, 1, 31));

        // When
        service.retourner(pret);

        // Then
        assertThat(adherent.estSuspendu()).isFalse();
        assertThat(adherent.getRetardsImportants()).isZero();
    }

    private Pret pret(LocalDate dateRetourPrevue) {
        return pret(new Adherent("Alice"), dateRetourPrevue);
    }

    private Pret pret(Adherent adherent, LocalDate dateRetourPrevue) {
        Ouvrage ouvrage = new Ouvrage("O1", "L'Étranger");
        ouvrage.setDisponible(false);
        return new Pret(adherent, ouvrage, dateRetourPrevue.minusDays(21), dateRetourPrevue);
    }
}
