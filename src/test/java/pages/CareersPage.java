package pages;

import locators.CareersLocators;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;


// Careers page: sections + navigation
public class CareersPage extends BasePage {
    public CareersPage(WebDriver driver) { super(driver); } // Ctor

    @Override
    protected ExpectedCondition<?> pageLoadedCondition() {
        // Page is ready when any core block is visible
        return ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(CareersLocators.LOCATIONS_BLOCK),
                ExpectedConditions.visibilityOfElementLocated(CareersLocators.TEAMS_BLOCK),
                ExpectedConditions.visibilityOfElementLocated(CareersLocators.LIFE_BLOCK)
        );
    }

    // Verify all key blocks
    public boolean areBlocksVisible() {
        try {
            log("Check TEAMS block...");
            scrollIntoView(CareersLocators.TEAMS_BLOCK);   // Scroll to TEAMS
            pause(500);                                   // Small settle
            visible(CareersLocators.TEAMS_BLOCK);          // Ensure visible

            log("Check LOCATIONS block...");
            scrollIntoView(CareersLocators.LOCATIONS_BLOCK); // Scroll to LOCATIONS
            pause(500);
            visible(CareersLocators.LOCATIONS_BLOCK);

            log("Check LIFE block...");
            scrollIntoView(CareersLocators.LIFE_BLOCK);    // Scroll to LIFE
            pause(500);
            visible(CareersLocators.LIFE_BLOCK);

            log("All blocks visible");
            return true;
        } catch (TimeoutException e) {
            log("Blocks not fully visible");
            return false;
        }
    }

    // Navigate to QA jobs page
    public QaJobsPage openQaJobs() {
        log("Open QA Jobs page...");
        click(CareersLocators.QA_PAGE_LINK);              // Click QA link
        return new QaJobsPage(driver);                    // Next page object
    }
}
