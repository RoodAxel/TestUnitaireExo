# language: fr
Fonctionnalité: Connexion
  En tant qu'utilisateur
  Je veux me connecter à mon compte
  Afin d'accéder à l'application et de passer des commandes

  Scénario: Connexion réussie
    Étant donné un compte "alice" avec le mot de passe "secret123"
    Quand l'utilisateur se connecte avec le nom d'utilisateur "alice" et le mot de passe "secret123"
    Alors la connexion réussit
    Et l'utilisateur est redirigé vers la page "ACCUEIL"

  Scénario: Connexion refusée avec un mauvais mot de passe
    Étant donné un compte "alice" avec le mot de passe "secret123"
    Quand l'utilisateur se connecte avec le nom d'utilisateur "alice" et le mot de passe "mauvais"
    Alors la connexion échoue avec un message d'erreur

  Scénario: Connexion refusée pour un compte inexistant
    Étant donné il n'existe aucun compte au nom de "bob"
    Quand l'utilisateur se connecte avec le nom d'utilisateur "bob" et le mot de passe "secret123"
    Alors la connexion échoue avec un message d'erreur
