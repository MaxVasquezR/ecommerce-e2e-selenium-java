package com.idat.qa.models;

public record PaymentCard(
        String nameOnCard,
        String number,
        String cvc,
        String expiryMonth,
        String expiryYear) {
}
