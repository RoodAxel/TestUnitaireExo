package com.exemple.controller;

import com.exemple.dto.CreerCompteRequest;
import com.exemple.dto.OperationRequest;
import com.exemple.dto.VirementRequest;
import com.exemple.model.Compte;
import com.exemple.service.ServiceCompte;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class CompteController {

    private final ServiceCompte serviceCompte;

    public CompteController(ServiceCompte serviceCompte) {
        this.serviceCompte = serviceCompte;
    }

    @PostMapping
    public ResponseEntity<Compte> creer(@RequestBody CreerCompteRequest request) {
        Compte compte = serviceCompte.creerCompte(request.numero(), request.titulaire());
        return ResponseEntity.status(HttpStatus.CREATED).body(compte);
    }

    @GetMapping
    public List<Compte> lister() {
        return serviceCompte.listerComptes();
    }

    @GetMapping("/{numero}")
    public ResponseEntity<Compte> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(serviceCompte.consulterCompte(numero));
    }

    @PostMapping("/{numero}/deposit")
    public ResponseEntity<Compte> deposer(@PathVariable String numero, @RequestBody OperationRequest request) {
        return ResponseEntity.ok(serviceCompte.deposer(numero, request.montant()));
    }

    @PostMapping("/{numero}/withdraw")
    public ResponseEntity<Compte> retirer(@PathVariable String numero, @RequestBody OperationRequest request) {
        return ResponseEntity.ok(serviceCompte.retirer(numero, request.montant()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> virer(@RequestBody VirementRequest request) {
        serviceCompte.virer(request.source(), request.destination(), request.montant());
        return ResponseEntity.ok().build();
    }
}
