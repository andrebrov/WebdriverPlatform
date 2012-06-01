package org.freespace.testingplatform.webdriver.listneres;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.stereotype.Component;
import org.w3c.css.sac.ErrorHandler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
@Component
public class ListenerConfig {

    private static final Logger log = Logger.getLogger(ListenerConfig.class);

    public static void setJSErrorListenerToWebClient(WebDriver driver, JavaScriptErrorListener javaScriptErrorListener) {
        try {
            Field field = HtmlUnitDriver.class.getDeclaredField("webClient");
            field.setAccessible(true);
            WebClient webClient = (WebClient) field.get(driver);
            webClient.setJavaScriptErrorListener(javaScriptErrorListener);
            field.set(driver, webClient);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void setCssErrorHandlerToWebClient(WebDriver driver, ErrorHandler cssErrorHandler) {
        try {
            Field field = HtmlUnitDriver.class.getDeclaredField("webClient");
            field.setAccessible(true);
            WebClient webClient = (WebClient) field.get(driver);
            webClient.setCssErrorHandler(cssErrorHandler);
            field.set(driver, webClient);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }
}
