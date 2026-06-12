# language: fr
Fonctionnalité: Suppression de produit d'une commande
  En tant qu'utilisateur
  Je veux supprimer des produits de ma commande
  Afin d'ajuster mon achat

  Scénario: Diminuer la quantité d'un produit présent en plusieurs exemplaires
    Étant donné une commande "CMD-1" contenant 2 unités du produit "REF-001"
    Quand l'utilisateur supprime le produit "REF-001" de la commande "CMD-1"
    Alors la quantité du produit "REF-001" dans la commande est 1

  Scénario: Retirer un produit présent en un seul exemplaire
    Étant donné une commande "CMD-1" contenant 1 unité du produit "REF-001"
    Quand l'utilisateur supprime le produit "REF-001" de la commande "CMD-1"
    Alors le produit "REF-001" n'est plus présent dans la commande

  Scénario: Supprimer un produit absent de la commande
    Étant donné une commande "CMD-1" vide
    Quand l'utilisateur supprime le produit "REF-001" de la commande "CMD-1"
    Alors une erreur de commande est renvoyée

  Scénario: Supprimer un produit d'une commande inexistante
    Étant donné la commande "CMD-404" n'existe pas
    Quand l'utilisateur supprime le produit "REF-001" de la commande "CMD-404"
    Alors une erreur de commande est renvoyée
