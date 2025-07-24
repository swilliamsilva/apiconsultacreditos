package com.apiconsultacreditos.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.apiconsultacreditos.dto.CreditoResponse; // Alterado para CreditoResponse
import com.apiconsultacreditos.model.Credito;

public class CreditoMapperTest {

    private final CreditoMapper mapper = Mappers.getMapper(CreditoMapper.class);

    @Test
    public void testEntityToDtoAndBack() {
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
      

        // Corrigido para CreditoResponse
        CreditoResponse dto = mapper.toResponse(credito);
        
        // Assertivas (ajustado para métodos do CreditoResponse)
        assertEquals(credito.getNumeroCredito(), dto.getNumeroCredito());
        assertEquals(credito.getNumeroNfse(), dto.getNumeroNfse());
        assertEquals(credito.getDataConstituicao(), dto.getDataConstituicao());
        assertEquals(credito.getValorIssqn(), dto.getValorIssqn());
        assertEquals(credito.getTipoCredito(), dto.getTipoCredito());
        assertEquals(credito.isSimplesNacional(), dto.simplesNacional());
        assertEquals(credito.getAliquota(), dto.getAliquota());
        assertEquals(credito.getValorFaturado(), dto.getValorFaturado());
        assertEquals(credito.getValorDeducao(), dto.getValorDeducao());
        assertEquals(credito.getBaseCalculo(), dto.getBaseCalculo());
      

        // Usando o novo método toEntity()
        Credito reverted = mapper.toEntity(dto);
        
        // Assertivas reversas
        assertEquals(dto.getNumeroCredito(), reverted.getNumeroCredito());
        assertEquals(dto.getNumeroNfse(), reverted.getNumeroNfse());
        assertEquals(dto.getDataConstituicao(), reverted.getDataConstituicao());
        assertEquals(dto.getValorIssqn(), reverted.getValorIssqn());
        assertEquals(dto.getTipoCredito(), reverted.getTipoCredito());
        assertEquals(dto.simplesNacional(), reverted.isSimplesNacional());
        assertEquals(dto.getAliquota(), reverted.getAliquota());
        assertEquals(dto.getValorFaturado(), reverted.getValorFaturado());
        assertEquals(dto.getValorDeducao(), reverted.getValorDeducao());
        assertEquals(dto.getBaseCalculo(), reverted.getBaseCalculo());
        
    }
}