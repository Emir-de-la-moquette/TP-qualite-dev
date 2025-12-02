# Exercice 2

### 1. Quelle est la différence entre les tests unitaires et les tests d'intégration ?

Les tests unitaires, qui évaluent les fonctions individuelles.
L’integration testing, pour voir comment les composants interagissent.


### 2. Est-il pertinent de systématiquement couvrir 100% de la base de code par des tests ? Expliquer votre réponse.

C'est quand même vachement couteux de faire ça, et pour certains éléments simple est-ce que ça vaut le cout ? pas forcément
je pense qu'il est rarement pertinent de couvrir 100%, mais c'est bien de couvrir une bonne partie, surtout celle critique.


### 3. Quels avantages apporte une architecture en couches d'oignon dans la couverture des tests ? Expliquer votre réponse en prenant pour exemple ce que vous avez pu observer sur l'écriture des tests de la tâche 3.

l'architecture en couche d'oignon isole les règles métier du reste de l’application
Les tests sont plus rapides et plus fiables.

- La base métier ne dépend pas de la bd, de l'API ou de l'ui.
- Les dépendances sont les couches externes (bd, ui, services) donc peuvent etre testé à part.

Un exemple ? euh ouais tkt...

### 4. Expliquer la nomenclature des packages infra, application, jpa, web, client, model.

| Package         | Rôle                          |
| --------------- | ----------------------------- |
| model           | Règles métier, entités métier |
| application     | Cas d’usage, orchestration    |
| infra           | Technique : BD, files, config |
| jpa             | Implémentations persist. JPA  |
| web             | API HTTP, controllers, DTO    |
| client          | Appels vers services externes |