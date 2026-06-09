package com.exemple;

import java.util.Optional;

public interface CatalogueProduit {

    Optional<Produit> trouverParReference(String reference);
}
