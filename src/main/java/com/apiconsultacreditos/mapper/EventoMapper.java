package com.apiconsultacreditos.mapper;

import com.apiconsultacreditos.event.ConsultaCreditoEvent;
import com.apiconsultacreditos.model.Credito;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    public ConsultaCreditoEvent toEvent(Credito credito) {
        return new ConsultaCreditoEvent.Builder()
            .numeroCredito(credito.getNumeroCredito())
            .numeroNfse(credito.getNumeroNfse())
            .dataConstituicao(credito.getDataConstituicao())
            .valorIssqn(credito.getValorIssqn())
            .tipoCredito(credito.getTipoCredito())
            .simplesNacional(credito.isSimplesNacional())
            .aliquota(credito.getAliquota())
            .valorFaturado(credito.getValorFaturado())
            .valorDeducao(credito.getValorDeducao())
            .baseCalculo(credito.getBaseCalculo())
            .build();
    }
}