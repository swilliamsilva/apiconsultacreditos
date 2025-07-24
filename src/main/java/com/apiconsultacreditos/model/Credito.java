package com.apiconsultacreditos.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
   
 
    // Construtor privado para o builder
    // Construtor padrão (obrigatório para JPA)
    public Credito() {
    }
    
// Construtor para criação de objetos
    @JsonCreator
    public Credito(@JsonProperty("numeroCredito") String numeroCredito) {
        this.numeroCredito = numeroCredito;
    }

 // Método builder estático
    public static Builder builder() {
        return new Builder();
    }

    // Classe Builder
    public static class Builder {
        private final Credito credito = new Credito();

        public Builder numeroCredito(String numeroCredito) {
            credito.setNumeroCredito(numeroCredito);
            return this;
        }

        public Builder numeroNfse(String numeroNfse) {
            credito.setNumeroNfse(numeroNfse);
            return this;
        }

        public Builder dataConstituicao(LocalDate dataConstituicao) {
            credito.setDataConstituicao(dataConstituicao);
            return this;
        }

        public Builder valorIssqn(BigDecimal valorIssqn) {
            credito.setValorIssqn(valorIssqn);
            return this;
        }

        public Builder tipoCredito(String tipoCredito) {
            credito.setTipoCredito(tipoCredito);
            return this;
        }

        public Builder simplesNacional(boolean simplesNacional) {
            credito.setSimplesNacional(simplesNacional);
            return this;
        }

        public Builder aliquota(BigDecimal aliquota) {
            credito.setAliquota(aliquota);
            return this;
        }

        public Builder valorFaturado(BigDecimal valorFaturado) {
            credito.setValorFaturado(valorFaturado);
            return this;
        }

        public Builder valorDeducao(BigDecimal valorDeducao) {
            credito.setValorDeducao(valorDeducao);
            return this;
        }

        public Builder baseCalculo(BigDecimal baseCalculo) {
            credito.setBaseCalculo(baseCalculo);
            return this;
        }

        
       
        public Credito build() {
            return credito;
        }
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

    	
}