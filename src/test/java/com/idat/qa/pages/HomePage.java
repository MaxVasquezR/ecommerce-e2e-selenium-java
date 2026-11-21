package com.idat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final By SLIDER = By.id("slider");
    private static final By FEATURES_ITEMS = By.cssSelector(".features_items");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        openPath("/");
        return waitUntilLoaded();
    }

    public HomePage waitUntilLoaded() {
        waitVisible(SLIDER);
        waitVisible(FEATURES_ITEMS);
        return this;
    }
}
