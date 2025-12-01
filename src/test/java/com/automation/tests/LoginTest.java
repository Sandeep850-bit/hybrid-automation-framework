package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.TestConfiguration;
import com.automation.pages.LoginPage;
import com.automation.utils.ExcelUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginTest.java
 * Sample test class demonstrating framework usage.
 * 
 * Features shown:
 * - Extending BaseTest for lifecycle management
 * - Using Page Object Model
 * - Using Allure annotations
 * - Using data-driven testing with DataProvider
 * - Using TestConfiguration for test data
 * - Proper assertions with Allure step logging
 */
@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);
    private static final TestConfiguration config = TestConfiguration.getInstance();

    /**
     * Test login with valid credentials from testdata.properties
     * Demonstrates positive test case using configuration-driven test data
     */
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Story("User logs in with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify successful login with valid username and password from testdata.properties")
    public void testLoginWithValidCredentials() {
        logger.info("Starting: testLoginWithValidCredentials");
        
        // Arrange - Get test data from testdata.properties
        String username = config.getValidUsername();
        String password = config.getValidPassword();
        
        logger.info("Using credentials - Username: " + username);
        
        try {
            // Act
            LoginPage.login(username, password);
            
            // Assert
            LoginPage.verifyLoginSuccess();
            
            logger.info("✓ Test passed: Login successful with valid credentials");
            
        } catch (Exception e) {
            logger.error("✗ Test failed: " + e.getMessage(), e);
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Test login with invalid credentials from testdata.properties
     * Demonstrates negative test case using configuration-driven test data
     */
    @Test(groups = {"smoke", "regression"}, priority = 2)
    @Story("User attempts login with invalid credentials")
    @Severity(SeverityLevel.MAJOR)
    @Description("Verify error message appears when using invalid credentials from testdata.properties")
    public void testLoginWithInvalidCredentials() {
        logger.info("Starting: testLoginWithInvalidCredentials");
        
        // Arrange - Get test data from testdata.properties
        String username = config.getInvalidUsername();
        String password = config.getInvalidPassword();
        
        logger.info("Using credentials - Username: " + username);
        
        try {
            // Act
            LoginPage.login(username, password);
            
            // Assert
            LoginPage.verifyLoginError();
            
            logger.info("✓ Test passed: Error message displayed for invalid credentials");
            
        } catch (Exception e) {
            logger.error("✗ Test failed: " + e.getMessage(), e);
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Data-driven test with multiple credentials from testdata.properties
     * Demonstrates using getTestData() for various test scenarios
     */
    @Test(groups = {"regression"}, priority = 3)
    @Story("Test with multiple user accounts")
    @Severity(SeverityLevel.MAJOR)
    @Description("Verify login with different test users from testdata.properties")
    public void testLoginWithMultipleUsers() {
        logger.info("Starting: testLoginWithMultipleUsers");
        
        try {
            // Test with valid credentials
            String validUser = config.getTestData("test.user1");
            String validUserPassword = config.getTestData("test.user1.password");
            
            if (validUser != null && validUserPassword != null) {
                logger.info("Testing with user: " + validUser);
                LoginPage.login(validUser, validUserPassword);
                LoginPage.verifyLoginSuccess();
                logger.info("✓ Login successful for test user 1");
            }
            
        } catch (Exception e) {
            logger.error("✗ Test failed: " + e.getMessage(), e);
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Test to verify all test data is loaded correctly
     * Useful for debugging test data configuration
     */
    @Test(groups = {"smoke"}, priority = 4)
    @Story("Verify test data configuration")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that all test data is loaded from testdata.properties")
    public void testVerifyTestDataConfiguration() {
        logger.info("Starting: testVerifyTestDataConfiguration");
        
        try {
            // Log all loaded test data
            Allure.step("Verify test data configuration");
            
            String validUsername = config.getValidUsername();
            String validPassword = config.getValidPassword();
            String invalidUsername = config.getInvalidUsername();
            String invalidPassword = config.getInvalidPassword();
            String testEmail = config.getTestEmail();
            String testFirstName = config.getTestFirstName();
            String testLastName = config.getTestLastName();
            String testCompany = config.getTestCompany();
            
            logger.info("✓ Valid Username: " + validUsername);
            logger.info("✓ Invalid Username: " + invalidUsername);
            logger.info("✓ Test Email: " + testEmail);
            logger.info("✓ Test First Name: " + testFirstName);
            logger.info("✓ Test Company: " + testCompany);
            
            // Assert that all critical data is loaded
            Assert.assertNotNull(validUsername, "Valid username should not be null");
            Assert.assertNotNull(validPassword, "Valid password should not be null");
            Assert.assertNotNull(invalidUsername, "Invalid username should not be null");
            Assert.assertNotNull(testEmail, "Test email should not be null");
            
            logger.info("✓ Test passed: All test data loaded successfully");
            
        } catch (Exception e) {
            logger.error("✗ Test failed: " + e.getMessage(), e);
            throw new RuntimeException("Test data verification failed", e);
        }
    }

    /**
     * Data-driven test with multiple credentials from Excel
     * Demonstrates using Excel DataProvider alongside testdata.properties
     */
    @Test(groups = {"regression"}, priority = 5, dataProvider = "loginData")
    @Story("Data-driven login tests from Excel")
    @Severity(SeverityLevel.MAJOR)
    @Description("Verify login with data from Excel file (if exists)")
    public void testLoginWithDataProvider(String[] testData) {
        logger.info("Starting: testLoginWithDataProvider");
        
        // Extract test data
        String username = testData[0];
        String password = testData[1];
        String expectedResult = testData[2];
        
        try {
            // Act
            LoginPage.login(username, password);
            
            // Assert based on expected result
            if ("success".equalsIgnoreCase(expectedResult)) {
                LoginPage.verifyLoginSuccess();
                logger.info("✓ Test passed: Login successful");
            } else {
                LoginPage.verifyLoginError();
                logger.info("✓ Test passed: Error displayed as expected");
            }
            
        } catch (Exception e) {
            logger.error("✗ Test failed: " + e.getMessage(), e);
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * DataProvider for Excel-based data
     * Reads from testdata.xlsx file (if file exists, otherwise returns empty array)
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        try {
            String excelPath = "src/test/resources/data/testdata.xlsx";
            return ExcelUtils.readExcelData(excelPath, "LoginTestData");
        } catch (Exception e) {
            logger.warn("Excel test data file not found, skipping Excel-based tests");
            return new Object[0][0];
        }
    }
}
