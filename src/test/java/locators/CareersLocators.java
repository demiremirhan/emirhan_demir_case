package locators;

import org.openqa.selenium.By;

public final class CareersLocators {
    private CareersLocators() {}
    public static final By TEAMS_BLOCK     = By.cssSelector("#career-find-our-calling");
    public static final By LOCATIONS_BLOCK = By.cssSelector("#career-our-location");
    public static final By LIFE_BLOCK      = By.cssSelector(".elementor-widget-wrap.elementor-element-populated.e-swiper-container");
    public static final By QA_PAGE_LINK    = By.xpath("(//*[text()=('Find your dream job')])[2]");
}
