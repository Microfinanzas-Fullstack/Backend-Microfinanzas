package com.silva.microfinanzas.application.mappers;

import com.silva.microfinanzas.application.dtos.TransactionResponseDTO;
import com.silva.microfinanzas.domain.aggregates.Transaction;
import com.silva.microfinanzas.domain.valueobjects.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre Transaction y DTOs.
 *
 * MapStruct genera automáticamente la implementación en tiempo de compilación.
 * componentModel = "spring" permite inyectar este mapper como un bean de Spring.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Mapea Transaction a TransactionResponseDTO.
     */
    @Mapping(target = "amount", expression = "java(transaction.getAmount().getAmount())")
    @Mapping(target = "currency", expression = "java(transaction.getAmount().getCurrency())")
    TransactionResponseDTO toResponseDTO(Transaction transaction);

    /**
     * Método helper para extraer amount del Value Object Money.
     */
    default java.math.BigDecimal mapMoneyToAmount(Money money) {
        return money != null ? money.getAmount() : null;
    }

    /**
     * Método helper para extraer currency del Value Object Money.
     */
    default String mapMoneyToCurrency(Money money) {
        return money != null ? money.getCurrency() : null;
    }
}
