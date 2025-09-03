package components;

import locators.QaJobsLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Job card component: reads data and performs actions
public class JobCardComponent {
    private final WebDriver driver;       // Browser driver
    private final WebElement root;        // Card root element
    private final WebDriverWait wait;     // Explicit wait

    // Eager-read values (stale-proof)
    private final String position;        // Job title text
    private final String department;      // Department text
    private final String location;        // Location text

    private static final Duration TIMEOUT = Duration.ofSeconds(15); // Default timeout

    public JobCardComponent(WebDriver driver, WebElement root) {
        this.driver = driver;             // Store driver
        this.root = root;                 // Store card element
        this.wait = new WebDriverWait(driver, TIMEOUT); // Init wait

        // Ensure card is visible
        wait.until(ExpectedConditions.visibilityOf(root));

        // Read child texts with nested waits
        this.position   = safeText(QaJobsLocators.CARD_POSITION);     // Title
        this.department = safeText(QaJobsLocators.CARD_DEPARTMENT);   // Department
        this.location   = safeText(QaJobsLocators.CARD_LOCATION);     // Location
    }

    private String safeText(By nestedLocator) {
        try {
            // Find visible child inside root
            var list = wait.until(
                    ExpectedConditions.visibilityOfNestedElementsLocatedBy(root, nestedLocator)
            );
            WebElement el = list.get(0);  // First match
            // Text with fallbacks
            String t = el.getText();
            if (t != null && !t.isBlank()) return t.trim();
            String inner = el.getDomProperty("innerText");
            if (inner != null && !inner.isBlank()) return inner.trim();
            String content = el.getAttribute("textContent");
            if (content != null && !content.isBlank()) return content.trim();
            return ""; // Empty if none
        } catch (StaleElementReferenceException e) {
            // Re-find globally as last resort
            try {
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nestedLocator));
                String t = el.getText();
                if (t != null && !t.isBlank()) return t.trim();
                String inner = el.getDomProperty("innerText");
                if (inner != null && !inner.isBlank()) return inner.trim();
                String content = el.getAttribute("textContent");
                if (content != null && !content.isBlank()) return content.trim();
            } catch (Exception ignore) {}
            return ""; // Fallback empty
        } catch (NoSuchElementException | TimeoutException e) {
            return ""; // Missing or slow element
        }
    }

    public String position()   { return position; }   // Getter
    public String department() { return department; } // Getter
    public String location()   { return location; }   // Getter

    public void openViewRole() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Shorter action wait

        Actions actions = new Actions(driver);    // Mouse actions
        scrollIntoViewCenter(root);               // Scroll card into view
        actions.moveToElement(root).perform();    // Hover to reveal button

        WebElement btn = wait.until(ExpectedConditions
                .elementToBeClickable(root.findElement(QaJobsLocators.VIEW_ROLE_BTN))); // Clickable button

        btn.click();                               // Open in new tab
    }

    private void scrollIntoViewCenter(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el
        ); // Center scroll

        // Optional: ensure fully within viewport
        wait.until(d -> (Boolean) ((JavascriptExecutor) d).executeScript(
                "const r = arguments[0].getBoundingClientRect();" +
                        "return r.top >= 0 && r.left >= 0 && " +
                        "r.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "r.right <= (window.innerWidth || document.documentElement.clientWidth);",
                el
        ));
    }
}
