package org.freespace.testingplatform.reporting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class ReportPrinter {

    public static void writeXML(List<JiraTestCase> records, Writer writer, int total, int success, int fail) throws IOException, XMLStreamException {
        // Create a XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        // Create XMLEventWriter
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(writer);
        // Create a EventFactory
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        // Create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        // Create config open tag
        StartElement testReportStartElement = eventFactory.createStartElement("",
                "", "test-report");
        eventWriter.add(testReportStartElement);
        eventWriter.add(end);
        //writing summary
        StartElement summaryStartElement = eventFactory.createStartElement("",
                "", "summary");
        eventWriter.add(summaryStartElement);
        eventWriter.add(end);
        createNode(eventWriter, "total", "" + total);
        createNode(eventWriter, "success", "" + success);
        createNode(eventWriter, "fail", "" + fail);
        eventWriter.add(eventFactory.createEndElement("", "", "summary"));
        eventWriter.add(end);
        //writing dettail report
        StartElement testCasesStartElement = eventFactory.createStartElement("",
                "", "test-cases");
        eventWriter.add(testCasesStartElement);
        eventWriter.add(end);
        for (JiraTestCase record : records) {
            StartElement testCaseStartElement = eventFactory.createStartElement("",
                    "", "test-case");
            eventWriter.add(testCaseStartElement);
            eventWriter.add(end);
            createNode(eventWriter, "jira-issue", record.getJiraIssue());
            createNode(eventWriter, "result", record.getStatus());
            eventWriter.add(eventFactory.createEndElement("", "", "test-case"));
            eventWriter.add(end);
        }
        //closing document
        eventWriter.add(eventFactory.createEndElement("", "", "test-cases"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "test-report"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    private static void createNode(XMLEventWriter eventWriter, String name,
                                   String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // Create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // Create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    public static void writeHTML(List<JiraTestCase> records, Writer writer, int total, int success, int fail) throws IOException, XMLStreamException {
        BufferedWriter out = new BufferedWriter(writer);
        out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Automation test report</title>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"summary\">\n" +
                "    <table>\n" +
                "        <thead>\n" +
                "        <tr>\n" +
                "            <td>Total</td>\n" +
                "            <td>Not run</td>\n" +
                "            <td>Fail</td>\n" +
                "            <td>Success</td>\n" +
                "        </tr>\n" +
                "        </thead>\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <div id=\"total\">" + total + "</div>\n" +
                "            </td>\n" +
                "            <td>\n" +
                "                <div id=\"notrun\">" + (total - success - fail) + "</div>\n" +
                "            </td>\n" +
                "            <td>\n" +
                "                <div id=\"fail\">" + fail + "</div>\n" +
                "            </td>\n" +
                "            <td>\n" +
                "                <div id=\"success\">" + success + "</div>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</div>\n");
        out.write("<div id=\"full\">\n" +
                "    <table>\n" +
                "        <thead>\n" +
                "        <tr>\n" +
                "            <td>Issue</td>\n" +
                "            <td>Status</td>\n" +
                "        </tr>\n" +
                "        </thead>\n");
        for (JiraTestCase record : records) {
            out.write("<tr>\n            <td>\n" +
                    "                <div class=\"issue\">" + record.getJiraIssue() + "</div>\n" +
                    "            </td>\n" +
                    "            <td>\n" +
                    "                <div class=\"status\">" + record.getStatus() + "</div>\n" +
                    "            </td>\n</tr>\n");
        }
        out.write("        </tr>\n" +
                "    </table>\n" +
                "</div>\n");
        out.write("</body>\n" +
                "</html>");
        out.close();
        writer.close();
    }
}
