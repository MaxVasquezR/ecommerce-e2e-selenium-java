package com.idat.qa.pages;

import com.idat.qa.models.PaymentCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private static final By PAYMENT_FORM = By.id("payment-form");
    private static final By NAME_ON_CARD = By.cssSelector("input[data-qa='name-on-card']");
    private static final By CARD_NUMBER = By.cssSelector("input[data-qa='card-number']");
    private static final By CVC = By.cssSelector("input[data-qa='cvc']");
    private static final By EXPIRY_MONTH = By.cssSelector("input[data-qa='expiry-month']");
    private static final By EXPIRY_YEAR = By.cssSelector("input[data-qa='expiry-year']");
    private static final By PAY_BUTTON = By.cssSelector("button[data-qa='pay-button']");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public PaymentPage waitUntilLoaded() {
        waitUrlContains("/payment");
        waitVisible(PAYMENT_FORM);
        return this;
    }

    public OrderPlacedPage payAndConfirmOrder(PaymentCard card) {
        type(NAME_ON_CARD, card.nameOnCard());
        type(CARD_NUMBER, card.number());
        type(CVC, card.cvc());
        type(EXPIRY_MONTH, card.expiryMonth());
        type(EXPIRY_YEAR, card.expiryYear());
        click(PAY_BUTTON);
        return new OrderPlacedPage(driver).waitUntilLoaded();
    }
}
