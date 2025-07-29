package com.apiconsultacreditos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credito")
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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

    public Credito() {}

    // Construtor completo
    public Credito(
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
        this.numeroCredito = numeroCredito;
        this.numeroNfse = numeroNfse;
        this.dataConstituicao = dataConstituicao;
        this.valorIssqn = valorIssqn;
        this.tipoCredito = tipoCredito;
        this.simplesNacional = simplesNacional;
        this.aliquota = aliquota;
        this.valorFaturado = valorFaturado;
        this.valorDeducao = valorDeducao;
        this.baseCalculo = baseCalculo;
    }

    // Getters e Setters para todos os campos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCredito() {
        return numeroCredito;
    }

    public void setNumeroCredito(String numeroCredito) {
        this.numeroCredito = numeroCredito;
    }

    public String getNumeroNfse() {
        return numeroNfse;
    }

    public void setNumeroNfse(String numeroNfse) {
        this.numeroNfse = numeroNfse;
    }

    public LocalDate getDataConstituicao() {
        return dataConstituicao;
    }

    public void setDataConstituicao(LocalDate dataConstituicao) {
        this.dataConstituicao = dataConstituicao;
    }

    public BigDecimal getValorIssqn() {
        return valorIssqn;
    }

    public void setValorIssqn(BigDecimal valorIssqn) {
        this.valorIssqn = valorIssqn;
    }

    public String getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(String tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public boolean isSimplesNacional() {
        return simplesNacional;
    }

    public void setSimplesNacional(boolean simplesNacional) {
        this.simplesNacional = simplesNacional;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getValorFaturado() {
        return valorFaturado;
    }

    public void setValorFaturado(BigDecimal valorFaturado) {
        this.valorFaturado = valorFaturado;
    }

    public BigDecimal getValorDeducao() {
        return valorDeducao;
    }

    public void setValorDeducao(BigDecimal valorDeducao) {
        this.valorDeducao = valorDeducao;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

 public static Builder builder() {
        return new Builder();
    }

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

        public Credito build() {
            Credito credito = new Credito();
            credito.setNumeroCredito(this.numeroCredito);
            credito.setNumeroNfse(this.numeroNfse);
            credito.setDataConstituicao(this.dataConstituicao);
            credito.setValorIssqn(this.valorIssqn);
            credito.setTipoCredito(this.tipoCredito);
            credito.setSimplesNacional(this.simplesNacional);
            credito.setAliquota(this.aliquota);
            credito.setValorFaturado(this.valorFaturado);
            credito.setValorDeducao(this.valorDeducao);
            credito.setBaseCalculo(this.baseCalculo);
            return credito;
        }
    }
}