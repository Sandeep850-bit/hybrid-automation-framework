# Test Data Setup

## Creating Test Data Excel File

### Location
`src/test/resources/data/testdata.xlsx`

### How to Create

1. **Create new Excel file** named `testdata.xlsx`
2. **Create sheet** named `LoginTestData`
3. **Add columns** (Header Row):
   - Column A: `username`
   - Column B: `password`
   - Column C: `expectedResult`

### Sample Data

| username | password | expectedResult |
|----------|----------|----------------|
| superuser@sks | Welcome1 | success |
| invalid@test.com | wrongpass | error |
| user2 | pass2 | success |
| user3 | pass3 | error |

### File Format
- **Format**: Excel 2007+ (.xlsx)
- **Encoding**: UTF-8
- **First Row**: Headers (not read as test data)
- **Data Rows**: Start from row 2

### Usage in Code

```java
@DataProvider(name = "loginData")
public Object[][] getLoginData() {
    return ExcelUtils.readExcelData(
        "src/test/resources/data/testdata.xlsx",
        "LoginTestData"
    );
}

@Test(dataProvider = "loginData")
public void testLoginWithDataProvider(String[] testData) {
    String username = testData[0];
    String password = testData[1];
    String expectedResult = testData[2];
    
    // Test logic here
}
```

### Alternative: Map-Based Access

```java
List<Map<String, String>> testData = ExcelUtils.readExcelDataAsMap(
    "src/test/resources/data/testdata.xlsx",
    "LoginTestData"
);

for (Map<String, String> row : testData) {
    String username = row.get("username");
    String password = row.get("password");
    String expectedResult = row.get("expectedResult");
}
```

### Multiple Sheets

You can have multiple sheets for different test scenarios:
- `LoginTestData` - For login tests
- `RegistrationTestData` - For registration tests
- `SearchTestData` - For search tests
- etc.

### Notes
- Keep data consistent and realistic
- Avoid hardcoding in test files
- Update test data without recompiling code
- Easy to share with QA team
- Can be maintained by non-developers

---

## File Structure

```
src/test/resources/data/
└── testdata.xlsx
    ├── LoginTestData (Sheet 1)
    │   ├── username | password | expectedResult
    │   ├── user1 | pass1 | success
    │   ├── user2 | pass2 | error
    │   └── ...
    │
    └── OtherTestData (Sheet 2)
        └── ...
```

---

## Running Tests with Different Data

Once Excel file is created and referenced in DataProvider, simply run:

```bash
mvn clean test
```

All data rows will be executed as separate test iterations!

**Each row = One test execution** (appears as separate test in report)
