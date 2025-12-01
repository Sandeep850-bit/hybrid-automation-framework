package com.automation.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automation.config.TestConfiguration;
import com.automation.core.Operation;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * AllureTestListener.java
 * TestNG listener for Allure report integration.
 * 
 * Responsibilities:
 * - Take screenshots on test failure
 * - Attach logs to Allure
 * - Track test outcomes
 */
public class AllureTestListener implements ITestListener {
    
    private static final Logger logger = LoggerFactory.getLogger(AllureTestListener.class);
    private static final TestConfiguration config = TestConfiguration.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("Test Started: " + result.getName());
        logger.info("▶ Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test Passed: " + result.getName());
        logger.info("✓ Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("✗ Test Failed: " + result.getName());
        
        try {
            // Take screenshot on failure
            Operation.takeScreenshot(result.getName() + "_FAILURE");
            
            // Attach error message
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                Allure.addAttachment(
                        "Error Stack Trace",
                        "text/plain",
                        new java.io.ByteArrayInputStream(
                                throwable.toString().getBytes()
                        ),
                        ".txt"
                );
            }
        } catch (Exception e) {
            logger.warn("⚠ Error attaching screenshot to Allure", e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⊘ Test Skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("⚠ Test Failed But Within Success Percentage: " + result.getName());
    }
}
