package com.idat.qa.pages;

import com.idat.qa.components.CartTableComponent;
import com.idat.qa.models.CartItem;
import com.idat.qa.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Checkout (/checkout): dirección de entrega, resumen del pedido y botón "Place Order".
 */
public class CheckoutPage extends BasePage {

    private static final By DELIVERY_ADDRESS = By.id("address_delivery");
    private static final By BILLING_ADDRESS = By.id("address_invoice");
    private static final By TOTAL_AMOUNT = By.xpath("//b[normalize-space()='Total Amount']/ancestor::tr//p[@class='cart_total_price']");
    private static final By ORDER_COMMENT = By.cssSelector("textarea[name='message']");
    private static final By PLACE_ORDER_BUTTON = By.cssSelector("a[href='/payment']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage waitUntilLoaded() {
        waitUrlContains("/checkout");
        waitVisible(DELIVERY_ADDRESS);
        return this;
    }

    public String deliveryAddress() {
        return textOf(DELIVERY_ADDRESS);
    }

    public String billingAddress() {
        return textOf(BILLING_ADDRESS);
    }

    public List<CartItem> items() {
        return new CartTableComponent(wait).items();
    }

    public int totalAmount() {
        return Product.parsePrice(textOf(TOTAL_AMOUNT));
    }

    public CheckoutPage addComment(String comment) {
        type(ORDER_COMMENT, comment);
        return this;
    }

    public PaymentPage placeOrder() {
        click(PLACE_ORDER_BUTTON);
        return new PaymentPage(driver).waitUntilLoaded();
    }
}
