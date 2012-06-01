package org.freespace.testingplatform.reporting;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class TestCase {

    private String methodName;
    private String status;

    public TestCase(String methodName, String status) {
        this.methodName = methodName;
        this.status = status;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
