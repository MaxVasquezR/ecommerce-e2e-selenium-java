package com.idat.qa.pages;

import com.idat.qa.models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Formulario "Enter Account Information" que aparece después de enviar el registro inicial.
 */
public class AccountInformationPage extends BasePage {

    private static final By TITLE = By.xpath("//h2/b[normalize-space()='Enter Account Information']");
    private static final By TITLE_MR = By.id("id_gender1");
    private static final By TITLE_MRS = By.id("id_gender2");
    private static final By NAME = By.cssSelector("input[data-qa='name']");
    private static final By EMAIL = By.cssSelector("input[data-qa='email']");
    private static final By PASSWORD = By.cssSelector("input[data-qa='password']");
    private static final By BIRTH_DAY = By.cssSelector("select[data-qa='days']");
    private static final By BIRTH_MONTH = By.cssSelector("select[data-qa='months']");
    private static final By BIRTH_YEAR = By.cssSelector("select[data-qa='years']");
    private static final By NEWSLETTER = By.id("newsletter");
    private static final By SPECIAL_OFFERS = By.id("optin");
    private static final By FIRST_NAME = By.cssSelector("input[data-qa='first_name']");
    private static final By LAST_NAME = By.cssSelector("input[data-qa='last_name']");
    private static final By COMPANY = By.cssSelector("input[data-qa='company']");
    private static final By ADDRESS_1 = By.cssSelector("input[data-qa='address']");
    private static final By ADDRESS_2 = By.cssSelector("input[data-qa='address2']");
    private static final By COUNTRY = By.cssSelector("select[data-qa='country']");
    private static final By STATE = By.cssSelector("input[data-qa='state']");
    private static final By CITY = By.cssSelector("input[data-qa='city']");
    private static final By ZIPCODE = By.cssSelector("input[data-qa='zipcode']");
    private static final By MOBILE_NUMBER = By.cssSelector("input[data-qa='mobile_number']");
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector("button[data-qa='create-account']");

    public AccountInformationPage(WebDriver driver) {
        super(driver);
    }

    public AccountInformationPage waitUntilLoaded() {
        waitVisible(TITLE);
        return this;
    }

    public String prefilledName() {
        return waitVisible(NAME).getDomProperty("value");
    }

    public String prefilledEmail() {
        return waitVisible(EMAIL).getDomProperty("value");
    }

    public AccountInformationPage fillAccountDetails(User user) {
        click("Mrs".equalsIgnoreCase(user.title()) ? TITLE_MRS : TITLE_MR);
        type(PASSWORD, user.password());
        selectByValue(BIRTH_DAY, user.birthDay());
        selectByValue(BIRTH_MONTH, user.birthMonth());
        selectByValue(BIRTH_YEAR, user.birthYear());
        check(NEWSLETTER);
        check(SPECIAL_OFFERS);

        type(FIRST_NAME, user.firstName());
        type(LAST_NAME, user.lastName());
        type(COMPANY, user.company());
        type(ADDRESS_1, user.address1());
        type(ADDRESS_2, user.address2());
        selectByValue(COUNTRY, user.country());
        type(STATE, user.state());
        type(CITY, user.city());
        type(ZIPCODE, user.zipcode());
        type(MOBILE_NUMBER, user.mobileNumber());
        return this;
    }

    public AccountCreatedPage createAccount() {
        click(CREATE_ACCOUNT_BUTTON);
        return new AccountCreatedPage(driver).waitUntilLoaded();
    }
}
