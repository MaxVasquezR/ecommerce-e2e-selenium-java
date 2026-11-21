package com.idat.qa.tests.negative;

import com.idat.qa.api.AccountApiClient;
import com.idat.qa.models.User;
import com.idat.qa.pages.LoginPage;
import com.idat.qa.tests.BaseTest;
import com.idat.qa.utils.ExpectedMessages;
import com.idat.qa.utils.TestDataFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Casos negativos de inicio de sesión: el sistema debe bloquear el acceso y mostrar el error correspondiente.
 */
public class NegativeLoginTest extends BaseTest {

    private User registeredUser;

    @BeforeClass(alwaysRun = true)
    public void createRegisteredUser() {
        registeredUser = TestDataFactory.newUser();
        AccountApiClient.createAccount(registeredUser);
    }

    @AfterClass(alwaysRun = true)
    public void deleteRegisteredUser() {
        if (registeredUser != null) {
            AccountApiClient.deleteAccountIfExists(registeredUser);
        }
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"Contraseña incorrecta", registeredUser.email(), "ClaveIncorrecta#123"},
                {"Correo no registrado", TestDataFactory.unregisteredEmail(), "Cualquier#123"},
                {"Contraseña con distinto uso de mayúsculas", registeredUser.email(), registeredUser.password().toUpperCase()},
                {"Inyección SQL en la contraseña", registeredUser.email(), "' OR '1'='1"},
        };
    }

    @Test(dataProvider = "invalidCredentials", description = "Login fallido con credenciales inválidas")
    public void shouldRejectLoginWithInvalidCredentials(String scenario, String email, String password) {
        step("Escenario: " + scenario + " (" + email + ")");
        LoginPage loginPage = new LoginPage(driver()).open().attemptLogin(email, password);

        String errorMessage = loginPage.loginErrorMessage();
        step("Mensaje mostrado por el sistema: \"" + errorMessage + "\"");

        assertEquals(errorMessage, ExpectedMessages.LOGIN_INVALID_CREDENTIALS,
                "[" + scenario + "] El mensaje de error no es el esperado");
        assertFalse(loginPage.header().isUserLoggedIn(),
                "[" + scenario + "] El sistema permitió el acceso con credenciales inválidas");
        assertTrue(loginPage.currentUrl().contains("/login"),
                "[" + scenario + "] Se abandonó la página de login: " + loginPage.currentUrl());
    }

    @DataProvider(name = "invalidFormInput")
    public Object[][] invalidFormInput() {
        return new Object[][]{
                {"Correo vacío", "", "Cualquier#123", "email"},
                {"Contraseña vacía", "qa.tester@example.com", "", "password"},
                {"Correo sin formato válido", "correo-sin-arroba", "Cualquier#123", "email"},
        };
    }

    /** El navegador bloquea el envío del formulario (validación HTML5) antes de llegar al servidor. */
    @Test(dataProvider = "invalidFormInput", description = "Login bloqueado por validación de campos")
    public void shouldBlockLoginWhenFormIsInvalid(String scenario, String email, String password, String invalidField) {
        step("Escenario: " + scenario);
        LoginPage loginPage = new LoginPage(driver()).open().attemptLogin(email, password);

        String validationMessage = "email".equals(invalidField)
                ? loginPage.loginEmailValidationMessage()
                : loginPage.loginPasswordValidationMessage();
        step("Mensaje de validación del navegador: \"" + validationMessage + "\"");

        assertFalse(validationMessage.isBlank(),
                "[" + scenario + "] El navegador no mostró validación para el campo " + invalidField);
        assertTrue(loginPage.isLoginFormDisplayed(), "[" + scenario + "] El formulario de login ya no se muestra");
        assertFalse(loginPage.header().isUserLoggedIn(), "[" + scenario + "] El sistema permitió el acceso");
    }
}
