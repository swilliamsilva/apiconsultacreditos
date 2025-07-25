package com.apiconsultacreditos.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaCreditoEvent implements Serializable {
    
    private final String numeroCredito;
    private final String numeroNfse;
    
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate dataConstituicao;
    
    private final BigDecimal valorIssqn;
    private final String tipoCredito;
    private final boolean simplesNacional;
    private final BigDecimal aliquota;
    private final BigDecimal valorFaturado;
    private final BigDecimal valorDeducao;
    private final BigDecimal baseCalculo;
    
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    @JsonCreator
    public ConsultaCreditoEvent(
            @JsonProperty("numeroCredito") String numeroCredito,
            @JsonProperty("numeroNfse") String numeroNfse,
            @JsonProperty("dataConstituicao") LocalDate dataConstituicao,
            @JsonProperty("valorIssqn") BigDecimal valorIssqn,
            @JsonProperty("tipoCredito") String tipoCredito,
            @JsonProperty("simplesNacional") boolean simplesNacional,
            @JsonProperty("aliquota") BigDecimal aliquota,
            @JsonProperty("valorFaturado") BigDecimal valorFaturado,
            @JsonProperty("valorDeducao") BigDecimal valorDeducao,
            @JsonProperty("baseCalculo") BigDecimal baseCalculo,
            @JsonProperty("timestamp") LocalDateTime timestamp) {
        
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
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    private ConsultaCreditoEvent(Builder builder) {
        this(
            builder.numeroCredito, 
            builder.numeroNfse,
            builder.dataConstituicao,
            builder.valorIssqn,
            builder.tipoCredito,
            builder.simplesNacional,
            builder.aliquota,
            builder.valorFaturado,
            builder.valorDeducao,
            builder.baseCalculo,
            null
        );
    }

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

    @Override
    public String toString() {
        return "ConsultaCreditoEvent{" +
                "numeroCredito='" + numeroCredito + '\'' +
                ", numeroNfse='" + numeroNfse + '\'' +
                ", dataConstituicao=" + dataConstituicao +
                ", valorIssqn=" + valorIssqn +
                ", tipoCredito='" + tipoCredito + '\'' +
                ", simplesNacional=" + simplesNacional +
                ", aliquota=" + aliquota +
                ", valorFaturado=" + valorFaturado +
                ", valorDeducao=" + valorDeducao +
                ", baseCalculo=" + baseCalculo +
                ", timestamp=" + timestamp +
                '}';
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

        public ConsultaCreditoEvent build() {
            return new ConsultaCreditoEvent(this);
        }
    }
}