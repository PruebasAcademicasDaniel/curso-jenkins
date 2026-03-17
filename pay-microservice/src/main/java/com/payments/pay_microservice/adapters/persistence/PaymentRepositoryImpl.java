package com.payments.pay_microservice.adapters.persistence;

import com.payments.pay_microservice.domain.Payment;
import com.payments.pay_microservice.ports.PaymentRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final Map<String, Payment> database = new ConcurrentHashMap<>();

    @Override
    public Mono<Payment> save(Payment payment) {

        database.put(payment.getId(), payment);

        return Mono.just(payment);
    }

    @Override
    public Mono<Payment> findById(String id) {

        Payment payment = database.get(id);

        if(payment == null){
            return Mono.empty();
        }

        return Mono.just(payment);
    }
}
