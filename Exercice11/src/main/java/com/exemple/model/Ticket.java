package com.exemple.model;

public class Ticket {

    private Long id;
    private String titre;
    private Priorite priorite;
    private Statut statut;

    public Ticket(Long id, String titre, Priorite priorite, Statut statut) {
        this.id = id;
        this.titre = titre;
        this.priorite = priorite;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}
