package com.idat.qa.tests;

import com.idat.qa.api.AccountApiClient;
import com.idat.qa.models.User;
import com.idat.qa.pages.HomePage;
import com.idat.qa.pages.LoginPage;
import com.idat.qa.utils.TestDataFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    private User user;

    @Test(description = "Inicio y cierre de sesión con credenciales válidas")
    public void shouldLoginAndLogoutWithValidCredentials() {
        user = TestDataFactory.newUser();
        step("Precondición: crear el usuario " + user.email() + " vía API");
        AccountApiClient.createAccount(user);

        step("Iniciar sesión");
        HomePage home = new LoginPage(driver()).open().loginAs(user.email(), user.password());
        assertEquals(home.header().loggedInUsername(), user.name(), "El header no muestra al usuario autenticado");

        step("Cerrar sesión");
        LoginPage loginPage = home.header().logout();
        assertTrue(loginPage.isLoginFormDisplayed(), "No se volvió a mostrar el formulario de login");
        assertFalse(loginPage.header().isUserLoggedIn(), "La sesión sigue activa después del logout");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (user != null) {
            AccountApiClient.deleteAccountIfExists(user);
        }
    }
}
