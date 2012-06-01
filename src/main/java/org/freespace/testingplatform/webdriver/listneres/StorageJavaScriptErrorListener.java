package org.freespace.testingplatform.webdriver.listneres;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class StorageJavaScriptErrorListener implements JavaScriptErrorListener {

    private static final Logger log = Logger.getLogger(StorageJavaScriptErrorListener.class);
    private Map<HtmlPage, List<ScriptException>> scriptExceptions = new HashMap<HtmlPage, List<ScriptException>>();
    private Map<HtmlPage, List<String>> timeoutErrors = new HashMap<HtmlPage, List<String>>();
    private Map<HtmlPage, List<String>> loadScriptErrors = new HashMap<HtmlPage, List<String>>();

    public void scriptException(HtmlPage htmlPage, ScriptException scriptException) {
        List<ScriptException> scriptExceptionsList = scriptExceptions.get(htmlPage);
        if (scriptExceptionsList == null) {
            scriptExceptionsList = new ArrayList<ScriptException>();
        }
        scriptExceptionsList.add(scriptException);
        scriptExceptions.put(htmlPage, scriptExceptionsList);
    }

    public void timeoutError(HtmlPage htmlPage, long allowedTime, long executionTime) {
        List<String> errorsList = timeoutErrors.get(htmlPage);
        if (errorsList == null) {
            errorsList = new ArrayList<String>();
        }
        errorsList.add("Timeout error: allowed time = " + allowedTime + ", execution time = " + executionTime);
        timeoutErrors.put(htmlPage, errorsList);
    }

    public void malformedScriptURL(HtmlPage htmlPage, String url, MalformedURLException malformedURLException) {

    }

    public void loadScriptError(HtmlPage htmlPage, URL scriptUrl, Exception exception) {
       List<String> errorsList = loadScriptErrors.get(htmlPage);
       if (errorsList == null) {
           errorsList = new ArrayList<String>();
       }
       errorsList.add(scriptUrl.toString());
       loadScriptErrors.put(htmlPage, errorsList);
    }

    public Map<HtmlPage, List<ScriptException>> getScriptExceptions() {
        return scriptExceptions;
    }

    public Map<HtmlPage, List<String>> getTimeoutErrors() {
        return timeoutErrors;
    }

    public Map<HtmlPage, List<String>> getLoadScriptErrors() {
        return loadScriptErrors;
    }

    public void printLoadScriptErrors(Logger logger) {
        for (HtmlPage htmlPage : loadScriptErrors.keySet()) {
            List<String> strings = loadScriptErrors.get(htmlPage);
            logger.error("Next load script error occurs on page " + htmlPage.getTitleText());
            for (String string : strings) {
                logger.error("\t" + string);
            }
        }
    }

    public void printScriptExceptions(Logger logger) {
        for (HtmlPage htmlPage : scriptExceptions.keySet()) {
            List<ScriptException> exceptionList = scriptExceptions.get(htmlPage);
            logger.error("Next script exception occurs on page " + htmlPage.getTitleText());
            for (ScriptException scriptException : exceptionList) {
                logger.error("\t Script exception occurred in " + scriptException.getScriptSourceCode()
                        + " line " + scriptException.getFailingLine());
            }
        }
    }

    public void printTimeouts(Logger logger) {
        for (HtmlPage htmlPage : timeoutErrors.keySet()) {
            List<String> strings = timeoutErrors.get(htmlPage);
            logger.error("Next timeouts occurs on page " + htmlPage.getTitleText());
            for (String string : strings) {
                logger.error("\t" + string);
            }
        }
    }
}
