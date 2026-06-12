# language: fr
Fonctionnalité: Validation de commande
  En tant qu'utilisateur
  Je veux valider une commande
  Afin de finaliser mon achat

  Scénario: Valider une commande existante
    Étant donné une commande "CMD-1" contenant 1 unité du produit "REF-001"
    Quand l'utilisateur valide la commande "CMD-1"
    Alors la commande est validée

  Scénario: Valider une commande inexistante
    Étant donné la commande "CMD-404" n'existe pas
    Quand l'utilisateur valide la commande "CMD-404"
    Alors une erreur de commande est renvoyée
