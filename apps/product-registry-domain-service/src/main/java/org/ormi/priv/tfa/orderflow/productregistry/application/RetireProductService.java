package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRetired;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RetireProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d'application responsable de gérer le retrait (déactivation) des produits.
 *
 * <p>Implémente le workflow de retrait d'un produit (transition ACTIVE → RETIRED) :
 * 1. Charge l'agrégat Product,
 * 2. Applique le retrait via la méthode du domaine (qui valide et produit l'événement),
 * 3. Persiste l'agrégat modifié,
 * 4. Enregistre l'événement {@code ProductRetired} dans l'Event Log,
 * 5. Publie l'événement via l'Outbox pour notification asynchrone.
 *
 * <p>Le retrait est irréversible : un produit retiré ne peut plus être modifié.
 * Cette contrainte est enforced au niveau du Kernel et du Projecteur.
 *
 * todo doc OK
 */
@ApplicationScoped
public class RetireProductService {

    @Inject
    ProductRepository repository;
    @Inject
    EventLogRepository eventLog;
    @Inject
    OutboxRepository outbox;

    @Transactional
    public void retire(RetireProductCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductRetired> evt = product.retire();
        repository.save(product);
        // Append event to the log
        final EventLogEntity persistedEvent = eventLog.append(evt);
        // Publish outbox
        outbox.publish(OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build());
    }
}
