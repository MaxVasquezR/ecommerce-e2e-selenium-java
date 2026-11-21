package com.idat.qa.pages;

import com.idat.qa.components.AddedToCartModal;
import com.idat.qa.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Detalle de un producto (/product_details/{id}): permite elegir la cantidad antes de agregar al carrito.
 */
public class ProductDetailPage extends BasePage {

    private static final By NAME = By.cssSelector(".product-information h2");
    private static final By PRICE = By.cssSelector(".product-information span > span");
    private static final By QUANTITY = By.id("quantity");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector(".product-information button.cart");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailPage waitUntilLoaded() {
        waitVisible(NAME);
        waitVisible(QUANTITY);
        return this;
    }

    public Product productInfo() {
        return new Product(textOf(NAME), Product.parsePrice(textOf(PRICE)));
    }

    public ProductDetailPage setQuantity(int quantity) {
        type(QUANTITY, String.valueOf(quantity));
        return this;
    }

    public AddedToCartModal addToCart() {
        click(ADD_TO_CART_BUTTON);
        return new AddedToCartModal(driver).waitUntilVisible();
    }
}
