package org.freespace.testingplatform.asserts.soft;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
public class TestListenerAdapter implements IInvokedMethodListener {

	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {}

	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {}
}
