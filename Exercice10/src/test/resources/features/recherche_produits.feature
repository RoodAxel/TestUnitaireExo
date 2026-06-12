# language: fr
Fonctionnalité: Recherche de produits
  En tant qu'utilisateur
  Je veux rechercher des produits
  Afin de trouver rapidement ce dont j'ai besoin

  Scénario: Recherche par mot-clé
    Étant donné que la recherche par mot-clé "clavier" renvoie les produits:
      | reference | nom              |
      | REF-001   | Clavier gamer    |
      | REF-002   | Clavier sans fil |
    Quand l'utilisateur recherche les produits avec le mot-clé "clavier"
    Alors le nombre de produits retournés est 2
    Et le produit "REF-001" fait partie des résultats

  Scénario: Recherche par prix maximum
    Étant donné que la recherche par prix maximum 50.0 renvoie les produits:
      | reference | nom    | prix  |
      | REF-003   | Souris | 25.0  |
    Quand l'utilisateur recherche les produits jusqu'au prix 50.0
    Alors le nombre de produits retournés est 1
    Et le produit "REF-003" fait partie des résultats
