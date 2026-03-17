package com.payments.pay_microservice.application.service;

import com.payments.pay_microservice.domain.Payment;
import com.payments.pay_microservice.domain.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentDomainService {

    public void validatePayment(Payment payment) {

        if(payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        if(payment.getAccountFrom().equals(payment.getAccountTo())) {
            throw new IllegalArgumentException("Origin and destination accounts cannot be the same");
        }

    }

    public void startProcessing(Payment payment) {

        if(payment.getStatus() != PaymentStatus.CREATED) {
            throw new IllegalStateException("Payment cannot be processed from current state");
        }

        payment.markProcessing();
    }

    public void completePayment(Payment payment) {

        if(payment.getStatus() != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Payment must be processing to complete");
        }

        payment.complete();
    }
}
