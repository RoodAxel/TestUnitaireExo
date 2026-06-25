package com.exemple.exception;

import com.exemple.dto.ReponseErreur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestionnaireExceptions {

    @ExceptionHandler(TicketInvalideException.class)
    public ResponseEntity<ReponseErreur> gererTicketInvalide(TicketInvalideException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReponseErreur(exception.getMessage()));
    }

    @ExceptionHandler(TicketIntrouvableException.class)
    public ResponseEntity<ReponseErreur> gererTicketIntrouvable(TicketIntrouvableException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ReponseErreur(exception.getMessage()));
    }

    @ExceptionHandler(TransitionInterditeException.class)
    public ResponseEntity<ReponseErreur> gererTransitionInterdite(TransitionInterditeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ReponseErreur(exception.getMessage()));
    }
}
