package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * Mappeur MapStruct pour la conversion bidirectionnelle agrégat ↔ entité JPA.
 *
 * <p>Définit les transformations :
 * - {@code toDomain()} : convertit {@link ProductEntity} en {@link Product},
 * - {@code toEntity()} : convertit {@link Product} en {@link ProductEntity},
 * - {@code updateEntity()} : met à jour partiellement une entité depuis l'agrégat.
 *
 * <p>Délègue aux mappeurs de types valeurs ({@link ProductIdMapper}, {@link SkuIdMapper})
 * pour la conversion des wrappers immuables.
 *
 * todo doc OK
 */

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = { ProductIdMapper.class, SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductJpaMapper {

    public abstract Product toDomain(ProductEntity entity);

    public abstract void updateEntity(Product product, @MappingTarget ProductEntity entity);

    public abstract ProductEntity toEntity(Product product);
}
