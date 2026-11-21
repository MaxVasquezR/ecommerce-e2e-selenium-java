package com.idat.qa.tests.negative;

import com.idat.qa.api.AccountApiClient;
import com.idat.qa.models.User;
import com.idat.qa.pages.LoginPage;
import com.idat.qa.tests.BaseTest;
import com.idat.qa.utils.ExpectedMessages;
import com.idat.qa.utils.TestDataFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class NegativeSignupTest extends BaseTest {

    private User existingUser;

    @Test(description = "Registro rechazado con un correo ya registrado")
    public void shouldRejectSignupWithExistingEmail() {
        existingUser = TestDataFactory.newUser();
        step("Precondición: crear el usuario " + existingUser.email() + " vía API");
        AccountApiClient.createAccount(existingUser);

        step("Intentar registrarse otra vez con el mismo correo");
        LoginPage loginPage = new LoginPage(driver()).open().attemptSignup("Otro Usuario", existingUser.email());

        String errorMessage = loginPage.signupErrorMessage();
        step("Mensaje mostrado por el sistema: \"" + errorMessage + "\"");
        assertEquals(errorMessage, ExpectedMessages.SIGNUP_EMAIL_ALREADY_EXISTS);
        assertTrue(loginPage.isLoginFormDisplayed(), "El sistema avanzó al formulario de cuenta con un correo duplicado");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (existingUser != null) {
            AccountApiClient.deleteAccountIfExists(existingUser);
        }
    }
}
