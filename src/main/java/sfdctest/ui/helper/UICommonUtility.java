package sfdctest.ui.helper;


import java.lang.String;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class UICommonUtility {
    public static void sendKeysWithEvent(WebDriver driver, WebElement element, String text, Keys event) {
//	    element.sendKeys(text);
        if (event.equals(Keys.UP)) {
            new Actions(driver).keyDown(element, Keys.CONTROL).keyUp(element, Keys.CONTROL).perform();
        } else if (event.equals("onblurJS")) {
            try {
                doJavascriptOnElement(driver, element, element.getAttribute("onblur"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.equals("onfocus")) {
            element.click();
        } else if (event.equals("keyupJS")) {
            try {
                doJavascriptOnElement(driver, element, element.getAttribute("onkeyup"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.equals("keyupTAB")) {
            element.sendKeys(Keys.TAB);
        } else if (event.equals(Keys.ENTER)) {
            new Actions(driver).sendKeys(Keys.ENTER).perform();
        }
    }

    public static void doJavascriptOnElement(WebDriver driver, WebElement element, String javascript) throws Exception {
        ((JavascriptExecutor) driver).executeScript(javascript, element);
    }

    public static void doJavascriptOnElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void scrollScreen(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + element.getLocation().y + ")");
    }

    public static void scrollScreen(WebDriver driver, int offset) {
        try {
            Thread.sleep(2000);
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + offset + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void doubleClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
    }
}
