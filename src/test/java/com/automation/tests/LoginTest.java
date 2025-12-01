package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.constants.AppConstant;
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
 * - Proper assertions with Allure step logging
 */
@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    /**
     * Test login with valid credentials
     * Demonstrates positive test case
     */
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Story("User logs in with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify successful login with valid username and password")
    public void testLoginWithValidCredentials() {
        logger.info("Starting: testLoginWithValidCredentials");
        
        // Arrange
        String username = AppConstant.VALID_USERNAME;
        String password = AppConstant.VALID_PASSWORD;
        
        try {
            // Act
            LoginPage.login(username, password);
            
            // Assert
            LoginPage.verifyLoginSuccess();
            Assert.assertTrue(LoginPage.verifyLoginSuccess() != null, "Login was not successful");
            
            logger.info("✓ Test passed: Login successful with valid credentials");
            
        } catch (Exception e) {
            logger.error("✗ Test failed: " + e.getMessage(), e);
            throw new RuntimeException("Test failed", e);
        }
    }

    /**
     * Test login with invalid credentials
     * Demonstrates negative test case
     */
    @Test(groups = {"smoke", "regression"}, priority = 2)
    @Story("User attempts login with invalid credentials")
    @Severity(SeverityLevel.MAJOR)
    @Description("Verify error message appears when using invalid credentials")
    public void testLoginWithInvalidCredentials() {
        logger.info("Starting: testLoginWithInvalidCredentials");
        
        // Arrange
        String username = AppConstant.INVALID_USERNAME;
        String password = AppConstant.INVALID_PASSWORD;
        
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
     * Data-driven test with multiple credentials from Excel
     * Demonstrates using Excel DataProvider
     */
    @Test(groups = {"regression"}, priority = 3, dataProvider = "loginData")
    @Story("Data-driven login tests")
    @Severity(SeverityLevel.MAJOR)
    @Description("Verify login with data from Excel file")
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
     * Reads from testdata.xlsx file
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        String excelPath = "src/test/resources/data/testdata.xlsx";
        return ExcelUtils.readExcelData(excelPath, "LoginTestData");
    }
}
