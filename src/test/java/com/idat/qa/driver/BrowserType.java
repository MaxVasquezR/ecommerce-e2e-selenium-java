package com.idat.qa.driver;

import java.util.Arrays;
import java.util.Locale;

public enum BrowserType {
    CHROME("chrome", "googlechrome", "google-chrome"),
    EDGE("edge", "msedge", "microsoftedge", "microsoft-edge");

    private final String[] aliases;

    BrowserType(String... aliases) {
        this.aliases = aliases;
    }

    public static BrowserType from(String name) {
        String normalized = name.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        return Arrays.stream(values())
                .filter(type -> Arrays.asList(type.aliases).contains(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Navegador no soportado: '" + name + "'. Valores válidos: chrome, edge"));
    }
}
