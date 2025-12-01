# Hybrid Automation Framework

A complete, modular, production-ready test automation framework built with:
- **Language**: Java 11+
- **Browser Automation**: Playwright for Java
- **Test Framework**: TestNG
- **Reporting**: Allure Reports
- **Build Tool**: Maven
- **Optional**: MCP Server Support

## 🎯 Framework Features

### ✅ Core Capabilities
- **Page Object Model (POM)** - Organized, maintainable page classes
- **No Hardcoded Locators** - All locators in `objectRep.properties`
- **Thread-Safe** - Support for parallel test execution
- **MCP Integration** - Optional Model Context Protocol server support
- **Data-Driven Testing** - Excel-based test data via Apache POI
- **Comprehensive Logging** - SLF4J + Logback integration
- **Allure Reports** - Beautiful, detailed test reports with screenshots

### 📊 Configuration Management
- `config.properties` - Environment and execution settings
- `objectRep.properties` - All UI locators in one place
- `TestConfiguration.java` - Centralized config access with environment variable override
- `logback.xml` - Logging configuration

### 🔄 Test Lifecycle
- **BeforeSuite** - Initialize browser once
- **BeforeMethod** - Create new context + page per test
- **AfterMethod** - Clean up resources
- **AfterSuite** - Close browser

## 📁 Project Structure

```
hybrid-automation-framework/
├── pom.xml                              # Maven configuration
├── src/
│   ├── main/java/com/automation/
│   │   ├── config/
│   │   │   └── TestConfiguration.java   # Config management
│   │   ├── constants/
│   │   │   └── AppConstant.java         # Locator key constants
│   │   ├── core/
│   │   │   └── Operation.java           # Playwright wrapper methods
│   │   ├── driver/
│   │   │   └── DriverFactory.java       # Browser lifecycle management
│   │   ├── mcp/
│   │   │   └── McpClient.java           # MCP server communication
│   │   ├── listeners/
│   │   │   ├── AllureTestListener.java  # Allure integration
│   │   │   └── AllureLifecycleConfig.java
│   │   └── utils/
│   │       ├── ExcelUtils.java          # Excel data reading
│   │       └── RetryAnalyzer.java       # Test retry logic
│   └── test/java/com/automation/
│       ├── base/
│       │   └── BaseTest.java            # Base test class
│       ├── pages/
│       │   └── LoginPage.java           # Sample page object
│       └── tests/
│           └── LoginTest.java           # Sample test class
│   └── test/resources/
│       ├── config.properties            # Test configuration
│       ├── objectRep.properties         # Locators repository
│       ├── testng.xml                   # TestNG suite configuration
│       ├── logback.xml                  # Logging configuration
│       └── data/
│           └── testdata.xlsx            # Test data file (to be created)
└── README.md                            # This file
```

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Chrome/Firefox/WebKit browser

### Installation

1. **Clone repository**
   ```bash
   git clone https://github.com/Sandeep850-bit/hybrid-automation-framework.git
   cd hybrid-automation-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Install Playwright browsers**
   ```bash
   mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
   ```

## ✍️ Running Tests

### Run all tests
```bash
mvn clean test
```

### Run specific test class
```bash
mvn clean test -Dtest=LoginTest
```

### Run specific test method
```bash
mvn clean test -Dtest=LoginTest#testLoginWithValidCredentials
```

### Run tests by group
```bash
mvn clean test -Dgroups=smoke
```

### Run with specific browser
```bash
mvn clean test -Dbrowser=firefox
```

### Run headless (default)
```bash
mvn clean test -Dheadless=true
```

### Run in headed mode
```bash
mvn clean test -Dheadless=false
```

## 📊 Allure Reports

### Generate Allure report after test run
```bash
mvn allure:report
mvn allure:serve
```

This will:
1. Generate report from test results
2. Start local Allure server (typically http://localhost:4040)
3. Open report in browser automatically

### Report contents
- Test execution summary
- Screenshots on failures
- Logs and stack traces
- Environment information
- Test timeline

## 🔧 Configuration Guide

### config.properties

```properties
# Browser: chromium, firefox, webkit
browser=chromium
headless=true

# Base URL for tests
baseUrl=https://example.com

# Timeouts in milliseconds
implicit.wait=5000
explicit.wait=10000
page.load.timeout=30000

# MCP Configuration (optional)
mcp.enabled=false
mcp.endpoint=http://localhost:8080
mcp.timeout=5000

# Screenshots
screenshot.on.failure=true
screenshot.on.success=false
screenshot.dir=target/screenshots
```

### Enabling MCP Mode

To use MCP server instead of local Playwright:

1. **Update config.properties**
   ```properties
   mcp.enabled=true
   mcp.endpoint=http://your-mcp-server:8080
   ```

2. **In Operation.java**, actions will automatically delegate to MCP server:
   ```java
   // Instead of executing locally, sends to MCP server
   Operation.click(AppConstant.SIGN_IN_BTN, "Click Sign In");
   ```

## 📝 Adding New Tests

### 1. Create Locators

Add to `src/test/resources/objectRep.properties`:
```properties
myElement=xpath://button[@id='myBtn']
myInput=id:myInput
```

### 2. Add Constants

Add to `src/main/java/com/automation/constants/AppConstant.java`:
```java
public static final String MY_ELEMENT = "myElement";
public static final String MY_INPUT = "myInput";
```

### 3. Create Page Object

Create `src/test/java/com/automation/pages/MyPage.java`:
```java
public class MyPage {
    public static void clickMyElement() {
        Operation.click(AppConstant.MY_ELEMENT, "Click my element");
    }
    
    public static void enterText(String text) {
        Operation.type(AppConstant.MY_INPUT, text, "Enter text");
    }
}
```

### 4. Write Test

Create `src/test/java/com/automation/tests/MyTest.java`:
```java
public class MyTest extends BaseTest {
    @Test
    public void myTest() {
        MyPage.enterText("Hello");
        MyPage.clickMyElement();
        Assert.assertTrue(condition, "Verification message");
    }
}
```

## 📊 Data-Driven Testing

### Create Excel file
Path: `src/test/resources/data/testdata.xlsx`

Sheet: `LoginTestData`
| username | password | expectedResult |
|----------|----------|----------------|
| user1 | pass1 | success |
| invalid | wrong | error |

### Use in test
```java
@Test(dataProvider = "loginData")
public void testLogin(String[] testData) {
    String username = testData[0];
    String password = testData[1];
    // ... test logic
}

@DataProvider(name = "loginData")
public Object[][] getLoginData() {
    return ExcelUtils.readExcelData(
        "src/test/resources/data/testdata.xlsx", 
        "LoginTestData"
    );
}
```

## 🏷️ Allure Annotations

```java
@Epic("Feature Name")
@Feature("Sub-feature")
@Story("User story description")
@Severity(SeverityLevel.CRITICAL)
@Description("Test description")
@Test
public void myTest() {
    Allure.step("Step 1 - Do something");
    // test code
    
    Allure.step("Step 2 - Verify something");
    // verification
}
```

## 🔄 Parallel Execution

Configure thread count in `testng.xml`:
```xml
<suite name="Suite" parallel="tests" thread-count="4">
```

Or in `pom.xml`:
```xml
<parallel>tests</parallel>
<threadCount>4</threadCount>
```

## 📸 Screenshots & Logging

### Automatic Screenshots
```java
// On test failure - automatic
// On success - if enabled in config

// Manual screenshot
Operation.takeScreenshot("custom-screenshot-name");
```

### Logging
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(MyTest.class);
logger.info("Info message");
logger.error("Error message", exception);
```

Logs are written to:
- **Console** - Real-time output
- **File** - `target/logs/test-automation.log`
- **Allure Report** - Automatically attached

## 🐛 Test Retry

### Using RetryAnalyzer
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void flakyTest() {
    // Will retry up to 2 times on failure
}
```

Configure retry count in `src/main/java/com/automation/utils/RetryAnalyzer.java`:
```java
private static final int MAX_RETRY_COUNT = 2;
```

## 🔐 Thread Safety

Framework uses ThreadLocal for thread-safe page management:
```java
// Each thread gets its own page instance
DriverFactory.initContextAndPage();    // In @BeforeMethod
Page page = DriverFactory.getPage();   // Get current thread's page
DriverFactory.closeContextAndPage();   // In @AfterMethod
```

Safely supports parallel test execution with `parallel="tests"` in testng.xml.

## 📦 Dependencies

Core dependencies included:
- `playwright:1.40.1` - Browser automation
- `testng:7.8.1` - Test framework
- `allure-testng:2.21.0` - Reporting
- `poi:5.2.3` - Excel support
- `slf4j:2.0.9` + `logback:1.4.11` - Logging
- `rest-assured:5.4.0` - API testing (MCP support)
- `gson:2.10.1` - JSON processing

## 🆘 Troubleshooting

### Browser not launching
```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### Playwright timeout
Increase timeout in `config.properties`:
```properties
explicit.wait=20000
page.load.timeout=40000
```

### Allure report not generating
```bash
mvn clean test
mvn allure:report
```

### MCP connection issues
Verify MCP server is running:
```bash
curl http://localhost:8080/health
```

## 📚 Framework Components Deep Dive

### Operation.java
Central operations class with automatic MCP override:
```java
Operation.click(key, message);           // Click element
Operation.type(key, text, message);      // Type text
Operation.select(key, value, message);   // Select dropdown
Operation.waitForVisible(key, message);  // Wait for element
Operation.isDisplayed(key);              // Check visibility
Operation.takeScreenshot(filename);      // Capture screenshot
```

### DriverFactory.java
Browser lifecycle management:
```java
DriverFactory.initBrowser(browserType, headless);
DriverFactory.initContextAndPage();
DriverFactory.getPage();
DriverFactory.closeContextAndPage();
DriverFactory.closeBrowser();
```

### TestConfiguration.java
Configuration management with environment override:
```java
config.getBrowser();           // Get browser type
config.isHeadless();           // Headless mode
config.getBaseUrl();           // Base URL
config.getValueFromObjectRep(key);  // Get locator
config.isMcpEnabled();         // MCP status
```

## 🚀 Performance Tips

1. **Use parallel execution** - `thread-count="4"` in testng.xml
2. **Minimize waits** - Adjust timeouts based on app performance
3. **Reuse browser context** - Uses shared browser instance
4. **Disable screenshots** - Set `screenshot.on.success=false` if not needed
5. **Use headless mode** - ~30% faster execution

## 📄 License

This framework is provided as-is for automation testing purposes.

## 👥 Contributing

To extend this framework:
1. Add new locators to `objectRep.properties`
2. Add constants to `AppConstant.java`
3. Create new page classes
4. Add test methods following the pattern
5. Run tests and generate Allure reports

## 📞 Support

For issues or questions:
1. Check existing test examples
2. Review Allure report for failures
3. Check logs in `target/logs/`
4. Verify `config.properties` settings

---

**Happy Testing! 🎉**

For the latest updates, visit: https://github.com/Sandeep850-bit/hybrid-automation-framework
