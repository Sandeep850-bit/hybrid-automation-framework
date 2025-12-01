package com.automation.pages;

import com.automation.constants.AppConstant;
import com.automation.core.Operation;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginPage.java
 * Page Object Model for Login page.
 * 
 * Defines all login-related actions using Operation wrapper.
 * No hardcoded locators - all use AppConstant keys and objectRep.properties.
 */
public class LoginPage {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    /**
     * Enter username in login form
     * @param username Username to enter
     */
    @Step("Enter username: {username}")
    public static void enterUsername(String username) {
        logger.info("Entering username: " + username);
        Operation.type(AppConstant.LOGIN_USER, username, AppConstant.LOG_ENTER_USERNAME + username);
    }

    /**
     * Enter password in login form
     * @param password Password to enter
     */
    @Step("Enter password")
    public static void enterPassword(String password) {
        logger.info("Entering password");
        Operation.type(AppConstant.LOGIN_PASSWORD, password, AppConstant.LOG_ENTER_PASSWORD);
    }

    /**
     * Click Sign In button
     */
    @Step("Click Sign In button")
    public static void clickSignIn() {
        logger.info("Clicking Sign In button");
        Operation.click(AppConstant.SIGN_IN_BTN, AppConstant.LOG_CLICK_SIGNIN);
    }

    /**
     * Verify login success popup appears
     */
    @Step("Verify login success")
    public static void verifyLoginSuccess() {
        logger.info("Verifying login success");
        Operation.waitForVisible(AppConstant.SUCCESS_POPUP, AppConstant.LOG_WAIT_SUCCESS);
        boolean isDisplayed = Operation.isDisplayed(AppConstant.SUCCESS_POPUP);
        
        if (isDisplayed) {
            logger.info("✓ Login success popup displayed");
        } else {
            throw new RuntimeException("Login success popup not found");
        }
    }

    /**
     * Verify error message appears
     */
    @Step("Verify login error message")
    public static void verifyLoginError() {
        logger.info("Verifying login error");
        Operation.waitForVisible(AppConstant.ERROR_MESSAGE, "Waiting for error message");
        boolean isDisplayed = Operation.isDisplayed(AppConstant.ERROR_MESSAGE);
        
        if (isDisplayed) {
            logger.info("✓ Login error message displayed");
        } else {
            throw new RuntimeException("Login error message not found");
        }
    }

    /**
     * Complete login flow
     * @param username Username to login with
     * @param password Password to login with
     */
    @Step("Complete login flow")
    public static void login(String username, String password) {
        logger.info("Starting login flow");
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
    }
}
