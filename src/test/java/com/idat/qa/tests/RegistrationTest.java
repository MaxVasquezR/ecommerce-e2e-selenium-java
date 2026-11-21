package com.idat.qa.tests;

import com.idat.qa.api.AccountApiClient;
import com.idat.qa.models.User;
import com.idat.qa.pages.AccountCreatedPage;
import com.idat.qa.pages.HomePage;
import com.idat.qa.utils.ExpectedMessages;
import com.idat.qa.utils.TestDataFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RegistrationTest extends BaseTest {

    private User user;

    @Test(description = "Registro de un usuario nuevo con todos los datos de la cuenta")
    public void shouldRegisterNewUser() {
        user = TestDataFactory.newUser();

        step("Completar el formulario de registro para " + user.email());
        AccountCreatedPage accountCreated = new HomePage(driver()).open()
                .header().goToSignupLogin()
                .startSignup(user.name(), user.email())
                .fillAccountDetails(user)
                .createAccount();

        assertTrue(accountCreated.title().equalsIgnoreCase(ExpectedMessages.ACCOUNT_CREATED),
                "Título inesperado tras crear la cuenta: " + accountCreated.title());

        step("Verificar que el usuario quedó autenticado");
        HomePage home = accountCreated.continueToHome();
        assertEquals(home.header().loggedInUsername(), user.name());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (user != null) {
            AccountApiClient.deleteAccountIfExists(user);
        }
    }
}
