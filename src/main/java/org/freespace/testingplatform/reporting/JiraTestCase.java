package org.freespace.testingplatform.reporting;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class JiraTestCase {
    private String jiraIssue;
    private String status;

    public JiraTestCase(String jiraIssue, String status) {
        this.jiraIssue = jiraIssue;
        this.status = status;
    }

    public String getJiraIssue() {
        return jiraIssue;
    }

    public void setJiraIssue(String jiraIssue) {
        this.jiraIssue = jiraIssue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
