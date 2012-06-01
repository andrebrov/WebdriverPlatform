package org.freespace.testingplatform.testng;

import javax.annotation.PreDestroy;

import org.freespace.testingplatform.config.Config;
import org.freespace.testingplatform.webdriver.listneres.ListenerConfig;
import org.freespace.testingplatform.webdriver.listneres.LoggingWebDriverEventListener;
import org.freespace.testingplatform.webdriver.listneres.StorageCssErrorHandler;
import org.freespace.testingplatform.webdriver.listneres.StorageJavaScriptErrorListener;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
@Configuration
public class WedbriverConfiguration {

    @Autowired
    private WebDriver driver;

    private static final String HTMLUNIT = "htmlunit";
    private static final String FIREFOX = "firefox";
    private static final String IEXPLORER = "iexplorer";
    public static final String MAXIMIZE_BROWSER_WINDOW = "if (window.screen) {window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};";

    public
    @Bean
    WebDriver driver() {
        WebDriver driver;
        if (HTMLUNIT.equals(Config.WEBDRIVER)) {
            driver = new HtmlUnitDriver();
            ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
            StorageJavaScriptErrorListener javaScriptErrorListener = new StorageJavaScriptErrorListener();
            StorageCssErrorHandler cssErrorHandler = new StorageCssErrorHandler();
            ListenerConfig.setJSErrorListenerToWebClient(driver, javaScriptErrorListener);
            ListenerConfig.setCssErrorHandlerToWebClient(driver, cssErrorHandler);
        } else if (FIREFOX.equals(Config.WEBDRIVER)) {
            driver = new FirefoxDriver();
            executeJavascript(driver, MAXIMIZE_BROWSER_WINDOW);
        } else if (IEXPLORER.equals(Config.WEBDRIVER)) {
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            ieCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            driver = new InternetExplorerDriver(ieCapabilities);
        } else {
            throw new RuntimeException("You must define webdriver type");
        }
        LoggingWebDriverEventListener eventListener = new LoggingWebDriverEventListener();
        return new EventFiringWebDriver(driver).register(eventListener);
    }

    private static Object executeJavascript(WebDriver driver, String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script);
    }

    @PreDestroy
    public void cleanUp() {
        try {
            driver.quit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}