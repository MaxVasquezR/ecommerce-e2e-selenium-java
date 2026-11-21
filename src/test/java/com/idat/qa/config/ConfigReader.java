package com.idat.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lee config.properties. Las propiedades del sistema (-Dclave=valor) tienen prioridad sobre el archivo.
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("No se encontró " + CONFIG_FILE + " en el classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + CONFIG_FILE, e);
        }
        return properties;
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta la propiedad de configuración: " + key);
        }
        return value.trim();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String apiUrl() {
        return get("api.url");
    }
}
