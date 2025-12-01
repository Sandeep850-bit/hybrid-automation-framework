package com.automation.core;

import com.automation.config.TestConfiguration;
import com.automation.driver.DriverFactory;
import com.automation.mcp.McpClient;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Operation.java
 * Core wrapper for all Playwright operations with MCP override support.
 * 
 * Features:
 * - All Playwright actions through wrapper methods
 * - Automatic MCP delegation when enabled
 * - Automatic screenshot attachment to Allure on failures
 * - Comprehensive logging
 * - Built-in waits and explicit waits
 * 
 * MCP Mode: If mcp.enabled=true, actions are sent to MCP server instead of Playwright.
 */
public class Operation {
    
    private static final Logger logger = LoggerFactory.getLogger(Operation.class);
    private static final TestConfiguration config = TestConfiguration.getInstance();
    private static McpClient mcpClient;
    private static final boolean MCP_ENABLED = config.isMcpEnabled();

    static {
        if (MCP_ENABLED) {
            mcpClient = new McpClient(config.getMcpEndpoint(), config.getMcpTimeout());
            logger.info("✓ MCP Mode ENABLED - Actions will be sent to MCP server");
        } else {
            logger.info("✓ LOCAL Mode ENABLED - Actions will execute via Playwright");
        }
    }

    /**
     * Click element identified by locator key
     * @param key Locator key from objectRep.properties
     * @param logMsg Custom log message
     */
    @Step("Click: {logMsg}")
    public static void click(String key, String logMsg) {
        try {
            Allure.step("Clicking element: " + logMsg);
            logger.info("→ Clicking: " + logMsg);

            if (MCP_ENABLED) {
                mcpClient.sendClick(key);
                logger.info("✓ MCP Click successful: " + logMsg);
            } else {
                getLocator(key).click();
                logger.info("✓ Click successful: " + logMsg);
            }
        } catch (Exception e) {
            logger.error("✗ Click failed: " + logMsg, e);
            captureScreenshot("click_failure_" + key);
            throw new RuntimeException("Click operation failed: " + logMsg, e);
        }
    }

    /**
     * Type text into element
     * @param key Locator key from objectRep.properties
     * @param text Text to type
     * @param logMsg Custom log message
     */
    @Step("Type: {logMsg}")
    public static void type(String key, String text, String logMsg) {
        try {
            Allure.step("Typing in element: " + logMsg);
            logger.info("→ Typing in: " + logMsg);

            if (MCP_ENABLED) {
                mcpClient.sendType(key, text);
                logger.info("✓ MCP Type successful: " + logMsg);
            } else {
                getLocator(key).fill(text);
                logger.info("✓ Type successful: " + logMsg);
            }
        } catch (Exception e) {
            logger.error("✗ Type failed: " + logMsg, e);
            captureScreenshot("type_failure_" + key);
            throw new RuntimeException("Type operation failed: " + logMsg, e);
        }
    }

    /**
     * Select option from dropdown
     * @param key Locator key from objectRep.properties
     * @param value Option value to select
     * @param logMsg Custom log message
     */
    @Step("Select: {logMsg}")
    public static void select(String key, String value, String logMsg) {
        try {
            Allure.step("Selecting option: " + logMsg);
            logger.info("→ Selecting: " + logMsg);

            if (MCP_ENABLED) {
                mcpClient.sendSelect(key, value);
                logger.info("✓ MCP Select successful: " + logMsg);
            } else {
                getLocator(key).selectOption(value);
                logger.info("✓ Select successful: " + logMsg);
            }
        } catch (Exception e) {
            logger.error("✗ Select failed: " + logMsg, e);
            captureScreenshot("select_failure_" + key);
            throw new RuntimeException("Select operation failed: " + logMsg, e);
        }
    }

    /**
     * Wait for element to be visible
     * @param key Locator key from objectRep.properties
     * @param logMsg Custom log message
     */
    @Step("Wait for visible: {logMsg}")
    public static void waitForVisible(String key, String logMsg) {
        try {
            Allure.step("Waiting for element to be visible: " + logMsg);
            logger.info("→ Waiting for visible: " + logMsg);

            if (MCP_ENABLED) {
                mcpClient.sendWait(key, config.getExplicitWait());
                logger.info("✓ MCP Wait successful: " + logMsg);
            } else {
                getLocator(key).waitFor();
                logger.info("✓ Wait successful: " + logMsg);
            }
        } catch (Exception e) {
            logger.error("✗ Wait failed: " + logMsg, e);
            captureScreenshot("wait_failure_" + key);
            throw new RuntimeException("Wait operation failed: " + logMsg, e);
        }
    }

    /**
     * Wait for loading spinner to disappear (implicit wait)
     */
    @Step("Wait for spinner to disappear")
    public static void waitForSpinnerDisAppear() {
        try {
            logger.info("→ Waiting for spinner to disappear");
            
            if (!MCP_ENABLED) {
                Page page = DriverFactory.getPage();
                page.waitForTimeout(1000); // Brief wait to ensure spinner appears
                
                Locator spinner = getLocator("loadingSpinner");
                try {
                    spinner.waitFor(new Locator.WaitForOptions().setTimeout(config.getImplicitWait()));
                    logger.info("✓ Spinner disappeared");
                } catch (Exception e) {
                    logger.warn("⚠ Spinner not found (might not have appeared)");
                }
            }
        } catch (Exception e) {
            logger.warn("⚠ Spinner wait issue: " + e.getMessage());
        }
    }

    /**
     * Check if element is displayed
     * @param key Locator key from objectRep.properties
     * @return true if visible
     */
    public static boolean isDisplayed(String key) {
        try {
            logger.info("→ Checking if visible: " + key);
            return getLocator(key).isVisible();
        } catch (Exception e) {
            logger.warn("⚠ Element not visible: " + key);
            return false;
        }
    }

    /**
     * Get element count
     * @param key Locator key from objectRep.properties
     * @return Number of matching elements
     */
    public static int getElementCount(String key) {
        try {
            return getLocator(key).count();
        } catch (Exception e) {
            logger.warn("⚠ Error getting element count: " + key, e);
            return 0;
        }
    }

    /**
     * Get element text
     * @param key Locator key from objectRep.properties
     * @return Element text content
     */
    public static String getText(String key) {
        try {
            return getLocator(key).textContent();
        } catch (Exception e) {
            logger.error("✗ Error getting text: " + key, e);
            throw new RuntimeException("Failed to get text for: " + key, e);
        }
    }

    /**
     * Take screenshot and attach to Allure
     * @param fileName Screenshot file name
     */
    @Step("Take screenshot: {fileName}")
    public static void takeScreenshot(String fileName) {
        try {
            if (!MCP_ENABLED) {
                Page page = DriverFactory.getPage();
                String screenshotDir = config.getScreenshotDir();
                
                // Create directory if not exists
                new File(screenshotDir).mkdirs();
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String filePath = screenshotDir + File.separator + fileName + "_" + timestamp + ".png";
                
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
                
                // Attach to Allure
                byte[] screenshot = Files.readAllBytes(Paths.get(filePath));
                Allure.addAttachment(fileName + "_" + timestamp, "image/png", new java.io.ByteArrayInputStream(screenshot), ".png");
                
                logger.info("✓ Screenshot saved: " + filePath);
            }
        } catch (Exception e) {
            logger.error("✗ Screenshot failed: " + fileName, e);
        }
    }

    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    @Step("Navigate to: {url}")
    public static void navigateTo(String url) {
        try {
            logger.info("→ Navigating to: " + url);
            DriverFactory.getPage().navigate(url);
            logger.info("✓ Navigation successful: " + url);
        } catch (Exception e) {
            logger.error("✗ Navigation failed: " + url, e);
            captureScreenshot("navigation_failure");
            throw new RuntimeException("Navigation failed: " + url, e);
        }
    }

    /**
     * Get current page URL
     * @return Current URL
     */
    public static String getCurrentUrl() {
        return DriverFactory.getPage().url();
    }

    /**
     * Get element using locator key
     * @param key Locator key from objectRep.properties
     * @return Playwright Locator object
     */
    private static Locator getLocator(String key) {
        String locatorValue = config.getValueFromObjectRep(key);
        String[] parsed = config.parseLocator(key);
        String type = parsed[0];
        String value = parsed[1];

        Page page = DriverFactory.getPage();

        switch (type.toLowerCase()) {
            case "id":
                return page.locator("#" + value);
            case "name":
                return page.locator("[name='" + value + "']");
            case "css":
                return page.locator(value);
            case "xpath":
            default:
                return page.locator(value);
        }
    }

    /**
     * Internal method to capture screenshot on failure
     */
    private static void captureScreenshot(String reason) {
        if (config.isTakeScreenshotOnFailure()) {
            takeScreenshot(reason);
        }
    }
}
