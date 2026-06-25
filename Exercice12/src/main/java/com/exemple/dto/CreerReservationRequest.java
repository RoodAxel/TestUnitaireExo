package com.exemple.dto;

import java.time.LocalDateTime;

public record CreerReservationRequest(Long salleId, String nomPersonne, LocalDateTime debut, LocalDateTime fin) {
}
