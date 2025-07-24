// CreditoRequest.java
package com.apiconsultacreditos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreditoRequest(
    @NotBlank(message = "Número NFSe é obrigatório")
    @Size(min = 5, max = 50, message = "NFSe deve ter entre 5 e 50 caracteres")
    String numeroNfse,
    
    @NotBlank(message = "Número do crédito é obrigatório")
    @Pattern(regexp = "^CRD\\d{5,}$", message = "Formato inválido. Ex: CRD12345")
    String numeroCredito
) {}