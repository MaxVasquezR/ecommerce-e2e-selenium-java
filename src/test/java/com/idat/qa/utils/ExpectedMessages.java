package com.idat.qa.utils;

/**
 * Textos que el sitio muestra al usuario y que las pruebas validan.
 * Los títulos se comparan ignorando mayúsculas porque el sitio los transforma con CSS (text-transform).
 */
public final class ExpectedMessages {

    public static final String ACCOUNT_CREATED = "Account Created!";
    public static final String ACCOUNT_DELETED = "Account Deleted!";
    public static final String ORDER_PLACED = "Order Placed!";
    public static final String ORDER_CONFIRMED = "Congratulations! Your order has been confirmed!";
    public static final String PRODUCT_ADDED_TO_CART = "Your product has been added to cart.";

    public static final String LOGIN_INVALID_CREDENTIALS = "Your email or password is incorrect!";
    public static final String SIGNUP_EMAIL_ALREADY_EXISTS = "Email Address already exist!";
    public static final String CHECKOUT_REQUIRES_LOGIN = "Register / Login account to proceed on checkout.";

    private ExpectedMessages() {
    }
}
