# Quick Start Guide

## ⚡ 5-Minute Setup

### Step 1: Clone & Build
```bash
git clone https://github.com/Sandeep850-bit/hybrid-automation-framework.git
cd hybrid-automation-framework
mvn clean install
```

### Step 2: Install Browsers
```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### Step 3: Run Tests
```bash
mvn clean test
```

### Step 4: View Report
```bash
mvn allure:serve
```

---

## 🔍 Project Overview at a Glance

| Component | Purpose | Edit | Example |
|-----------|---------|------|---------|
| `objectRep.properties` | UI locators | Always | `loginUser=id:userid` |
| `config.properties` | Test settings | Sometimes | `browser=chromium` |
| `AppConstant.java` | Locator keys | When adding locators | `LOGIN_USER = "loginUser"` |
| `Operation.java` | Test actions | Rarely | `Operation.click(key, msg)` |
| `LoginPage.java` | Page actions | For each page | `LoginPage.login(user, pass)` |
| `LoginTest.java` | Test cases | For each test | `@Test public void test()` |

---

## 📋 Adding Your First Test

### 1️⃣ Add Locators
**File**: `src/test/resources/objectRep.properties`
```properties
# Add your locators
myButton=xpath://button[@id='my-btn']
myInput=id:my-input
```

### 2️⃣ Add Constants  
**File**: `src/main/java/com/automation/constants/AppConstant.java`
```java
public static final String MY_BUTTON = "myButton";
public static final String MY_INPUT = "myInput";
```

### 3️⃣ Create Page Class
**File**: `src/test/java/com/automation/pages/MyPage.java`
```java
public class MyPage {
    public static void clickButton() {
        Operation.click(AppConstant.MY_BUTTON, "Click my button");
    }
    
    public static void enterText(String text) {
        Operation.type(AppConstant.MY_INPUT, text, "Enter text: " + text);
    }
}
```

### 4️⃣ Create Test Class
**File**: `src/test/java/com/automation/tests/MyTest.java`
```java
@Epic("My Feature")
@Feature("My Sub-Feature")
public class MyTest extends BaseTest {
    
    @Test(groups = {"smoke"})
    @Description("My first test")
    public void myFirstTest() {
        MyPage.enterText("Hello World");
        MyPage.clickButton();
        Assert.assertTrue(true, "Test passed");
    }
}
```

### 5️⃣ Run Test
```bash
mvn clean test -Dtest=MyTest
```

### 6️⃣ View Report
```bash
mvn allure:serve
```

---

## 🎯 Key Concepts

### No Hardcoded Locators ✅
**WRONG:**
```java
page.click("//button[@id='submit']");  // ❌ Hardcoded
```

**RIGHT:**
```java
Operation.click(AppConstant.SUBMIT_BTN, "Click submit");  // ✅ Uses property
```

### Configuration is External ✅
**Change behavior without code:**
```bash
# Change browser
mvn clean test -Dbrowser=firefox

# Change URL
mvn clean test -DbaseUrl=https://staging.example.com

# Enable MCP
mvn clean test -Dmcp.enabled=true
```

### MCP Mode (Optional) ✅
Enable in `config.properties`:
```properties
mcp.enabled=true
mcp.endpoint=http://your-mcp-server:8080
```

Then Operation automatically sends to MCP server instead of executing locally.

### Thread-Safe Parallel Execution ✅
Each test gets its own Page/BrowserContext:
```xml
<!-- Run 4 tests in parallel -->
<suite name="Suite" parallel="tests" thread-count="4">
```

### Screenshots & Allure ✅
Automatic on failure, configurable in `config.properties`:
```properties
screenshot.on.failure=true
screenshot.on.success=false
```

---

## 📊 Running Tests

### All tests
```bash
mvn clean test
```

### Smoke tests only
```bash
mvn clean test -Dgroups=smoke
```

### Single test class
```bash
mvn clean test -Dtest=LoginTest
```

### Single test method
```bash
mvn clean test -Dtest=LoginTest#testLoginWithValidCredentials
```

### With specific config
```bash
mvn clean test \
  -Dbrowser=firefox \
  -Dheadless=false \
  -DbaseUrl=https://staging.example.com
```

---

## 📊 Allure Reports

### After running tests:
```bash
# Generate and serve report
mvn allure:serve

# Or generate only
mvn allure:report
# Report in: target/site/allure-report/index.html
```

**Report includes:**
- ✅ Test summary and statistics
- 📸 Screenshots on failures
- 📝 Logs and stack traces
- 🏷️ Epic, Feature, Story tags
- ⏱️ Test timeline
- 🌍 Environment info

---

## 🔄 Test Lifecycle

```
TEST SUITE STARTS
    ↓
@BeforeSuite
  ├─ Initialize Playwright Browser (shared across all tests)
  ├─ Configure Allure environment
  └─ Log suite start

@BeforeMethod (for each test)
  ├─ Create new BrowserContext + Page
  ├─ Navigate to baseUrl
  └─ Log test start

TEST METHOD EXECUTION
  ├─ Page actions via Operation class
  ├─ Assertions with Allure steps
  └─ Automatic screenshot on failure

@AfterMethod
  ├─ Close Page
  ├─ Close BrowserContext
  └─ Log test end

@AfterSuite (after all tests)
  ├─ Close shared Browser
  └─ Log suite completion

ALLURE REPORT GENERATED
  └─ With all screenshots, logs, and results
```

---

## 🐛 Troubleshooting

### Tests not running?
1. Check Java version: `java -version` (need 11+)
2. Rebuild: `mvn clean install`
3. Check classpath: `mvn compile`

### Browser not launching?
```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### Timeouts?
Increase in `config.properties`:
```properties
explicit.wait=20000
page.load.timeout=40000
```

### Allure not generating?
```bash
# Full rebuild and report
mvn clean test allure:report allure:serve
```

### Logs not appearing?
Check `target/logs/test-automation.log`

---

## 📁 Important Files to Edit

| File | When | What |
|------|------|------|
| `objectRep.properties` | Adding new UI elements | Add `key=xpath:value` |
| `AppConstant.java` | Adding new locators | Add `CONSTANT_NAME = "key"` |
| `*Page.java` | Adding new page actions | Add methods using Operation |
| `*Test.java` | Adding new tests | Add @Test methods |
| `config.properties` | Changing test settings | Modify timeout/browser/URL |

---

## 🚀 Pro Tips

1. **Keep locators in properties** - Easy to update without recompiling
2. **Use meaningful constants** - Makes tests more readable
3. **Group related tests** - Use @groups in testng.xml
4. **Leverage Allure annotations** - Better reporting (@Epic, @Feature, @Story)
5. **Enable parallel execution** - Faster test runs
6. **Use data providers** - Test multiple scenarios
7. **Take strategic screenshots** - Manual takeScreenshot() at key points

---

## 📞 Need Help?

- Check existing `LoginTest.java` for examples
- Review `LoginPage.java` for page object pattern
- Look at `Operation.java` for available methods
- Check `README.md` for detailed documentation
- Review logs in `target/logs/test-automation.log`

---

**Start writing tests now! 🚀**
