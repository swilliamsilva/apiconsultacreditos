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

) {
    
    public String getNumeroCredito() {
        return numeroCredito();
    }

    public String getNumeroNfse() {
        return numeroNfse();
    }

    public LocalDate getDataConstituicao() {
        return dataConstituicao();
    }

    public BigDecimal getValorIssqn() {
        return valorIssqn();
    }

    public String getTipoCredito() {
        return tipoCredito();
    }

    public boolean isSimplesNacional() {
        return simplesNacional();
    }

    public BigDecimal getAliquota() {
        return aliquota();
    }

    public BigDecimal getValorFaturado() {
        return valorFaturado();
    }

    public BigDecimal getValorDeducao() {
        return valorDeducao();
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo();
    }

    
}