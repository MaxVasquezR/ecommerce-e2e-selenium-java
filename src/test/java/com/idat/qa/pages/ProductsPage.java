package com.idat.qa.pages;

import com.idat.qa.components.AddedToCartModal;
import com.idat.qa.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Catálogo de productos (/products).
 */
public class ProductsPage extends BasePage {

    private static final By TITLE = By.xpath("//h2[contains(@class,'title') and normalize-space()='All Products']");
    private static final String PRODUCT_CARD = "//div[contains(@class,'productinfo')][a[@data-product-id='%d']]";
    private static final By CARD_NAME = By.tagName("p");
    private static final By CARD_PRICE = By.tagName("h2");
    private static final String ADD_TO_CART_BUTTON = ".productinfo a.add-to-cart[data-product-id='%d']";
    private static final String VIEW_PRODUCT_LINK = "a[href='/product_details/%d']";

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage open() {
        openPath("/products");
        return waitUntilLoaded();
    }

    public ProductsPage waitUntilLoaded() {
        waitVisible(TITLE);
        return this;
    }

    /** Nombre y precio del producto tal como aparecen en el catálogo. */
    public Product productInfo(int productId) {
        WebElement card = waitVisible(By.xpath(PRODUCT_CARD.formatted(productId)));
        return new Product(
                card.findElement(CARD_NAME).getText().trim(),
                Product.parsePrice(card.findElement(CARD_PRICE).getText()));
    }

    public AddedToCartModal addToCart(int productId) {
        click(By.cssSelector(ADD_TO_CART_BUTTON.formatted(productId)));
        return new AddedToCartModal(driver).waitUntilVisible();
    }

    public ProductDetailPage openProductDetail(int productId) {
        click(By.cssSelector(VIEW_PRODUCT_LINK.formatted(productId)));
        return new ProductDetailPage(driver).waitUntilLoaded();
    }
}
