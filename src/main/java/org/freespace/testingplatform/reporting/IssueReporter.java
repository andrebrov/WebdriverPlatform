package org.freespace.testingplatform.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.freespace.testingplatform.reporting.annotations.JiraIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class IssueReporter {

    private static final Logger LOG = LoggerFactory.getLogger(IssueReporter.class);
    private static final String FAILURE = "failed";
    private static final String PASS = "PASS";
    private static final String SCAN_MESSAGE = "Scanning for JIRA issue annotations class ";
    private static final String JIRA_ISSUE_IN_CLASS = "Jira Issue ";
    private static final String SUCCESS = "successed";
    private static final String NOT_RUN = "NOT RUN";

    public static Map<String, String> readAnnotation(Class clazz) throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        Map<String, List<TestCase>> testCases = getTestCases();
        Map<String, String> report = new HashMap<String, String>();
        try {
            String className = clazz.getName();
            if (className.length() > 0) {
                LOG.debug(SCAN_MESSAGE + className);
                List<TestCase> classTestCases = testCases.get(className);
                if (classTestCases == null) return Collections.emptyMap();
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    List<Annotation> annotations = Arrays.asList(method.getAnnotations());
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof JiraIssue) {
                            JiraIssue issue = (JiraIssue) annotation;
                            String issueResult = FAILURE;
                            int calls = 0;
                            int successCalls = 0;
                            for (TestCase classTestCase : classTestCases) {
                                if (classTestCase.getMethodName().equals(method.getName())) {
                                    calls++;
                                    if (classTestCase.getStatus().equals(PASS)) {
                                        successCalls++;
                                    }
                                }
                            }
                            if (calls == successCalls) {
                                issueResult = SUCCESS;
                            }
                            report.put(issue.number(), issueResult);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return report;
    }

    private static Map<String, List<TestCase>> getTestCases() throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {
        Map<String, List<TestCase>> resultMap = new HashMap<String, List<TestCase>>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        XPathExpression expr = null;
        builder = factory.newDocumentBuilder();
        doc = builder.parse("./target/surefire-reports/testng-results.xml");

        // Create a XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();

        // Create a XPath object
        XPath xpath = xFactory.newXPath();

        // Compile the XPath expression
        expr = xpath.compile("//test-method");
        // Run the query and get a nodeset
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        // Cast the result to a DOM NodeList
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            String parentName = nodes.item(i).getParentNode().getAttributes().getNamedItem("name").getNodeValue();
            List<TestCase> testCases = resultMap.get(parentName);
            if (testCases == null) {
                testCases = new ArrayList<TestCase>();
            }
            String status = nodes.item(i).getAttributes().getNamedItem("status").getNodeValue();
            String name = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
            TestCase testCase = new TestCase(name, status);
            testCases.add(testCase);
            resultMap.put(parentName, testCases);
        }
        return resultMap;
    }

    public static void main(String args[]) throws Exception {
        String packageName = "com.ubs.testing.lem.selenium.testcases";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        Map<String, String> testReport = new HashMap<String, String>();
        for (Class aClass : classes) {
            testReport = readAnnotation(aClass);
        }
        List<String> jiraIssues = JiraScanner.getJiraIssues();
        List<JiraTestCase> report = new ArrayList<JiraTestCase>();
        int success = 0;
        int fail = 0;
        int total = jiraIssues.size();
        for (String jiraIssue : jiraIssues) {
            if (testReport.containsKey(jiraIssue)) {
                report.add(new JiraTestCase(jiraIssue, testReport.get(jiraIssue)));
            } else {
                report.add(new JiraTestCase(jiraIssue, NOT_RUN));
            }
        }
        File xmlFile = new File("jira-report.xml");
        FileWriter xmlWriter = new FileWriter(xmlFile);
        LOG.debug("Writing xml report... ");
        ReportPrinter.writeXML(report, xmlWriter, total, success, fail);
        File htmlFile = new File("jira-report.html");
        FileWriter htmlWriter = new FileWriter(htmlFile);
        LOG.debug("Writing html report... ");
        ReportPrinter.writeHTML(report, htmlWriter, total, success, fail);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

   }
