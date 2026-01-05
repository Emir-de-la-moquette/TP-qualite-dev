package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent;
import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent.DomainEventPayload;
import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * MapStruct mapper converting {@link EventEnvelope} instances
 * into {@link EventLogEntity} for JPA persistence.
 * 
 * Handles extraction of aggregate metadata and serialization
 * of the event payload to JSON.
 */


@Mapper(
    componentModel = "cdi", // au moins un stage serait bien sniff
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EventLogJpaMapper {

    @Mapping(target = "aggregateType", expression = "java(envelope.aggregateType())")
    @Mapping(target = "aggregateId", expression = "java(envelope.aggregateId())")
    @Mapping(target = "aggregateVersion", expression = "java(envelope.sequence())")
    @Mapping(target = "eventType", expression = "java(resolveEventType(envelope.event()))")
    @Mapping(target = "eventVersion", expression = "java(resolveEventVersion(envelope.event()))")
    @Mapping(target = "occurredAt", expression = "java(envelope.timestamp())")
    @Mapping(target = "payload", expression = "java(toJson(envelope.event().payload(), objectMapper))")
    public EventLogEntity toEntity(EventEnvelope<?> envelope, @Context ObjectMapper objectMapper);

    default String resolveEventType(DomainEvent event) {
        return event.eventType();
    }

    default int resolveEventVersion(DomainEvent event) {
        return event.version();
    }

    default JsonNode toJson(DomainEventPayload payload, @Context ObjectMapper om) {
        return om.valueToTree(payload);
    }
}
