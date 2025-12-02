# Exercice 1

### 1. Quels sont les principaux domaines métiers de l'application Order flow ?

-> création de produit
-> modif de produit

-> UI

### 2. Comment les services sont-ils conçus pour implémenter les domaines métiers ?

- product registry
- store back / front
- liquibase ---> postgre

```
┌─────────────────────────┐
│  Store Front (UI/Web)   │
└───────────────▲─────────┘
                │ Rendu web
┌───────────────┴─────────┐
│   Store Back Service    │
│  (Domaine magasin)      │
└───────┬─────────────────┘
        │ Appel Données
┌───────┴─────────────────┐
│   Product Registry      │
│  (Domaine produit)      │
└───────┬─────────────────┘
        │ Request BD
        ▼
PostgreSQL + Liquibase
```

### 3. Que fait la bibliothèque libs/cqrs-support ? Comment est-elle utilisée dans les autres modules (relation entre métier et structure du code) ?

CQRS (Command Query Responsibility Segregation) est un modèle, il gère les events, les vues (projection des microservices), Typage, et persistances des données (save des events)


L'application order flow utilise le modèle CQRS pour séparer les opérations de lecture et d'écriture. Cela permet d'optimiser les performances et de simplifier la gestion des données.


### 4. Que fait la bibliothèque libs/bom-platform ?

bom-platform permet de gérer les dépendances et les "materials"


### 5. Comment l'implémentation actuelle du CQRS et du Kernel assure-t-elle la fiabilité des états internes de l'application ?

CQRS gère que tout est bien géré et sauvegardé et le kernel permet de s'assurer de l'orchestration des opérations en isolant les transactions.
Dcp tout ça fait que l'application est bien solide et les données fiables tkt.
