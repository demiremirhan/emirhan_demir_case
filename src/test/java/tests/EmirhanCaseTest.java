package tests;

import base.BaseTest;
import components.JobCardComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CareersPage;
import pages.HomePage;
import pages.QaJobsPage;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// E2E smoke for QA jobs flow
public class EmirhanCaseTest extends BaseTest {

    @Test
    @DisplayName("E2E: Home -> Careers -> QA -> Filter -> Validate -> View Role -> Lever")
    void e2e_qajobs_flow() {
        String baseUrl = ConfigReader.get("baseUrl");                   // Base URL

        HomePage home = (HomePage) new HomePage(driver).open(baseUrl);  // Open home
        assertTrue(home.isLoaded(), "Homepage should be loaded");       // Load check
        home.openAndDismissBanner(baseUrl);                             // Close cookies
        assertTrue(home.isLoaded(), "Homepage should load without cookie banner"); // Re-check

        CareersPage careers = home.goToCareers();                       // Navigate to Careers
        assertTrue(careers.isLoaded(), "Careers page should load");     // Load check
        assertTrue(careers.areBlocksVisible(), "Locations/Teams/Life blocks should be visible"); // Key sections

        // QA page + filters
        QaJobsPage qa = careers.openQaJobs()
                .clickSeeAllQa();                                       // Apply QA/Istanbul filters

        List<JobCardComponent> cards = qa.jobCards();                   // Collect cards
        assertFalse(cards.isEmpty(), "Filtered QA jobs should not be empty"); // Non-empty

        // Log & validate card data
        for (JobCardComponent c : cards) {
            System.out.println(c.position());                           // Title log
            System.out.println(c.department());                         // Dept log
            System.out.println(c.location());                           // Location log

            assertTrue(c.position().toLowerCase().contains("quality assurance"),
                    "Position must include QA");                        // Title contains QA
            assertEquals("Istanbul, Turkiye", c.location(),
                    "Location must be Istanbul, Turkiye");              // Location equals
        }

        // New tab handling
        var wait = new WebDriverWait(driver, Duration.ofSeconds(15));   // Short wait
        String parent = driver.getWindowHandle();                       // Parent handle
        int beforeTabs = driver.getWindowHandles().size();              // Tab count before

        cards.get(0).openViewRole();                                    // Click View Role
        wait.until(ExpectedConditions.numberOfWindowsToBe(beforeTabs + 1)); // Wait new tab

        for (String h : driver.getWindowHandles()) {                    // Switch to new tab
            if (!h.equals(parent)) {
                driver.switchTo().window(h);
                break;
            }
        }

        wait.until(ExpectedConditions.urlContains("lever.co"));         // Lever domain check
        assertTrue(driver.getCurrentUrl().contains("jobs.lever.co/")
                        || driver.getTitle().toLowerCase().contains("lever"),
                "Should navigate to Lever application page");           // Final assert
    }
}
