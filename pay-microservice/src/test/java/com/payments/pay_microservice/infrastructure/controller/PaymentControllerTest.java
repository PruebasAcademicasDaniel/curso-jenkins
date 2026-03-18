package com.payments.pay_microservice.infrastructure.controller;

import com.payments.pay_microservice.adapters.dto.PaymentRequest;
import com.payments.pay_microservice.application.usecase.ProcessPaymentUseCase;
import com.payments.pay_microservice.domain.Payment;
import com.payments.pay_microservice.domain.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private ProcessPaymentUseCase processPaymentUseCase;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void createPayment_shouldReturnOk_whenPaymentIsProcessedSuccessfully() {

        // Arrange
        PaymentRequest request = new PaymentRequest(
                "ACC001",
                "ACC002",
                150.0,
                "USD",
                "idem-123"
        );
        Payment payment = new Payment(
                "pay-123",
                "ACC001",
                "ACC002",
                150.0,
                PaymentStatus.CREATED
        );

        when(processPaymentUseCase.processPayment(request))
                .thenReturn(Mono.just(payment));

        // Act
        Mono<ResponseEntity<Payment>> responseMono =
                paymentController.createPayment(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getStatusCode().is2xxSuccessful()
                                && response.getBody().equals(payment)
                )
                .verifyComplete();

        verify(processPaymentUseCase, times(1))
                .processPayment(request);
    }

    @Test
    void createPayment_shouldReturnError_whenUseCaseFails() {
        // Arrange
        PaymentRequest request = new PaymentRequest(
                "ACC001",
                "ACC002",
                150.0,
                "USD",
                "idem-123"
        );

        when(processPaymentUseCase.processPayment(request))
                .thenReturn(Mono.error(new RuntimeException("Error processing payment")));

        // Act
        Mono<ResponseEntity<Payment>> responseMono =
                paymentController.createPayment(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Error processing payment")
                )
                .verify();

        verify(processPaymentUseCase, times(1))
                .processPayment(request);
    }

}