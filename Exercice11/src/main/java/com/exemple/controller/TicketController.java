package com.exemple.controller;

import com.exemple.dto.ChangerStatutRequest;
import com.exemple.dto.CreerTicketRequest;
import com.exemple.model.Ticket;
import com.exemple.service.ServiceTicket;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final ServiceTicket serviceTicket;

    public TicketController(ServiceTicket serviceTicket) {
        this.serviceTicket = serviceTicket;
    }

    @PostMapping
    public ResponseEntity<Ticket> creer(@RequestBody CreerTicketRequest request) {
        Ticket ticket = serviceTicket.creerTicket(request.titre(), request.priorite());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/{id}")
    public Ticket consulter(@PathVariable Long id) {
        return serviceTicket.trouverParId(id);
    }

    @GetMapping
    public List<Ticket> lister() {
        return serviceTicket.listerTickets();
    }

    @PatchMapping("/{id}/statut")
    public Ticket changerStatut(@PathVariable Long id, @RequestBody ChangerStatutRequest request) {
        return serviceTicket.changerStatut(id, request.statut());
    }
}
