package com.payments.pay_microservice.domain;

public class Payment {

    private String id;
    private String accountFrom;
    private String accountTo;
    private double amount;
    private PaymentStatus status;

    public Payment(String id, String accountFrom, String accountTo, double amount, PaymentStatus status) {
        this.id = id;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.status = status;
    }

    public void markProcessing() {
        if(status != PaymentStatus.CREATED) {
            throw new IllegalStateException("Payment cannot start processing");
        }

        status = PaymentStatus.PROCESSING;
    }

    public void complete() {
        if(status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Payment cannot complete");
        }

        status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        status = PaymentStatus.FAILED;
    }

    public String getId() {
        return id;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}