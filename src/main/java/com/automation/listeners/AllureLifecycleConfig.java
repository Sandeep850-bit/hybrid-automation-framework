package com.automation.listeners;

import io.qameta.allure.Allure;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automation.config.TestConfiguration;

/**
 * AllureLifecycleConfig.java
 * Configures Allure environment details and lifecycle.
 * 
 * Sets environment properties that appear in Allure report.
 */
public class AllureLifecycleConfig implements ISuiteListener {
    
    private static final Logger logger = LoggerFactory.getLogger(AllureLifecycleConfig.class);
    private static final TestConfiguration config = TestConfiguration.getInstance();

    @Override
    public void onStart(ISuite suite) {
        logger.info("Configuring Allure environment...");
        setEnvironmentInfo(
                config.getBrowser(),
                config.getBaseUrl(),
                String.valueOf(config.isMcpEnabled())
        );
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("Allure configuration complete");
    }

    /**
     * Set environment info for Allure report
     */
    public static void setEnvironmentInfo(String browser, String baseUrl, String mcpEnabled) {
        try {
            Allure.parameter("Browser", browser);
            Allure.parameter("Base URL", baseUrl);
            Allure.parameter("MCP Enabled", mcpEnabled);
            Allure.parameter("OS", System.getProperty("os.name"));
            Allure.parameter("Java Version", System.getProperty("java.version"));
            
            logger.info("✓ Allure environment parameters set");
        } catch (Exception e) {
            logger.warn("⚠ Error setting Allure environment", e);
        }
    }
}
