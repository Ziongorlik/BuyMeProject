package Pages;

import Enums.PresentReceiver;
import Enums.SendingMethod;
import Enums.SendingTime;
import org.openqa.selenium.By;
import org.testng.Assert;

/***
 * Money page management
 */
public class MoneyPage extends BasePage{
    /***
     * The class constructor.
     */
    public MoneyPage() {

    }

    /***
     * Fills the first step of the Money page - "Send To Who" form.
     * @param receiver The receiver of the present - the user or someone else.
     * @param receiverName The name of the receiver, if it someone else.
     * @param eventType The type of the event, if the receiver is someone else.
     * @param blessing The blessing message, if the receiver is someone else.
     */
    public void fillForm_SendToWho(PresentReceiver receiver, String receiverName, int eventType, String blessing, String picturePath){
        switch (receiver){
            case ME:{
                clickElement(By.className("button-forMyself"));
                break;
            }
            case OTHER: {
                clickElement(By.className("button-forSomeone"));
                break;
            }
        }

        sendKeysToElement(By.cssSelector("input[type='text']"),receiverName);
        // TODO: check choosing event type
        selectValueFromListElement(By.cssSelector("div[class='selected-name']"),"li",eventType);
        sendKeysToElement(By.tagName("textarea"),blessing);
        sendKeysToElement(By.cssSelector("input[type='file']"),picturePath);

        Assert.assertEquals(getElementAttribute(By.cssSelector("input[type='text']"),"value"),receiverName);

        clickElement(By.cssSelector("button[gtm='המשך']"));
    }

    /***
     * Fills the second step of the Money page - "How To Send" form.
     * @param sendingTime When to send the present - Now or Later.
     * @param sendingMethod Send the present by Sms or Email.
     * @param methodText The method text: SMS - Phone number / Email = Email address.
     */
    public void fillForm_HowToSend(String senderName, SendingTime sendingTime, SendingMethod sendingMethod, String methodText){
        switch (sendingTime){
            case NOW: {
                clickElement(By.className("button-now"));
                break;
            }

            case LATER:{
                clickElement(By.className("button-later"));
                break;
            }
        }

        switch (sendingMethod){
            case SMS: {
                clickElement(By.cssSelector("svg[gtm='method-sms']"));
                sendKeysToElement(By.id("sms"),methodText);
                break;
            }

            case EMAIL:{
                clickElement(By.cssSelector("svg[gtm='method-email']"));
                sendKeysToElement(By.id("email"),methodText);
                break;
            }
        }
        Assert.assertEquals(getElementAttribute(By.cssSelector("input[type='text']"),"value"),senderName);
        clickElement(By.cssSelector("button[gtm='המשך לתשלום']"));
    }
}
