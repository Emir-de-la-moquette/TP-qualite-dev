package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

/**
 * Interface scellée regroupant les commandes métier du domaine Product.
 *
 * <p>Les commandes sont des instructions d'ordre de mettre à jour l'état du domaine :
 * - {@code RegisterProductCommand} : créer un nouveau produit,
 * - {@code RetireProductCommand} : retirer un produit existant,
 * - {@code UpdateProductNameCommand} : modifier le nom d'un produit,
 * - {@code UpdateProductDescriptionCommand} : modifier la description d'un produit.
 *
 * <p>Chaque commande encapsule ses paramètres métier (SKU, identifiant, nouvelles valeurs).
 * Les services applicatifs ({@code RegisterProductService}, {@code UpdateProductService},
 * {@code RetireProductService}) implémentent les gestionnaires pour traiter ces commandes.
 *
 * todo doc OK
 */

public sealed interface ProductCommand {
    public record RegisterProductCommand(
            String name,
            String description,
            SkuId skuId) implements ProductCommand {
    }

    public record RetireProductCommand(ProductId productId) implements ProductCommand {
    }

    public record UpdateProductNameCommand(ProductId productId, String newName) implements ProductCommand {
    }

    public record UpdateProductDescriptionCommand(ProductId productId, String newDescription) implements ProductCommand {
    }
}
