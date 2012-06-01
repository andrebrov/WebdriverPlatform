package org.freespace.testingplatform.asserts.soft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class TestBase {

    private static Map verificationFailuresMap = new HashMap();

    public static void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    public static void assertFalse(boolean condition) {
        Assert.assertFalse(condition);
    }

    public static void assertEquals(Object actual, Object expected) {
        Assert.assertEquals(actual, expected);
    }

    public static void verifyTrue(boolean condition) {
        try {
            assertTrue(condition);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void verifyFalse(boolean condition) {
        try {
            assertFalse(condition);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void verifyEquals(Object actual, Object expected) {
        try {
            assertEquals(actual, expected);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static List getVerificationFailures() {
        List verificationFailures = (List) verificationFailuresMap.get(Reporter.getCurrentTestResult());
        return verificationFailures == null ? new ArrayList() : verificationFailures;
    }

    private static void addVerificationFailure(Throwable e) {
        List verificationFailures = getVerificationFailures();
        verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
        verificationFailures.add(e);
    }
}
