# language: fr
Fonctionnalité: Commande acceptée
  En tant que boutique en ligne
  Je veux accepter une commande quand le produit existe et que le stock est suffisant
  Afin de retourner un reçu avec la remise correspondant au profil client

  Scénario: Commande acceptée pour un client STANDARD
    Étant donné un produit "REF-001" nommé "Clavier" au prix unitaire de 50.0 avec un stock de 10
    Quand le client "STANDARD" commande 2 unités du produit "REF-001"
    Alors la commande est acceptée
    Et le montant total du reçu est 100.0

  Scénario: Commande acceptée pour un client PREMIUM
    Étant donné un produit "REF-001" nommé "Clavier" au prix unitaire de 50.0 avec un stock de 10
    Quand le client "PREMIUM" commande 2 unités du produit "REF-001"
    Alors la commande est acceptée
    Et le montant total du reçu est 90.0

  Scénario: Commande acceptée pour un client VIP
    Étant donné un produit "REF-001" nommé "Clavier" au prix unitaire de 50.0 avec un stock de 10
    Quand le client "VIP" commande 2 unités du produit "REF-001"
    Alors la commande est acceptée
    Et le montant total du reçu est 80.0

  Scénario: Commande acceptée jusqu'au stock maximum disponible
    Étant donné un produit "REF-001" nommé "Clavier" au prix unitaire de 50.0 avec un stock de 10
    Quand le client "STANDARD" commande 10 unités du produit "REF-001"
    Alors la commande est acceptée
    Et le montant total du reçu est 500.0
