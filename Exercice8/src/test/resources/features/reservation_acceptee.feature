# language: fr
Fonctionnalité: Réservation acceptée
  En tant qu'entreprise
  Je veux accepter une réservation de salle valide
  Afin de confirmer le créneau à l'utilisateur

  Scénario: Réservation acceptée pour une salle libre
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S1" pour 5 participants de "2026-06-10T09:00" à "2026-06-10T11:00"
    Alors la réservation est acceptée

  Scénario: Réservation acceptée à la capacité maximale de la salle
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S1" pour 10 participants de "2026-06-10T09:00" à "2026-06-10T11:00"
    Alors la réservation est acceptée

  Scénario: Réservation acceptée si le créneau commence après une réservation existante
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Et la salle "S1" est déjà réservée de "2026-06-10T09:00" à "2026-06-10T11:00"
    Quand l'utilisateur réserve la salle "S1" pour 5 participants de "2026-06-10T11:00" à "2026-06-10T12:00"
    Alors la réservation est acceptée
