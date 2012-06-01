package org.freespace.testingplatform.webdriver.utils;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JsUtils {

    private JavascriptExecutor js;
    private static final Logger log = Logger.getLogger(JsUtils.class);

    public JsUtils(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    public void jsClickById(String id) {
        jsEvent("#" + id, "click");
    }

    public void jsEvent(String selector, String eventName) {
        String script = "$('" + selector + "')." + eventName + "()";
        exec(script);
    }

    private void exec(String script) {
        log.info(script);
        js.executeScript(script);
    }

    public boolean isAjaxComplete() {
        final Boolean result = (Boolean) js.executeScript("return $.active == 0");
        return result;
    }

    public static String innerHtml(WebElement element, WebDriver driver) {
        return execute("return arguments[0].innerHTML", driver, element);
    }

    /**
     * Dispatches a javascript mouse event in the browser on a specified element
     *
     * @param event  The name of the event to dispatch. eg. load.
     * @param el     The element to fire the event on.
     * @param driver the webdriver instance that executes the javascript event.
     */
    public static void dispatchMouseEvent(String event, WebElement el, WebDriver driver) {

        // TODO: move this to a real js file and import the file if needed.
        String js = "if ( document.createEvent ) {"
                + "var eventObj = document.createEvent('MouseEvents');"
                + "eventObj.initEvent('" + event + "', false, true);"
                + "arguments[0].dispatchEvent(eventObj)"
                + "} else if ( document.createEventObject ) {"
                + "arguments[0].fireEvent('on" + event + "');"
                + "}";

        execute(js, driver, el);
    }

    public static void loadScript(String jsScriptName, WebDriver driver) {
        try {
            String lineSep = System.getProperty("line.separator");
            BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(jsScriptName)));
            String nextLine = "";
            StringBuffer sb = new StringBuffer();
            while ((nextLine = br.readLine()) != null) {
                sb.append(nextLine);
                //
                // note:
                //   BufferedReader strips the EOL character
                //   so we add a new one!
                //
                sb.append(lineSep);
            }
            String jsSource = sb.toString();

            JsUtils.execute(jsSource, driver);

        } catch (IOException e) {
            throw new RuntimeException("Unable to load the javascript file: " + jsScriptName, e);
        }

    }

    public static <T> T execute(String js, WebDriver driver) {
        return (T) execute(js, driver, new Object[0]);
    }

    public static <T> T execute(String js, WebDriver driver, Object... arguments) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (T) jsExecutor.executeScript(js, arguments);
    }

}