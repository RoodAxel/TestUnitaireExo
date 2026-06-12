# language: fr
Fonctionnalité: Ajout de produit à une commande
  En tant qu'utilisateur
  Je veux ajouter des produits à ma commande
  Afin de préparer mon achat

  Scénario: Ajouter un produit à une commande vide
    Étant donné une commande "CMD-1" vide
    Quand l'utilisateur ajoute le produit "REF-001" à la commande "CMD-1"
    Alors la quantité du produit "REF-001" dans la commande est 1

  Scénario: Ajouter un produit déjà présent augmente la quantité
    Étant donné une commande "CMD-1" contenant 1 unité du produit "REF-001"
    Quand l'utilisateur ajoute le produit "REF-001" à la commande "CMD-1"
    Alors la quantité du produit "REF-001" dans la commande est 2

  Scénario: Ajouter un produit à une commande inexistante
    Étant donné la commande "CMD-404" n'existe pas
    Quand l'utilisateur ajoute le produit "REF-001" à la commande "CMD-404"
    Alors une erreur de commande est renvoyée
