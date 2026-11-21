package com.idat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountCreatedPage extends BasePage {

    private static final By TITLE = By.cssSelector("h2[data-qa='account-created']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountCreatedPage(WebDriver driver) {
        super(driver);
    }

    public AccountCreatedPage waitUntilLoaded() {
        waitVisible(TITLE);
        return this;
    }

    public String title() {
        return textOf(TITLE);
    }

    public HomePage continueToHome() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver).waitUntilLoaded();
    }
}
