# language: fr
Fonctionnalité: Création de compte
  En tant qu'utilisateur
  Je veux créer un compte
  Afin de pouvoir passer des commandes

  Scénario: Inscription réussie avec un nouvel identifiant
    Étant donné aucun compte n'existe pour le nom d'utilisateur "alice"
    Quand l'utilisateur s'inscrit avec l'email "alice@boutique.com", le nom d'utilisateur "alice" et le mot de passe "secret123"
    Alors l'inscription est confirmée
    Et le compte "alice" est enregistré

  Scénario: Inscription refusée avec un identifiant déjà existant
    Étant donné un compte existe déjà pour le nom d'utilisateur "alice"
    Quand l'utilisateur s'inscrit avec l'email "alice@boutique.com", le nom d'utilisateur "alice" et le mot de passe "secret123"
    Alors l'inscription est refusée car le compte existe déjà
