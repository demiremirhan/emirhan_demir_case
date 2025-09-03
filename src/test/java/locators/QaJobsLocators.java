package locators;

import org.openqa.selenium.By;

public final class QaJobsLocators {
    private QaJobsLocators() {}
    public static final By LOCATION_DROPDOWN = By.cssSelector("#filter-by-location + span.select2 span.select2-selection");
    public static final By LOCATION_FILTER_IST = By.cssSelector("[id*=\"filter-by-location\"][id*=\"Istanbul\"][id*=\"Turkiye\"]");

    public static final By LOCATION_FILTER_LOADER = By.cssSelector(".job-location.istanbul-turkiye");


    public static final By DEPT_DROPDOWN     = By.cssSelector("#filter-by-department + span.select2 span.select2-selection");
    public static final By DEPARTMENT_FILTER_QA = By.cssSelector("[id*=\"Quality Assurance\"]");

    public static final By FILTED_LIST_LOADER = By.xpath("(//div[contains(@class,'position-list-item')][ .//span[contains(.,'Quality Assurance')] and .//div[contains(.,'Istanbul')] ])");


    public static final By JOB_CARDS       = By.cssSelector("[data-role='job-item'], .jobs-list .job-item, .position-list-item");
    public static final By CARD_POSITION   = By.cssSelector("div[data-team='qualityassurance']>div");
    public static final By CARD_DEPARTMENT = By.cssSelector("[class*=\"position-department\"]");
    public static final By CARD_LOCATION   = By.xpath("(//*[@class='position-location text-large'])[1]");
    public static final By CARD_INFO       = By.cssSelector("#jobs-list > div:nth-child(1)");
    public static final By VIEW_ROLE_BTN   = By.cssSelector("#jobs-list > div:nth-child(1) > div > a");
    public static final By RESULT_COUNT =
            By.xpath("//*[contains(normalize-space(.),'Showing') and contains(.,'of')]");
}
