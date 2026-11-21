package com.idat.qa.utils;

import com.idat.qa.models.PaymentCard;
import com.idat.qa.models.User;

import java.util.UUID;

/**
 * Genera datos de prueba. Cada usuario tiene un correo único para que las ejecuciones
 * (incluso en paralelo en Chrome y Edge) no choquen entre sí.
 */
public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User newUser() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new User(
                "Mr",
                "QA Tester " + id,
                "qa.tester." + id + "@idat-qa.com",
                "Idat#Qa" + id,
                "15",
                "6",
                "1998",
                "Alexander",
                "Navas",
                "IDAT QA Team",
                "Av. Javier Prado Este 123",
                "Dpto. 402",
                "United States",
                "California",
                "Los Angeles",
                "90001",
                "987654321");
    }

    public static String unregisteredEmail() {
        return "no.registrado." + UUID.randomUUID().toString().substring(0, 8) + "@idat-qa.com";
    }

    public static PaymentCard validCard(User user) {
        return new PaymentCard(user.fullName(), "4111111111111111", "123", "12", "2030");
    }
}
