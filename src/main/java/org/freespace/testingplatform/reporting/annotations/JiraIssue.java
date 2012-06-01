package org.freespace.testingplatform.reporting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation used to map test method with jira issues
 * Author: Andrey Rebrov &lt;andrey.rebrov@ubs.com>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JiraIssue {
    String number();
}
