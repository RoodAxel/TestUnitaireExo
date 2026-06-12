package com.exemple.service;

import com.exemple.model.Priorite;
import com.exemple.model.Statut;
import com.exemple.model.Ticket;
import com.exemple.repository.RepertoireTicket;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceTicket {

    private final RepertoireTicket repertoireTicket;

    public ServiceTicket(RepertoireTicket repertoireTicket) {
        this.repertoireTicket = repertoireTicket;
    }

    public Ticket creerTicket(String titre, Priorite priorite) {
        throw new UnsupportedOperationException("creerTicket n'est pas encore implémentée");
    }

    public Ticket trouverParId(Long id) {
        throw new UnsupportedOperationException("trouverParId n'est pas encore implémentée");
    }

    public List<Ticket> listerTickets() {
        throw new UnsupportedOperationException("listerTickets n'est pas encore implémentée");
    }

    public Ticket changerStatut(Long id, Statut nouveauStatut) {
        throw new UnsupportedOperationException("changerStatut n'est pas encore implémentée");
    }
}
