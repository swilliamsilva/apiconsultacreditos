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
        String nfse = "NFSE-001";
        Credito credito1 = criarCredito("CRD-001", nfse);
        Credito credito2 = criarCredito("CRD-002", nfse);
        
        when(creditoService.consultarPorNfse(nfse)).thenReturn(List.of(credito1, credito2));
        
        // Configurar o mapper para retornar respostas completas
        when(creditoMapper.toResponse(credito1)).thenReturn(criarResponse("CRD-001", nfse));
        when(creditoMapper.toResponse(credito2)).thenReturn(criarResponse("CRD-002", nfse));
        
        // Act & Assert
        mockMvc.perform(get("/api/creditos/nfse/{numeroNfse}", nfse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCredito").value("CRD-001"))
                .andExpect(jsonPath("$[1].numeroCredito").value("CRD-002"));
    }

    @Test
    void listarTodos_DeveRetornarTodosCreditos() throws Exception {
        // Arrange
        Credito credito1 = criarCredito("CRD-001", "NFSE-001");
        Credito credito2 = criarCredito("CRD-002", "NFSE-002");
        
        when(creditoService.listarTodos()).thenReturn(List.of(credito1, credito2));
        
        // Configurar o mapper para retornar respostas completas
        when(creditoMapper.toResponse(credito1)).thenReturn(criarResponse("CRD-001", "NFSE-001"));
        when(creditoMapper.toResponse(credito2)).thenReturn(criarResponse("CRD-002", "NFSE-002"));
        
        // Act & Assert
        mockMvc.perform(get("/api/creditos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCredito").value("CRD-001"))
                .andExpect(jsonPath("$[1].numeroCredito").value("CRD-002"));
    }

    private Credito criarCredito(String numeroCredito, String numeroNfse) {
        Credito credito = new Credito();
        credito.setNumeroCredito(numeroCredito);
        credito.setNumeroNfse(numeroNfse);
        credito.setDataConstituicao(LocalDate.now());
        credito.setValorIssqn(new BigDecimal("1500.00"));
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("5.0"));
        credito.setValorFaturado(new BigDecimal("30000.00"));
        credito.setValorDeducao(new BigDecimal("5000.00"));
        credito.setBaseCalculo(new BigDecimal("25000.00"));
        return credito;
    }
    
    private CreditoResponse criarResponse(String numeroCredito, String numeroNfse) {
        return new CreditoResponse(
            numeroCredito,
            numeroNfse,
            LocalDate.now(),
            new BigDecimal("1500.00"),
            "ISSQN",
            true,
            new BigDecimal("5.0"),
            new BigDecimal("30000.00"),
            new BigDecimal("5000.00"),
            new BigDecimal("25000.00")
        );
    }
}