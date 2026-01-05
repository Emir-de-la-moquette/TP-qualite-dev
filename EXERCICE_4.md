# Exercice 4

## Tâche 1 : Questions sur la base de code

### 1. Expliquer le rôle de l'interface Projector dans le système de gestion des événements.
L'interface permet de projeter les evenements sur les etats cqrs, elle met à jour l'état.

### 2. Expliquer le rôle du type S dans l'interface Projector.
le type S est l'objet générique lié aux évènements :

- OrderState pour une commande
- UserProfile pour un utilisateur
- InventoryState pour un stock

donc le return du projector c'est ce type

### 3. Compléter la Javadoc de l'interface Projector en ajoutant la description de S.

/**
 * Projector interface for projecting events onto a state.
 * @param < S > the type of the state being reconstructed or updated through event projection
 * @param < E > the type of event envelopes handled by the projector

 */

### 4. Quel est l'intérêt de passer par une interface Projector plutôt que d'utiliser une classe concrète ?
c'est modulaire -> séparation des composants -> indépendance/flexibilité -> meilleur pour test


### 5. Quel est le rôle de la classe ProjectionResult dans l'interface Projector ?
c'est le résultat des états/projections

-> permet d'effectuer des actions sur ces dernières

- isSuccess()
- orElse()
- map()
- flatMap()

Dcp ça permet d'enchainer plusieurs projections, d'arrêter proprement en cas d’erreur/éviter les crash et exceptions




## Tâche 2 : Questions concernant l'Outboxing

### 1. Expliquer le rôle de l'interface OutboxRepository dans le système de gestion des événements.
cette interface permet l'accès à outboxentity, elle garantie la persistence

elle peut enregistrer ou supprimer les evenements dans outbox
regarder les envenements et peut les marquer comme "failed"


### 2. Expliquer comment l'Outbox Pattern permet de garantir la livraison des événements dans un système distribué.
le principe d'outbox permet de faire l'entre 2, quand les opérations sont pas atomique il distribue et met en attente.

il gère les opérations puis met à jour les évènements liés

### 3. En analysant le code existant, décrire le fonctionnement de l'Outbox Pattern concrètement dans le contexte de l'application. Créez un diagramme pour illustrer le flux des événements. Créez un diagramme de séquence pour montrer le séquencement des interactions entre les différents composants. Précisez les intéractions transactionnelles.

```
 ┌─────────────┐         ┌────────────────┐          ┌───────────────────┐
 |  Domaine    |         |   Outbox       |          | Event Publisher   |
 | (Service)   |         | (DB table)     |          | (Scheduler/Worker)|
 └──────┬──────┘         └───────┬────────┘          └────────┬──────────┘
        |                        |                            |
        | Produit un événement   |                            |
        |----------------------->|                            |
        |                        |                            |
        | 1. Transaction :       |                            |
        | - maj état métier      |                            |
        | - insert outbox        |                            |
        |----------------------->|                            |
        |                        |                            |
        |                        | 2. Lire entrées PENDING    |
        |                        |<---------------------------|
        |                        |                            |
        |                        | 3. Publier événement       |
        |                        |--------------------------->|
        |                        |                            |
        |                        | 4. Mark as SENT            |
        |                        |<---------------------------|

```
(chatgpt à refait le truc propre)

### 4. Expliquer comment l'Outbox Pattern peut être utilisé pour gérer les erreurs de livraison des événements dans cette base de code. Référez-vous ici au schéma de données dans les fichiers XML liquibase et aux implémentations concrètes.

L’événement est marqué FAILED ou PENDING (jsp vraiment mdr) si il a été foiré

dans outboxentity on peut voir qu'il va tenter de retry, jusqu'à un certain point
après s'il a tjrs pas été livré alors il sera bien marqué comme FAILED sans retry

- void markFailed(OutboxEntity entity, String err);
- void markFailed(OutboxEntity entity, String err, int retryAfter);



## Tâche 3 : Questions concernant le journal d'évènements

### 1. Expliquer le rôle du journal d'événements dans le système de gestion des événements.
Le journal d'événements (Event Log) est le registre immuable de tous les événements du domaine. Il constitue la source de vérité unique (single source of truth) permettant la reconstruction complète de l'état du système à tout moment. C'est la base de l'event sourcing.

### 2. Pourquoi l'interface EventLogRepository ne comporte-t-elle qu'une seule méthode append ?
Append-only garantit l'immuabilité du journal. Pas de modification/suppression = cohérence garantie. Les lectures se font via des projections (Projector). C'est la séparation des responsabilités : l'Event Log n'écrit que, les lectures passent par CQRS.

### 3. Implications de cette conception et autres usages.
L'immuabilité assure une trace d'audit complète et irrévocable. Autres usages : replay d'événements, reconstruction de projections, debugging, conformité réglementaire, migration de données.




## Tâche 4 : Limites de CQRS

### 1. Principales limites de CQRS.
- Complexité accrue (multi-modèles)
- Cohérence éventuelle (lag entre commandes et lectures)
- Synchronisation des projections difficile
- Coût de maintenance élevé

### 2. Limites compensées par l'implémentation.
Outbox Pattern garantit la livraison des événements. Projections avec ProjectionResult gèrent les erreurs proprement. Event Log assure l'audit et le replay.

### 3. Autres limites introduites.
Dépendance accrue à la base de données. Points de contention sur Outbox. Latence de projection imprévisible.

### 4. Projection multiple.
Les événements multiples sont publiés parallèlement vers différentes projections via les Projector concrets. Un événement peut déclencher plusieurs actions asynchrones indépendantes. Risque : incohérence temporaire entre projections.

### 5. Solutions pour atténuer les limites.
- Timeouts sur les projections
- Cache des projections fréquentes
- Monitoring du lag Outbox-Projection
- Idempotence obligatoire des handlers
- Dead letter queues pour événements non traitables