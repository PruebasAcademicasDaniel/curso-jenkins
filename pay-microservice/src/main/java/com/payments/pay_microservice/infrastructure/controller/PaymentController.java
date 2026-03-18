package com.payments.pay_microservice.infrastructure.controller;

import com.payments.pay_microservice.adapters.dto.PaymentRequest;
import com.payments.pay_microservice.application.usecase.ProcessPaymentUseCase;
import com.payments.pay_microservice.domain.Payment;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//Este es el controller1
//Este es el controller2
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<Payment>> createPayment(
            @Valid @RequestBody PaymentRequest request
    ){
        return processPaymentUseCase
                .processPayment(request)
                .map(ResponseEntity::ok);
    }

//    cambios para nuevo commit
}