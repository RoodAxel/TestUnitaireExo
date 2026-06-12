package com.exemple.repository;

import com.exemple.model.Ticket;
import java.util.List;
import java.util.Optional;

public interface RepertoireTicket {

    Ticket sauvegarder(Ticket ticket);

    Optional<Ticket> trouverParId(Long id);

    List<Ticket> trouverTous();
}
