package com.exemple.service;

import com.exemple.exception.ConflitMetierException;
import com.exemple.exception.DonneesInvalidesException;
import com.exemple.exception.RessourceIntrouvableException;
import com.exemple.model.Compte;
import com.exemple.repository.RepertoireCompte;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceCompte {

    private final RepertoireCompte repertoireCompte;

    public ServiceCompte(RepertoireCompte repertoireCompte) {
        this.repertoireCompte = repertoireCompte;
    }

    public Compte creerCompte(String numero, String titulaire) {
        if (repertoireCompte.existeParNumero(numero)) {
            throw new ConflitMetierException("Le numéro de compte existe déjà");
        }
        return repertoireCompte.sauvegarder(new Compte(numero, titulaire));
    }

    public Compte consulterCompte(String numero) {
        return repertoireCompte.trouverParNumero(numero)
                .orElseThrow(() -> new RessourceIntrouvableException("Compte introuvable"));
    }

    public List<Compte> listerComptes() {
        return repertoireCompte.trouverTous();
    }

    public Compte deposer(String numero, BigDecimal montant) {
        exigerMontantPositif(montant);
        Compte compte = consulterCompte(numero);
        compte.setSolde(compte.getSolde().add(montant));
        return repertoireCompte.sauvegarder(compte);
    }

    public Compte retirer(String numero, BigDecimal montant) {
        exigerMontantPositif(montant);
        Compte compte = consulterCompte(numero);
        exigerFondsSuffisants(compte, montant);
        compte.setSolde(compte.getSolde().subtract(montant));
        return repertoireCompte.sauvegarder(compte);
    }

    public void virer(String numeroSource, String numeroDestination, BigDecimal montant) {
        exigerMontantPositif(montant);
        Compte source = consulterCompte(numeroSource);
        Compte destination = consulterCompte(numeroDestination);
        exigerFondsSuffisants(source, montant);
        source.setSolde(source.getSolde().subtract(montant));
        destination.setSolde(destination.getSolde().add(montant));
        repertoireCompte.sauvegarder(source);
        repertoireCompte.sauvegarder(destination);
    }

    private void exigerMontantPositif(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DonneesInvalidesException("Le montant doit être strictement positif");
        }
    }

    private void exigerFondsSuffisants(Compte compte, BigDecimal montant) {
        if (compte.getSolde().compareTo(montant) < 0) {
            throw new ConflitMetierException("Solde insuffisant");
        }
    }
}
