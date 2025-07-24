package com.apiconsultacreditos.mapper;

import com.apiconsultacreditos.dto.CreditoResponse;
import com.apiconsultacreditos.model.Credito;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreditoMapper {
    CreditoResponse toResponse(Credito entity);
    
    // Método para conversão reversa
    Credito toEntity(CreditoResponse dto);
}