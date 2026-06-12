# language: fr
Fonctionnalité: Navigation par catégorie
  En tant qu'utilisateur
  Je veux naviguer par catégorie de produits
  Afin de découvrir ce qui est disponible

  Scénario: Sélection d'une catégorie
    Étant donné que la catégorie "Périphériques" contient les produits:
      | reference | nom     |
      | REF-001   | Clavier |
      | REF-003   | Souris  |
    Quand l'utilisateur sélectionne la catégorie "Périphériques"
    Alors le nombre de produits retournés est 2
    Et le produit "REF-001" fait partie des résultats
