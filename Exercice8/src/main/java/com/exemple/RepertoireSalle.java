package com.exemple;

import java.util.Optional;

public interface RepertoireSalle {

    Optional<Salle> trouverParCode(String code);
}
