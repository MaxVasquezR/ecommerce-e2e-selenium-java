package com.idat.qa.components;

import com.idat.qa.pages.BasePage;
import com.idat.qa.pages.CartPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Modal "Added!" que aparece al agregar un producto al carrito (desde el catálogo o desde el detalle).
 */
public class AddedToCartModal extends BasePage {

    private static final By MODAL = By.id("cartModal");
    private static final By MESSAGE = By.cssSelector("#cartModal .modal-body p");
    private static final By VIEW_CART_LINK = By.cssSelector("#cartModal a[href='/view_cart']");
    private static final By CONTINUE_SHOPPING_BUTTON = By.cssSelector("#cartModal button.close-modal");

    public AddedToCartModal(WebDriver driver) {
        super(driver);
    }

    public AddedToCartModal waitUntilVisible() {
        waitVisible(MODAL);
        waitVisible(CONTINUE_SHOPPING_BUTTON);
        return this;
    }

    public String message() {
        return textOf(MESSAGE);
    }

    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        waitInvisible(MODAL);
    }

    public CartPage viewCart() {
        click(VIEW_CART_LINK);
        return new CartPage(driver).waitUntilLoaded();
    }
}
