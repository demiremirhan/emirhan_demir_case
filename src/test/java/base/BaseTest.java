package base;

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader; // Config access

import java.time.Duration;

// Test base: setup/teardown
public abstract class BaseTest {
    protected WebDriver driver;           // Browser session
    protected WebDriverWait wait;         // Explicit wait

    @BeforeEach
    @Step("Initialize WebDriver")         // Allure step
    public void setUp() {
        DriverFactory.initDriver();       // Start driver
        driver = DriverFactory.getDriver(); // Get driver instance

        // Read timeout from config
        String s = ConfigReader.get("explicitWaitSeconds");
        long sec = (s != null && !s.isBlank()) ? Long.parseLong(s) : 15L;

        wait = new WebDriverWait(driver, Duration.ofSeconds(sec)); // Create wait
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.quitDriver();       // Close driver
    }
}
