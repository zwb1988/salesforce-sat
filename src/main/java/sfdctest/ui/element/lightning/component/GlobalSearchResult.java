package sfdctest.ui.element.lightning.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sfdctest.common.Constant;
import sfdctest.ui.element.Alias;
import sfdctest.ui.element.Element;

public class GlobalSearchResult extends Element {
    public GlobalSearchResult(WebDriver webDriver) {
        super(webDriver, null, GlobalSearchResult.class);
    }

    @Override
    public void initialize() {
        this.currentElement = this.getElement(Alias.RefGlobalSearchResult.CONTROL.toString());
    }

    public WebElement getHeaderElement() {
        return this.getChildElement(Alias.RefGlobalSearchResult.HEADER.toString());
    }

    public WebElement getResultTable() {
        return this.getChildElement(Alias.RefGlobalSearchResult.TABLE.toString());
    }

    public void clickOn(String anchorText) {
        Element table = new Element(this.webDriver, this.getResultTable());
        table.getChildElement(Constant.XPATH, String.format("tbody//a[text()='%s']", anchorText)).click();
    }

    public void clickOnByCSS(String anchorCSS) {
        Element table = new Element(this.webDriver, this.getResultTable());
        table.getChildElement(Constant.XPATH, String.format("tbody//a[@class='%s']", anchorCSS)).click();
    }

    public void clickOnByRecordId(String recordId) {
        Element table = new Element(this.webDriver, this.getResultTable());
        table.getChildElement(Constant.XPATH, String.format("tbody//a[@data-recordid='%s']", recordId)).click();
    }
}
