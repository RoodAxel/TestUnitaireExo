package com.exemple.service;

import com.exemple.horloge.Horloge;
import com.exemple.model.Adherent;
import com.exemple.model.Ouvrage;
import com.exemple.model.Pret;

public class ServicePret {

    private final Horloge horloge;

    public ServicePret(Horloge horloge) {
        this.horloge = horloge;
    }

    public Pret creerPret(Adherent adherent, Ouvrage ouvrage) {
        throw new UnsupportedOperationException();
    }

    public Pret retourner(Pret pret) {
        throw new UnsupportedOperationException();
    }
}
