package org.freespace.testingplatform.webdriver.pages;

import static org.apache.commons.lang.StringUtils.isBlank;

import org.freespace.testingplatform.webdriver.WebdriverLogger;
import org.freespace.testingplatform.webdriver.utils.JsUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class TestingPlatformWait {

    private final WebdriverLogger log = new WebdriverLogger();
    private WebDriverWait wait;
    private WebDriver driver;

    public TestingPlatformWait(WebDriver driver, int defaultTimeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, defaultTimeout);
    }

    public WebElement waitUntilFound(final By by) {
        log.verbose("waiting until found", by);
        try {
            return wait.until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(by);
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public WebElement waitUntilFoundIgnoringStale(final By by) {
        log.verbose("waiting until found", by);
        try {
            WebDriverWait wait1 = (WebDriverWait) new WebDriverWait(driver, 60).ignoring(StaleElementReferenceException.class);
            return wait1.until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(by);
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public void waitUntilExist(final By by) {
        log.verbose("waiting for exist", by);
        wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });
    }

    public void waitUntilExist(final By by, int seconds) {
        log.verbose("waiting for exist", by);
        WebDriverWait waitWithTimeout = new WebDriverWait(driver, seconds);
        waitWithTimeout.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });
    }

    public void waitUntilExistOneOf(final By one, final By two) {
        log.verbose("waiting for displayed one of: ", one, two);
        wait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement element = driver.findElement(one);
                if (element == null) {
                    element = driver.findElement(two);
                }
                return element;
            }
        });
    }

    public void waitUntilDisplayed(final By by) {
        log.verbose("waiting for displayed", by);
        waitUntilExist(by);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(by).isDisplayed();
            }
        });
    }

    public void waitUntilDisplayedOneOf(final By one, final By two) {
        log.verbose("waiting for displayed one of: ", one, two);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                boolean firstDisplayed = false;
                try {
                    firstDisplayed = driver.findElement(one).isDisplayed();
                } catch (Exception e) {

                }
                boolean secondDisplayed = false;
                try {
                    secondDisplayed = driver.findElement(two).isDisplayed();
                } catch (Exception e) {

                }
                return firstDisplayed || secondDisplayed;
            }
        });
    }

    public void waitUntilHidden(final By by) {
        log.verbose("waiting for hidden", by);
        waitUntilExist(by);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return !driver.findElement(by).isDisplayed();
            }
        });

    }

    public void selectByText(WebElement
                                     webElement, String text) {
        log.verbose("select by text", webElement, text);
        if (isBlank(text)) {
            return;
        }
        new Select(webElement).selectByVisibleText(text);
    }

    public void waitForSelectFill(WebDriver driver, WebElement
            selectElement) {
        final Select select = new Select(selectElement);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return select.getOptions().size() > 0;
            }
        });
    }

    public void waitForAjaxComplete() {
        log.verbose("waiting for ajax completion");
        final JsUtils js = new JsUtils(driver);
        wait.until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver) {
                return js.isAjaxComplete();
            }
        });
        log.verbose("All ajax calls are complete");
    }

}
