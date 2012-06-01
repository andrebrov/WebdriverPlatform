package org.freespace.testingplatform.webdriver.helpers;

import java.util.List;
import java.util.Vector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Helper for testing an option group in an HTML select list.
 * <p/>
 * Create an OptionGroupHelper from a {@link SelectHelper} if it uses option groups and
 * you want to test in terms of those groups. (As an alternative, you can always ignore the option groups
 * and just use {@link SelectHelper} and {@link OptionHelper}.
 * As an example, for the following HTML:
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
 *   OptionGroupHelper[] groups = helper.getGroups();
 *   assertEquals(2, groups.length);
 *   assertEquals(3, groups[0].getOptions().length);
 *   assertEquals(3, groups[1].getOptions().length);
 *   assertEquals("primary", groups[0].getLabel());
 *   assertEquals("secondary", groups[1].getLabel());
 *   assertArrayEquals(
 *     new String[] {"Green", "Orange", "Violet"},
 *     groups[1].getOptionText()
 *     );
 * </pre>
 * You can also change the selections through a group helper:
 * <pre>
 *   groups[1].selectValues(new String[] {"Violet"} );
 * </pre>
 * You can get the WebElement for an individual option group:
 * <pre>
 *   WebElement firstGroup = groups[0].getElement();
 * </pre>
 *
 * @see OptionHelper
 * @see SelectHelper
 */
public class OptionGroupHelper {

    private Vector<OptionHelper> _options;
    private List<WebElement> _elements;
    private WebElement _group;
    private boolean _isMultiSelect;

    public OptionGroupHelper(WebElement group, boolean isMultiSelect) {
        _group = group;
        _isMultiSelect = isMultiSelect;
        _elements = _group.findElements(By.tagName("option"));
    }

    /**
     * Get the WebElement representing this option group.
     *
     * @return the underlying WebElement
     */
    public WebElement getElement() {
        return _group;
    }

    private void populateOptions() {
        if (_options == null) {
            _options = new Vector<OptionHelper>();
            for (WebElement element : _elements) {
                OptionHelper helper = new OptionHelper(element);
                _options.add(helper);
            }
        }
    }

    /**
     * Is this select list disabled?
     *
     * @return True if disabled.
     */
    public boolean isDisabled() {
        String disabled = _group.getAttribute("disabled");
        return disabled != null && disabled.equals("true");
    }

    /**
     * Get an array of helpers for the individual options in the list.
     *
     * @return Helpers for the individual options in the list.
     */
    public OptionHelper[] getOptions() {
        populateOptions();
        return _options.toArray(new OptionHelper[0]);
    }

    /**
     * Get an array of text values for the individual options in the list.
     *
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
     *
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
     * Only options actually part of this group can be selected here, and if the select list
     * is enabled for multi select, the other options in this group are cleared, but those
     * in other groups are not. To select values globally, use {@link SelectHelper#selectValues(String[])}.
     *
     * @param selections The 'value' attributes of the options to be selected.
     * @return True if all the supplied options could be selected (all exist and the requires number
     *         of selections can be made).
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
                    success = success && (!_isMultiSelect || option.setSelected(false));
                }
            }
        }
        return success;
    }

    /**
     * Get the name of the select list.
     *
     * @return The name of the select list.
     */
    public String getLabel() {
        String name = _group.getAttribute("label");
        return name;
    }
}
