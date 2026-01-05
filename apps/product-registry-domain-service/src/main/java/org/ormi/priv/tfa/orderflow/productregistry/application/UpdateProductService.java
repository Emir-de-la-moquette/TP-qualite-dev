package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductDescriptionCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductNameCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d'application responsable de gérer les mises à jour des propriétés d'un produit.
 *
 * <p>Expose deux méthodes de gestion pour traiter les commandes de mise à jour :
 * - {@code handle(UpdateProductNameCommand)} : met à jour le nom d'un produit,
 * - {@code handle(UpdateProductDescriptionCommand)} : met à jour la description.
 *
 * <p>Chaque opération suit le même workflow CQRS/Event Sourcing :
 * 1. Charge l'agrégat Product depuis le référentiel,
 * 2. Applique la modification via la méthode du domaine (qui produit un événement),
 * 3. Persiste l'agrégat mis à jour,
 * 4. Enregistre l'événement dans l'Event Log,
 * 5. Publie l'événement dans l'Outbox pour les consommateurs asynchrones.
 *
 * <p>Les modifications ne sont autorisées que sur les produits ACTIFS
 * (cette validation est appliquée par le Kernel).
 *
 * todo doc OK
 */

@ApplicationScoped
public class UpdateProductService {

    ProductRepository repository;
    EventLogRepository eventLog;
    OutboxRepository outbox;

    @Inject
    public UpdateProductService(
        ProductRepository repository,
        EventLogRepository eventLog,
        OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    @Transactional
    public void handle(UpdateProductNameCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductNameUpdated> event = product.updateName(cmd.newName());
        // Save domain object
        repository.save(product);
        // Append event to event log
        final EventLogEntity persistedEvent = eventLog.append(event);
        // Publish event to outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }

    @Transactional
    public void handle(UpdateProductDescriptionCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductDescriptionUpdated> event = product.updateDescription(cmd.newDescription());
        // Save domain object
        repository.save(product);
        // Append event to event log
        final EventLogEntity persistedEvent = eventLog.append(event);
        // Publish event to outbox
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }
}
