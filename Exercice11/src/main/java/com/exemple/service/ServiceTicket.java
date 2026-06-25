package com.exemple.service;

import com.exemple.exception.TicketIntrouvableException;
import com.exemple.exception.TicketInvalideException;
import com.exemple.exception.TransitionInterditeException;
import com.exemple.model.Priorite;
import com.exemple.model.Statut;
import com.exemple.model.Ticket;
import com.exemple.repository.RepertoireTicket;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceTicket {

    private static final int LONGUEUR_TITRE_MINIMALE = 3;

    private final RepertoireTicket repertoireTicket;

    public ServiceTicket(RepertoireTicket repertoireTicket) {
        this.repertoireTicket = repertoireTicket;
    }

    public Ticket creerTicket(String titre, Priorite priorite) {
        if (titre == null || titre.trim().length() < LONGUEUR_TITRE_MINIMALE) {
            throw new TicketInvalideException("Le titre est obligatoire et doit contenir au moins 3 caractères utiles");
        }
        if (priorite == null) {
            throw new TicketInvalideException("La priorité est obligatoire");
        }
        Ticket ticket = new Ticket(null, titre, priorite, Statut.OPEN);
        return repertoireTicket.sauvegarder(ticket);
    }

    public Ticket trouverParId(Long id) {
        return repertoireTicket.trouverParId(id)
                .orElseThrow(() -> new TicketIntrouvableException("Ticket introuvable : " + id));
    }

    public List<Ticket> listerTickets() {
        return repertoireTicket.trouverTous();
    }

    public Ticket changerStatut(Long id, Statut nouveauStatut) {
        Ticket ticket = trouverParId(id);
        if (!transitionAutorisee(ticket.getStatut(), nouveauStatut)) {
            throw new TransitionInterditeException(
                    "Transition de statut interdite : " + ticket.getStatut() + " vers " + nouveauStatut);
        }
        ticket.setStatut(nouveauStatut);
        return repertoireTicket.sauvegarder(ticket);
    }

    private boolean transitionAutorisee(Statut actuel, Statut cible) {
        return switch (actuel) {
            case OPEN -> cible == Statut.IN_PROGRESS || cible == Statut.RESOLVED;
            case IN_PROGRESS -> cible == Statut.RESOLVED;
            case RESOLVED -> false;
        };
    }
}
