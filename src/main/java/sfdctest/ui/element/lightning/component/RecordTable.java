package sfdctest.ui.element.lightning.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sfdctest.ui.element.Alias;
import sfdctest.ui.element.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A wrapper for the standard data table used on the lightning standard interface.
 */
public class RecordTable extends Element {

    /**
     * Constructor
     *
     * @param webDriver    Web Driver
     * @param tableElement WebElement of the table
     */
    public RecordTable(WebDriver webDriver, WebElement tableElement) {
        super(webDriver, tableElement, RecordTable.class);
    }

    /**
     * Gets a list of headers
     *
     * @return a list of table header elements
     */
    public List<Element> getHeaders() {
        return this.getChildElements(Alias.RefRecordTable.HEADER_ITEMS.toString())
                .stream()
                .map(item -> new Element(this.webDriver, item, RecordTable.class))
                .collect(Collectors.toList());
    }

    /**
     * Gets a list of table cell Elements
     *
     * @return A list of table cell elements
     */
    public List<Element> getDataCells() {
        return this.getChildElements(Alias.RefRecordTable.ROW_ITEMS.toString())
                .stream()
                .map(item -> new Element(this.webDriver, item, RecordTable.class))
                .collect(Collectors.toList());
    }

    /**
     * Extract the table data
     *
     * @return A list of rows of the table data
     */
    public List<List<String>> extractData() {
        List<String> allHeaders = new ArrayList<>();
        for (Element element : this.getHeaders()) {
            allHeaders.add(element.getChildElement(Alias.RefRecordTable.HEADER_ITEM_TEXT.toString()).getText());
        }
        List<String> tableHeader = allHeaders.stream().filter(header -> header != null && !header.isEmpty())
                .collect(Collectors.toList());

        // if a column doesn't contain a header, ignore
        List<Integer> effectiveIdx = IntStream.range(0, allHeaders.size())
                .filter(i -> allHeaders.get(i) != null && !allHeaders.get(i).isEmpty())
                .boxed()
                .collect(Collectors.toList());

        List<String> cells = this.getDataCells()
                .stream()
                .map(element -> element.getCurrentElement().getText())
                .collect(Collectors.toList());

        int currentCellIdx = 0;
        int noOfCellPerRow = allHeaders.size();
        List<List<String>> tableData = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();

        for (String cellText : cells) {
            int cellIdxInCurrentRow = currentCellIdx % noOfCellPerRow;
            if (effectiveIdx.contains(cellIdxInCurrentRow)) {
                currentRow.add(cellText);
            }
            if (currentCellIdx > (noOfCellPerRow - 1) && cellIdxInCurrentRow == 0) {
                tableData.add(currentRow);
                currentRow = new ArrayList<>();
            }
            currentCellIdx++;
        }
        tableData.add(0, tableHeader);
        return tableData;
    }
}
