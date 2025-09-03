# QA Automation Case — Emirhan Demir

Flow:
1) Home → Company → Careers
2) Verify blocks (Locations/Teams/Life)
3) QA page → See all QA jobs → Filter (Istanbul, Turkey + Quality Assurance)
4) Validate every job card
5) “View Role” → Lever page

Stack: Java 17, Selenium 4, JUnit 5, Maven, WebDriverManager, Allure  
Pattern: POM + BasePage + **Component Objects** + per-page Locators

Run:
```bash
mvn clean test
# Allure (optional)
mvn allure:serve
