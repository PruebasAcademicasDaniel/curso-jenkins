package com.payments.pay_microservice.ports;

import reactor.core.publisher.Mono;

public interface IdempotencyRepository {

    Mono<Boolean> tryLock(String key);

    Mono<Void> savePaymentId(String key, String paymentId);

    Mono<String> getPaymentId(String key);
}