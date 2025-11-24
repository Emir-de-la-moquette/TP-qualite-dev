### 1. Quels sont les principaux domaines métiers de l'application Order flow ?

-> création de produit
-> modif de produit

-> achat de produit
--> gestion des commandes

-> UI

### 2. Comment les services sont-ils conçus pour implémenter les domaines métiers ?

-> product registry
-> store back / front

--> liquibase
---> postgre

┌─────────────────────────┐
│  Store Front (UI/Web)   │
└───────────────▲─────────┘
                │ HTTP API
┌───────────────┴─────────┐
│   Store Back Service     │
│  (Domaine magasin)       │
└───────┬─────────────────┘
        │ appelle
┌───────┴─────────────────┐
│   Product Registry       │
│  (Domaine produit)       │
└───────┬─────────────────┘
        │ via ports
        ▼
PostgreSQL + Liquibase
