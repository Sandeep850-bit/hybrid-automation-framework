package com.automation.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RetryAnalyzer.java
 * TestNG retry mechanism for flaky tests.
 * 
 * Automatically retries failed tests up to MAX_RETRY_COUNT times.
 * Usage: Add @Retry annotation to test methods
 * 
 * Example:
 * @Test(retryAnalyzer = RetryAnalyzer.class)
 * public void testFlaky() { ... }
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("⚠ Retrying test: " + result.getName() + " (Attempt: " + retryCount + ")");
            return true;
        }
        return false;
    }
}
