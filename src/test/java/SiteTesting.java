import Enums.PresentReceiver;
import Enums.SendingMethod;
import Enums.SendingTime;
import Pages.*;
import Utils.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.FileAssert.fail;

/***
 * The testing class
 */
public class SiteTesting {
    private WebDriver driver;
    private ExtentTest extentTest;
    ExtentReports extentReports;

    private HomePage homePage;
    private IntroPage introPage;
    private String findPresentValues;

    private final String firstName = "Zion";

    /***
     * The class constructor
     */
    public SiteTesting() {
        introPage = new IntroPage();
        homePage = new HomePage();
    }

    /***
     * Takes a screenshot and save it in a file
     * @param imagePath The path of the saved image file.
     * @return Returns the image file path.
     */
    private String takeScreenshot(String imagePath){
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File screenShotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(imagePath + ".jpg");
        try{
            FileUtils.copyFile(screenShotFile,destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagePath + ".jpg";
    }

    /***
     * Logs a screenshot when NoSuchElementException occurs
     * @param e The NoSuchElementException
     */
    private void logTheScreenshot(NoSuchElementException e){
        String timeNow = String.valueOf(System.currentTimeMillis());
        extentTest.log(Status.FAIL, "Element Not Found : " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot("images/Screenshots/" + timeNow)).build());
    }

    @BeforeClass
    private void initializeSite() {
        boolean driverEstablish = false;

        extentReports = new ExtentReports();
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\extent.html");
        extentReports.attachReporter(htmlReporter);
        extentTest = extentReports.createTest("BuyMeTest","Second Project Testing");
        extentReports.setSystemInfo("Environment","Production");
        extentReports.setSystemInfo("Tester","Zion G.");
        extentTest.log(Status.INFO,"@Before Class");

        try {
            driver = DriverSingleton.getDriverInstance();
            driver.manage().window().maximize();
            driverEstablish = true;
            driver.get(FileHandler.getData("websiteURL"));
        } catch (Exception e){
            e.printStackTrace();
            fail("Can't connect webdriver");
            extentTest.log(Status.FAIL,"Driver Connection Failed! " + e.getMessage());
            driverEstablish = false;
        } finally {
            if (driverEstablish){
                extentTest.log(Status.PASS,"Driver Established Successfully");
            }
        }
    }

    @Test(priority = 1)
    public void assertLoginErrors() {
        try {
            extentTest.info("Asserting empty login errors");
            introPage.emptyLogin();
            extentTest.log(Status.PASS, "Assertion Was Successful.");
        } catch (AssertionError e) {
            extentTest.log(Status.FAIL, "Assertion Failed : " + e.getMessage());
        } catch (NoSuchElementException e) {
            logTheScreenshot(e);
        }
    }

    @Test(priority = 2)
    public void testIntroPage(){
        try {
            extentTest.info("Register / Login");
            String email = "zionTest@test.co.il";
            String password = "Password1";
            if (introPage.register(firstName, email, password)){
                extentTest.log(Status.PASS, "Inputting Credentials Was Successful.");
                extentTest.log(Status.PASS, "Register Was Successful.");
            } else {
                extentTest.log(Status.FAIL, "Register Was Unsuccessful. Trying To Login.");
                introPage.login();
                extentTest.log(Status.PASS, "Login Was Successful.");
            }
        } catch (NoSuchElementException e) {
            logTheScreenshot(e);
        }
    }

    @Test(priority = 2)
    public void testFindPresent(){
        extentTest.info("Find A Present");
        try {
            findPresentValues = homePage.findPresent(1, 2, 3);
            extentTest.log(Status.PASS, "Finding A Present Was Successful.");
        } catch (NoSuchElementException e) {
            logTheScreenshot(e);
        }
    }

    @Test(priority = 3)
    public void testPickBusiness(){
        extentTest.info("Pick A Business");
        try {
            extentTest.info("Asserting the URL");
            Assert.assertEquals(driver.getCurrentUrl(), findPresentValues);
            extentTest.log(Status.PASS, "Asserting Was Successful.");
            homePage.waitForElement(By.className("bm-product-cards"));
            homePage.selectValueFromListElement(By.className("bm-product-cards"), "li", 2);
            extentTest.log(Status.PASS, "Supplier Chosen");
            SupplierPage supplierPage = new SupplierPage();
            supplierPage.inputMoneyAmount(2);
            extentTest.log(Status.PASS, "Amount Of Money Submitted");
        } catch (AssertionError e){
            extentTest.log(Status.FAIL, "Asserting Was Unsuccessful : " + e.getMessage());
        } catch (NoSuchElementException e) {
            logTheScreenshot(e);
        }
    }

    @Test(priority = 4)
    public void payAndSendGift(){
        String recipientEmail = "test@test.il";
        extentTest.info("Pay and send gift");
        try {
            MoneyPage moneyPage = new MoneyPage();
            moneyPage.fillForm_SendToWho(PresentReceiver.OTHER,"בנצי",3,"Happy Birthday","D:\\Computers\\Qa Experts\\MyCode\\BuyMeProject\\images\\Elmo.jpg");
            extentTest.log(Status.PASS,"Money - step 1 was successful.");
            moneyPage.fillForm_HowToSend(firstName, SendingTime.NOW, SendingMethod.EMAIL, recipientEmail);
            extentTest.log(Status.PASS,"Money - step 2 was successful.");
        } catch (NoSuchElementException e){
            logTheScreenshot(e);
        } catch (AssertionError e){
            extentTest.log(Status.FAIL, "Asserting Was Unsuccessful : "+ e.getMessage());
        }
    }

    @AfterClass
    private void closeTheSite(){
        driver.quit();
        extentReports.flush();
    }
}