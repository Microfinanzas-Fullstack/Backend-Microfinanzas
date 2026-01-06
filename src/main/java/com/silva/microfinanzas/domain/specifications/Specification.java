package com.silva.microfinanzas.domain.specifications;

/**
 * Interface base para el patrón Specification.
 *
 * El patrón Specification permite encapsular reglas de negocio que pueden
 * ser combinadas, reutilizadas y testeadas de forma independiente.
 *
 * Es fundamental en DDD para mantener la lógica de validación y reglas
 * de negocio dentro del dominio.
 *
 * @param <T> El tipo de entidad que será evaluada por esta especificación
 */
public interface Specification<T> {

    /**
     * Verifica si la entidad satisface la especificación.
     *
     * @param entity La entidad a evaluar
     * @return true si cumple la especificación, false en caso contrario
     */
    boolean isSatisfiedBy(T entity);

    /**
     * Combina esta especificación con otra usando AND lógico.
     */
    default Specification<T> and(Specification<T> other) {
        return entity -> this.isSatisfiedBy(entity) && other.isSatisfiedBy(entity);
    }

    /**
     * Combina esta especificación con otra usando OR lógico.
     */
    default Specification<T> or(Specification<T> other) {
        return entity -> this.isSatisfiedBy(entity) || other.isSatisfiedBy(entity);
    }

    /**
     * Niega esta especificación.
     */
    default Specification<T> not() {
        return entity -> !this.isSatisfiedBy(entity);
    }
}

