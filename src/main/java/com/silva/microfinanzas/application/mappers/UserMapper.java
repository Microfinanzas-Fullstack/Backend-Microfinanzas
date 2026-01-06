package com.silva.microfinanzas.application.mappers;

import com.silva.microfinanzas.application.dtos.UserResponseDTO;
import com.silva.microfinanzas.domain.entities.User;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre User y DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Mapea User a UserResponseDTO.
     * Excluye el password por seguridad.
     */
    UserResponseDTO toResponseDTO(User user);
}

