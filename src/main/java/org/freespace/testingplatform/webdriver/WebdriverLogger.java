package org.freespace.testingplatform.webdriver;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.freespace.testingplatform.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class WebdriverLogger {
    private static final Logger log = Logger.getLogger("Action");

    public void verbose(String type, WebElement webElement) {
        verbose(type, elementToString(webElement), null);
    }

    public void verbose(String type, WebElement webElement, String text) {
        verbose(type, elementToString(webElement), text);
    }

    public void verbose(String type, By by) {
        verbose(type, by.toString(), null);
    }

    public void verbose(String type, By one, By two) {
        verbose(type, one.toString() + " - " + two.toString(), null);
    }

    public void verbose(String type, String element, String text) {
        String msg =
                "\n\ttype:    " + type +
                        "\n\telement: " + element;
        if (!StringUtils.isBlank(text)) {
            msg += "\n\ttext:    " + text;
        }
        verbose(msg);
    }

    public String elementToString(WebElement webElement) {
        return "tag='" + webElement.getTagName() + "', " +
                "id='" + webElement.getAttribute("id") + "', " +
                "name='" + webElement.getAttribute("name") + "'";
    }

    public void verbose(String msg) {
        if (Config.VERBOSE) {
            log.debug(msg);
        }
    }
}