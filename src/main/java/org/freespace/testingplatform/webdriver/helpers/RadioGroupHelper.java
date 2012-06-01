
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

import java.util.Vector;

/**
 * Helper for testing an HTML radio button group. 
 * <p>
 * Create a RadioGroupHelper from a {@link FormHelper} by specifying the name of the group.
 * For example, for the following HTML: 
 * <pre>
 * &lt;form id="color_form"&gt;
 *   &lt;input id="red" type="radio" name="primary_colors" value="r" /&gt;Red&lt;br&gt;
 *   &lt;input id="yellow" type="radio" name="primary_colors" value="y" checked /&gt;Yellow&lt;br&gt;
 *   &lt;input id="blue" type="radio" name="primary_colors" value="b" /&gt;Blue&lt;br&gt;
 * &lt;/form> 
 * </pre>
 * use the following code:
 * <pre>
 *   RadioGroupHelper group = form.getRadioGroup("primary_colors");
 *   String[] groupIds = group.getIds();
 *   assertArrayEquals(
 *       new String[] { "red", "yellow", "blue" },
 *       groupIds );
 *   assertEquals("yellow", group.getCheckedId());
 *   String[] groupValues = group.getValues();
 *   assertArrayEquals(
 *       new String[] { "r", "y", "b" },
 *       groupValues );
 *   assertEquals("y", group.getCheckedValue());
 * </pre>
 * You can also change which button is selected:
 * <pre>
 *   assertTrue(group.selectValue("r"));
 * </pre>
 * @see FormHelper
 *
 */
public class RadioGroupHelper {
  
  RadioGroupHelper(Vector<WebElement> elements) {
    _elements = elements;
  }
  
  private void populateButtons() {
    if (_buttons == null) {
      _buttons = new Vector<RadioButtonHelper>();
      for (WebElement element : _elements) {
        RadioButtonHelper button = new RadioButtonHelper(element);
        _buttons.add(button);
      }
    }
  }
  
  /**
   * Get an array of helpers for the individual buttons in the group. 
   * @return Helpers for the individual buttons in the group. 
   */
  public RadioButtonHelper[] getButtons() {
    populateButtons();
    return _buttons.toArray(new RadioButtonHelper[0]);
  }
  
  /**
   * Get an array of all the 'id' attributes of the buttons in the group. 
   * @return Array of 'id' attribute values. 
   */
  public String[] getIds() {
    populateButtons();
    Vector<String> ids = new Vector<String>();
    for (RadioButtonHelper button : _buttons) {
      ids.add(button.getId());
    }
    return ids.toArray(new String[0]);
  }
  
  /**
   * Get an array of all the 'value' attributes of the buttons in the group. 
   * @return Array of 'value' attribute values. 
   */
  public String[] getValues() {
    populateButtons();
    Vector<String> ids = new Vector<String>();
    for (RadioButtonHelper button : _buttons) {
      ids.add(button.getValue());
    }
    return ids.toArray(new String[0]);
  }
  
  /**
   * Get the value of the 'id' attribute of the checked button in the group. 
   * @return Value of the 'id' attribute of the checked button, or null. 
   */
  public String getCheckedId() {
    populateButtons();
    for (RadioButtonHelper button : _buttons) {
      if (button.isChecked()) return button.getId();
    }
    return null;
  }
  
  /**
   * Select the button with the given 'id' attribute value. 
   * @param id The 'id' attribute value of the button to be selected (or 'checked')
   * @return Success (false if button is not found)
   */
  public boolean selectId(String id) {
    populateButtons();
    for (RadioButtonHelper button : _buttons) {
      if (button.getId().equals(id)) {
        return button.select();
      }
    }
    return false;
  }
  
  /**
   * Select the button with the given 'value' attribute value. 
   * @param value The 'value' attribute value of the button to be selected (or 'checked')
   * @return Success (false if button is not found)
   */
  public boolean selectValue(String value) {
    populateButtons();
    for (RadioButtonHelper button : _buttons) {
      if (button.getValue().equals(value)) {
        return button.select();
      }
    }
    return false;
  }
  
  /**
   * Get the value of the 'value' attribute of the checked button in the group. 
   * @return Value of the 'value' attribute of the checked button, or null. 
   */
  public String getCheckedValue() {
    populateButtons();
    for (RadioButtonHelper button : _buttons) {
      if (button.isChecked()) return button.getValue();
    }
    return null;
  }
  
  private Vector<RadioButtonHelper> _buttons; 
  private Vector<WebElement> _elements;
}
