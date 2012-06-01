
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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.By;

import java.util.Vector;
import java.util.List;

/**
 * Helper for testing an HTML select list. 
 * <p>
 * Create a SelectHelper by passing in a WebElement corresponding to the HTML SELECT element. 
 * For example, for the following HTML: 
 * <pre>
 *   &lt;select id="colors" size="3" multiple="true"&gt;
 *     &lt;optgroup label="primary"&gt;
 *       &lt;option value="r" id="red"&gt;Red&lt;/option&gt;
 *       &lt;option selected value="y" id="yellow"&gt;Yellow&lt;/option&gt;
 *       &lt;option value="b" id="blue"&gt;Blue&lt;/option&gt;
 *     &lt;/optgroup"&gt;
 *     &lt;optgroup label="secondary"&gt;
 *       &lt;option selected value="g" id="green"&gt;Green&lt;/option&gt;
 *       &lt;option value="o" id="orange"&gt;Orange&lt;/option&gt;
 *       &lt;option value="v" id="violet"&gt;Violet&lt;/option&gt;
 *     &lt;/optgroup"&gt;
 *   &lt;/select&gt;
 * </pre>
 * use the following code:
 * <pre>
 *   WebElement element = _driver.findElement(By.id("colors"));
 *   SelectHelper helper = new SelectHelper(element);
 *   assertTrue(helper.isMultiSelect());
 *   assertEquals(3, helper.getSize());
 *   assertTrue(helper.isSelected("yellow"));
 *   assertFalse(helper.isSelected("red"));
 *   assertFalse(helper.isSelected("fuchsia")); // doesn't exist
 *   assertArrayEquals(
 *       new String[] {"Yellow", "Green"},
 *       helper.getSelectedText()
 *       );
 * </pre>
 * You can also change the selections:
 * <pre>
 *   helper.selectValues(new String[] {"r", "o"} );
 * </pre>
 * If the select list is partitioned into option groups, you have three choices:
 * <ul>
 * <li>Deal with all the options together, regardless of group membership, as shown above.</li>
 * <li>Deal with the option groups individually, by calling {@link #getGroups()}.</li>
 * <li>A combination of the above</li>
 * </ul>
 * For example:
 * <pre>
 *   OptionGroupHelper primaryColors = helper.getGroup("primary");
 *   assertArrayEquals(
 *       new String[] {"Red", "Yellow", "Blue"},
 *       primaryColors.getOptionText()
 *       );
 * </pre>
 * @see FormHelper
 * @see OptionHelper
 * @see OptionGroupHelper
 *
 */
public class SelectHelper {
  
  /**
   * Create a SelectHelper from the WebElement corresponding to an HTML select list.  
   * @param select A WebElement corresponding to the HTML SELECT element.
   */
  public SelectHelper(WebElement select) {
    _select = select;
    _elements = _select.findElements(By.tagName("option"));
  }
  
  /**
   * Get the WebElement representing this select list.
   * @return the underlying WebElement
   */
  public WebElement getElement() {
    return _select;
  }
  
  private void populateOptions() {
    if (_options == null) {
      _options = new Vector<OptionHelper>();
      _groups = new Vector<OptionGroupHelper>();
      List<WebElement> groups = _select.findElements(By.tagName("optgroup"));
      if (groups.size() > 0) {
        // there are groups
        for (WebElement group : groups) {
          OptionGroupHelper helper = new OptionGroupHelper(group,isMultiSelect());
          _groups.add(helper);
          for (OptionHelper opt : helper.getOptions()) {
            _options.add(opt);
          }
        }
      } else {
        for (WebElement element : _elements) {
          OptionHelper helper = new OptionHelper(element);
          _options.add(helper);
        }
      }
    }
  }
  
  /**
   * Does this select list allow multiple selections?
   * @return True if multiple selections are allowed. 
   */
  public boolean isMultiSelect() {
    String multiple = _select.getAttribute("multiple");
    return multiple != null && multiple.equals("true"); 
  }
  
  /**
   * Is this select list disabled?
   * @return True if disabled. 
   */
  public boolean isDisabled() {
    String disabled = _select.getAttribute("disabled");
    return disabled != null && disabled.equals("true"); 
  }
  
  /**
   * Get an array of helpers for the individual options in the list. 
   * @return Helpers for the individual options in the list. 
   */
  public OptionHelper[] getOptions() {
    populateOptions();
    return _options.toArray(new OptionHelper[0]);
  }
  
  /**
   * Get an array of option groups for this select list. 
   * @return Array of option group helpers, zero length if none.
   */
  public OptionGroupHelper[] getGroups() {
    populateOptions();
    return _groups.toArray(new OptionGroupHelper[0]);
  }
  
  /**
   * Get an individual option group helper by its label.
   * @param label The desired group
   * @return The option group helper or none if it's not found.
   */
  public OptionGroupHelper getGroup(String label) {
    populateOptions();
    for (OptionGroupHelper group : _groups) {
      if (group.getLabel().equals(label))
        return group;
    }
    return null;
  }
  
  /**
   * Get an array of group labels of the option groups for this select list. 
   * @return Array of group labels, zero length if none.
   */
  public String[] getGroupLabels() {
    populateOptions();
    Vector<String> labels = new Vector<String>();
    for (OptionGroupHelper group : _groups) {
      labels.add(group.getLabel());
    }
    return labels.toArray(new String[0]);
  }
  
  /**
   * Get an array of text values for the individual options in the list. 
   * @return Text values for the individual options in the list. 
   */
  public String[] getOptionText() {
    OptionHelper[] options = getOptions();
    Vector<String> text = new Vector<String>();
    for (OptionHelper option : options) {
      text.add(option.getText());
    }
    return text.toArray(new String[0]);
  }
  
  /**
   * Get an array of text values for the SELECTED options in the list. 
   * @return Text values for the SELECTED options in the list. 
   */
  public String[] getSelectedText() {
    OptionHelper[] options = getOptions();
    Vector<String> text = new Vector<String>();
    for (OptionHelper option : options) {
      if (option.isSelected()) text.add(option.getText());
    }
    return text.toArray(new String[0]);
  }
  
  /**
   * Change the selection status of the options to the ones whose 'value' attributes are given.
   * @param selections The 'value' attributes of the options to be selected.
   * @return True if all the supplied options could be selected (all exist and the requires number 
   * of selections can be made). 
   */
  public boolean selectValues(String[] selections) {
    populateOptions();
    boolean success = true;
    for (OptionHelper option : _options) {
      // if this is an option to select, select it, otherwise deselect it
      for (String selection : selections) {
        if (option.getValue().equals(selection)) {
          success = success && option.setSelected(true);
          break;
        } else {
          // only deselect if it's mutli select; otherwise toggle doesn't work but deselecting 
          // is unnecessary: this means you can't clear a single select
          success = success && (!isMultiSelect() || option.setSelected(false));
        }
      }
    }
    return success;
  }
  
  /**
   * Change the selection status of the options to the ones whose 'id' attributes are given.
   * @param selections The 'id' attributes of the options to be selected.
   * @return True if all the supplied options could be selected (all exist and the requires number 
   * of selections can be made). 
   */
  public boolean selectIds(String[] selections) {
    populateOptions();
    boolean success = true;
    for (OptionHelper option : _options) {
      // if this is an option to select, select it, otherwise deselect it
      for (String selection : selections) {
        if (option.getId().equals(selection)) {
          success = success && option.setSelected(true);
          break;
        } else {
          // only deselect if it's mutli select; otherwise toggle doesn't work but deselecting 
          // is unnecessary: this means you can't clear a single select
          success = success && (!isMultiSelect() || option.setSelected(false));
        }
      }
    }
    return success;
  }
  
  /**
   * Is the option with the given 'id' attribute selected?
   * @param id The value of the 'id' attribute of the option. 
   * @return True if the option exists and is selected.
   */
  public boolean isSelected(String id) {
    WebElement option;
    try {
      option = _select.findElement(By.id(id));
    } catch (NoSuchElementException nsee) {
      return false;
    }
    OptionHelper helper = new OptionHelper(option);
    return helper.isSelected();
  }
  
  /**
   * Get the number of options. 
   * @return The number of options.
   */
  public int getSize() {
    String size = _select.getAttribute("size");
    if (size != null) 
      return Integer.parseInt(size);
    else
      return 0; 
  }
  
  /**
   * Get the name of the select list. 
   * @return The name of the select list.
   */
  public String getName() {
    String name = _select.getAttribute("name");
    return name;
  }
  
  private Vector<OptionHelper> _options;
  private Vector<OptionGroupHelper> _groups;
  private List<WebElement> _elements;
  private WebElement _select;
}
