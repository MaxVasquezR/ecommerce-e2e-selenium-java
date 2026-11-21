package com.idat.qa.components;

import com.idat.qa.models.CartItem;
import com.idat.qa.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Tabla de productos que aparece tanto en el carrito (/view_cart) como en el resumen del checkout (/checkout).
 */
public class CartTableComponent {

    private static final By PRODUCT_ROWS = By.cssSelector("#cart_info tbody tr[id^='product-']");
    private static final By NAME = By.cssSelector(".cart_description h4 a");
    private static final By PRICE = By.cssSelector(".cart_price p");
    private static final By QUANTITY = By.cssSelector(".cart_quantity button");
    private static final By TOTAL = By.cssSelector(".cart_total_price");

    private final WebDriverWait wait;

    public CartTableComponent(WebDriverWait wait) {
        this.wait = wait;
    }

    public List<CartItem> items() {
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(PRODUCT_ROWS));
        return rows.stream().map(CartTableComponent::toCartItem).toList();
    }

    private static CartItem toCartItem(WebElement row) {
        return new CartItem(
                row.findElement(NAME).getText().trim(),
                Product.parsePrice(row.findElement(PRICE).getText()),
                Integer.parseInt(row.findElement(QUANTITY).getText().trim()),
                Product.parsePrice(row.findElement(TOTAL).getText()));
    }
}
