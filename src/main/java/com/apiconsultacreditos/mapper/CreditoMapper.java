package com.apiconsultacreditos.mapper;

import com.apiconsultacreditos.dto.CreditoResponse;
import com.apiconsultacreditos.model.Credito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface CreditoMapper {
    CreditoResponse toResponse(Credito entity);
    
    
    default Credito toEntity(CreditoResponse dto) {
        if (dto == null) {
            return null;
        }
        return Credito.builder()
            .numeroCredito(dto.numeroCredito())
            .numeroNfse(dto.numeroNfse())
            .dataConstituicao(dto.dataConstituicao())
            .valorIssqn(dto.valorIssqn())
            .tipoCredito(dto.tipoCredito())
            .simplesNacional(dto.simplesNacional())
            .aliquota(dto.aliquota())
            .valorFaturado(dto.valorFaturado())
            .valorDeducao(dto.valorDeducao())
            .baseCalculo(dto.baseCalculo())
            .build();
    }
}