
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
 * Helper for testing a single option in an HTML select list. 
 * <p>
 * Create an array of OptionHelper from a {@link SelectHelper}. 
 * Frequently, this will not be 
 * necessary, as the methods on OptionHelper aggregate this functionality.  
 * For example, for the following HTML: 
 * <pre>
 *   &lt;select id="colors" size="3" multiple="true"&gt;
 *     &lt;option value="r" id="red"&gt;Red&lt;/option&gt;
 *     &lt;option selected value="y" id="yellow"&gt;Yellow&lt;/option&gt;
 *     &lt;option value="b" id="blue"&gt;Blue&lt;/option&gt;
 *     &lt;option selected value="g" id="green"&gt;Green&lt;/option&gt;
 *     &lt;option value="o" id="orange"&gt;Orange&lt;/option&gt;
 *     &lt;option value="v" id="violet"&gt;Violet&lt;/option&gt;
 *   &lt;/select&gt;
 * </pre>
 * use the following code:
 * <pre>
 *   OptionHelper[] options = helper.getOptions();
 *   assertEquals(6, options.length);
 *   assertEquals("Green", options[3].getText());
 *   assertEquals("g", options[3].getValue());
 *   assertFalse(options[0].isSelected());
 *   assertTrue(options[3].isSelected());
 * </pre>
 * You can also change the selection status of an option:
 * <pre>
 *   options[0].setSelected(true);
 * </pre>
 * You can get the WebElement for an individual option:
 * <pre>
 *   WebElement firstOption = options[0].getElement();
 * </pre>
 * @see SelectHelper
 * @see OptionGroupHelper
 *
 */
public class OptionHelper {
  /**
   * <b>Most users should not need this constructor as the intended way to create an 
   * OptionHelper is to get an array of them from a {@link SelectHelper}.</b>
   * Create an OptionHelper from the WebElement corresponding to an HTML select option.  
   * @param option A WebElement corresponding to the HTML OPTION element.
   */
  public OptionHelper(WebElement option) {
    _option = option;
  }
  
  /**
   * Get the WebElement representing this option.
   * @return the underlying WebElement
   */
  public WebElement getElement() {
    return _option;
  }
  
  /**
   * Get the text value of the option. 
   * @return The text value. 
   */
  public String getText() { return _option.getText(); }
  
  /**
   * Is this option selected?
   * @return True if selected. 
   */
  public boolean isSelected() { 
    String selected = _option.getAttribute("selected");
    return selected != null && selected.equals("true"); 
  }
  
  /**
   * Get the 'value' attribute of this option. 
   * @return The 'value' attribute. 
   */
  public String getValue() { return _option.getAttribute("value"); }
  
  /**
   * Get the 'id' attribute of this option. 
   * @return The 'id' attribute. 
   */
  public String getId() { return _option.getAttribute("id"); }
  
  /**
   * Set the selection status of this option. If the list is single select 
   * and the given status is true, the other 
   * options in the list will be deselected automatically. 
   * @param selected The selection value to use. 
   * @return Always true. 
   */
  public boolean setSelected(boolean selected) { 
    if (selected) 
      _option.setSelected();
    else {
      _option.setSelected();
      _option.toggle();
    }
    return true;
  }
  
  private WebElement _option;
}
