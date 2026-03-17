package com.payments.pay_microservice.application.usecase;

import com.payments.pay_microservice.adapters.dto.PaymentRequest;
import com.payments.pay_microservice.application.service.PaymentDomainService;
import com.payments.pay_microservice.domain.Payment;
import com.payments.pay_microservice.infrastructure.mapper.PaymentMapper;
import com.payments.pay_microservice.infrastructure.metrics.PaymentMetrics;
import com.payments.pay_microservice.ports.IdempotencyRepository;
import com.payments.pay_microservice.ports.PaymentEventPublisher;
import com.payments.pay_microservice.ports.PaymentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final PaymentEventPublisher eventPublisher;
    private final PaymentDomainService domainService;
    private final PaymentMetrics metrics;

    public ProcessPaymentUseCase(
            PaymentRepository paymentRepository,
            IdempotencyRepository idempotencyRepository,
            PaymentEventPublisher eventPublisher,
            PaymentDomainService domainService,
            PaymentMetrics metrics) {

        this.paymentRepository = paymentRepository;
        this.idempotencyRepository = idempotencyRepository;
        this.eventPublisher = eventPublisher;
        this.domainService = domainService;
        this.metrics = metrics;
    }

    public Mono<Payment> processPayment(PaymentRequest request){

        String key = request.idempotencyKey();

        return idempotencyRepository
                .getPaymentId(key)

                // SI YA EXISTE -> devolver el mismo payment
                .flatMap(paymentRepository::findById)

                .switchIfEmpty(

                        idempotencyRepository.tryLock(key)

                                .flatMap(acquired -> {

                                    if(!acquired){
                                        return idempotencyRepository
                                                .getPaymentId(key)
                                                .flatMap(paymentRepository::findById);
                                    }

                                    Payment payment = PaymentMapper.toDomain(request);

                                    domainService.validatePayment(payment);

                                    payment.markProcessing();

                                    return paymentRepository
                                            .save(payment)

                                            .flatMap(saved ->
                                                    idempotencyRepository
                                                            .savePaymentId(key, saved.getId())
                                                            .thenReturn(saved)
                                            );
                                })

                                .flatMap(payment -> {

                                    payment.complete();

                                    return paymentRepository
                                            .save(payment)

                                            .flatMap(saved ->
                                                    eventPublisher
                                                            .publishPaymentCompleted(saved)
                                                            .thenReturn(saved)
                                            );
                                })
                )

                .doOnSuccess(p -> metrics.increment())
                .doOnError(error ->
                        System.out.println("Payment error: " + error.getMessage())
                );
    }
}