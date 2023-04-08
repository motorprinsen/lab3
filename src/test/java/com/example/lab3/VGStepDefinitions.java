package com.example.lab3;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class VGStepDefinitions {

    private static WebDriver driver;
    private static final int toWait = 5;
    private static final String svtPlayUrl = "https://www.svtplay.se/";

    @Before
    public static void setup() {
        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--incognito");
        driver = new ChromeDriver(options);
    }

    private static void acceptCookieConsentDialog() {
        // The modal takes a couple of seconds to show up
        var modalXpath = "//div[@data-rt='cookie-consent-modal']";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(modalXpath)));

        // Accept the default settings
        var modal = driver.findElement(By.xpath(modalXpath));
        var consentButton = modal.findElement(By.xpath(".//button[text() = 'Acceptera alla']"));
        consentButton.click();

        // The modal takes a couple of seconds to close after accepting
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(modalXpath)));
    }

    @After
    public static void teardown() {
        driver.quit();
    }

    @Given("SVT Play is available")
    public void svt_play_is_available() {
        driver.get(svtPlayUrl);
        driver.manage().window().maximize();
    }

    @Given("a user navigates to start page")
    public void a_user_navigates_to_start_page() {
        driver.get(svtPlayUrl);
    }

    @Given("accepts the cookie consent dialog")
    public void accepts_the_cookie_consent_dialog() {
        acceptCookieConsentDialog();
    }

    @When("a user clicks on the newsletter link \\({string})")
    public void a_user_clicks_on_the_newsletter_link(String linkText) {
        // Navigate to the newsletter page.
        // We locate the nested span and then finds its parent,
        // which is the link
        driver.findElement(By.xpath("//span[text()=\"" + linkText + "\"]"))
                .findElement(By.xpath("./..")).click();
    }
    @When("clicks the sign-up button \\({string}) without entering an email address")
    public void clicks_the_sign_up_button_without_entering_an_email_address(String caption) {
        driver.findElement(By.xpath("//button[text()=\"" + caption + "\"]")).click();
    }

    @Then("the error message {string} should show")
    public void the_error_message_should_show(String message) {
        var errorMessage = driver.findElement(By.id("error-messages"))
                .findElement(By.xpath("p[text()=\"" + message + "\"]"));

        assertTrue(errorMessage.isDisplayed(), "No '" + message + "' message is shown");
    }

    @When("enter an email address")
    public void enter_an_email_address() {
        driver.findElement(By.id("email")).sendKeys("email@example.com");
    }
    @When("clicks the sign-up button \\({string}) without ticking the consent box")
    public void clicks_the_sign_up_button_without_ticking_the_consent_box(String caption) {
        driver.findElement(By.xpath("//button[text()=\"" + caption + "\"]")).click();
    }

    @When("a user clicks on {string}")
    public void a_user_clicks_on(String linkText) {
        driver.findElement(By.linkText(linkText.toUpperCase())).click();
    }
    @Then("the first listed program should have a start time before the current time")
    public void the_first_listed_program_should_have_a_start_time_before_the_current_time() {
        var programsClassName = "sc-c63b48f0-1";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className(programsClassName)));
        var firstProgram = driver.findElements(By.className(programsClassName)).get(0);
        var rawStartTime = firstProgram.findElement(By.xpath("li[1]/p/span[@aria-hidden='true']")).getText();

        // Parse the time string
        var startTime = LocalTime.parse(rawStartTime, DateTimeFormatter.ofPattern("HH:mm"));

        // Get the current time
        var currentTime = LocalTime.now();

        // Compare the target time with the current time
        var result = startTime.compareTo(currentTime);

        assertTrue(result <= 0, "Start time is in the future");
    }

    @Then("all programs should be listed correctly according to the first letter in the program name")
    public void all_programs_should_be_listed_correctly_according_to_the_first_letter_in_the_program_name() {
        var sectionsClassName = "sc-73d4a946-0";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className(sectionsClassName)));
        var sections = driver.findElements(By.className(sectionsClassName));

        for (var section : sections) {
            var startingLetter = section.findElement(By.xpath("h2")).getText().toLowerCase();
            var program = section.findElement(By.xpath("ul/li/a")).getText().toLowerCase();

            var errorMessage = "Program is in wrong section";
            if (startingLetter.equals("#")) {
                assertTrue(Character.isDigit(program.toCharArray()[0]), errorMessage);
            } else {
                assertTrue(program.startsWith(startingLetter), errorMessage);
            }
        }
    }

    @When("a user searches for {string}")
    public void a_user_searches_for(String program) {
        driver.findElement(By.name("q")).sendKeys(program);

        var searchFormXpath = "//button[@type='submit']";
        driver.findElement(By.xpath(searchFormXpath)).click();
    }
    @Then("the first listed program should be {string}")
    public void the_first_listed_program_should_be(String program) {
        var listingId = "play_main-content";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(listingId)));
        var mainSection = driver.findElement(By.id(listingId));

        // This is one of the things I find really quirky with Selenium:
        // I implemented the test, it passed and I moved on to the next test.
        // When all tests are written, I run all the tests in sequence, and then this test starts to fail.
        // The xpath that worked 30 minutes ago doesn't work anymore. And explicitly wait doesn't help either.
        // But it works fine when I debug (of course).
        // So the only way to get it to pass is to do the ugly, and not recommended, Thread.sleep().
        // And I still have no clue to why it suddenly started failing...
        try {
            Thread.sleep(toWait * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var firstProgramXpath = "section/div/ul/li[1]/article/a/div[2]/h2";
        var firstProgramName = mainSection.findElement(By.xpath(firstProgramXpath)).getText();

        assertTrue(firstProgramName.toLowerCase().equals(program.toLowerCase()), "Unexpected search result");
    }

    @Then("no results should be shown")
    public void no_results_should_be_shown() {
        var emptySearchResultXpath = "//main/section/div[@data-rt='search-header-empty-result']";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(emptySearchResultXpath)));

        var emptySearchResult = driver.findElement(By.xpath(emptySearchResultXpath));

        assertTrue(emptySearchResult.isDisplayed(), "Found search results when we shouldn't have");
    }
    @Then("the text {string} should be shown")
    public void the_text_should_be_shown(String expectedMessage) {
        var resultMessageXpath = "//main/section/div[@data-rt='search-header-empty-result']/p[1]";
        var actualMessage = driver.findElement(By.xpath(resultMessageXpath)).getText();

        assertEquals(expectedMessage, actualMessage);
    }

    @When("a user clicks on the first available program")
    public void a_user_clicks_on_the_first_available_program() {
        var recommendedProgramXpath = "//main/div/section/article/div/a";
        driver.findElement(By.xpath(recommendedProgramXpath)).click();
    }
    @When("starts the video player")
    public void starts_the_video_player() {
        var playButtonXpath = "//a[@role='button']";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(playButtonXpath)));

        driver.findElement(By.xpath(playButtonXpath)).click();
    }
    @When("clicks the fullscreen button \\({string})")
    public void clicks_the_fullscreen_button(String caption) {
        // Ugly, but we have to wait a bit for the video to start playing.
        // And we (or at least I) can't explicitly wait for the fullscreen
        // button to become visible, since it is only visible when moving the mouse...
        try {
            Thread.sleep(toWait * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var fullscreenXpath = "//button[@title='" + caption + "']";
        var fullscreenButton = driver.findElement(By.xpath(fullscreenXpath));
        var action = new Actions(driver);

        // Hover over the button to display it
        action.moveToElement(fullscreenButton).perform();

        fullscreenButton.click();
    }
    @Then("the player should be shown fullscreen")
    public void the_player_should_be_shown_fullscreen() {
        var executor = (JavascriptExecutor) driver;
        var fullscreen = (WebElement) executor.executeScript("var element = document.fullscreenElement; return element");

        // fullscreen is something if in fullscreen, otherwise it is null
        assertNotNull(fullscreen, "We should be in fullscreen but we're not");
    }

    @When("a user clicks on a program in the {string} section")
    public void a_user_clicks_on_a_program_in_the_section(String linkText) {
        driver.findElement(By.linkText(linkText.toUpperCase())).click();

        var programClassname = "sc-b6440fda-0";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className(programClassname)));
        driver.findElement(By.className(programClassname)).click();
    }
    @Then("the program should display an overlay indicating the time left to view it")
    public void the_program_should_display_an_overlay_indicating_the_time_left_to_view_it() {
        var overlayClassname = "sc-88951c95-0";
        new WebDriverWait(driver, Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className(overlayClassname)));
        var overlay = driver.findElement(By.className(overlayClassname));

        assertTrue(overlay.isDisplayed(), "No overlay of time remaining is displayed");
    }
}
