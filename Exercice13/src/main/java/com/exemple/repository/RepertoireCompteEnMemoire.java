package com.exemple.repository;

import com.exemple.model.Compte;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class RepertoireCompteEnMemoire implements RepertoireCompte {

    private final Map<String, Compte> comptes = new ConcurrentHashMap<>();

    @Override
    public Compte sauvegarder(Compte compte) {
        comptes.put(compte.getNumero(), compte);
        return compte;
    }

    @Override
    public Optional<Compte> trouverParNumero(String numero) {
        return Optional.ofNullable(comptes.get(numero));
    }

    @Override
    public boolean existeParNumero(String numero) {
        return comptes.containsKey(numero);
    }

    @Override
    public List<Compte> trouverTous() {
        return new ArrayList<>(comptes.values());
    }

    public void vider() {
        comptes.clear();
    }
}
