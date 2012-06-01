package org.freespace.testingplatform.webdriver.listneres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class StorageCssErrorHandler implements ErrorHandler, Serializable {

    private static final Log log = LogFactory.getLog(StorageCssErrorHandler.class);

    private Map<String, List<String>> cssErrors = new HashMap<String, List<String>>();
    private Map<String, List<String>> cssFatalErrors = new HashMap<String, List<String>>();
    private Map<String, List<String>> cssWarnings = new HashMap<String, List<String>>();

    public void error(final CSSParseException exception) {
        putException(cssErrors, exception);
    }

    public void fatalError(final CSSParseException exception) {
        putException(cssFatalErrors, exception);
    }

    public void warning(final CSSParseException exception) {
        putException(cssWarnings, exception);
    }

    private void putException(Map<String, List<String>> map, CSSParseException exception) {
        String uri = exception.getURI();
        List<String> strings = map.get(uri);
        if (strings == null) {
            strings = new ArrayList<String>();
        }
        strings.add(buildMessage(exception));
        map.put(uri, strings);
    }

    /**
     * Builds a message for the specified CSS parsing exception.
     *
     * @param exception the CSS parsing exception to build a message for
     * @return a message for the specified CSS parsing exception
     */
    private String buildMessage(final CSSParseException exception) {
        final String uri = exception.getURI();
        final int line = exception.getLineNumber();
        final int col = exception.getColumnNumber();

        if (null == uri) {
            return "[" + line + ":" + col + "] " + exception.getMessage();
        }
        return "'" + uri + "' [" + line + ":" + col + "] " + exception.getMessage();
    }

    public void printErrors(Logger logger) {
        printMessagesForMap(logger, cssErrors, "errors");
    }

    public void printFatalErrors(Logger logger) {
        printMessagesForMap(logger, cssFatalErrors, "fatal errors");
    }

    public void printWarnings(Logger logger) {
        printMessagesForMap(logger, cssWarnings, "warnings");
    }

    private void printMessagesForMap(Logger logger, Map<String, List<String>> map, String keyWord) {
        for (String cssFile : map.keySet()) {
            List<String> strings = map.get(cssFile);
            logger.error("Next css " + keyWord + " occurs in " + cssFile);
            for (String string : strings) {
                logger.error("\t" + string);
            }
        }
    }
}
