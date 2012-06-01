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
 * Helper for testing an individual button in an HTML radio button group.
 * <p/>
 * Create an array of RadioButtonHelper from a {@link RadioGroupHelper}.
 * Frequently, this will not be
 * necessary, as the methods on RadioGroupHelper aggregate this functionality.
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
 *   RadioButtonHelper[] buttons = group.getButtons();
 *   assertFalse(buttons[0].isChecked())
 *   assertTrue(buttons[1].isChecked())
 * </pre>
 * You can also selected a specific button, which will deselect whichever is currently selected:
 * <pre>
 *   buttons[0].select()
 * </pre>
 * You can get the WebElement for an individual button:
 * <pre>
 *   WebElement firstButton = buttons[0].getElement();
 * </pre>
 *
 * @see RadioGroupHelper
 */
public class RadioButtonHelper {

    private WebElement _element;

    RadioButtonHelper(WebElement element) {
        _element = element;
    }

    /**
     * Get the WebElement representing this button.
     *
     * @return the underlying WebElement
     */
    public WebElement getElement() {
        return _element;
    }

    /**
     * Get the value of the button's 'id' attribute.
     *
     * @return Value of the button's 'id' attribute, or null.
     */
    public String getId() {
        return _element.getAttribute("id");
    }

    /**
     * Get the value of the button's 'value' attribute.
     *
     * @return Value of the button's 'value' attribute, or null.
     */
    public String getValue() {
        return _element.getAttribute("value");
    }

    /**
     * Determine whether the button is checked.
     *
     * @return True if the button is checked.
     */
    public boolean isChecked() {
        String checked = _element.getAttribute("checked");
        return checked != null && checked.equals("true");
    }

    /**
     * Check this button and uncheck all others in this group
     *
     * @return Always true.
     */
    public boolean select() {
        _element.setSelected();
        return true;
    }


}
