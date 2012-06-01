
/*
 * Copyright 2010 Jonella Michaylov 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package org.freespace.testingplatform.webdriver.helpers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Vector;

/**
 * Helper for testing an entire HTML table. 
 * <p>
 * Create a TableHelper by passing in a WebElement corresponding to the HTML TABLE element. 
 * For example, with the following HTML:
 * <pre>
 * &lt;table id="numbers"&gt;
 *   &lt;tr&gt;&lt;th&gt;English&lt;/th&gt;&lt;th&gt;Spanish&lt;/th&gt;&lt;/tr&gt;
 *   &lt;tr&gt;&lt;td&gt;One&lt;/td&gt;&lt;td&gt;Uno&lt;/td&gt;&lt;/tr&gt;
 *   &lt;tr&gt;&lt;td&gt;Two&lt;/td&gt;&lt;td&gt;Dos&lt;/td&gt;&lt;/tr&gt;
 * &lt;/table&gt; 
 * </pre>
 * use the following code:
 * <pre>
 *   WebElement element = _driver.findElement(By.id("numbers"));
 *   TableHelper table = new TableHelper(element);
 *   assertEquals(3, table.getRowCount())
 *   TableCellHelper[][] cells = table.getCells();
 * </pre>
 * @see TableCellHelper
 */

public class TableHelper {
  
  /**
   * Create a TableHelper from the WebElement corresponding to an HTML table.  
   * @param table A WebElement corresponding to the HTML TABLE element.
   */
  public TableHelper(WebElement table) {
    _table = table;
  }
  
  /**
   * Get the WebElement representing this table.
   * @return the underlying WebElement
   */
  public WebElement getElement() {
    return _table;
  }
  
  /**
   * Get the number of rows in the table. All rows count, regardless of 
   * how many of the cells are header calls.
   * @return The number of rows. 
   */
  public int getRowCount() {
    List<WebElement> tableRows = _table.findElements(By.tagName("tr"));
    return tableRows.size();
  }
  
  /**
   * Get all the cells in the table as an array of arrays. The individual arrays may be of 
   * different lengths because of ROWSPAN. 
   * <p>
   * Note that the grid of cells returned does not correspond to the rendered geometric grid.
   * For example, you may wish that a single TD element with a ROWSPAN of 5 would appear
   * in five rows in this grid, but it only appears once, exactly as it appears in the HTML.  
   * @return An array of arrays of helpers for the individual table cells. 
   */
  public TableCellHelper[][] getCells() {
    Vector<TableCellHelper[]> rows = new Vector<TableCellHelper[]>();
    List<WebElement> tableRows = _table.findElements(By.tagName("tr"));
    for (WebElement e : tableRows) {
      rows.add(getRowCells(e));
    }
    return rows.toArray(new TableCellHelper[0][0]);
  }
  
  protected TableCellHelper[] getRowCells(WebElement row) {
    Vector<TableCellHelper> cells = new Vector<TableCellHelper>();
    List<WebElement> rowData = row.findElements(By.xpath("td|th"));
    for (WebElement e : rowData) {
      cells.add(new TableCellHelper(e));
    }
    return cells.toArray(new TableCellHelper[0]);
  }
  
  private WebElement _table;
}
