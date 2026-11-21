package com.idat.qa.tests;

import com.idat.qa.config.ConfigReader;
import com.idat.qa.driver.BrowserType;
import com.idat.qa.driver.DriverManager;
import com.idat.qa.listeners.TestListener;
import com.idat.qa.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.nio.file.Path;

/**
 * Abre un navegador nuevo antes de cada prueba y lo cierra al terminar.
 * <p>
 * Prioridad para elegir el navegador:
 * 1) parámetro "browser" del suite de TestNG (cross-browser.xml),
 * 2) propiedad del sistema -Dbrowser,
 * 3) valor de config.properties.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browser) {
        String selected = browser != null && !browser.isBlank() ? browser : ConfigReader.get("browser");
        DriverManager.start(BrowserType.from(selected));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!DriverManager.hasDriver()) {
            return;
        }
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String name = DriverManager.getBrowser() + "_" + result.getMethod().getMethodName();
                Path screenshot = ScreenshotUtil.capture(DriverManager.getDriver(), name);
                Reporter.log("Captura de la falla: " + screenshot.toAbsolutePath(), true);
            }
        } finally {
            DriverManager.quit();
        }
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    /** Registra un paso de la prueba en la consola y en el reporte de TestNG. */
    protected void step(String description) {
        Reporter.log("   -> " + description, true);
    }
}
