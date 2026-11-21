package com.idat.qa.tests.negative;

import com.idat.qa.pages.CartPage;
import com.idat.qa.pages.LoginPage;
import com.idat.qa.pages.ProductsPage;
import com.idat.qa.tests.BaseTest;
import com.idat.qa.utils.ExpectedMessages;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class NegativeCheckoutTest extends BaseTest {

    @Test(description = "Checkout bloqueado para un usuario sin sesión iniciada")
    public void shouldBlockCheckoutForGuestUser() {
        step("Agregar un producto al carrito sin iniciar sesión");
        CartPage cartPage = new ProductsPage(driver()).open().addToCart(1).viewCart();

        step("Intentar continuar al checkout");
        cartPage.attemptCheckoutAsGuest();
        assertTrue(cartPage.isCheckoutLoginModalDisplayed(), "No se mostró el aviso para iniciar sesión");

        String message = cartPage.checkoutModalMessage();
        step("Mensaje mostrado por el sistema: \"" + message + "\"");
        assertEquals(message, ExpectedMessages.CHECKOUT_REQUIRES_LOGIN);
        assertTrue(cartPage.currentUrl().contains("/view_cart"), "El invitado accedió al checkout: " + cartPage.currentUrl());

        step("El enlace del aviso debe llevar a la página de login");
        LoginPage loginPage = cartPage.goToLoginFromCheckoutModal();
        assertTrue(loginPage.isLoginFormDisplayed());
    }
}
