package com.payments.pay_microservice.ports;

import com.payments.pay_microservice.domain.Payment;
import reactor.core.publisher.Mono;

public interface PaymentRepository {

    Mono<Payment> save(Payment payment);

    Mono<Payment> findById(String id);

}
