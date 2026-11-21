package com.idat.qa.utils;

import com.idat.qa.models.PaymentCard;
import com.idat.qa.models.User;

import java.util.UUID;

/**
 * Genera datos de prueba. Cada usuario tiene un correo único para que las ejecuciones
 * (incluso en paralelo en Chrome y Edge) no choquen entre sí.
 * <p>
 * Todos los datos son ficticios; el dominio example.com está reservado para pruebas (RFC 2606).
 */
public final class TestDataFactory {

    private static final String EMAIL_DOMAIN = "@example.com";

    private TestDataFactory() {
    }

    public static User newUser() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new User(
                "Mr",
                "QA Tester " + id,
                "qa.tester." + id + EMAIL_DOMAIN,
                "Idat#Qa" + id,
                "15",
                "6",
                "1998",
                "Usuario",
                "Prueba",
                "Empresa Demo QA",
                "Calle Ficticia 123",
                "Oficina 101",
                "United States",
                "Estado Demo",
                "Ciudad Demo",
                "00000",
                "987654321");
    }

    public static String unregisteredEmail() {
        return "no.registrado." + UUID.randomUUID().toString().substring(0, 8) + EMAIL_DOMAIN;
    }

    public static PaymentCard validCard(User user) {
        return new PaymentCard(user.fullName(), "4111111111111111", "123", "12", "2030");
    }
}
