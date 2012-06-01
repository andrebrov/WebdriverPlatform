package org.freespace.testingplatform.reporting;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class JiraScanner {

    public static List<String> getJiraIssues() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc;
        XPathExpression expr;
        builder = factory.newDocumentBuilder();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("./src/main/resources/jira.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jiraUrl = properties.getProperty("jira.url");
        String flterKey = properties.getProperty("filter.key");
        /*ResourceBundle resourceBundle = ResourceBundle.getBundle("jira.properties");
        String jiraUrl = resourceBundle.getString("jira.url");
        String flterKey = resourceBundle.getString("filter.key");*/
        String xmlUrl = jiraUrl + "sr/jira.issueviews:searchrequest-xml/" + flterKey + "/SearchRequest-" + flterKey + ".xml?tempMax=1000&os_username=admin&os_password=admin";
        URL url = new URL(xmlUrl);
        doc = builder.parse(url.openStream());

        // Create a XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();

        // Create a XPath object
        XPath xpath = xFactory.newXPath();

        // Compile the XPath expression
        expr = xpath.compile("//item/key");
        // Run the query and get a nodeset
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        // Cast the result to a DOM NodeList
        NodeList nodes = (NodeList) result;
        List<String> issuesList = new ArrayList<String>();
        for (int i = 0; i < nodes.getLength(); i++) {
            issuesList.add(nodes.item(i).getTextContent());
        }
        return issuesList;
    }
}
