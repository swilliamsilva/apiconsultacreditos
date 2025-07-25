package com.apiconsultacreditos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditoResponse(
    String numeroCredito,
    String numeroNfse,
    LocalDate dataConstituicao,
    BigDecimal valorIssqn,
    String tipoCredito,
    boolean simplesNacional,
    BigDecimal aliquota,
    BigDecimal valorFaturado,
    BigDecimal valorDeducao,
    BigDecimal baseCalculo
) {}