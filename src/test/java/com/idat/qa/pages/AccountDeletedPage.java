package com.idat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDeletedPage extends BasePage {

    private static final By TITLE = By.cssSelector("h2[data-qa='account-deleted']");

    public AccountDeletedPage(WebDriver driver) {
        super(driver);
    }

    public AccountDeletedPage waitUntilLoaded() {
        waitVisible(TITLE);
        return this;
    }

    public String title() {
        return textOf(TITLE);
    }
}
