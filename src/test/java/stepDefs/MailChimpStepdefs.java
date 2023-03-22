package stepDefs;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailChimpStepdefs {
    private WebDriver driver;
    private WebDriverWait wait;
    private String site = "https://login.mailchimp.com/signup/";
    private String setBrowser;

    @Given("{string} is used to access page")
    public void isUsedToAccessPage(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            setBrowser = browser;
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("incognito"); //, "--remote-allow-origins=*", "ignore-certificate-errors");

            driver = new ChromeDriver(chromeOptions);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");

            driver.manage().window().maximize();
            driver.get(site);
            waitForClickableById("onetrust-reject-all-handler");

        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("-private");
            setBrowser = browser;
            driver = new FirefoxDriver(firefoxOptions);
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\geckodriver.exe");

            driver.manage().window().maximize();
            driver.get(site);
            waitForClickableById("onetrust-reject-all-handler");
        } else {
            System.out.println("Invalid browser chosen.");
        }
    }

    @And("a unique {string} have been entered")
    public void aValidHaveBeenEntered(String username) {

        WebElement userNameField = driver.findElement(By.id("new_username"));
        if(username != null){
            Date now = new Date();
            username += String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", now);
            System.out.println(username);
            userNameField.sendKeys(username);
        }

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
    public void theSignUpButtonIsPressed() {
        WebElement newsLetter = driver.findElement(By.id("marketing_newsletter"));
        waitForClickableById("marketing_newsletter");

        WebElement signUpButton = driver.findElement(By.id("create-account-enabled"));
        waitForClickableById("create-account-enabled");
    }

    @Then("a new account is {string}")
    public void aNewAccountIs(String expected) {
        WebElement captchaButton;
        WebElement checkEmail;
        String ceText = "error";
        String capValue = "error";
        String actual = "";

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(3));
            waitForClickableById("recaptcha-help-button");
            /*captchaButton = driver.findElement(By.id("recaptcha-help-button"));
            captchaButton.click();*/

            WebElement helpInfo = driver.findElement(By.cssSelector(".rc-challenge-help"));
            capValue = helpInfo.getText();
        } catch (Exception ignored) {
        }
        try {
            waitForVisibleByCSS(".\\!margin-bottom--lv3");
            checkEmail = driver.findElement(By.cssSelector(".\\!margin-bottom--lv3"));
            ceText = checkEmail.getText();
        } catch (Exception ignored) {
        }

        if ((capValue.equals("Select each image that contains the object" +
                " described in the text or in the image at the top of the UI. " +
                "Then click Verify. To get a new challenge, click the reload icon. " +
                "Learn more.")) || ceText.equals("Check your email")) {
            actual = "yes";
        } else {
            actual = "no";
        }

        assertEquals(expected, actual);
    }

    private void waitForVisibleByCSS(String selector) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
    }

    private void waitForClickableById(String id) {
        Actions action = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        WebElement element = driver.findElement(By.id(id));
        action.moveToElement(element).perform();
        element.click();
    }

    @After
    public void tearDown() {
        if (setBrowser.equalsIgnoreCase("chrome")) {
            driver.close();
        }
        driver.quit();
    }


}
