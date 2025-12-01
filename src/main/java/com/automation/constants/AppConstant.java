package com.automation.constants;

/**
 * AppConstant.java
 * Central repository for all locator key constants used throughout the framework.
 * These constants are used with objectRep.properties to retrieve actual locator values.
 * 
 * Purpose:
 * - Provides a single source of truth for locator keys
 * - Reduces hardcoding and improves maintainability
 * - Enables IDE autocomplete for locator references
 * 
 * NOTE: Test data constants have been moved to testdata.properties for better maintainability.
 *       Use TestConfiguration.getInstance().getValidUsername(), etc. to access test data.
 */
public class AppConstant {

    // ========== LOGIN PAGE LOCATORS ==========
    public static final String LOGIN_USER = "loginUser";
    public static final String LOGIN_PASSWORD = "loginPassword";
    public static final String SIGN_IN_BTN = "signInBtn";
    public static final String SUCCESS_POPUP = "successPopup";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String FORGOT_PASSWORD_LINK = "forgotPasswordLink";
    public static final String SSO_BUTTON = "ssoButton";

    // ========== DASHBOARD PAGE LOCATORS ==========
    public static final String DASHBOARD_HEADER = "dashboardHeader";
    public static final String USER_PROFILE = "userProfile";
    public static final String LOGOUT_BTN = "logoutBtn";
    public static final String MY_DASHBOARD = "myDashboard";

    // ========== COMMON LOCATORS ==========
    public static final String LOADING_SPINNER = "loadingSpinner";
    public static final String PAGE_LOADER = "pageLoader";
    public static final String CONFIRM_DIALOG = "confirmDialog";
    public static final String CONFIRM_OK_BTN = "confirmOkBtn";
    public static final String CONFIRM_CANCEL_BTN = "confirmCancelBtn";

    // ========== NOTIFICATION LOCATORS ==========
    public static final String SUCCESS_NOTIFICATION = "successNotification";
    public static final String ERROR_NOTIFICATION = "errorNotification";
    public static final String WARNING_NOTIFICATION = "warningNotification";
    public static final String INFO_NOTIFICATION = "infoNotification";

    // ========== TIMEOUT CONSTANTS (in milliseconds) ==========
    public static final int DEFAULT_TIMEOUT = 10000;
    public static final int SHORT_TIMEOUT = 5000;
    public static final int LONG_TIMEOUT = 30000;

    // ========== LOG MESSAGES ==========
    public static final String LOG_ENTER_USERNAME = "Entering username: ";
    public static final String LOG_ENTER_PASSWORD = "Entering password";
    public static final String LOG_CLICK_SIGNIN = "Clicking Sign In button";
    public static final String LOG_WAIT_SUCCESS = "Waiting for success popup";
}
