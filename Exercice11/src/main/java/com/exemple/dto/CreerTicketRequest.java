package com.exemple.dto;

import com.exemple.model.Priorite;

public record CreerTicketRequest(String titre, Priorite priorite) {
}
