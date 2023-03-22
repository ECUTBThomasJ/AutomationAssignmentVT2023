package stepDefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailChimpStepdefs {
    private WebDriver driver;
    private WebDriverWait wait;
    private String site = "https://login.mailchimp.com/signup/";
    private String setBrowser;


    @Given("{string} is used to access page")
    public void isUsedToAccessPage(String browser) throws InterruptedException {
        if (browser.equalsIgnoreCase("chrome")) {
            setBrowser = browser;
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("incognito"); //, "--remote-allow-origins=*", "ignore-certificate-errors");

            driver = new ChromeDriver(chromeOptions);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");

            driver.manage().window().maximize();
            driver.get(site);
            Thread.sleep(3000);
            waitForCookies("onetrust-reject-all-handler");

        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("-private");
            setBrowser = browser;
            driver = new FirefoxDriver(firefoxOptions);
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\geckodriver.exe");

            driver.manage().window().maximize();
            driver.get(site);
            waitForCookies("onetrust-reject-all-handler");
        } else {
            System.out.println("Invalid browser chosen.");
        }

    }

    private void waitForCookies(String selector) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(selector)));
        WebElement element = driver.findElement(By.id(selector));
        element.click();
    }

    @And("a unique {string} have been entered")
    public void aValidHaveBeenEntered(String username) {

        WebElement userNameField = driver.findElement(By.id("new_username"));
        userNameField.sendKeys(username);
    }

    @And("a valid {string} have been entered")
    public void aUniqueHaveBeenEntered(String email) {

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(email);
    }

    @And("a valid {string} have been chosen")
    public void aValidHaveBeenChosen(String password) {
        WebElement pwdField = driver.findElement(By.id("new_password"));
        pwdField.sendKeys(password);
    }

    @When("the sign up button is pressed")
    public void theSignUpButtonIsPressed() throws InterruptedException {
        WebElement newsLetter = driver.findElement(By.id("marketing_newsletter"));
        //Thread.sleep(2000);
        newsLetter.click();

        WebElement signUpButton = driver.findElement(By.id("create-account-enabled"));
        //Thread.sleep(500);
        signUpButton.click();
    }

    @Then("a new account is {string}")
    public void aNewAccountIs(String expected) throws InterruptedException {
        WebElement captchaButton;
        WebElement checkEmail;
        String ceText = "error";
        String capValue = "error";
        String actual = "";

        Thread.sleep(2000);
        try {
            captchaButton = driver.findElement(By.id("recaptcha-help-button"));
            captchaButton.click();

            WebElement helpInfo = driver.findElement(By.cssSelector(".rc-challenge-help"));
            capValue = helpInfo.getText();
        }catch (Exception e){

        }
        Thread.sleep(2000);
        try{
            checkEmail= driver.findElement(By.cssSelector(".\\!margin-bottom--lv3"));
            ceText = checkEmail.getText();
        }catch (Exception e){

        }

        if((capValue.equals("Select each image that contains the object" +
                " described in the text or in the image at the top of the UI. " +
                "Then click Verify. To get a new challenge, click the reload icon. " +
                "Learn more.")) || ceText.equals("Check your email")){
            actual = "yes";
        }else{
            actual = "no";
        }


        assertEquals(expected, actual);

    }
    @After
    public void tearDown(){
        if (setBrowser.equalsIgnoreCase("chrome")) {
            driver.close();
        }
        driver.quit();
    }


}
