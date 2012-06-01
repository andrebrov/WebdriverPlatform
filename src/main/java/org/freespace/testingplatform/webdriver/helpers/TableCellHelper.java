
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

import org.openqa.selenium.WebElement;

/**
 * Helper for testing individual cells of an HTML table. 
 * <p>
 * Create an array of arrays of TableCellHelper from a {@link TableHelper}.  
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
 *   TableCellHelper[][] cells = table.getCells();
 *   assertTrue(cells[0][0].isHeader());
 *   assertFalse(cells[2][0].isHeader());
 *   assertEquals("Uno", cells[1][1].getText());
 * </pre>
 * You can also get the underlying WebElement for a cell:
 * <pre>
 *   WebElement topLeft = cells[0][0].getElement();
 * </pre>
 * @see TableHelper
 */
public class TableCellHelper {
  /**
   * <b>Most users should not need this constructor as the intended way to create a 
   * TabelCellHelper is to get a grid of them from a {@link TableHelper}.</b>
   * Create a TableCellHelper from the WebElement corresponding to an HTML table cell.  
   * @param cell A WebElement corresponding to the HTML TR or TD element.
   */
  public TableCellHelper(WebElement cell) {
    _cell = cell;
    _isHeader = cell.getTagName().equalsIgnoreCase("th");
  }
  
  /**
   * Get the WebElement representing this cell.
   * @return the underlying WebElement
   */
  public WebElement getElement() {
    return _cell;
  }
  
  /**
   * Get the text (contents) of this table cell.  
   * @return Cell contents. 
   */
  public String getText() { return _cell.getText(); }
  
  /**
   * Get the value of the 'id' attribute of this table cell.  
   * @return Value of 'id' attribute. 
   */
  public String getId() { return _cell.getAttribute("id"); }
  
  /**
   * Get the value of the 'colspan' attribute of this table cell.  
   * @return Value of 'colspan' attribute. 
   */
  public int getColumnSpan() { return Integer.parseInt(_cell.getAttribute("colspan")); }
  
  /**
   * Get the value of the 'rowspan' attribute of this table cell.  
   * @return Value of 'rowspan' attribute. 
   */
  public int getRowSpan() { return Integer.parseInt(_cell.getAttribute("rowspan")); }
  
  /**
   * Determine whether this was a TH element (true) or a TR element (false). 
   * @return True if this is a header cell. 
   */
  public boolean isHeader() { return _isHeader; }
  
  private boolean _isHeader = false;
  private WebElement _cell;
}
