package com.idat.qa.tests;

import com.idat.qa.api.AccountApiClient;
import com.idat.qa.models.CartItem;
import com.idat.qa.models.Product;
import com.idat.qa.models.User;
import com.idat.qa.pages.AccountCreatedPage;
import com.idat.qa.pages.AccountInformationPage;
import com.idat.qa.pages.CartPage;
import com.idat.qa.pages.CheckoutPage;
import com.idat.qa.pages.HomePage;
import com.idat.qa.pages.LoginPage;
import com.idat.qa.pages.OrderPlacedPage;
import com.idat.qa.pages.ProductsPage;
import com.idat.qa.utils.ExpectedMessages;
import com.idat.qa.utils.TestDataFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Flujo de venta completo de punta a punta:
 * Registro de usuario -> Cierre de sesión -> Inicio de sesión -> Carrito -> Checkout -> Pago -> Eliminación de la cuenta.
 */
public class PurchaseFlowE2ETest extends BaseTest {

    private static final int BLUE_TOP_ID = 1;
    private static final int MEN_TSHIRT_ID = 2;

    private User user;

    @Test(description = "Flujo E2E: registro, login, carrito, checkout y pago")
    public void shouldCompleteFullPurchaseFlow() {
        user = TestDataFactory.newUser();

        // 1. Creación de usuario
        step("Registrar el usuario " + user.email());
        LoginPage loginPage = new HomePage(driver()).open().header().goToSignupLogin();
        AccountInformationPage accountInformation = loginPage.startSignup(user.name(), user.email());
        assertEquals(accountInformation.prefilledName(), user.name(), "El nombre del registro inicial no se conservó");
        assertEquals(accountInformation.prefilledEmail(), user.email(), "El correo del registro inicial no se conservó");

        AccountCreatedPage accountCreated = accountInformation.fillAccountDetails(user).createAccount();
        assertTrue(accountCreated.title().equalsIgnoreCase(ExpectedMessages.ACCOUNT_CREATED),
                "Título inesperado tras crear la cuenta: " + accountCreated.title());
        HomePage home = accountCreated.continueToHome();
        assertEquals(home.header().loggedInUsername(), user.name(), "El usuario no quedó autenticado tras el registro");

        // 2. Cierre de sesión e inicio de sesión con la cuenta recién creada
        step("Cerrar sesión e iniciar sesión con las credenciales creadas");
        loginPage = home.header().logout();
        assertFalse(loginPage.header().isUserLoggedIn(), "La sesión sigue activa después del logout");
        home = loginPage.loginAs(user.email(), user.password());
        assertEquals(home.header().loggedInUsername(), user.name(), "El login con credenciales válidas falló");

        // 3. Agregar productos al carrito
        step("Agregar 2 productos al carrito");
        ProductsPage productsPage = home.header().goToProducts();
        Product blueTop = productsPage.productInfo(BLUE_TOP_ID);
        Product menTshirt = productsPage.productInfo(MEN_TSHIRT_ID);
        productsPage.addToCart(BLUE_TOP_ID).continueShopping();
        CartPage cartPage = productsPage.addToCart(MEN_TSHIRT_ID).viewCart();

        List<CartItem> expectedItems = List.of(
                new CartItem(blueTop.name(), blueTop.price(), 1, blueTop.price()),
                new CartItem(menTshirt.name(), menTshirt.price(), 1, menTshirt.price()));
        assertEquals(cartPage.items(), expectedItems, "El contenido del carrito no coincide con los productos agregados");

        // 4. Checkout
        step("Validar dirección y resumen del pedido en el checkout");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        String deliveryAddress = checkoutPage.deliveryAddress();
        for (String expected : List.of(user.fullName(), user.address1(), user.city(), user.country(), user.mobileNumber())) {
            assertTrue(deliveryAddress.contains(expected),
                    "La dirección de entrega no contiene '" + expected + "'. Dirección mostrada:\n" + deliveryAddress);
        }
        assertEquals(checkoutPage.items(), expectedItems, "El resumen del pedido no coincide con el carrito");
        assertEquals(checkoutPage.totalAmount(), blueTop.price() + menTshirt.price(), "El total del pedido es incorrecto");

        // 5. Pago y confirmación
        step("Realizar el pago y confirmar la orden");
        OrderPlacedPage orderPlaced = checkoutPage
                .addComment("Pedido generado por la automatización E2E - IDAT Calidad de Software")
                .placeOrder()
                .payAndConfirmOrder(TestDataFactory.validCard(user));
        assertTrue(orderPlaced.title().equalsIgnoreCase(ExpectedMessages.ORDER_PLACED),
                "Título inesperado tras el pago: " + orderPlaced.title());
        assertEquals(orderPlaced.confirmationMessage(), ExpectedMessages.ORDER_CONFIRMED);

        // 6. Limpieza desde la UI
        step("Eliminar la cuenta creada");
        String deletedTitle = orderPlaced.continueToHome().header().deleteAccount().title();
        assertTrue(deletedTitle.equalsIgnoreCase(ExpectedMessages.ACCOUNT_DELETED),
                "Título inesperado al eliminar la cuenta: " + deletedTitle);
    }

    /** Si la prueba falla a mitad del flujo, la cuenta se elimina por API para no dejar datos basura. */
    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (user != null) {
            AccountApiClient.deleteAccountIfExists(user);
        }
    }
}
