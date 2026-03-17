package com.payments.pay_microservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PaymentMetrics {

    private final Counter paymentCounter;

    public PaymentMetrics(MeterRegistry registry) {

        paymentCounter = Counter
                .builder("payments.processed")
                .description("Total processed payments")
                .register(registry);
    }

    public void increment(){
        paymentCounter.increment();
    }
}