package org.freespace.testingplatform.testng;

import java.lang.reflect.Method;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
@Component
@ContextConfiguration(locations = {"classpath:service-context.xml"})
public abstract class AbstractTestClass extends AbstractTestNGSpringContextTests {

    protected static final Logger log = Logger.getLogger("LEM AUTOTESTS");

    @Autowired
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void printTestName(Method method) {
        log.info("---------------------------------------");
        log.info(method.getDeclaringClass() + "." + method.getName() + " started.");
        log.info("---------------------------------------");
    }

    @AfterMethod(alwaysRun = true)
    public void clearCookies(Method method) throws Exception {
        log.debug("Cookies before delete:\n" + cookiesToString());
        try {
            driver.manage().deleteAllCookies();
        } catch (Exception e) {
            log.debug("Selenium can not delete cookies");
        }
        log.debug("Cookies after delete:\n" + cookiesToString());
        log.info(method.getDeclaringClass() + "." + method.getName() + " finished.");
    }

    private String cookiesToString() {
        String result = "";
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            result += cookie + "\n";
        }
        return result;
    }

    protected WebDriver getWebDriver() {
        return driver;
    }

}
