# language: fr
Fonctionnalité: Réservation de salles de réunion
  En tant qu'utilisateur
  Je veux réserver une salle de réunion
  Afin d'organiser mes réunions sans conflit

  Scénario: Réservation acceptée quand la salle existe et le créneau est libre
    Étant donné une salle "Salle Bleue" d'une capacité de 8
    Quand une personne "Alice" réserve la salle de "09:00" à "10:00"
    Alors la réservation est confirmée

  Scénario: Réservation refusée quand la salle n'existe pas
    Quand une personne "Alice" réserve une salle inexistante de "09:00" à "10:00"
    Alors la réservation est refusée

  Scénario: Réservation refusée quand le créneau chevauche une réservation existante
    Étant donné une salle "Salle Bleue" d'une capacité de 8
    Et une réservation confirmée de "Bob" sur cette salle de "09:30" à "10:30"
    Quand une personne "Alice" réserve la salle de "09:00" à "10:00"
    Alors la réservation est refusée
