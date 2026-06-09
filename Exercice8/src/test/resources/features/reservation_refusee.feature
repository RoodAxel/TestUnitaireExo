# language: fr
Fonctionnalité: Réservation refusée
  En tant qu'entreprise
  Je veux refuser une réservation invalide
  Afin de garantir la cohérence du planning des salles

  Scénario: Réservation refusée si la salle est inconnue
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S2" pour 5 participants de "2026-06-10T09:00" à "2026-06-10T11:00"
    Alors la réservation est refusée

  Scénario: Réservation refusée si la capacité est insuffisante
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S1" pour 15 participants de "2026-06-10T09:00" à "2026-06-10T11:00"
    Alors la réservation est refusée

  Scénario: Réservation refusée si la période est invalide
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S1" pour 5 participants de "2026-06-10T11:00" à "2026-06-10T09:00"
    Alors la réservation est refusée

  Scénario: Réservation refusée si le créneau chevauche une réservation existante
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Et la salle "S1" est déjà réservée de "2026-06-10T09:00" à "2026-06-10T11:00"
    Quand l'utilisateur réserve la salle "S1" pour 5 participants de "2026-06-10T10:00" à "2026-06-10T12:00"
    Alors la réservation est refusée
