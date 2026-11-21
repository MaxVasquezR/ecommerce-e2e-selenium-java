package com.idat.qa.components;

import com.idat.qa.pages.AccountDeletedPage;
import com.idat.qa.pages.BasePage;
import com.idat.qa.pages.CartPage;
import com.idat.qa.pages.LoginPage;
import com.idat.qa.pages.ProductsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Barra de navegación superior, común a todas las páginas del sitio.
 */
public class HeaderComponent extends BasePage {

    private static final By SIGNUP_LOGIN_LINK = By.cssSelector("#header a[href='/login']");
    private static final By PRODUCTS_LINK = By.cssSelector("#header a[href='/products']");
    private static final By CART_LINK = By.cssSelector("#header a[href='/view_cart']");
    private static final By LOGOUT_LINK = By.cssSelector("#header a[href='/logout']");
    private static final By DELETE_ACCOUNT_LINK = By.cssSelector("#header a[href='/delete_account']");
    private static final By LOGGED_IN_AS = By.xpath("//header//a[contains(normalize-space(), 'Logged in as')]");
    private static final By LOGGED_IN_USERNAME = By.xpath("//header//a[contains(normalize-space(), 'Logged in as')]/b");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public LoginPage goToSignupLogin() {
        click(SIGNUP_LOGIN_LINK);
        return new LoginPage(driver).waitUntilLoaded();
    }

    public ProductsPage goToProducts() {
        click(PRODUCTS_LINK);
        return new ProductsPage(driver).waitUntilLoaded();
    }

    public CartPage goToCart() {
        click(CART_LINK);
        return new CartPage(driver).waitUntilLoaded();
    }

    /** Estado actual del header (sin esperar): usar después de que la página terminó de cargar. */
    public boolean isUserLoggedIn() {
        return isVisibleNow(LOGGED_IN_AS);
    }

    public String loggedInUsername() {
        return textOf(LOGGED_IN_USERNAME);
    }

    public LoginPage logout() {
        click(LOGOUT_LINK);
        return new LoginPage(driver).waitUntilLoaded();
    }

    public AccountDeletedPage deleteAccount() {
        click(DELETE_ACCOUNT_LINK);
        return new AccountDeletedPage(driver).waitUntilLoaded();
    }
}
