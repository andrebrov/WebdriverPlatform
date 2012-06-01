
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
import org.openqa.selenium.By;

import java.util.Vector;
import java.util.List;

/**
 * Helper for testing an entire HTML form. 
 * <p>
 * Create a FormHelper by passing in a WebElement corresponding to the HTML FORM element. 
 * For example, with the following HTML:
 * <pre>
 * &lt;form id="color_form"&gt;
 *   &lt;input id="red" type="radio" name="primary_colors" value="r" /&gt;Red&lt;br&gt;
 *   &lt;input id="yellow" type="radio" name="primary_colors" value="y" checked /&gt;Yellow&lt;br&gt;
 *   &lt;input id="blue" type="radio" name="primary_colors" value="b" /&gt;Blue&lt;br&gt;
 * &lt;/form> 
 * </pre>
 * use the following code:
 * <pre>
 *   WebElement element = _driver.findElement(By.id("color_form"));
 *   FormHelper form = new FormHelper(element);
 *   RadioGroupHelper group = form.getRadioGroup("primary_colors");
 *   assertNotNull(group);
 * </pre>
 * @see RadioGroupHelper
 */
public class FormHelper {
  
  /**
   * Create a FormHelper from the WebElement corresponding to an HTML form.  
   * @param element A WebElement corresponding to the HTML FORM element.
   */
  public FormHelper(WebElement element) {
    _element = element;
  }

  /**
   * Get the WebElement representing this form.
   * @return the underlying WebElement
   */
  public WebElement getElement() {
    return _element;
  }
  
  /**
   * Create a {@link RadioGroupHelper} for the radio button group with the given name.
   * @param groupName The name of the radio button group. 
   * @return A {@link RadioGroupHelper} for the radio button group with the given name.
   */
  public RadioGroupHelper getRadioGroup(String groupName) {
    List<WebElement> inputs = _element.findElements(By.tagName("input"));
    Vector<WebElement> elements = new Vector<WebElement>();
    for (WebElement input : inputs) {
      String type = input.getAttribute("type");
      String name = input.getAttribute("name");
      if (type != null && type.equals("radio") &&
          name != null && name.equals(groupName)) {
        elements.add(input);
      }
    }
    if (elements.size() == 0) 
      return null;
    else 
      return new RadioGroupHelper(elements);
  }
  
  private WebElement _element;
}
