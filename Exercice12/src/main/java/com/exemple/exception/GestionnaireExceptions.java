package com.exemple.exception;

import com.exemple.dto.ReponseErreur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestionnaireExceptions {

    @ExceptionHandler(DonneesInvalidesException.class)
    public ResponseEntity<ReponseErreur> gererDonneesInvalides(DonneesInvalidesException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReponseErreur(exception.getMessage()));
    }

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<ReponseErreur> gererRessourceIntrouvable(RessourceIntrouvableException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ReponseErreur(exception.getMessage()));
    }

    @ExceptionHandler(ConflitMetierException.class)
    public ResponseEntity<ReponseErreur> gererConflitMetier(ConflitMetierException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ReponseErreur(exception.getMessage()));
    }
}
