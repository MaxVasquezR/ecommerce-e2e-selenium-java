package com.idat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Página /login: contiene el formulario de inicio de sesión y el de registro (New User Signup).
 */
public class LoginPage extends BasePage {

    private static final By LOGIN_FORM = By.cssSelector(".login-form form[action='/login']");
    private static final By LOGIN_EMAIL = By.cssSelector("input[data-qa='login-email']");
    private static final By LOGIN_PASSWORD = By.cssSelector("input[data-qa='login-password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[data-qa='login-button']");
    private static final By LOGIN_ERROR = By.cssSelector(".login-form form[action='/login'] p");

    private static final By SIGNUP_NAME = By.cssSelector("input[data-qa='signup-name']");
    private static final By SIGNUP_EMAIL = By.cssSelector("input[data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.cssSelector("button[data-qa='signup-button']");
    private static final By SIGNUP_ERROR = By.cssSelector(".signup-form form[action='/signup'] p");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        openPath("/login");
        return waitUntilLoaded();
    }

    public LoginPage waitUntilLoaded() {
        waitVisible(LOGIN_FORM);
        waitVisible(SIGNUP_NAME);
        return this;
    }

    public boolean isLoginFormDisplayed() {
        return isVisible(LOGIN_FORM);
    }

    /** Login con credenciales válidas: el sitio redirige al Home con el usuario autenticado. */
    public HomePage loginAs(String email, String password) {
        attemptLogin(email, password);
        return new HomePage(driver).waitUntilLoaded();
    }

    /** Envía el formulario sin asumir el resultado (usado en los casos negativos). */
    public LoginPage attemptLogin(String email, String password) {
        type(LOGIN_EMAIL, email);
        type(LOGIN_PASSWORD, password);
        click(LOGIN_BUTTON);
        return this;
    }

    public String loginErrorMessage() {
        return textOf(LOGIN_ERROR);
    }

    public String loginEmailValidationMessage() {
        return validationMessageOf(LOGIN_EMAIL);
    }

    public String loginPasswordValidationMessage() {
        return validationMessageOf(LOGIN_PASSWORD);
    }

    public AccountInformationPage startSignup(String name, String email) {
        attemptSignup(name, email);
        return new AccountInformationPage(driver).waitUntilLoaded();
    }

    public LoginPage attemptSignup(String name, String email) {
        type(SIGNUP_NAME, name);
        type(SIGNUP_EMAIL, email);
        click(SIGNUP_BUTTON);
        return this;
    }

    public String signupErrorMessage() {
        return textOf(SIGNUP_ERROR);
    }
}
