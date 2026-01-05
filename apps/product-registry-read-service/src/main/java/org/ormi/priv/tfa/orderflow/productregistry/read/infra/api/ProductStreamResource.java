package org.ormi.priv.tfa.orderflow.productregistry.read.infra.api;

import jakarta.ws.rs.Path;

/**
 * Contrôleur REST pour le streaming en temps réel des changements produits.
 *
 * <p>Actuellement un squelette (TODO Exercice 5) : sera implémenté pour exposer
 * des endpoints WebSocket/SSE permettant aux clients de s'abonner aux flux
 * d'événements produits par agrégat, diffusés via {@link ProductEventBroadcaster}.
 *
 * todo doc OK
 */

@Path("/products")
public class ProductStreamResource {

    // TODO: implement [Exercice 5]
    // private final ReadProductService readProductService;
    // private final ProductIdMapper productIdMapper;

    // @Inject
    // public ProductStreamResource(
    //         ReadProductService readProductService,
    //         ProductIdMapper productIdMapper) {
    //     this.readProductService = readProductService;
    //     this.productIdMapper = productIdMapper;
    // }

    // TODO: implement [Exercice 5]
    // @GET
    // @Path("/{id}/pending/stream")
    // @RestStreamElementType(MediaType.APPLICATION_JSON)
    // public Multi<ProductStreamElementDto> streamPendingOutboxMessagesByProdutId(
    //         @PathParam("id") String id) {
    //     throw new UnsupportedOperationException("TODO: implement [Exercice 5]");
    // }
}
