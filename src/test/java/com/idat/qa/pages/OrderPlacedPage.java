package com.idat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderPlacedPage extends BasePage {

    private static final By TITLE = By.cssSelector("h2[data-qa='order-placed']");
    private static final By CONFIRMATION_MESSAGE = By.xpath("//h2[@data-qa='order-placed']/following-sibling::p");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public OrderPlacedPage(WebDriver driver) {
        super(driver);
    }

    public OrderPlacedPage waitUntilLoaded() {
        waitUrlContains("/payment_done");
        waitVisible(TITLE);
        return this;
    }

    public String title() {
        return textOf(TITLE);
    }

    public String confirmationMessage() {
        return textOf(CONFIRMATION_MESSAGE);
    }

    public HomePage continueToHome() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver).waitUntilLoaded();
    }
}
