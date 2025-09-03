package locators;

import org.openqa.selenium.By;

public final class HomeLocators {
    private HomeLocators() {}
    public static final By BRAND = By.cssSelector("img[alt='insider_logo']");
    public static final By COMPANY_MENU = By.xpath("//a[contains(text(), 'Company')]");
    public static final By CAREERS_LINK = By.xpath("//a[text()='Careers']");
}
