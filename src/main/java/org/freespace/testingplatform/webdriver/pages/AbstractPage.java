package org.freespace.testingplatform.webdriver.pages;

import static org.apache.commons.lang.StringUtils.isBlank;

import org.freespace.testingplatform.config.Config;
import org.freespace.testingplatform.webdriver.WebdriverLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;


public abstract class AbstractPage extends LoadableComponent<AbstractPage> {

    private static final int DEFAULT_TIMEOUT = 60;
    protected final WebDriver driver;
    protected TestingPlatformWait wait;
    private final WebdriverLogger log = new WebdriverLogger();

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new TestingPlatformWait(driver, DEFAULT_TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {
        driver.get(Config.BASE_URL);
    }

    protected abstract By getPageLoadedCheckElementLocator();

    // Primitive actions
    protected void clickOn(WebElement webElement) {
        log.verbose("click", webElement);
        new Actions(driver).moveToElement(webElement).click(webElement).perform();
    }

    protected void type(WebElement webElement, String text) {
        log.verbose("type", webElement, text);
        if (isBlank(text)) {
            return;
        }
        webElement.sendKeys(text);
    }

    protected void clear(WebElement webElement) {
        log.verbose("clear", webElement);
        webElement.clear();
    }

    protected void clearAndType(WebElement webElement, String text) {
        log.verbose("clear and type", webElement, text);
        if (isBlank(text)) {
            return;
        }
        webElement.clear();
        webElement.sendKeys(text);
    }

    protected void submit(WebElement webElement) {
        log.verbose("submit", webElement);
        webElement.submit();
    }

    // Keys
    protected void pressEnter(WebElement webElement) {
        webElement.sendKeys(Keys.ENTER);
    }

    protected void pressRight(WebElement webElement) {
        webElement.sendKeys(Keys.RIGHT);
    }


    // Attributes
    protected String getValue(WebElement webElement) {
        return webElement.getAttribute("value");
    }

    protected String getAttributeValue(WebElement webElement, String attr) {
        return webElement.getAttribute(attr);
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isElementDisplayed(By by) {
        try {
            driver.findElement(by).isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public static void clearAndSelectByValue(WebElement webElement, String text) {
        if (isBlank(text)) {
            return;
        }
        new Select(webElement).selectByValue(text);
    }

    public void selectByIndex(WebElement
                                        webElement, int index) {
          // note that original Select.selectByIndex(int) actually using html attribute 'index'
          // and it don't fail to find it - always returning '1' (IE8)
          log.verbose("select by index", webElement, "" + index);
          Select select = new Select(webElement);
          int i = 0;
          for (WebElement option : select.getOptions()) {
              if (i == index) {
                  select.selectByValue(getValue(option));
                  return;
              }
              ++i;
          }
      }

}


