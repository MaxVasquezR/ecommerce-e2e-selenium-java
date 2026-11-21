package com.idat.qa.pages;

import com.idat.qa.components.CartTableComponent;
import com.idat.qa.models.CartItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Carrito de compras (/view_cart).
 */
public class CartPage extends BasePage {

    private static final By BREADCRUMB = By.xpath("//ol[@class='breadcrumb']/li[normalize-space()='Shopping Cart']");
    private static final By PROCEED_TO_CHECKOUT = By.cssSelector("a.check_out");
    private static final By CHECKOUT_MODAL = By.cssSelector("#checkoutModal.show");
    private static final By CHECKOUT_MODAL_MESSAGE = By.cssSelector("#checkoutModal .modal-body p");
    private static final By CHECKOUT_MODAL_LOGIN_LINK = By.cssSelector("#checkoutModal a[href='/login']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage open() {
        openPath("/view_cart");
        return waitUntilLoaded();
    }

    public CartPage waitUntilLoaded() {
        waitUrlContains("/view_cart");
        waitVisible(BREADCRUMB);
        return this;
    }

    public List<CartItem> items() {
        return new CartTableComponent(wait).items();
    }

    /** Checkout con un usuario autenticado. */
    public CheckoutPage proceedToCheckout() {
        click(PROCEED_TO_CHECKOUT);
        return new CheckoutPage(driver).waitUntilLoaded();
    }

    /** Checkout sin sesión: el sitio debe bloquearlo mostrando un modal que pide registrarse o iniciar sesión. */
    public CartPage attemptCheckoutAsGuest() {
        click(PROCEED_TO_CHECKOUT);
        return this;
    }

    public boolean isCheckoutLoginModalDisplayed() {
        return isVisible(CHECKOUT_MODAL);
    }

    public String checkoutModalMessage() {
        return textOf(CHECKOUT_MODAL_MESSAGE);
    }

    public LoginPage goToLoginFromCheckoutModal() {
        click(CHECKOUT_MODAL_LOGIN_LINK);
        return new LoginPage(driver).waitUntilLoaded();
    }
}
