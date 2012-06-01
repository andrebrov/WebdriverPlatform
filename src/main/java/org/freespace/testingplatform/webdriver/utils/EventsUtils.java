package org.freespace.testingplatform.webdriver.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class EventsUtils {

    private EventsUtils() {
    }

    /**
     * Fires a mouse over event on an element that matches the By
     *
     * @param by the element matcher to apply the hover to.
     */
    public static WebElement hover(By by, WebDriver driver) {
        return hover(driver.findElement(by), driver);
    }

    /**
     * Fires a mouse over event on the specified element.
     *
     * @param el The element to fire the hover event on.
     */
    public static WebElement hover(WebElement el, WebDriver driver) {
        JavaScriptUtils.dispatchMouseEvent("mouseover", el, driver);

        return el;
    }

    public static void mouseout(By by, WebDriver driver) {
        mouseout(driver.findElement(by), driver);
    }

    public static void mouseout(WebElement el, WebDriver driver) {
        JavaScriptUtils.dispatchMouseEvent("mouseout", el, driver);
    }
}
