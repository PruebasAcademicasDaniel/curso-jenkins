package com.payments.pay_microservice.adapters.rabbitmq;

import com.payments.pay_microservice.config.RabbitConfig;
import com.payments.pay_microservice.domain.Payment;
import com.payments.pay_microservice.ports.PaymentEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class RabbitPaymentPublisher implements PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitPaymentPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Mono<Void> publishPaymentCompleted(Payment payment) {

        return Mono.fromRunnable(() -> {
                    System.out.println(">>> INTENTANDO ENVIAR PAGO A RABBITMQ: " + payment.getId());
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EXCHANGE,
                            RabbitConfig.ROUTING_KEY,
                            payment
                    );
                })
                .subscribeOn(Schedulers.boundedElastic()).then()
                .doOnSuccess(v -> System.out.println(">>> MENSAJE ENVIADO CON ÉXITO A RABBITMQ"))
                .doOnError(e -> System.err.println(">>> ERROR AL ENVIAR A RABBITMQ: " + e.getMessage()))
                .then();
    }
}