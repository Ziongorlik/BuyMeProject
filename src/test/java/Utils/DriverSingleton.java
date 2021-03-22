package Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/***
 * The Class manage the web driver
 */
public class DriverSingleton {
    private static WebDriver driver;

    /***
     * Creates a single instance of a web driver.
     * @return the instance of the web driver.
     */
    public static WebDriver getDriverInstance() {
        if (driver == null){
            switch (FileHandler.getData("browserType")){
                case "Chrome" : {
                    System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
                    driver = new ChromeDriver();
                    break;
                }

                case "Firefox" : {
                    System.setProperty("webdriver.firefox.driver","src/main/resources/geckodriver.exe");
                    driver = new FirefoxDriver();
                    break;
                }
            }
        }
        return driver;
    }
}
