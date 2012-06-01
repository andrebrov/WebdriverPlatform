package org.freespace.testingplatform.webdriver.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class WebdriverUtils {

    public static List<WebElement> tryGetWebElements(final WebDriver driver, final By by, Integer retries) {
        // todo: delete or generalize
        for (int retry = 0; retry < retries; ++retry) {
            List<WebElement> elements = null;
            try {
                elements = driver.findElements(by);
            } catch (Exception ignored) {
            }
            if (elements != null) {
                return elements;
            }
            delay(1000);
        }
        return null;
    }

    public static WebElement tryGetWebElement(final WebDriver driver, final By by, Integer retries) {
        return tryGetWebElement(driver, by, retries, 1000);
    }

    public static WebElement tryGetWebElement(final WebDriver driver, final By by, Integer retries, Integer delay) {
        for (int retry = 0; retry < retries; ++retry) {
            WebElement element = null;
            try {
                element = driver.findElement(by);
            } catch (Exception ignored) {
            }
            if (element != null) {
                return element;
            }
            delay(delay);
        }
        return null;
    }

    @Deprecated
    public static void delay(int millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {

        }
    }

    public static List<String> getErrors(WebElement errorList) {
        List<String> errors = new ArrayList<String>();
        if (errorList == null) {
            return errors;
        }
        List<WebElement> li = errorList.findElements(By.xpath("//ul[@class='errorList']/li"));
        for (WebElement webElement : li) {
            errors.add(webElement.getText());
        }
        return errors;
    }

    public static String textCyclicShiftLeft(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }
        String result = text.substring(1) + text.charAt(0);
        if (result.equals(text)) {
            result += text.length();
        }
        return result;
    }
}
