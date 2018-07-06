package sfdctest.ui.element.lightning.page;

import org.openqa.selenium.WebDriver;
import sfdctest.ui.element.Element;

public class Page extends Element {

    public Page(WebDriver webDriver) {
        super(webDriver, null);
    }

    public Page(WebDriver webDriver, Class propertyNameForClass) {
        super(webDriver, null, propertyNameForClass);
    }

    public Page(WebDriver webDriver, String filename) {
        super(webDriver, null, filename);
    }

    public static Page getInstance(WebDriver webDriver) {
        return new Page(webDriver);
    }

    public static Page getInstance(WebDriver webDriver, String filename) {
        return new Page(webDriver, filename);
    }
}
