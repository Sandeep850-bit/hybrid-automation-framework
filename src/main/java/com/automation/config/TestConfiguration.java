package com.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * TestConfiguration.java
 * Handles loading and retrieval of configuration properties from:
 * 1. config.properties - Test execution configuration
 * 2. objectRep.properties - UI element locators
 * 3. testdata.properties - Test data (username, password, etc.)
 * 
 * Features:
 * - Singleton pattern for thread-safe access
 * - Automatic property loading on initialization
 * - Fallback values for missing properties
 * - Environment variable override support
 */
public class TestConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(TestConfiguration.class);
    private static TestConfiguration instance;
    private Properties configProperties;
    private Properties objectRepProperties;
    private Properties testDataProperties;
    
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    private static final String OBJECT_REP_FILE_PATH = "src/test/resources/objectRep.properties";
    private static final String TEST_DATA_FILE_PATH = "src/test/resources/testdata.properties";

    /**
     * Private constructor to enforce singleton pattern
     */
    private TestConfiguration() {
        loadProperties();
    }

    /**
     * Get singleton instance
     */
    public static synchronized TestConfiguration getInstance() {
        if (instance == null) {
            instance = new TestConfiguration();
        }
        return instance;
    }

    /**
     * Load all configuration files
     */
    private void loadProperties() {
        configProperties = new Properties();
        objectRepProperties = new Properties();
        testDataProperties = new Properties();

        try {
            // Load config.properties
            FileInputStream configFile = new FileInputStream(CONFIG_FILE_PATH);
            configProperties.load(configFile);
            configFile.close();
            logger.info("✓ config.properties loaded successfully");

            // Load objectRep.properties
            FileInputStream objectRepFile = new FileInputStream(OBJECT_REP_FILE_PATH);
            objectRepProperties.load(objectRepFile);
            objectRepFile.close();
            logger.info("✓ objectRep.properties loaded successfully");

            // Load testdata.properties
            FileInputStream testDataFile = new FileInputStream(TEST_DATA_FILE_PATH);
            testDataProperties.load(testDataFile);
            testDataFile.close();
            logger.info("✓ testdata.properties loaded successfully");

        } catch (IOException e) {
            logger.error("✗ Failed to load properties files", e);
            throw new RuntimeException("Unable to load configuration files", e);
        }
    }

    /**
     * Get value from config.properties with environment variable override
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getEnvValue(String key) {
        String envValue = System.getenv(key);
        if (envValue != null) {
            logger.debug("Using environment variable for key: " + key);
            return envValue;
        }
        return configProperties.getProperty(key);
    }

    /**
     * Get value from config.properties with default fallback
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public String getEnvValue(String key, String defaultValue) {
        String value = getEnvValue(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get locator value from objectRep.properties
     * @param key Locator key
     * @return Locator value (e.g., "id:userid" or "//xpath/expression")
     */
    public String getValueFromObjectRep(String key) {
        String value = objectRepProperties.getProperty(key);
        if (value == null) {
            logger.warn("Locator not found in objectRep.properties: " + key);
            throw new RuntimeException("Locator key not found: " + key);
        }
        return value;
    }

    /**
     * Get test data value from testdata.properties
     * @param key Test data key
     * @return Test data value
     */
    public String getTestData(String key) {
        String value = testDataProperties.getProperty(key);
        if (value == null) {
            logger.warn("Test data not found in testdata.properties: " + key);
            return null;
        }
        return value;
    }

    /**
     * Get test data value with default fallback
     * @param key Test data key
     * @param defaultValue Default value if key not found
     * @return Test data value or default
     */
    public String getTestData(String key, String defaultValue) {
        String value = getTestData(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Parse locator value into locator type and value
     * Supports formats: id:value, name:value, xpath:value, css:value
     * 
     * @param key Locator key
     * @return Array [type, value] e.g., ["id", "userid"]
     */
    public String[] parseLocator(String key) {
        String locator = getValueFromObjectRep(key);
        if (locator.contains(":")) {
            return locator.split(":", 2);
        }
        // Assume XPath if no prefix specified
        return new String[]{"xpath", locator};
    }

    /**
     * Get browser type
     */
    public String getBrowser() {
        return getEnvValue("browser", "chromium");
    }

    /**
     * Check if headless mode enabled
     */
    public boolean isHeadless() {
        return Boolean.parseBoolean(getEnvValue("headless", "true"));
    }

    /**
     * Get base URL for testing
     */
    public String getBaseUrl() {
        return getEnvValue("baseUrl", "http://localhost:3000");
    }

    /**
     * Get implicit wait timeout in milliseconds
     */
    public int getImplicitWait() {
        return Integer.parseInt(getEnvValue("implicit.wait", "5000"));
    }

    /**
     * Get explicit wait timeout in milliseconds
     */
    public int getExplicitWait() {
        return Integer.parseInt(getEnvValue("explicit.wait", "10000"));
    }

    /**
     * Get page load timeout in milliseconds
     */
    public int getPageLoadTimeout() {
        return Integer.parseInt(getEnvValue("page.load.timeout", "30000"));
    }

    /**
     * Check if MCP mode is enabled
     */
    public boolean isMcpEnabled() {
        return Boolean.parseBoolean(getEnvValue("mcp.enabled", "false"));
    }

    /**
     * Get MCP endpoint URL
     */
    public String getMcpEndpoint() {
        return getEnvValue("mcp.endpoint", "http://localhost:8080");
    }

    /**
     * Get MCP timeout in milliseconds
     */
    public int getMcpTimeout() {
        return Integer.parseInt(getEnvValue("mcp.timeout", "5000"));
    }

    /**
     * Check if screenshots should be taken on failure
     */
    public boolean isTakeScreenshotOnFailure() {
        return Boolean.parseBoolean(getEnvValue("screenshot.on.failure", "true"));
    }

    /**
     * Get screenshot directory path
     */
    public String getScreenshotDir() {
        return getEnvValue("screenshot.dir", "target/screenshots");
    }

    /**
     * Get test data file path
     */
    public String getDataFilePath() {
        return getEnvValue("data.file.path", "src/test/resources/data/testdata.xlsx");
    }

    /**
     * Get log level
     */
    public String getLogLevel() {
        return getEnvValue("log.level", "INFO");
    }

    // ========== TEST DATA CONVENIENCE METHODS ==========

    /**
     * Get valid username for login tests
     */
    public String getValidUsername() {
        return getTestData("valid.username", "superuser@sks");
    }

    /**
     * Get valid password for login tests
     */
    public String getValidPassword() {
        return getTestData("valid.password", "Welcome1");
    }

    /**
     * Get invalid username for negative tests
     */
    public String getInvalidUsername() {
        return getTestData("invalid.username", "invalid@test.com");
    }

    /**
     * Get invalid password for negative tests
     */
    public String getInvalidPassword() {
        return getTestData("invalid.password", "wrongpass");
    }

    /**
     * Get test user email
     */
    public String getTestEmail() {
        return getTestData("test.email.valid", "test@example.com");
    }

    /**
     * Get test first name
     */
    public String getTestFirstName() {
        return getTestData("form.first.name", "John");
    }

    /**
     * Get test last name
     */
    public String getTestLastName() {
        return getTestData("form.last.name", "Doe");
    }

    /**
     * Get test company name
     */
    public String getTestCompany() {
        return getTestData("form.company", "Test Company");
    }
}
