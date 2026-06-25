package com.exemple.repository;

import com.exemple.model.Ticket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class RepertoireTicketEnMemoire implements RepertoireTicket {

    private final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Ticket sauvegarder(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(sequence.incrementAndGet());
        }
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> trouverParId(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    @Override
    public List<Ticket> trouverTous() {
        return new ArrayList<>(tickets.values());
    }
}
