# language: fr
Fonctionnalité: Réservation d'ouvrages indisponibles
  En tant qu'adhérent de la médiathèque
  Je veux réserver un ouvrage déjà emprunté
  Afin de l'obtenir dès qu'il est restitué

  Scénario: Réservation d'un ouvrage indisponible
    Étant donné un ouvrage "L'Étranger" emprunté par "Alice"
    Quand "Bob" réserve l'ouvrage "L'Étranger"
    Alors "Bob" est en position 1 dans la file d'attente de "L'Étranger"

  Scénario: Plusieurs réservations sur le même ouvrage
    Étant donné un ouvrage "L'Étranger" emprunté par "Alice"
    Quand "Bob" réserve l'ouvrage "L'Étranger"
    Et "Chloé" réserve l'ouvrage "L'Étranger"
    Alors "Bob" est en position 1 dans la file d'attente de "L'Étranger"
    Et "Chloé" est en position 2 dans la file d'attente de "L'Étranger"

  Scénario: Restitution d'un ouvrage réservé
    Étant donné un ouvrage "L'Étranger" emprunté par "Alice"
    Et "Bob" réserve l'ouvrage "L'Étranger"
    Quand "Alice" restitue l'ouvrage "L'Étranger"
    Alors la réservation de "Bob" est honorée
    Et la file d'attente de "L'Étranger" est vide

  Scénario: Refus d'une réservation pour un adhérent suspendu
    Étant donné un ouvrage "L'Étranger" emprunté par "Alice"
    Et un adhérent suspendu "Bob"
    Quand "Bob" tente de réserver l'ouvrage "L'Étranger"
    Alors la réservation est refusée

  Scénario: Réservation refusée pour un ouvrage disponible
    Étant donné un ouvrage disponible "Germinal"
    Quand "Bob" tente de réserver l'ouvrage "Germinal"
    Alors la réservation est refusée
