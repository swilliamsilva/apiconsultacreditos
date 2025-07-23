// ============================
// CreditoMapper.java
// ============================
package com.apiconsultacreditos.mapper;

import com.apiconsultacreditos.dto.CreditoDTO;
import com.apiconsultacreditos.model.Credito;


import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditoMapper {
    CreditoDTO toDto(Credito entity);
    Credito toEntity(CreditoDTO dto);
    List<CreditoDTO> toDtoList(List<Credito> entities);
}