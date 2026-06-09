# language: fr
Fonctionnalité: Notification de confirmation
  En tant qu'utilisateur
  Je veux être notifié uniquement quand ma réservation est acceptée
  Afin de connaître l'état réel de ma demande

  Scénario: Une confirmation est envoyée lorsque la réservation réussit
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S1" pour 5 participants de "2026-06-10T09:00" à "2026-06-10T11:00"
    Alors la réservation est acceptée
    Et une confirmation est envoyée

  Scénario: Aucune confirmation n'est envoyée lorsque la réservation échoue
    Étant donné une salle "S1" nommée "Salle A" avec une capacité de 10
    Quand l'utilisateur réserve la salle "S1" pour 15 participants de "2026-06-10T09:00" à "2026-06-10T11:00"
    Alors la réservation est refusée
    Et aucune confirmation n'est envoyée
