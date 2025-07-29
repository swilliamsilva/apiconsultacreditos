package com.apiconsultacreditos.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.apiconsultacreditos.dto.CreditoResponse;
import com.apiconsultacreditos.model.Credito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreditoMapperTest {
    
    private final CreditoMapper mapper = Mappers.getMapper(CreditoMapper.class);

    @Test
    void testEntityToDtoAndBack() {
        Credito credito = new Credito();
        credito.setNumeroCredito("CRD123");
        credito.setNumeroNfse("NF456");
        credito.setDataConstituicao(LocalDate.now());
        credito.setValorIssqn(new BigDecimal("100.00"));
        credito.setTipoCredito("Simples");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("3.5"));
        credito.setValorFaturado(new BigDecimal("5000"));
        credito.setValorDeducao(new BigDecimal("500"));
        credito.setBaseCalculo(new BigDecimal("4500"));

        CreditoResponse dto = mapper.toResponse(credito);
        
        // Usar métodos de acesso do record (sem 'get')
        assertEquals(credito.getNumeroCredito(), dto.numeroCredito());
        assertEquals(credito.getNumeroNfse(), dto.numeroNfse());
        assertEquals(credito.getDataConstituicao(), dto.dataConstituicao());
        assertEquals(credito.getValorIssqn(), dto.valorIssqn());
        assertEquals(credito.getTipoCredito(), dto.tipoCredito());
        assertEquals(credito.isSimplesNacional(), dto.simplesNacional());
        assertEquals(credito.getAliquota(), dto.aliquota());
        assertEquals(credito.getValorFaturado(), dto.valorFaturado());
        assertEquals(credito.getValorDeducao(), dto.valorDeducao());
        assertEquals(credito.getBaseCalculo(), dto.baseCalculo());

        Credito reverted = mapper.toEntity(dto);
        
        // Usar getters tradicionais na entidade
        assertEquals(dto.numeroCredito(), reverted.getNumeroCredito());
        assertEquals(dto.numeroNfse(), reverted.getNumeroNfse());
        assertEquals(dto.dataConstituicao(), reverted.getDataConstituicao());
        assertEquals(dto.valorIssqn(), reverted.getValorIssqn());
        assertEquals(dto.tipoCredito(), reverted.getTipoCredito());
        assertEquals(dto.simplesNacional(), reverted.isSimplesNacional());
        assertEquals(dto.aliquota(), reverted.getAliquota());
        assertEquals(dto.valorFaturado(), reverted.getValorFaturado());
        assertEquals(dto.valorDeducao(), reverted.getValorDeducao());
        assertEquals(dto.baseCalculo(), reverted.getBaseCalculo());
    }

    @Test
void toResponse_QuandoEntidadeNula_DeveRetornarNulo() {
    CreditoResponse response = mapper.toResponse(null);
    assertThat(response).isNull();
}

@Test
void toEntity_QuandoDTONulo_DeveRetornarNulo() {
    Credito entity = mapper.toEntity(null);
    assertThat(entity).isNull();
}
}