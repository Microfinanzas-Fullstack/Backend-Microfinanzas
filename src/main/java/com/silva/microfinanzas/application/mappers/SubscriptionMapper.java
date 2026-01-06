package com.silva.microfinanzas.application.mappers;

import com.silva.microfinanzas.application.dtos.SubscriptionResponseDTO;
import com.silva.microfinanzas.domain.entities.Subscription;
import com.silva.microfinanzas.domain.valueobjects.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre Subscription y DTOs.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    /**
     * Mapea Subscription a SubscriptionResponseDTO.
     */
    @Mapping(target = "amount", expression = "java(subscription.getAmount().getAmount())")
    @Mapping(target = "currency", expression = "java(subscription.getAmount().getCurrency())")
    SubscriptionResponseDTO toResponseDTO(Subscription subscription);

    default java.math.BigDecimal mapMoneyToAmount(Money money) {
        return money != null ? money.getAmount() : null;
    }

    default String mapMoneyToCurrency(Money money) {
        return money != null ? money.getCurrency() : null;
    }
}

