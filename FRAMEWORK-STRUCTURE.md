# Hybrid Automation Framework - File Tree

```
hybrid-automation-framework/
│
├── pom.xml                                          # Maven project configuration
├── README.md                                        # Documentation
│
├── src/
│   ├── main/java/com/automation/
│   │   ├── config/
│   │   │   └── TestConfiguration.java              # Configuration management
│   │   │
│   │   ├── constants/
│   │   │   └── AppConstant.java                    # All locator key constants
│   │   │
│   │   ├── core/
│   │   │   └── Operation.java                      # Playwright wrapper with MCP support
│   │   │
│   │   ├── driver/
│   │   │   └── DriverFactory.java                  # Browser lifecycle & ThreadLocal
│   │   │
│   │   ├── mcp/
│   │   │   └── McpClient.java                      # MCP server communication (HTTP)
│   │   │
│   │   ├── listeners/
│   │   │   ├── AllureTestListener.java             # Allure integration listener
│   │   │   └── AllureLifecycleConfig.java          # Allure environment setup
│   │   │
│   │   └── utils/
│   │       ├── ExcelUtils.java                     # Excel data reading (Apache POI)
│   │       └── RetryAnalyzer.java                  # TestNG test retry mechanism
│   │
│   └── test/java/com/automation/
│       ├── base/
│       │   └── BaseTest.java                       # Base test class (lifecycle)
│       │
│       ├── pages/
│       │   └── LoginPage.java                      # Sample Page Object
│       │
│       └── tests/
│           └── LoginTest.java                      # Sample test class
│
└── src/test/resources/
    ├── config.properties                           # Test configuration
    ├── objectRep.properties                        # All UI locators
    ├── testng.xml                                  # TestNG suite configuration
    ├── logback.xml                                 # Logging configuration
    ├── logback.properties                          # Logback reference
    │
    └── data/
        └── testdata.xlsx                           # Excel test data (create manually)
```

## Key Classes Overview

| Class | Purpose | Location |
|-------|---------|----------|
| **BaseTest** | Test lifecycle management (BeforeSuite, BeforeMethod, AfterMethod, AfterSuite) | `test/base/` |
| **TestConfiguration** | Configuration loader with env var override | `main/config/` |
| **DriverFactory** | Browser & Page lifecycle with ThreadLocal | `main/driver/` |
| **Operation** | Playwright wrapper with MCP override support | `main/core/` |
| **McpClient** | MCP server HTTP communication | `main/mcp/` |
| **AppConstant** | Locator key constants | `main/constants/` |
| **AllureTestListener** | Screenshot & log attachment on failure | `main/listeners/` |
| **ExcelUtils** | Excel data reading for DataProviders | `main/utils/` |
| **RetryAnalyzer** | Automatic test retry on failure | `main/utils/` |
| **LoginPage** | Sample Page Object (no hardcoded locators) | `test/pages/` |
| **LoginTest** | Sample test with Allure annotations | `test/tests/` |

## Configuration Files

### config.properties
- Browser type (chromium, firefox, webkit)
- Headless mode
- Base URL
- Timeouts
- MCP settings
- Screenshot options

### objectRep.properties
- Login page locators: `loginUser`, `loginPassword`, `signInBtn`
- Dashboard locators: `dashboardHeader`, `userProfile`, `myDashboard`
- Common locators: `loadingSpinner`, `confirmDialog`
- All in format: `key=xpath:...` or `key=id:...`

### testng.xml
- Suite configuration
- Parallel execution settings (tests, methods, instances)
- Test groups (smoke, regression)
- Listener registration

## MCP Integration Flow

```
Test Method
    ↓
Operation.click(key)
    ↓
Is MCP Enabled?
    ├─ YES → McpClient.sendClick(key) → HTTP POST to MCP Server
    └─ NO  → Playwright.click(locator) → Local execution
    ↓
Screenshot on failure (if enabled)
    ↓
Allure attachment
```

## Test Execution Flow

```
@BeforeSuite
    ↓ Initialize Browser (once)
    ├─ Create Playwright instance
    ├─ Launch browser (chromium/firefox/webkit)
    └─ Configure Allure

@BeforeMethod (per test)
    ↓ Create new Context + Page
    ├─ BrowserContext from shared browser
    ├─ Page from context (thread-local)
    └─ Navigate to base URL

Test Method Execution
    ↓
@AfterMethod
    ↓ Close Context + Page
    ├─ Close page
    └─ Close context

@AfterSuite
    ↓ Close Browser
    └─ Close shared browser
```

## Adding New Test

1. **Create locators** → `objectRep.properties`
2. **Add constants** → `AppConstant.java`
3. **Create page** → `src/test/java/com/automation/pages/MyPage.java`
4. **Write test** → `src/test/java/com/automation/tests/MyTest.java`
5. **Run** → `mvn clean test`
6. **Report** → `mvn allure:serve`

All locators referenced by key, no hardcoding!

---

**Framework is production-ready and follows all best practices!**
