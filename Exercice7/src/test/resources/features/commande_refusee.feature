# language: fr
Fonctionnalité: Commande refusée
  En tant que boutique en ligne
  Je veux refuser une commande invalide
  Afin de protéger le stock et la cohérence des commandes

  Scénario: Commande refusée si le produit est inconnu
    Étant donné un produit "REF-001" nommé "Clavier" au prix unitaire de 50.0 avec un stock de 10
    Quand le client "STANDARD" commande 1 unités du produit "REF-999"
    Alors la commande est refusée

  Scénario: Commande refusée si le stock est insuffisant
    Étant donné un produit "REF-001" nommé "Clavier" au prix unitaire de 50.0 avec un stock de 10
    Quand le client "STANDARD" commande 20 unités du produit "REF-001"
    Alors la commande est refusée
