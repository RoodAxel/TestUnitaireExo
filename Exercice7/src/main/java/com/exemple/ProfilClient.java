package com.exemple;

public enum ProfilClient {
    STANDARD(0.0),
    PREMIUM(0.10),
    VIP(0.20);

    private final double tauxRemise;

    ProfilClient(double tauxRemise) {
        this.tauxRemise = tauxRemise;
    }

    public double tauxRemise() {
        return tauxRemise;
    }
}
