package sfdctest.ui.element.lightning.component;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sfdctest.ui.element.Alias;
import sfdctest.ui.element.Element;
import sfdctest.ui.helper.UICommonUtility;

public class GlobalSearch extends Element {

    public GlobalSearch(WebDriver webDriver) {
        super(webDriver, null, GlobalSearch.class);
    }

    public void initialize() {
        super.currentElement = this.getElement(Alias.RefGlobalSearch.CONTROL.toString());
    }

    public GlobalSearch changeType(String type) {
        Element typeDropDown = new Element(
                this.webDriver,
                this.getChildElement(Alias.RefGlobalSearch.DROPDOWN.toString()),
                GlobalSearch.class.getSimpleName()
        );
        typeDropDown.getCurrentElement().click();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        typeDropDown
                .getChildElement(Alias.RefGlobalSearch.DROPDOWN_ITEMS.toString(), new String[]{type})
                .click();
        return this;
    }

    public GlobalSearch search(String data) {
        return this.search(data, false);
    }

    public GlobalSearch search(String data, boolean waitForLookupPopup) {
        WebElement searchBox = this.getElement(Alias.RefGlobalSearch.INPUT.toString());
        searchBox.click();
        if (waitForLookupPopup) {
            this.getElement(Alias.RefGlobalSearch.LOOKUP_BOX_VISIBLE.toString());
        }
        searchBox.sendKeys(data);
        UICommonUtility.sendKeysWithEvent(this.webDriver, null, "", Keys.ENTER);
        return this;
    }
}
