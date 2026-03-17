package com.payments.pay_microservice.ports;

import com.payments.pay_microservice.domain.Payment;
import reactor.core.publisher.Mono;

public interface PaymentEventPublisher {

    Mono<Void> publishPaymentCompleted(Payment payment);
}
