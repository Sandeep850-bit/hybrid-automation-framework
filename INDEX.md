# 📚 Hybrid Automation Framework - Documentation Index

Welcome! Here's your complete guide to this production-ready automation framework.

---

## 🚀 Start Here (Choose Your Path)

### 👤 I'm New to the Framework
**→ Start with [QUICK-START.md](QUICK-START.md)**
- 5-minute setup guide
- Your first test in 10 minutes
- Common commands
- Troubleshooting tips

### 🏗️ I Want to Understand the Architecture
**→ Read [FRAMEWORK-STRUCTURE.md](FRAMEWORK-STRUCTURE.md)**
- Complete file tree
- Component descriptions
- Data flow diagrams
- MCP integration flow

### 📖 I Need Complete Documentation
**→ Read [README.md](README.md)**
- Comprehensive guide
- All features explained
- Configuration details
- Advanced usage

### ⚡ I Want a Quick Overview
**→ Read [SUMMARY.md](SUMMARY.md)**
- Framework highlights
- What's included
- Architecture overview
- Quick reference

### 📊 I Need to Set Up Test Data
**→ Read [TEST-DATA-SETUP.md](TEST-DATA-SETUP.md)**
- Creating Excel files
- Data format
- DataProvider usage
- Multiple sheets

---

## 🎯 Quick Navigation

| Goal | Document | Time |
|------|----------|------|
| Get started immediately | [QUICK-START.md](QUICK-START.md) | 5 min |
| Understand architecture | [FRAMEWORK-STRUCTURE.md](FRAMEWORK-STRUCTURE.md) | 10 min |
| Learn all features | [README.md](README.md) | 20 min |
| Get overview | [SUMMARY.md](SUMMARY.md) | 5 min |
| Setup test data | [TEST-DATA-SETUP.md](TEST-DATA-SETUP.md) | 10 min |

---

## 📁 Project Structure Quick Reference

```
hybrid-automation-framework/
├── README.md                          ← Complete documentation
├── QUICK-START.md                     ← 5-minute setup
├── FRAMEWORK-STRUCTURE.md             ← Architecture details
├── SUMMARY.md                         ← Framework overview
├── TEST-DATA-SETUP.md                 ← Excel data guide
├── pom.xml                            ← Maven dependencies
├── src/
│   ├── main/java/com/automation/
│   │   ├── config/TestConfiguration.java
│   │   ├── constants/AppConstant.java
│   │   ├── core/Operation.java
│   │   ├── driver/DriverFactory.java
│   │   ├── mcp/McpClient.java
│   │   ├── listeners/
│   │   └── utils/
│   └── test/java/com/automation/
│       ├── base/BaseTest.java
│       ├── pages/LoginPage.java
│       └── tests/LoginTest.java
└── src/test/resources/
    ├── config.properties
    ├── objectRep.properties
    ├── testng.xml
    ├── logback.xml
    └── data/testdata.xlsx
```

---

## 🔥 Most Common Tasks

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test
```bash
mvn clean test -Dtest=LoginTest
```

### Change Browser
```bash
mvn clean test -Dbrowser=firefox
```

### View Report
```bash
mvn allure:serve
```

### Enable MCP Mode
```bash
mvn clean test -Dmcp.enabled=true
```

---

## 💡 Framework Highlights

✅ **Page Object Model** - No hardcoded locators  
✅ **Thread-Safe** - Parallel execution support  
✅ **MCP Support** - Optional Model Context Protocol  
✅ **Data-Driven** - Excel-based test parameters  
✅ **Reporting** - Beautiful Allure reports  
✅ **Logging** - Comprehensive SLF4J logging  
✅ **Screenshots** - Automatic on failure  
✅ **Retry Logic** - Flaky test handling  

---

## 📖 Document Descriptions

### [QUICK-START.md](QUICK-START.md)
Your 5-minute guide to getting up and running. Includes:
- Installation steps
- Running your first test
- Common commands
- Troubleshooting

**Best for**: Getting started immediately

### [FRAMEWORK-STRUCTURE.md](FRAMEWORK-STRUCTURE.md)
Deep dive into how the framework is organized. Includes:
- Complete file tree
- Component descriptions
- Execution flow diagrams
- MCP integration details

**Best for**: Understanding architecture

### [README.md](README.md)
Comprehensive documentation covering everything. Includes:
- Feature overview
- Detailed setup
- Running tests
- Configuration guide
- Adding new tests
- Data-driven testing
- Parallel execution
- Performance tips

**Best for**: Learning all features

### [SUMMARY.md](SUMMARY.md)
High-level overview of the complete framework. Includes:
- What's included
- Architecture overview
- Getting started
- How to use
- Best practices
- Performance tips

**Best for**: Quick overview and reference

### [TEST-DATA-SETUP.md](TEST-DATA-SETUP.md)
Guide to creating and using test data. Includes:
- Excel file creation
- Data format
- DataProvider usage
- Multiple sheets
- Best practices

**Best for**: Setting up test data

---

## 🎓 Learning Path

```
Day 1 - Setup & First Test
  1. Read: QUICK-START.md (5 min)
  2. Run: mvn clean install (5 min)
  3. Run: First test (5 min)
  4. View: Allure report (2 min)
  └─ Total: ~20 minutes

Day 2 - Add Your Tests
  1. Read: TEST-DATA-SETUP.md (10 min)
  2. Create: Your first page object
  3. Create: Your first test
  4. Run: mvn clean test
  5. Analyze: Allure report

Day 3 - Optimize & Scale
  1. Read: README.md (20 min)
  2. Enable: Parallel execution
  3. Enable: MCP mode
  4. Create: Test data Excel file
  5. Add: More tests
```

---

## 🆘 Need Help?

### Issue: Tests not running
**Solution**: Check [QUICK-START.md](QUICK-START.md) Troubleshooting section

### Issue: Can't find locators
**Solution**: Read [README.md](README.md) - Adding New Tests section

### Issue: Don't understand architecture
**Solution**: Read [FRAMEWORK-STRUCTURE.md](FRAMEWORK-STRUCTURE.md)

### Issue: Need test data examples
**Solution**: Read [TEST-DATA-SETUP.md](TEST-DATA-SETUP.md)

### Issue: Want to understand everything
**Solution**: Read [SUMMARY.md](SUMMARY.md) then [README.md](README.md)

---

## 🚀 Next Steps

1. **Read**: Start with [QUICK-START.md](QUICK-START.md)
2. **Setup**: Run `mvn clean install`
3. **Test**: Run `mvn clean test`
4. **Report**: Run `mvn allure:serve`
5. **Create**: Add your first test
6. **Learn**: Read [README.md](README.md) for advanced features
7. **Scale**: Enable parallel execution and MCP mode

---

## 📞 Quick Reference

### Files to Edit When
| Task | File | When |
|------|------|------|
| Add new locator | `objectRep.properties` | Adding new UI element |
| Add locator constant | `AppConstant.java` | Adding new locator |
| Create page methods | `*Page.java` | Building page objects |
| Write tests | `*Test.java` | Adding new test cases |
| Change timeout | `config.properties` | Adjusting execution |
| Change browser | `config.properties` | Or use `-Dbrowser=firefox` |

### Commands
```bash
mvn clean test                          # Run all tests
mvn clean test -Dtest=LoginTest         # Run one test class
mvn allure:serve                        # View report
mvn clean install                       # Install dependencies
mvn compile                             # Compile only
```

---

## 🎉 You're All Set!

The framework is:
- ✅ Production-ready
- ✅ Fully documented  
- ✅ Best practices implemented
- ✅ Sample tests included
- ✅ Ready to extend

**Pick a document above and start exploring!** 🚀

---

## 📌 Document Map

```
START HERE → Choose Your Path ↓

I'm new? → QUICK-START.md → Run your first test
                          → View report
                          → Understand basics

Need architecture? → FRAMEWORK-STRUCTURE.md → Understand design
                                            → See file tree
                                            → Learn components

Want all features? → README.md → Complete guide
                              → Advanced usage
                              → Best practices

Want overview? → SUMMARY.md → Quick summary
                           → Key features
                           → Getting started

Need test data? → TEST-DATA-SETUP.md → Create Excel
                                     → Use DataProvider
                                     → Multiple scenarios
```

---

**Ready to start? Open [QUICK-START.md](QUICK-START.md) now!** ⚡
