package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.config.TestSecurityConfig;
import com.apiconsultacreditos.dto.CreditoResponse;
import com.apiconsultacreditos.kafka.ConsultaCreditoProducer;
import com.apiconsultacreditos.mapper.CreditoMapper;
import com.apiconsultacreditos.mapper.EventoMapper;
import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.service.CreditoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CreditoController.class)
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class CreditoControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditoService creditoService;
    
    @MockBean
    private ConsultaCreditoProducer consultaCreditoProducer;
    
    @MockBean
    private CreditoMapper creditoMapper;
    
    @MockBean
    private EventoMapper eventoMapper;
@Test
    void buscarPorNumeroNfse_DeveRetornarCreditos() throws Exception {
        // Arrange
        String nfse = "7891011";
        Credito credito1 = criarCredito("123456", nfse);
        Credito credito2 = criarCredito("789012", nfse);
        
        when(creditoService.consultarPorNfse(nfse)).thenReturn(List.of(credito1, credito2));
        
        // Configurar o mapper para retornar respostas completas
        when(creditoMapper.toResponse(credito1)).thenReturn(criarResponse("123456", nfse));
        when(creditoMapper.toResponse(credito2)).thenReturn(criarResponse("789012", nfse));
        
        // Act & Assert
        mockMvc.perform(get("/api/creditos/nfse/{numeroNfse}", nfse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCredito").value("123456"))
                .andExpect(jsonPath("$[1].numeroCredito").value("789012"));
    }

    @Test
    void buscarPorNumeroCredito_DeveRetornarCredito() throws Exception {
        // Arrange
        String numeroCredito = "123456";
        Credito credito = criarCredito(numeroCredito, "7891011");
        when(creditoService.consultarPorNumeroCredito(numeroCredito))
            .thenReturn(Optional.of(credito));
        
        when(creditoMapper.toResponse(credito)).thenReturn(criarResponse(numeroCredito, "7891011"));
        
        // Act & Assert
        mockMvc.perform(get("/api/creditos/numero/{numeroCredito}", numeroCredito))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCredito").value("123456"));
    }

    @Test
    void buscarPorNumeroCredito_NaoEncontrado() throws Exception {
        // Arrange
        String numeroCredito = "000000";
        when(creditoService.consultarPorNumeroCredito(numeroCredito))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/creditos/numero/{numeroCredito}", numeroCredito))
                .andExpect(status().isNotFound());
    }

    private Credito criarCredito(String numeroCredito, String numeroNfse) {
        return Credito.builder()
            .numeroCredito(numeroCredito)
            .numeroNfse(numeroNfse)
            .dataConstituicao(LocalDate.of(2024, 2, 25))
            .valorIssqn(new BigDecimal("1500.75"))
            .tipoCredito("ISSQN")
            .simplesNacional(true)
            .aliquota(new BigDecimal("5.00"))
            .valorFaturado(new BigDecimal("30000.00"))
            .valorDeducao(new BigDecimal("5000.00"))
            .baseCalculo(new BigDecimal("25000.00"))
            .build();
    }
    
    private CreditoResponse criarResponse(String numeroCredito, String numeroNfse) {
        return new CreditoResponse(
            numeroCredito,
            numeroNfse,
            LocalDate.of(2024, 2, 25),
            new BigDecimal("1500.75"),
            "ISSQN",
            true,
            new BigDecimal("5.00"),
            new BigDecimal("30000.00"),
            new BigDecimal("5000.00"),
            new BigDecimal("25000.00")
        );
    }
}