package com.payments.pay_microservice.infrastructure.mapper;

import com.payments.pay_microservice.adapters.dto.PaymentRequest;
import com.payments.pay_microservice.domain.Payment;
import com.payments.pay_microservice.domain.PaymentStatus;

import java.util.UUID;

public class PaymentMapper {

    public static Payment toDomain(PaymentRequest request){

        return new Payment(
                UUID.randomUUID().toString(),
                request.accountFrom(),
                request.accountTo(),
                request.amount(),
                PaymentStatus.CREATED
        );

    }

}
