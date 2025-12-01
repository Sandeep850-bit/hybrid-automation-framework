package com.automation.driver;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DriverFactory.java
 * Manages Playwright browser lifecycle and thread-safe page management.
 * 
 * Features:
 * - ThreadLocal storage for parallel test execution
 * - Single browser instance shared across threads
 * - Isolated BrowserContext + Page per thread
 * - Automatic cleanup
 */
public class DriverFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    
    private static Browser browser;
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    /**
     * Initialize browser (called once before suite)
     * @param browserType chromium, firefox, or webkit
     * @param headless true for headless mode
     */
    public static synchronized void initBrowser(String browserType, boolean headless) {
        if (browser != null) {
            logger.warn("Browser already initialized, skipping...");
            return;
        }

        try {
            Playwright playwright = Playwright.create();
            
            switch (browserType.toLowerCase()) {
                case "firefox":
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "chromium":
                default:
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
            }
            
            logger.info("✓ Browser initialized: " + browserType + " (headless: " + headless + ")");
        } catch (Exception e) {
            logger.error("✗ Failed to initialize browser", e);
            throw new RuntimeException("Browser initialization failed", e);
        }
    }

    /**
     * Initialize new BrowserContext and Page for current thread
     */
    public static void initContextAndPage() {
        if (browser == null) {
            throw new RuntimeException("Browser not initialized. Call initBrowser() first.");
        }

        try {
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            
            contextThreadLocal.set(context);
            pageThreadLocal.set(page);
            
            logger.info("✓ New BrowserContext and Page created for thread: " + Thread.currentThread().getName());
        } catch (Exception e) {
            logger.error("✗ Failed to create context and page", e);
            throw new RuntimeException("Context/Page initialization failed", e);
        }
    }

    /**
     * Get Page instance for current thread
     */
    public static Page getPage() {
        Page page = pageThreadLocal.get();
        if (page == null) {
            throw new RuntimeException("Page not initialized for thread: " + Thread.currentThread().getName());
        }
        return page;
    }

    /**
     * Get BrowserContext instance for current thread
     */
    public static BrowserContext getContext() {
        BrowserContext context = contextThreadLocal.get();
        if (context == null) {
            throw new RuntimeException("Context not initialized for thread: " + Thread.currentThread().getName());
        }
        return context;
    }

    /**
     * Close Page and BrowserContext for current thread
     */
    public static void closeContextAndPage() {
        try {
            Page page = pageThreadLocal.get();
            BrowserContext context = contextThreadLocal.get();

            if (page != null) {
                page.close();
                pageThreadLocal.remove();
            }

            if (context != null) {
                context.close();
                contextThreadLocal.remove();
            }

            logger.info("✓ Page and BrowserContext closed for thread: " + Thread.currentThread().getName());
        } catch (Exception e) {
            logger.error("✗ Error closing page/context", e);
        }
    }

    /**
     * Close browser (called after suite)
     */
    public static synchronized void closeBrowser() {
        try {
            if (browser != null) {
                browser.close();
                browser = null;
                logger.info("✓ Browser closed");
            }
        } catch (Exception e) {
            logger.error("✗ Error closing browser", e);
        }
    }

    /**
     * Check if page is initialized for current thread
     */
    public static boolean isPageInitialized() {
        return pageThreadLocal.get() != null;
    }
}
