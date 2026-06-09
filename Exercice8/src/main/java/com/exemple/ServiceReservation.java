package com.exemple;

public class ServiceReservation {

    private final RepertoireSalle repertoire;
    private final PlanningReservation planning;
    private final ServiceNotification notification;

    public ServiceReservation(RepertoireSalle repertoire, PlanningReservation planning,
                              ServiceNotification notification) {
        this.repertoire = repertoire;
        this.planning = planning;
        this.notification = notification;
    }

    public ConfirmationReservation reserver(Reservation reservation) {
        Salle salle = repertoire.trouverParCode(reservation.codeSalle())
                .orElseThrow(() -> new ReservationRefuseeException(
                        "Salle inconnue : " + reservation.codeSalle()));

        if (!reservation.dateFin().isAfter(reservation.dateDebut())) {
            throw new ReservationRefuseeException("Période invalide");
        }

        if (reservation.nombreParticipants() > salle.capaciteMax()) {
            throw new ReservationRefuseeException("Capacité insuffisante pour la salle : " + salle.code());
        }

        if (chevaucheUneReservationExistante(reservation)) {
            throw new ReservationRefuseeException("Salle déjà réservée sur le créneau demandé");
        }

        notification.envoyerConfirmation(reservation);

        return new ConfirmationReservation(salle.code(), reservation.nombreParticipants(),
                "Réservation confirmée pour la salle " + salle.nom());
    }

    private boolean chevaucheUneReservationExistante(Reservation demande) {
        return planning.reservationsPour(demande.codeSalle()).stream()
                .anyMatch(existante ->
                        demande.dateDebut().isBefore(existante.dateFin())
                                && existante.dateDebut().isBefore(demande.dateFin()));
    }
}
