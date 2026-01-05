package org.ormi.priv.tfa.orderflow.productregistry.infra.web.dto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * Mappeur MapStruct pour la conversion entre DTOs HTTP et commandes métier.
 *
 * <p>Transforme les DTOs reçus depuis l'API REST en commandes applicatives :
 * - {@code toCommand()} : convertit {@link RegisterProductCommandDto} en {@link RegisterProductCommand},
 * - {@code toDto()} : fait la conversion inverse pour les réponses.
 *
 * <p>Utilise {@link SkuIdMapper} pour la conversion des valeurs SKU.
 *
 * todo doc OK
 */

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = true),
    uses = { SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommandDtoMapper {
    public RegisterProductCommand toCommand(RegisterProductCommandDto dto);
    public RegisterProductCommandDto toDto(RegisterProductCommand command);
}
