package com.apiconsultacreditos.mapper;

import com.apiconsultacreditos.event.ConsultaCreditoEvent;
import com.apiconsultacreditos.model.Credito;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    public ConsultaCreditoEvent toEvent(Credito credito) {
        return new ConsultaCreditoEvent(
            credito.getNumeroCredito(),
            credito.getNumeroNfse(),
            credito.getDataConstituicao(),
            credito.getValorIssqn(),
            credito.getTipoCredito(),
            credito.isSimplesNacional(),
            credito.getAliquota(),
            credito.getValorFaturado(),
            credito.getValorDeducao(),
            credito.getBaseCalculo(),
            null  // Timestamp será gerado automaticamente
        );
    }
}