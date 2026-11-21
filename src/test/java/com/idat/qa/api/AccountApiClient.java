package com.idat.qa.api;

import com.idat.qa.config.ConfigReader;
import com.idat.qa.models.User;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Cliente de la API pública de automationexercise.com.
 * Se usa solo para preparar y limpiar datos de prueba (crear / eliminar cuentas) sin pasar por la UI,
 * de modo que cada prueba de UI se enfoque en la funcionalidad que valida.
 */
public final class AccountApiClient {

    private static final Pattern RESPONSE_CODE = Pattern.compile("\"responseCode\"\\s*:\\s*(\\d+)");
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    private AccountApiClient() {
    }

    public static void createAccount(User user) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("name", user.name());
        form.put("email", user.email());
        form.put("password", user.password());
        form.put("title", user.title());
        form.put("birth_date", user.birthDay());
        form.put("birth_month", user.birthMonth());
        form.put("birth_year", user.birthYear());
        form.put("firstname", user.firstName());
        form.put("lastname", user.lastName());
        form.put("company", user.company());
        form.put("address1", user.address1());
        form.put("address2", user.address2());
        form.put("country", user.country());
        form.put("zipcode", user.zipcode());
        form.put("state", user.state());
        form.put("city", user.city());
        form.put("mobile_number", user.mobileNumber());

        expectResponseCode(send("POST", "/createAccount", form), 201, "crear la cuenta " + user.email());
    }

    /** Elimina la cuenta si existe. No falla si ya fue eliminada (por ejemplo, desde la UI). */
    public static void deleteAccountIfExists(User user) {
        send("DELETE", "/deleteAccount", Map.of("email", user.email(), "password", user.password()));
    }

    private static String send(String method, String path, Map<String, String> form) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(ConfigReader.apiUrl() + path))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(method, HttpRequest.BodyPublishers.ofString(encode(form)))
                .build();
        try {
            return HTTP.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            throw new IllegalStateException("Error llamando a la API " + path, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Llamada a la API interrumpida: " + path, e);
        }
    }

    private static void expectResponseCode(String body, int expected, String action) {
        Matcher matcher = RESPONSE_CODE.matcher(body);
        if (!matcher.find() || Integer.parseInt(matcher.group(1)) != expected) {
            throw new IllegalStateException("No se pudo " + action + ". Respuesta de la API: " + body);
        }
    }

    private static String encode(Map<String, String> form) {
        return form.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
