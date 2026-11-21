package com.idat.qa.pages;

import com.idat.qa.components.HeaderComponent;
import com.idat.qa.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Clase base de todas las páginas. Centraliza las esperas dinámicas (WebDriverWait) para que
 * ninguna prueba use Thread.sleep ni esperas implícitas.
 */
public abstract class BasePage {

    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(3);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
    }

    public HeaderComponent header() {
        return new HeaderComponent(driver);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    protected void openPath(String path) {
        driver.get(ConfigReader.baseUrl() + path);
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> waitAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void waitInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // Algún elemento flotante (banner, overlay) cubre el botón: se hace click vía JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return waitVisible(locator).getText().trim();
    }

    protected void selectByValue(By locator, String value) {
        new Select(waitVisible(locator)).selectByValue(value);
    }

    protected void check(By locator) {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        if (!checkbox.isSelected()) {
            click(locator);
        }
    }

    /** Verifica si un elemento es visible sin esperar el tiempo completo (útil para validar ausencias). */
    protected boolean isVisible(By locator) {
        try {
            new WebDriverWait(driver, SHORT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Verifica la visibilidad en el estado actual de la página, sin esperar. */
    protected boolean isVisibleNow(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    /** Mensaje de validación HTML5 del navegador (por ejemplo "Completa este campo"). */
    protected String validationMessageOf(By locator) {
        WebElement element = waitVisible(locator);
        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", element);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
