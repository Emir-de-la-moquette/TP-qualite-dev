package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.concurrent.CopyOnWriteArrayList;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Diffuseur d'événements (broadcaster) utilisant Mutiny pour les flux événementiels en temps réel.
 *
 * <p>Maintient une liste thread-safe d'émetteurs (un par client connecté) et diffuse
 * les changements à tous les abonnés en temps réel :
 * - {@code broadcast()} : envoie un élément à tous les émetteurs,
 * - {@code stream()} : crée un nouveau flux Multi auquel un client peut s'abonner.
 *
 * <p>Utilé via des endpoints WebSocket pour les streamed des événements produits/actualisés
 * sur les produits. Chaque client obtient ses propres émetteur/flux.
 *
 * todo doc OK
 */

@ApplicationScoped
public class ProductEventBroadcaster {

    private final CopyOnWriteArrayList<MultiEmitter<? super ProductStreamElementDto>> emitters = new CopyOnWriteArrayList<>();

    public void broadcast(ProductStreamElementDto element) {
        emitters.forEach(emitter -> emitter.emit(element));
    }

    public Multi<ProductStreamElementDto> stream() {
        return Multi.createFrom().emitter(emitter -> {
            emitters.add(emitter);
            // TODO: log a debug, "New emitter added"

            // TODO: Hey! remove emitters, my RAM is melting! (and log for debugging)
            // TODO: TODO
            emitter.onTermination(() -> emitters.remove(emitter));
        });
    }

    // TODO: implement [Exercice 5]
    // public Multi<ProductStreamElementDto> streamByProductId(String productId) {
    // }

    // TODO: implement [Exercice 5]
    // public Multi<ProductStreamElementDto> streamByProductIds(List<String> productIds) {
    // }
}
