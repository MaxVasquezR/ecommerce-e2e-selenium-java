package com.idat.qa.driver;

import org.openqa.selenium.WebDriver;

/**
 * Guarda un WebDriver por hilo, lo que permite ejecutar Chrome y Edge en paralelo sin que se mezclen sesiones.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserType> BROWSER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void start(BrowserType browser) {
        DRIVER.set(DriverFactory.create(browser));
        BROWSER.set(browser);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("El WebDriver no fue iniciado en este hilo");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static BrowserType getBrowser() {
        return BROWSER.get();
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
                BROWSER.remove();
            }
        }
    }
}
