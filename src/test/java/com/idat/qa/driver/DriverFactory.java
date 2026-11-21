package com.idat.qa.driver;

import com.idat.qa.config.ConfigReader;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Crea instancias de WebDriver para Chrome y Edge.
 * <p>
 * Los drivers (chromedriver / msedgedriver) los resuelve automáticamente Selenium Manager,
 * incluido en Selenium 4: detecta la versión instalada del navegador y descarga el driver compatible.
 * No es necesario descargar drivers manualmente ni usar WebDriverManager.
 */
public final class DriverFactory {

    private static final List<String> AD_URL_PATTERNS = List.of(
            "*googlesyndication.com*",
            "*doubleclick.net*",
            "*googleadservices.com*",
            "*adservice.google.*",
            "*fundingchoicesmessages.google.com*",
            "*google-analytics.com*",
            "*googletagmanager.com*");

    private DriverFactory() {
    }

    public static WebDriver create(BrowserType browser) {
        ChromiumDriver driver = switch (browser) {
            case CHROME -> new ChromeDriver(applyCommonOptions(new ChromeOptions()));
            case EDGE -> new EdgeDriver(applyCommonOptions(new EdgeOptions()));
        };

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds")));
        if (!ConfigReader.getBoolean("headless")) {
            driver.manage().window().maximize();
        }
        if (ConfigReader.getBoolean("block.ads")) {
            blockAds(driver);
        }
        return driver;
    }

    /** Chrome y Edge comparten el motor Chromium, por eso usan las mismas opciones. */
    private static <T extends ChromiumOptions<?>> T applyCommonOptions(T options) {
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments(
                "--disable-notifications",
                "--disable-search-engine-choice-screen",
                "--no-first-run",
                "--no-default-browser-check");
        // Evita los popups de "guardar contraseña" y "contraseña filtrada" que interrumpen el flujo
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    /** Bloquea la publicidad (anuncios de pantalla completa "google_vignette") mediante Chrome DevTools Protocol. */
    private static void blockAds(ChromiumDriver driver) {
        driver.executeCdpCommand("Network.enable", Map.of());
        driver.executeCdpCommand("Network.setBlockedURLs", Map.of("urls", AD_URL_PATTERNS));
    }
}
