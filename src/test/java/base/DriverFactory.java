package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import utils.ConfigReader; // read browser/headless

import java.util.Optional;

// WebDriver lifecycle factory (config-driven browser)
public class DriverFactory {
    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>(); // Thread-safe store

    public static WebDriver getDriver() { return TL_DRIVER.get(); } // Current driver

    public static void initDriver() {
        // Read config
        String browser = Optional.ofNullable(ConfigReader.get("browser")).orElse("chrome").toLowerCase(); // chrome|firefox|edge
        boolean headless = Boolean.parseBoolean(Optional.ofNullable(ConfigReader.get("headless")).orElse("false")); // true|false

        switch (browser) {
            case "firefox" -> {
                // Firefox setup
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) { opts.addArguments("--width=1920", "--height=1080"); } // size in headless
                TL_DRIVER.set(new FirefoxDriver(opts));
            }
            case "edge" -> {
                // Edge (Chromium) setup
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless=new", "--window-size=1920,1080"); // headless toggle
                opts.addArguments("--start-maximized"); // maximize
                TL_DRIVER.set(new EdgeDriver(opts));
            }
            default -> {
                // Chrome setup (default)
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new", "--window-size=1920,1080"); // headless toggle
                opts.addArguments("--start-maximized"); // maximize
                TL_DRIVER.set(new ChromeDriver(opts));
            }
        }
    }

    public static void quitDriver() {
        WebDriver d = getDriver(); // Fetch driver
        if (d != null) { d.quit(); TL_DRIVER.remove(); } // Quit & clean
    }
}
