package com.exemple.service;

import com.exemple.exception.EmpruntImpossibleException;
import com.exemple.horloge.Horloge;
import com.exemple.model.Adherent;
import com.exemple.model.Ouvrage;
import com.exemple.model.Pret;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ServicePret {

    private static final int DUREE_PRET_JOURS = 21;
    private static final long JOURS_RETARD_IMPORTANT = 30;
    private static final BigDecimal PENALITE_PAR_JOUR = new BigDecimal("0.15");

    private final Horloge horloge;

    public ServicePret(Horloge horloge) {
        this.horloge = horloge;
    }

    public Pret creerPret(Adherent adherent, Ouvrage ouvrage) {
        if (adherent.estSuspendu()) {
            throw new EmpruntImpossibleException("Adhérent suspendu");
        }
        if (!ouvrage.estDisponible()) {
            throw new EmpruntImpossibleException("Ouvrage indisponible");
        }
        LocalDate aujourdhui = horloge.aujourdhui();
        ouvrage.setDisponible(false);
        return new Pret(adherent, ouvrage, aujourdhui, aujourdhui.plusDays(DUREE_PRET_JOURS));
    }

    public Pret retourner(Pret pret) {
        LocalDate dateRetour = horloge.aujourdhui();
        long joursRetard = Math.max(0, ChronoUnit.DAYS.between(pret.getDateRetourPrevue(), dateRetour));
        pret.setDateRetourReelle(dateRetour);
        pret.setPenalite(PENALITE_PAR_JOUR.multiply(BigDecimal.valueOf(joursRetard)));
        pret.getOuvrage().setDisponible(true);
        if (joursRetard >= JOURS_RETARD_IMPORTANT) {
            pret.getAdherent().enregistrerRetardImportant();
        }
        return pret;
    }
}
