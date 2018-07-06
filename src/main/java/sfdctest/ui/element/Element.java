package sfdctest.ui.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sfdctest.common.AliasInterface;
import sfdctest.common.AliasManager;
import sfdctest.common.Constant;
import sfdctest.ui.helper.UIWaiter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Properties;

public class Element {
    protected WebDriver webDriver;
    protected WebElement currentElement;
    private Properties prop;
    private String propFilePath;

    private Element(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Element(WebDriver webDriver, WebElement userDefinedElement) {
        this(webDriver);
        this.currentElement = userDefinedElement;
    }

    public Element(WebDriver webDriver, WebElement userDefinedElement, Class propertyNameForClass) {
        this(webDriver, userDefinedElement);
        this.setupProperties(propertyNameForClass);
        this.initialize();
    }


    public Element(WebDriver webDriver, WebElement userDefinedElement, String propertyName) {
        this(webDriver, userDefinedElement);
        this.setupProperties(propertyName);
        this.initialize();
    }

    protected void setupProperties(Class targetClass) {
        this.setupProperties(targetClass.getSimpleName());
    }

    protected void setupProperties(String className) {
        AliasInterface matchedAliasClass = AliasManager.getInstance().all()
                .stream()
                .filter(alias -> alias.getFileMap().get(className) != null)
                .findFirst()
                .orElse(null);
        if (matchedAliasClass == null) return;

        // try to find the resource file in the jar file
        String filepath = matchedAliasClass.getFileMap().get(className);
        URL resourceURL = getClass().getResource("/" + filepath);
        if (resourceURL != null) {
            try {
                this.loadProperties(resourceURL.openStream());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String propertyPath = System.getProperty(Constant.USER_DIR)
                + Paths.get(Constant.RESOURCE_DIR, filepath);
        this.setupProperties(Paths.get(propertyPath));
    }

    protected void setupProperties(Path filePath) {
        if (!Files.exists(filePath)) return;
        try {
            this.loadProperties(new FileInputStream(filePath.toFile()));
            this.propFilePath = filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProperties(InputStream instream) {
        this.prop = new Properties();
        try {
            this.prop.load(instream);
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Initialize method should populate the currentElement variable by locating a component on the page
     */
    protected void initialize() {

    }

    public WebElement getCurrentElement() {
        return this.currentElement;
    }

    public Properties getProp() {
        return this.prop;
    }

    public WebElement getElement(String propRef) {
        return this.getElement(propRef, new Object[]{});
    }

    public List<WebElement> getElements(String propRef) {
        return this.getElements(propRef, new Object[]{});
    }

    public List<WebElement> getElements(String propRef, Object... testValues) {
        String locatorType = this.getLocatorProperty(propRef)[0];
        String locatorValue = this.getLocatorProperty(propRef)[1];
        if (testValues != null && testValues.length > 0) {
            locatorValue = String.format(locatorValue, (Object[]) testValues);
        }
        return this.getElements(locatorType, locatorValue);
    }

    protected List<WebElement> getElements(String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        if (locator != null) {
            UIWaiter.waitUntilElementShow(this.webDriver, locator);
        }
        try {
            return this.webDriver.findElements(Element.getLocator(locatorType, locatorValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WebElement getElement(String propRef, Object... testValues) {
        String locatorType = this.getLocatorProperty(propRef)[0];
        String locatorValue = this.getLocatorProperty(propRef)[1];
        if (testValues != null && testValues.length > 0) {
            locatorValue = String.format(locatorValue, (Object[]) testValues);
        }
        return this.getElementByLocatorType(locatorType, locatorValue);
    }

    // Methods is depreciated, and it is meant for support legacy test usage.
    // Please use getElement with a list of testValues
    @Deprecated()
    public WebElement getElementWithPlaceHolder(String propRef, String placeholderValue) {
        String locatorType = this.getLocatorProperty(propRef)[0];
        String locatorValue = this.getLocatorProperty(propRef)[1];
        if (locatorValue.contains(Constant.PLACE_HOLDER)) {
            locatorValue = locatorValue.replace(Constant.PLACE_HOLDER, placeholderValue);
        }

        return this.getElementByLocatorType(locatorType, locatorValue);
    }

    protected WebElement getElementByLocatorType(String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        if (locator != null) {
            UIWaiter.waitUntilElementShow(this.webDriver, locator);
        }
        try {
            return this.webDriver.findElement(Element.getLocator(locatorType, locatorValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WebElement getChildElement(String propRef) {
        return this.getChildElement(propRef, new String[]{});
    }

    public WebElement getChildElement(String propRef, String[] testValues) {
        String locatorType = this.getLocatorProperty(propRef)[0];
        String locatorValue = this.getLocatorProperty(propRef)[1];
        if (testValues != null && testValues.length > 0) {
            locatorValue = String.format(locatorValue, (Object[]) testValues);
        }
        return this.getChildElement(locatorType, locatorValue);
    }

    public WebElement getChildElement(String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        return this.getChildElement(locator);
    }

    public WebElement getChildElement(By locateBy) {
        if (locateBy != null) {
            UIWaiter.waitUntilChildElemShow(
                    this.webDriver,
                    this.currentElement,
                    locateBy
            );
        }
        try {
            return this.currentElement.findElement(locateBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<WebElement> getChildElements(String propRef) {
        return this.getChildElements(propRef, new String[]{});
    }

    public List<WebElement> getChildElements(String propRef, String[] testValues) {
        String locatorType = this.getLocatorProperty(propRef)[0];
        String locatorValue = this.getLocatorProperty(propRef)[1];
        if (testValues != null && testValues.length > 0) {
            locatorValue = String.format(locatorValue, (Object[]) testValues);
        }
        return this.getChildElements(locatorType, locatorValue);
    }

    public List<WebElement> getChildElements(String locatorType, String locatorValue) {
        By locator = getLocator(locatorType, locatorValue);
        return this.getChildElements(locator);
    }

    public List<WebElement> getChildElements(By locateBy) {
        if (locateBy != null) {
            UIWaiter.waitUntilChildElemsShow(
                    this.webDriver,
                    this.currentElement,
                    locateBy
            );
        }
        try {
            return this.currentElement.findElements(locateBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String[] getLocatorProperty(String elementName) {
        if (this.prop == null) {
            throw new RuntimeException("No property was loaded. Please check your Alias registration and mapping");
        }
        String locator = this.prop.getProperty(elementName.toLowerCase());
        if (locator == null) {
            throw new RuntimeException(
                    String.format("Failed to load property: [%s] from [%s]", elementName, this.propFilePath)
            );
        }
        return locator.split(Constant.SEPARATOR);
    }

    public String getLocatorValue(String elementName) {
        return this.getLocatorProperty(elementName)[1];
    }

    /* Util methods */
    public WebElement getSameLevelElement(WebElement element) {
        return element.findElement(By.xpath("./following-sibling::*"));
    }

    public WebElement getParentElement(WebElement childElement) {
        return childElement.findElement(By.xpath("./parent::*"));
    }

    public WebElement getParentSameLevelElement(WebElement childElement) {
        WebElement plannedOrderParentElement = getParentElement(childElement);
        return getSameLevelElement(plannedOrderParentElement);
    }

    public WebElement getChildWebElement(WebElement parentElement) {
        return parentElement.findElement(By.xpath("./child::*"));
    }

    public List<WebElement> getChildWebElements(WebElement parentElement) {
        return parentElement.findElements(By.xpath("./child::*"));
    }

    public WebElement getChildWebElement(WebElement parentElement, String tagName) {
        return parentElement.findElement(By.xpath("./child::" + tagName));
    }

    public WebElement waitElementByPath(String element) {
        String locatorValue = this.getLocatorValue(element);
        return UIWaiter.waitUntilElemByXpath(this.webDriver, locatorValue);
    }

    public WebElement waitElementByPath(String element, String testData) {
        String locatorValue = this.getLocatorValue(element);
        if (locatorValue.contains(Constant.PLACE_HOLDER)) {
            locatorValue = locatorValue.replace(Constant.PLACE_HOLDER, testData);
        }
        return UIWaiter.waitUntilElemByXpath(webDriver, locatorValue);
    }

    public List<WebElement> waitListElementsShowByPath(String element) {
        String locatorValue = this.getLocatorValue(element);
        return UIWaiter.waitUntilElemsShowByXpath(webDriver, locatorValue);
    }

    public List<WebElement> waitListElementsShowByPath(String element, String testData) {
        String locatorValue = this.getLocatorValue(element);
        if (locatorValue.contains(Constant.PLACE_HOLDER)) {
            locatorValue = locatorValue.replace(Constant.PLACE_HOLDER, testData);
        }
        return UIWaiter.waitUntilElemsShowByXpath(webDriver, locatorValue);
    }

    public static By getLocator(String locatorType, String locatorValue) {
        // Read value using the logical name as Key
        // Split the value which contains locator type and locator value
        System.out.println("type= " + locatorType + " value= " + locatorValue);
        // Return a instance of By class based on type of locator
        switch (locatorType.toLowerCase()) {
            case Constant.ID:
                return By.id(locatorValue);
            case Constant.NAME:
                return By.name(locatorValue);
            case Constant.CLASS_NAME:
            case Constant.CLASS:
                return By.className(locatorValue);
            case Constant.TAG_NAME:
            case Constant.TAG:
                return By.tagName(locatorValue);
            case Constant.LINK_TEXT:
            case Constant.LINK:
                return By.linkText(locatorValue);
            case Constant.PARTIALL_LINK_TEXT:
                return By.partialLinkText(locatorValue);
            case Constant.CSS_SELECTOR:
            case Constant.CSS:
                return By.cssSelector(locatorValue);
            case Constant.XPATH:
                return By.xpath(locatorValue);
            default:
                throw new InvalidParameterException("Locator type '" + locatorType + "' not defined!!");
        }
    }
}
