package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import utils.ConfigReader;

import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Base Selenium page: driver, waits, and common helpers (grouped)
public abstract class BasePage {

    private int explicitWaitSec() {
        // reads "explicitWait" from config
        return Integer.parseInt(Optional.ofNullable(ConfigReader.get("explicitWait")).orElse("30"));
    }
    // === Core state ===
    protected final WebDriver driver;                 // Browser driver
    protected final WebDriverWait wait;               // Explicit wait
    protected void log(String msg, Object... args) { System.out.println("[PAGE] " + String.format(msg, args)); }


    
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSec()));
    }

    // === Page load contract ===
    protected abstract ExpectedCondition<?> pageLoadedCondition();

    @Step("Verify page loaded")
    public boolean isLoaded() {
        try { wait.until(pageLoadedCondition()); return true; }
        catch (TimeoutException e) { return false; }
    }

    protected int getTotalCount(By labelLocator) {
        String txt = visible(labelLocator).getText();                 // e.g. "Showing 1 – 12 of 5"
        Matcher m = Pattern.compile("of\\s*(\\d+)").matcher(txt);     // capture trailing number
        return m.find() ? Integer.parseInt(m.group(1)) : -1;          // -1 if parse fails
    }

    protected boolean waitTotalCountChanged(By labelLocator, int oldTotal) {
        int sec = Integer.parseInt(Optional.ofNullable(
                ConfigReader.get("countWaitSeconds")).orElse("25"));  // config timeout
        log("WAIT total count change (old=%d, timeout=%ds)", oldTotal, sec);
        return new WebDriverWait(driver, Duration.ofSeconds(sec))
                .until(d -> {
                    try {
                        int now = getTotalCount(labelLocator);
                        log("Current total: %d", now);
                        return now != -1 && now != oldTotal;
                    } catch (StaleElementReferenceException ignored) { return false; }
                });
    }

    // === Navigation ===
    @Step("Open URL: {url}")
    public BasePage open(String url) {
        System.out.println(">>> OPEN: " + url);
        driver.get(url);
        return this;
    }

    // === JS helpers ===
    protected JavascriptExecutor js() { return (JavascriptExecutor) driver; }

    // === Element waits ===

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // === Basic interactions ===
    protected WebElement click(By locator) {
        WebElement el = clickable(locator);
        el.click();
        return el;
    }
    protected void hover(By locator) {
        new Actions(driver).moveToElement(visible(locator)).perform();
    }

    protected void waitForOptionsPresence(By optionLocator) {
        new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSec()))
                .until(ExpectedConditions.presenceOfElementLocated(optionLocator));
    }

    // === Page/network readiness ===
    protected boolean waitForJsReady() {
        return wait.until(d -> {
            try { return "complete".equals(js().executeScript("return document.readyState")); }
            catch (Exception e) { return true; }
        });
    }

    // === Scrolling ===
    protected void scrollIntoView(By locator) {
        WebElement el = driver.findElement(locator);

        // Disable smooth scroll
        js().executeScript("document.documentElement.style.scrollBehavior='auto'");

        // handles sticky headers
        js().executeScript(
                "const el=arguments[0];" +
                        "const r=el.getBoundingClientRect();" +
                        "const target = r.bottom + window.scrollY - (window.innerHeight*0.85);" +
                        "window.scrollTo(0, Math.max(0, target));", el);

        // down a bit
        js().executeScript("window.scrollBy(0, 60)");

        // PAGE_DOWN if still not fully in view
        try {
            Boolean inView = (Boolean) js().executeScript(
                    "const r=arguments[0].getBoundingClientRect();" +
                            "return r.top>=0 && r.bottom<= (window.innerHeight||document.documentElement.clientHeight);", el);
            if (inView == null || !inView) {
                new Actions(driver).sendKeys(Keys.PAGE_DOWN).perform();
            }
        } catch (Exception ignore) {}
    }

    // === Utilities ===
    protected void pause(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // === Cookie banner handling ===
    protected void dismissCookieBannerIfAny() {
        By[] buttons = new By[] {
                By.cssSelector("#wt-cli-accept-all-btn"),
                By.xpath("//*[@id='wt-cli-accept-all-btn']")
        };
        for (By b : buttons) {
            try {
                WebElement btn = driver.findElement(b);
                if (btn.isDisplayed()) {
                    try { btn.click(); }
                    catch (ElementClickInterceptedException e) { js().executeScript("arguments[0].click();", btn); }
                    pause(200);
                    break;
                }
            } catch (NoSuchElementException ignored) {}
        }
    }
}
