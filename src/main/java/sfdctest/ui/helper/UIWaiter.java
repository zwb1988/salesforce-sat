package sfdctest.ui.helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sfdctest.common.Constant;

import java.util.List;

public class UIWaiter {
    public static void waitUntilElementShow(WebDriver driver, final By searchBy) {
        WebDriverWait waiter = new WebDriverWait(driver, Constant.WAIT_TIME);
        waiter.until(ExpectedConditions.presenceOfElementLocated(searchBy));
    }

    public static void waitUntilChildElemShow(WebDriver driver, final WebElement parent, final By searchBy) {
        WebDriverWait waiter = new WebDriverWait(driver, Constant.WAIT_TIME);
        waiter.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, searchBy));
    }

    public static void waitUntilChildElemsShow(WebDriver driver, final WebElement parent, final By searchBy) {
        WebDriverWait waiter = new WebDriverWait(driver, Constant.WAIT_TIME);
        waiter.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, searchBy));
    }

    public static ExpectedCondition<WebElement> elemByXpath(final String xPath) {
        return driver -> driver.findElement(By.xpath(xPath));
    }

    public static ExpectedCondition<List<WebElement>> elemsShowByXpath(final String xPath) {
        return driver -> driver.findElements(By.xpath(xPath));
    }

    public static WebElement waitUntilElemByXpath(WebDriver driver, String xPath) {
        WebDriverWait wait = new WebDriverWait(driver, Constant.WAIT_TIME);
        return wait.until(elemByXpath(xPath));
    }

    public static List<WebElement> waitUntilElemsShowByXpath(WebDriver driver, String xPath) {
        WebDriverWait wait = new WebDriverWait(driver, Constant.WAIT_TIME);
        return wait.until(elemsShowByXpath(xPath));
    }
}
