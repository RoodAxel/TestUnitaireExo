# language: fr
Fonctionnalité: Gestion des comptes bancaires
  En tant que client de la banque
  Je veux gérer mon compte et mes opérations
  Afin de suivre et déplacer mon argent en toute fiabilité

  Scénario: Création d'un nouveau compte
    Quand je crée un compte "C1" au nom de "Alice"
    Alors le compte "C1" existe avec un solde de 0

  Scénario: Dépôt d'argent sur un compte
    Étant donné un compte "C1" au nom de "Alice"
    Quand je dépose 100 sur le compte "C1"
    Alors le solde du compte "C1" est 100

  Scénario: Retrait avec fonds suffisants
    Étant donné un compte "C1" au nom de "Alice" avec un solde de 100
    Quand je retire 40 du compte "C1"
    Alors le solde du compte "C1" est 60

  Scénario: Retrait avec fonds insuffisants
    Étant donné un compte "C1" au nom de "Alice" avec un solde de 50
    Quand je retire 100 du compte "C1"
    Alors l'opération est refusée
    Et le solde du compte "C1" est 50

  Scénario: Virement entre deux comptes
    Étant donné un compte "C1" au nom de "Alice" avec un solde de 100
    Et un compte "C2" au nom de "Bob"
    Quand je vire 30 du compte "C1" vers le compte "C2"
    Alors le solde du compte "C1" est 70
    Et le solde du compte "C2" est 30

  Scénario: Virement refusé pour solde insuffisant
    Étant donné un compte "C1" au nom de "Alice" avec un solde de 20
    Et un compte "C2" au nom de "Bob"
    Quand je vire 100 du compte "C1" vers le compte "C2"
    Alors l'opération est refusée
    Et le solde du compte "C1" est 20
