package com.exemple;

import java.time.LocalDateTime;

public record Reservation(String emailUtilisateur, String codeSalle, int nombreParticipants,
                          LocalDateTime dateDebut, LocalDateTime dateFin) {
}
