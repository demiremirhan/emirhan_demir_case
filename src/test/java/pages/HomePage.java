package pages;

import locators.HomeLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;


// Home page: brand check, navigate to Careers, dismiss cookie banner
public class HomePage extends BasePage {
    public HomePage(WebDriver driver) { super(driver); } // Ctor

    @Override
    protected ExpectedCondition<?> pageLoadedCondition() {
        return ExpectedConditions.visibilityOfElementLocated(HomeLocators.BRAND); // Brand visible
    }

    public CareersPage goToCareers() {
        log("Navigate to Careers via Company menu");
        hover(HomeLocators.COMPANY_MENU);   // Open Company dropdown
        click(HomeLocators.CAREERS_LINK);   // Click Careers
        return new CareersPage(driver);     // Next page object
    }

    public HomePage openAndDismissBanner(String url) {
        log("Dismiss cookie banner if present");
        dismissCookieBannerIfAny();         // Close cookies
        return this;
    }
}
