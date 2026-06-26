package com.exemple.dto;

import java.math.BigDecimal;

public record VirementRequest(String source, String destination, BigDecimal montant) {
}
