package sfdctest.ui.element.lightning.component.tabset;

import org.openqa.selenium.WebDriver;
import sfdctest.ui.element.Alias;
import sfdctest.ui.element.Element;

public class StandardDetailTab extends Element {

    public StandardDetailTab(WebDriver webDriver) {
        super(webDriver, null, StandardDetailTab.class);
    }

    @Override
    public void initialize() {
        super.currentElement = this.getElement(Alias.RefGlobalSearch.CONTROL.toString());
    }
}
