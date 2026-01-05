package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;

/**
 * Interface scellée regroupant les requêtes métier du domaine Product (CQRS Read).
 *
 * <p>Les requêtes sont des instructions de lecture sans effet de bord :
 * - {@code GetProductByIdQuery} : récupérer un produit par son identifiant,
 * - {@code ListProductQuery} : lister tous les produits (paginé),
 * - {@code ListProductBySkuIdPatternQuery} : rechercher des produits par pattern de SKU (paginé).
 *
 * <p>Chaque requête encapsule ses paramètres (identifiant, pagination, pattern de recherche).
 * Le service d'application ({@link ReadProductService}) implémente les gestionnaires
 * pour traiter ces requêtes.
 *
 * todo doc OK
 */

public sealed interface ProductQuery {
    public record GetProductByIdQuery(ProductId productId) implements ProductQuery {
    }

    public record ListProductQuery(int page, int size) implements ProductQuery {
    }

    public record ListProductBySkuIdPatternQuery(String skuIdPattern, int page, int size) implements ProductQuery {
    }
}
