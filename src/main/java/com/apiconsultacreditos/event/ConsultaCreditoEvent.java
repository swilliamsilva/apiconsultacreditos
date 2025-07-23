package com.apiconsultacreditos.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConsultaCreditoEvent implements Serializable {
    private final String numeroCredito;
    private final String numeroNfse;
    private final LocalDate dataConstituicao;
    private final BigDecimal valorIssqn;
    private final String tipoCredito;
    private final boolean simplesNacional;
    private final BigDecimal aliquota;
    private final BigDecimal valorFaturado;
    private final BigDecimal valorDeducao;
    private final BigDecimal baseCalculo;
    private final LocalDateTime timestamp;

    // Construtor privado usado pelo Builder
    private ConsultaCreditoEvent(Builder builder) {
        this.numeroCredito = builder.numeroCredito;
        this.numeroNfse = builder.numeroNfse;
        this.dataConstituicao = builder.dataConstituicao;
        this.valorIssqn = builder.valorIssqn;
        this.tipoCredito = builder.tipoCredito;
        this.simplesNacional = builder.simplesNacional;
        this.aliquota = builder.aliquota;
        this.valorFaturado = builder.valorFaturado;
        this.valorDeducao = builder.valorDeducao;
        this.baseCalculo = builder.baseCalculo;
        this.timestamp = LocalDateTime.now();
    }

    // Construtor vazio necessário para serialização
    public ConsultaCreditoEvent() {
        this.timestamp = LocalDateTime.now();
        this.numeroCredito = null;
        this.numeroNfse = null;
        this.dataConstituicao = null;
        this.valorIssqn = null;
        this.tipoCredito = null;
        this.simplesNacional = false;
        this.aliquota = null;
        this.valorFaturado = null;
        this.valorDeducao = null;
        this.baseCalculo = null;
    }

    // Getters (mantidos iguais)
    public String getNumeroCredito() { return numeroCredito; }
    public String getNumeroNfse() { return numeroNfse; }
    public LocalDate getDataConstituicao() { return dataConstituicao; }
    public BigDecimal getValorIssqn() { return valorIssqn; }
    public String getTipoCredito() { return tipoCredito; }
    public boolean isSimplesNacional() { return simplesNacional; }
    public BigDecimal getAliquota() { return aliquota; }
    public BigDecimal getValorFaturado() { return valorFaturado; }
    public BigDecimal getValorDeducao() { return valorDeducao; }
    public BigDecimal getBaseCalculo() { return baseCalculo; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Classe Builder
    public static class Builder {
        private String numeroCredito;
        private String numeroNfse;
        private LocalDate dataConstituicao;
        private BigDecimal valorIssqn;
        private String tipoCredito;
        private boolean simplesNacional;
        private BigDecimal aliquota;
        private BigDecimal valorFaturado;
        private BigDecimal valorDeducao;
        private BigDecimal baseCalculo;

        public Builder numeroCredito(String numeroCredito) {
            this.numeroCredito = numeroCredito;
            return this;
        }

        public Builder numeroNfse(String numeroNfse) {
            this.numeroNfse = numeroNfse;
            return this;
        }

        public Builder dataConstituicao(LocalDate dataConstituicao) {
            this.dataConstituicao = dataConstituicao;
            return this;
        }

        public Builder valorIssqn(BigDecimal valorIssqn) {
            this.valorIssqn = valorIssqn;
            return this;
        }

        public Builder tipoCredito(String tipoCredito) {
            this.tipoCredito = tipoCredito;
            return this;
        }

        public Builder simplesNacional(boolean simplesNacional) {
            this.simplesNacional = simplesNacional;
            return this;
        }

        public Builder aliquota(BigDecimal aliquota) {
            this.aliquota = aliquota;
            return this;
        }

        public Builder valorFaturado(BigDecimal valorFaturado) {
            this.valorFaturado = valorFaturado;
            return this;
        }

        public Builder valorDeducao(BigDecimal valorDeducao) {
            this.valorDeducao = valorDeducao;
            return this;
        }

        public Builder baseCalculo(BigDecimal baseCalculo) {
            this.baseCalculo = baseCalculo;
            return this;
        }

        public ConsultaCreditoEvent build() {
            return new ConsultaCreditoEvent(this);
        }
    }
}