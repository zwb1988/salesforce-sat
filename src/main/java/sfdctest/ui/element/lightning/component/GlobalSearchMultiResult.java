package sfdctest.ui.element.lightning.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sfdctest.ui.element.Alias;
import sfdctest.ui.element.Element;

public class GlobalSearchMultiResult extends Element {

    public GlobalSearchMultiResult(WebDriver webDriver) {
        super(webDriver, null, GlobalSearchMultiResult.class);
    }

    @Override
    public void initialize() {
        this.currentElement = this.getElement(Alias.RefGlobalSearchMultiResult.CONTROL.toString());
    }

    public WebElement getSearchResultFor(String objectName) {
        return this.getElement(Alias.RefGlobalSearchMultiResult.RESULT_CONTAINER.toString(), objectName);
    }
}
