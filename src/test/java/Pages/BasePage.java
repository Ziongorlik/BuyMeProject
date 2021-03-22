package Pages;

import Utils.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/***
 * The class manages repeat operations on web pages.
 */
public class BasePage {
    WebDriver driver = DriverSingleton.getDriverInstance();

    public BasePage() {
    }

    /***
     * Finds an element by locator.
     * @param locator The attributes and values of the element.
     * @return Returns the wanted element.
     */
    private WebElement getWebElement(By locator) throws NoSuchElementException {
        return driver.findElement(locator);
    }

    /***
     * Click on the element.
     * @param locator The attributes and values of the wanted element.
     */
    public void clickElement(By locator){
        getWebElement(locator).click();
    }

    /***
     * Returns the text of an element.
     * @param locator The attributes and values of the element.
     * @return Returns the text of the element.
     */
    public String getElementAttribute(By locator, String attribute){
        return getWebElement(locator).getAttribute(attribute);
    }

    public boolean waitForElement(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return getWebElement(locator).isDisplayed();
    }

    /***
     * Send text to the element using a locator.
     * @param locator The attributes and values of the wanted element.
     * @param text The text to input in the element.
     */
    public void sendKeysToElement(By locator, String text){
        getWebElement(locator).clear();
        getWebElement(locator).sendKeys(text);
    }

    /***
     * Sends text to the element using the Web element.
     * @param element The web element.
     * @param text The text to input in the element.
     */
    public void sendKeysToElement(WebElement element, String text){
        element.clear();
        element.sendKeys(text);
    }

    /***
     * Selects the wanted index from a list element.
     * @param locator The attributes and values of the wanted element.
     * @param index the index of the wanted value in the list.
     */
    public void selectValueFromListElement(By locator,String tagName, int index){
        WebElement listElement = getWebElement(locator);
        listElement.click();
        listElement.findElements(By.tagName(tagName)).get(index).click();
    }

    public void selectValueFromListElement(WebElement element,String tagName, int index){
        element.click();
        element.findElements(By.tagName(tagName)).get(index).click();
    }

    /***
     * Gets the child elements from a parent element by tag name.
     * @param locator The attributes and values of the wanted element.
     * @param cssSelector the cssSelector of the child elements search for.
     * @return Returns a list of the children elements found.
     */
    public List<WebElement> getElementChildren(By locator, String cssSelector){
        WebElement listElement = getWebElement(locator);
        listElement.click();
        return listElement.findElements(By.cssSelector(cssSelector));
    }


}
