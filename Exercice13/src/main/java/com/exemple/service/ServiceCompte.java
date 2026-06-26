package com.exemple.service;

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
        throw new UnsupportedOperationException();
    }

    public Compte consulterCompte(String numero) {
        throw new UnsupportedOperationException();
    }

    public List<Compte> listerComptes() {
        throw new UnsupportedOperationException();
    }

    public Compte deposer(String numero, BigDecimal montant) {
        throw new UnsupportedOperationException();
    }

    public Compte retirer(String numero, BigDecimal montant) {
        throw new UnsupportedOperationException();
    }

    public void virer(String numeroSource, String numeroDestination, BigDecimal montant) {
        throw new UnsupportedOperationException();
    }
}
