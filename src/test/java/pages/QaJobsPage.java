package pages;

import components.JobCardComponent;
import locators.QaJobsLocators;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

// QA jobs page: filters + card collection
public class QaJobsPage extends BasePage {
    public QaJobsPage(WebDriver driver) { super(driver); } // Ctor

    @Override
    protected ExpectedCondition<?> pageLoadedCondition() {
        // Page ready when filters or cards visible
        return or(visibilityOfElementLocated(QaJobsLocators.DEPT_DROPDOWN),
                visibilityOfElementLocated(QaJobsLocators.JOB_CARDS));
    }

    public QaJobsPage clickSeeAllQa() {
        log("Apply QA filters (Location: Istanbul, Dept: QA)");
        waitForOptionsPresence(QaJobsLocators.LOCATION_FILTER_LOADER);  // Ensure filter list present
        int before = getTotalCount(QaJobsLocators.RESULT_COUNT);
        log("Open location dropdown");
        click(QaJobsLocators.LOCATION_DROPDOWN);

        log("Select Istanbul");
        click(QaJobsLocators.LOCATION_FILTER_IST);
        //pause(3000); // settle
        waitTotalCountChanged(QaJobsLocators.RESULT_COUNT, before);
        log("Open department dropdown");
        click(QaJobsLocators.DEPT_DROPDOWN);

        log("Select Quality Assurance");
        click(QaJobsLocators.DEPARTMENT_FILTER_QA);
        //pause(3000); // settle
        waitTotalCountChanged(QaJobsLocators.RESULT_COUNT, before);
        log("Wait filtered list loader");
        waitForOptionsPresence(QaJobsLocators.FILTED_LIST_LOADER);

        log("QA filters applied ");
        return this;
    }

    public List<JobCardComponent> jobCards() {
        log("Collect QA job cards");
        List<WebElement> old = driver.findElements(QaJobsLocators.JOB_CARDS); // current cards (pre-refresh)

        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for DOM refresh (if any)
        if (!old.isEmpty()) {
            try {
                log("Wait staleness of first old card");
                w.until(ExpectedConditions.stalenessOf(old.get(0)));
            } catch (TimeoutException ignored) {
                log("Staleness not detected (SPA) — continue");
            }
        }

        // Ensure new cards arrived and are visible
        log("Wait for cards > 0");
        w.until(numberOfElementsToBeMoreThan(QaJobsLocators.JOB_CARDS, 0));

        log("Wait visibility of all cards");
        List<WebElement> fresh = w.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(QaJobsLocators.JOB_CARDS)
        );

        // Map to components (driver + root)
        var list = fresh.stream()
                .map(el -> new JobCardComponent(driver, el))
                .toList();

        log("Mapped to JobCardComponent list");
        return list;
    }
}
