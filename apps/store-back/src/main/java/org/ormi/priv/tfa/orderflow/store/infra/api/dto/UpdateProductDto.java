package org.ormi.priv.tfa.orderflow.store.infra.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * DTO encapsulant une demande complexe de mise à jour de produit avec opérations multiples.
 *
 * <p>Structure :
 * - {@code id} : identifiant du produit à modifier,
 * - {@code operations} : tableau d'opérations polymorphes à appliquer.
 *
 * <p>Types d'opérations supportées (via {@link UpdateOperation}) :
 * - {@code UpdateNameOperation} : change le nom du produit,
 * - {@code UpdateDescriptionOperation} : change la description.
 *
 * <p>Utilise la sérialisation polymorphe Jackson avec annotation
 * {@code @JsonTypeInfo} et {@code @JsonSubTypes} pour supporter
 * la désérialisation intelligente des opérations hétérogènes.
 *
 * todo doc OK
 */

public record UpdateProductDto(String id, UpdateOperation[] operations) {

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateNameOperation.class, name = "UpdateProductName"),
        @JsonSubTypes.Type(value = UpdateDescriptionOperation.class, name = "UpdateProductDescription")
    })
    public interface UpdateOperation {
        UpdateProductOperationType type();
    }

    @JsonTypeName("UpdateProductName")
    public record UpdateNameOperation(UpdateProductOperationType type, UpdateNameOperationPayload payload) implements UpdateOperation {
        public record UpdateNameOperationPayload(String name) {}
    }

    @JsonTypeName("UpdateProductDescription")
    public record UpdateDescriptionOperation(UpdateProductOperationType type, UpdateDescriptionOperationPayload payload) implements UpdateOperation {
        public record UpdateDescriptionOperationPayload(String description) {}
    }

    public enum UpdateProductOperationType {
        @JsonProperty("UpdateProductName")
        UPDATE_NAME,
        @JsonProperty("UpdateProductDescription")
        UPDATE_DESCRIPTION;
    }
}
