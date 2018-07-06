package sfdctest.ui.element.lightning.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sfdctest.ui.element.Alias;
import sfdctest.ui.element.Element;
import sfdctest.ui.element.lightning.component.tabset.TabSetControl;

import java.util.Optional;
import java.util.stream.Stream;

public class RecordPage extends Page {
    private static final String TITLE_FILTER = "@title='%s'";

    public RecordPage(WebDriver webDriver) {
        super(webDriver, RecordPage.class);
    }

    @Override
    public void initialize() {
        this.currentElement = this.getElement(Alias.RefRecordPage.CONTROL.toString());
    }

    public static RecordPage goToRecordPage(WebDriver webDriver, String url) {
        webDriver.navigate().to(url);
        return new RecordPage(webDriver);
    }

    public WebElement getCurrentRecord() {
        return this.getChildElement(Alias.RefRecordPage.CURRENT_RECORD.toString());
    }

    public TabSetControl getTabSet(String... tabNames) {
        Optional<Element> tabSet = this.getChildElements(Alias.RefRecordPage.TABSET.toString())
                .stream()
                .map(webElement -> new Element(this.webDriver, webElement, RecordPage.class))
                .filter(element -> {
                    try {
                        return Stream.of(tabNames).allMatch(tabName ->
                                element.getChildElement(Alias.RefRecordPage.TABSET_FILTER.toString(),
                                        new String[]{
                                                String.format(TITLE_FILTER, tabName)
                                        }) != null
                        );
                    } catch (Exception ex) {
                        return false;
                    }
                }).findFirst();

        return tabSet.map(element -> new TabSetControl(this.webDriver, element.getCurrentElement())).orElse(null);
    }
}
