package com.exemple.controller;

import com.exemple.dto.CreerSalleRequest;
import com.exemple.model.Salle;
import com.exemple.service.ServiceSalle;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class SalleController {

    private final ServiceSalle serviceSalle;

    public SalleController(ServiceSalle serviceSalle) {
        this.serviceSalle = serviceSalle;
    }

    @PostMapping
    public ResponseEntity<Salle> creer(@RequestBody CreerSalleRequest request) {
        Salle salle = serviceSalle.creerSalle(request.nom(), request.capacite());
        return ResponseEntity.status(HttpStatus.CREATED).body(salle);
    }

    @GetMapping
    public List<Salle> lister() {
        return serviceSalle.listerSalles();
    }
}
