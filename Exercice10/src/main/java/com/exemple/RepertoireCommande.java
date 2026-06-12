package com.exemple;

import java.util.Optional;

public interface RepertoireCommande {

    Optional<Commande> trouverParId(String id);
}
