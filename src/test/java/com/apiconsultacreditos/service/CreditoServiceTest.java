package com.apiconsultacreditos.service;

import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.repository.CreditoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Remove a necessidade do contexto Spring
@ActiveProfiles("test")
class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @InjectMocks
    private CreditoService service;

    @Test
    void testConsultarPorNfse() {
        Credito mockCredito = new Credito();
        mockCredito.setNumeroNfse("7891011");
        when(creditoRepository.findByNumeroNfse("7891011"))
                .thenReturn(List.of(mockCredito));

        List<Credito> result = service.consultarPorNfse("7891011");
        assertEquals(1, result.size());
        assertEquals("7891011", result.get(0).getNumeroNfse());
    }

    @Test
    void testConsultarPorNumeroCredito() {
        Credito mockCredito = new Credito();
        mockCredito.setNumeroCredito("123456");
        
        when(creditoRepository.findByNumeroCredito("123456"))
            .thenReturn(Optional.of(mockCredito));
        
        Credito result = service.consultarPorNumeroCredito("123456");
        
        assertNotNull(result, "O crédito não deveria ser nulo");
        assertEquals("123456", result.getNumeroCredito(), 
            "Número do crédito diferente do esperado");
    }
}