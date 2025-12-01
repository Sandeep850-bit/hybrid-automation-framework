package com.automation.base;

import com.automation.config.TestConfiguration;
import com.automation.driver.DriverFactory;
import com.automation.listeners.AllureLifecycleConfig;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * BaseTest.java
 * Base test class for all test classes.
 * 
 * Responsibilities:
 * - Initialize Playwright browser in @BeforeSuite
 * - Create BrowserContext + Page for each test in @BeforeMethod
 * - Close resources after each test in @AfterMethod
 * - Close browser after suite in @AfterSuite
 * - Thread-safe execution using ThreadLocal
 * 
 * Every test class must extend BaseTest to get automatic lifecycle management.
 */
public class BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private static final TestConfiguration config = TestConfiguration.getInstance();

    /**
     * BeforeSuite: Initialize browser once for entire suite
     * Called before any tests run
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        logger.info("═══════════════════════════════════════════════════");
        logger.info("   STARTING TEST SUITE: " + context.getSuite().getName());
        logger.info("═══════════════════════════════════════════════════");
        
        try {
            // Initialize Playwright Browser
            DriverFactory.initBrowser(config.getBrowser(), config.isHeadless());
            
            // Configure Allure
            AllureLifecycleConfig.setEnvironmentInfo(
                    config.getBrowser(),
                    config.getBaseUrl(),
                    String.valueOf(config.isMcpEnabled())
            );
            
            logger.info("✓ Suite initialization complete");
            logger.info("  Browser: " + config.getBrowser());
            logger.info("  Headless: " + config.isHeadless());
            logger.info("  MCP Enabled: " + config.isMcpEnabled());
            logger.info("  Base URL: " + config.getBaseUrl());
            
        } catch (Exception e) {
            logger.error("✗ Suite initialization failed", e);
            throw new RuntimeException("Failed to initialize test suite", e);
        }
    }

    /**
     * BeforeMethod: Create new BrowserContext + Page for each test
     * Called before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestContext context) {
        String testName = context.getName();
        logger.info("");
        logger.info("───────────────────────────────────────────────────");
        logger.info("   TEST START: " + testName);
        logger.info("───────────────────────────────────────────────────");
        
        try {
            // Create new context and page for this thread
            DriverFactory.initContextAndPage();
            
            // Navigate to base URL
            String baseUrl = config.getBaseUrl();
            DriverFactory.getPage().navigate(baseUrl);
            
            logger.info("✓ Test initialization complete");
            logger.info("  Thread: " + Thread.currentThread().getName());
            logger.info("  Page initialized and navigated to: " + baseUrl);
            
        } catch (Exception e) {
            logger.error("✗ Test setup failed", e);
            throw new RuntimeException("Failed to initialize test context", e);
        }
    }

    /**
     * AfterMethod: Close BrowserContext + Page after each test
     * Called after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestContext context) {
        try {
            DriverFactory.closeContextAndPage();
            logger.info("✓ Test cleanup complete");
            logger.info("───────────────────────────────────────────────────");
            logger.info("");
            
        } catch (Exception e) {
            logger.error("✗ Test cleanup failed", e);
        }
    }

    /**
     * AfterSuite: Close browser after entire suite
     * Called after all tests complete
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        try {
            logger.info("");
            logger.info("═══════════════════════════════════════════════════");
            logger.info("   SUITE COMPLETED: " + context.getSuite().getName());
            logger.info("═══════════════════════════════════════════════════");
            
            DriverFactory.closeBrowser();
            logger.info("✓ Browser closed");
            
        } catch (Exception e) {
            logger.error("✗ Suite cleanup failed", e);
        }
    }

    /**
     * Helper method to get current page
     * Use: getPage() or DriverFactory.getPage()
     */
    protected Page getPage() {
        return DriverFactory.getPage();
    }
}
