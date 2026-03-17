package com.payments.pay_microservice.adapters.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
        @NotBlank
        String accountFrom,

        @NotBlank
        String accountTo,

        @Positive
        double amount,

        @NotBlank
        String currency,

        @NotBlank
        String idempotencyKey
) {
}
