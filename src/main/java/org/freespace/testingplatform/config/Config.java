package org.freespace.testingplatform.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.w3c.css.sac.ErrorHandler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

@Component
public class Config {

    private static final Logger log = Logger.getLogger(Config.class);

    public static final String BASE_URL;
    public static final String WEBDRIVER;
    public static final Integer DATA_COUNT;
    public static final Boolean SKIP_LOGIN;
    public static final String LOGIN;
    public static final String PASSWORD;
    public static final boolean VERBOSE = true;

    static {
        String config = "testing.properties";
        if (StringUtils.isEmpty(config)) {
            throw new RuntimeException("Couldn't read properties");
        }
    
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadAllProperties(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE_URL = props.getProperty("app.url");
        WEBDRIVER = props.getProperty("webdriver");
        DATA_COUNT = Integer.valueOf(props.getProperty("data.count"));
        SKIP_LOGIN = Boolean.valueOf(props.getProperty("skip.login"));
        LOGIN = props.getProperty("login");
        PASSWORD = props.getProperty("password");

        log.info("Base url:   " + BASE_URL);
        log.info("WebDriver:  " + WEBDRIVER);
        log.info("Data count: " + DATA_COUNT);
        log.info("Skip login: " + SKIP_LOGIN);
        log.info("Login:      " + LOGIN);
        log.info("Password:   " + PASSWORD);
    }


}
