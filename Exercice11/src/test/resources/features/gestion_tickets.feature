# language: fr
Fonctionnalité: Gestion des tickets de support
  En tant qu'utilisateur du support
  Je veux gérer le cycle de vie des tickets
  Afin de suivre la résolution des incidents

  Scénario: Création d'un ticket valide
    Étant donné un titre "Bug sur la page de paiement" et une priorité "HIGH"
    Quand l'utilisateur crée le ticket
    Alors le ticket est créé avec le statut "OPEN"

  Scénario: Résolution d'un ticket
    Étant donné un ticket ouvert intitulé "Bug sur la page de paiement" de priorité "MEDIUM"
    Quand l'utilisateur change le statut du ticket en "RESOLVED"
    Alors le ticket a le statut "RESOLVED"

  Scénario: Refus de modification d'un ticket déjà résolu
    Étant donné un ticket résolu intitulé "Incident serveur" de priorité "HIGH"
    Quand l'utilisateur change le statut du ticket en "IN_PROGRESS"
    Alors une erreur de conflit de statut est renvoyée

  Scénario: Consultation d'un ticket inexistant
    Quand l'utilisateur consulte le ticket numéro 99999
    Alors une erreur de ticket introuvable est renvoyée
