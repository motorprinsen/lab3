package com.example.lab3;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.example.lab3.Driver.driver;
import static org.junit.jupiter.api.Assertions.*;

public class GTestsStepDefinitions {

    private static final int toWait = 5;

    @When("a user visits SVT Play")
    public void a_user_visits_svt_play() {
        assertTrue(driver().getTitle().endsWith("SVT Play"), "We are not at SVT Play");
    }

    @Then("the title should be {string}")
    public void the_title_should_be(String title) {
        assertEquals(title, driver().getTitle(), "Incorrect title is shown");
    }

    @Then("the logo should be displayed")
    public void the_logo_should_be_displayed() {
        var logo = driver().findElement(By.tagName("svg"));

        assertTrue(logo.isDisplayed(), "No logo is displayed");
    }

    @Then("the main link texts should be {string}, {string} and {string}")
    public void the_main_link_texts_should_be_and(String expectedStartText, String expectedProgramsText, String expectedChannelsText) {
        // Actual values are Pascal cased but then upper-cased through CSS
        expectedStartText = expectedStartText.toUpperCase();
        var startTextXpath = "//li[@type='start']/a";
        var startText = driver().findElement(By.xpath(startTextXpath));

        assertEquals(expectedStartText, startText.getText(), "Wrong text on 'Start' link");

        expectedProgramsText = expectedProgramsText.toUpperCase();
        var expectedProgramsXpath = "//li[@type='programs']/a";
        var programsText = driver().findElement(By.xpath(expectedProgramsXpath));

        assertEquals(expectedProgramsText, programsText.getText(), "Wrong text on 'Program' link");

        expectedChannelsText = expectedChannelsText.toUpperCase();
        var expectedChannelsXpath = "//li[@type='channels']/a";
        var channelsText = driver().findElement(By.xpath(expectedChannelsXpath));

        assertEquals(expectedChannelsText, channelsText.getText(), "Wrong text on 'Kanaler' link");
    }

    @Then("the link to the availability page should be visible")
    public void the_link_to_the_availability_page_should_be_visible() {
        var linkXpath = "//a[@href='https://kontakt.svt.se/guide/tillganglighet']";
        var link = driver().findElement(By.xpath(linkXpath));

        assertTrue(link.isDisplayed(), "No availability link displayed");
    }

    @Then("the link text should be {string}")
    public void the_link_text_should_be(String expectedLinkText) {
        var linkXpath = "//a[@href='https://kontakt.svt.se/guide/tillganglighet']";
        var link = driver().findElement(By.xpath(linkXpath));

        // Selenium cannot CSS select using compound class names,
        // so we work around it by concatenating the names
        var linkSpan = link.findElement(By.cssSelector(".sc-343fed33-3.dmRxHt"));
        assertEquals(expectedLinkText, linkSpan.getText(), "Wrong text on availability link");
    }

    @When("navigates to the availability page")
    public void navigates_to_the_availability_page() {
        var linkXpath = "//a[@href='https://kontakt.svt.se/guide/tillganglighet']";

        // Navigate to the availability site
        driver().findElement(By.xpath(linkXpath)).click();
    }

    @Then("the heading should be {string}")
    public void the_heading_should_be(String expectedHeading) {
        // Grab the heading
        var heading = driver().findElement(By.tagName("h1"));
        assertEquals(expectedHeading, heading.getText(), "Wrong heading on availability page");
    }

    @When("navigates to {string} page")
    public void navigates_to_page(String page) {
        // Since lab 2, SVT has added a plural 's' on the main links
        page += "s";
        // Navigate to the page
        var linkXpath = "//li[@type='" + page.toLowerCase() + "']/a";
        driver().findElement(By.xpath(linkXpath)).click();
    }

    @Then("the number of categories displayed should be {int}")
    public void the_number_of_categories_displayed_should_be(Integer expectedCategories) {
        var categoriesXpath = "//article";

        // Get the actual categories
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(categoriesXpath)));
        var actualCategories = driver().findElements(By.xpath(categoriesXpath)).size();

        assertEquals(expectedCategories, actualCategories, "Wrong number of categories displayed");
    }

    @When("changes the {string} cookie consent setting")
    public void changes_the_cookie_consent_setting(String setting) {
        // The name of the cookie we're interested in
        var cookieName = "cookie-consent-1";

        // Try to get the raw cookie
        Cookie rawCookie = null;
        try {
            rawCookie = new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                    .until(
                            (ExpectedCondition<Cookie>) webDriver -> {
                                // We either return the actual cookie or null if not found
                                return driver().manage().getCookieNamed(cookieName);
                            }
                    );

        } catch (Exception e) {
            fail("Exception while getting initial cookie");
        }

        if (rawCookie == null) {
            fail("Failed to get initial cookie");
        }

        // Convert the cookie to json
        var cookie = rawCookie.toJson();

        // We accepted all cookies in the setup
        var expectedInitialAdStorageConsent = true;
        var adStorageConsent = false;

        try {
            // Json-parse the cookie and get the "ad_storage" value
            adStorageConsent = (boolean) new org.json.JSONObject(cookie.get("value").toString()).get(setting);
        } catch (JSONException e) {
            // Invalid json in the cookie. We handle it in the assertion.
        }

        assertEquals(expectedInitialAdStorageConsent, adStorageConsent,
                "Wrong default 'ad storage' consent setting");

        // Navigate to the Settings page
        var settingsLinkXpath = "//a[@class='sc-5b00349a-0 hwpvwu sc-87f10045-4 imzlFR' and @href='/installningar']";
        driver().findElement(By.xpath(settingsLinkXpath)).click();

        // Open the cookie consent dialog
        var cookieButtonClass = ".sc-5b00349a-2.hLpVUw";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cookieButtonClass)));
        driver().findElement(By.cssSelector(cookieButtonClass)).click();

        // Wait for ad storage option to show up
        var adStorageId = "play_cookie_consent_ad_storage";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(adStorageId)));

        // Toggle the ad storage consent switch
        var consentSwitchXpath = "//label[@for='play_cookie_consent_ad_storage']";
        driver().findElement(By.xpath(consentSwitchXpath)).click();

        // Save the new cookie preferences
        var saveButtonSelector = ".sc-5b00349a-2.fuGbXH.sc-4f221cd2-9.hEiUxP";
        driver().findElement(By.cssSelector(saveButtonSelector)).click();

        // The modal takes a couple of seconds to close after accepting
        var modalXpath = "//div[@data-rt='cookie-consent-modal']";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(modalXpath)));


    }

    @Then("the {string} setting should be changed")
    public void the_setting_should_be_changed(String setting) {
        // Note: The cookie handling really annoys me...
        //       Sometimes it works flawlessly without any waits or anything,
        //       other times the cookie cannot be found.
        //       I don't know if it's because of my aging computer or if it's
        //       something I'm doing wrong.
        //
        //       Anyhow, the trick that seems to work best is to construct a custom
        //       ExpectedCondition to check if the cookie is there.
        //       And to do a very ugly Thread.sleep()...

        try {
            Thread.sleep(toWait * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // The name of the cookie we're interested in
        var cookieName = "cookie-consent-1";

        // Try to get the updated cookie
        Cookie rawCookie = null;
        try {
            rawCookie = new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                    .until(
                            (ExpectedCondition<Cookie>) webDriver -> {
                                // We either return the actual cookie or null if not found
                                return driver().manage().getCookieNamed(cookieName);
                            }
                    );

        } catch (Exception e) {
            fail("Exception while getting updated cookie");
        }

        if (rawCookie == null) {
            fail("Failed to get updated cookie");
        }

        // Convert the updated cookie
        var cookie = rawCookie.toJson();

        var expectedAdStorageConsent = false;
        var adStorageConsentAfterToggle = false;

        try {
            adStorageConsentAfterToggle = (boolean) new org.json.JSONObject(cookie.get("value").toString()).get(setting);
        } catch (JSONException e) {
            // Invalid json in the cookie. We handle it in the assertion.
            adStorageConsentAfterToggle = true;
        }

        assertEquals(expectedAdStorageConsent, adStorageConsentAfterToggle,
                "Wrong 'ad storage' consent setting after toggling");
    }

    @When("sets a child protection pin code")
    public void sets_a_child_protection_pin_code() {
        // Navigate to the Settings page.
        driver().findElement(By.linkText("Inställningar".toUpperCase())).click();

        // Toggle the child protection switch
        var childProtectionSwitchXpath = "//label[@data-rt='child-protection-switch']";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(childProtectionSwitchXpath)));
        driver().findElement(By.xpath(childProtectionSwitchXpath)).click();

        // The pin code must be four digits.
        // If we only enter three, the "Activate" button shouldn't be available.
        var input = driver().findElement(By.id("play_settings-parental-control-input"));
        input.sendKeys("111");

        var button = driver().findElement(By.xpath("//button[@data-rt='child-protection-password-activate']"));
        assertFalse(button.isEnabled(), "Button is enabled when only three digits have been entered");

        // Set the last digit of the code and verify that the button is enabled
        input.sendKeys("1");
        assertTrue(button.isEnabled(), "Button is not enabled although a valid code has been entered");

        // Save the code
        button.click();
    }

    @Then("age restricted programs do not play")
    public void age_restricted_programs_do_not_play() {
        // Search for a known program that has age restrictions.
        var searchText = driver().findElement(By.xpath("//input[@data-rt='combobox-input']"));
        searchText.sendKeys("detektiven från beledweyne");
        searchText.submit();

        // Navigate to the program page
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main/section/div/ul/li[1]/article/a")));
        var seriesLink = driver().findElement(By.xpath("//main/section/div/ul/li[1]/article/a"));
        seriesLink.click();

        // Try to play the first episode
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@data-rt='top-area-play-button']")));
        var playFirstEpisode = driver().findElement(By.xpath("//a[@data-rt='top-area-play-button']"));
        playFirstEpisode.click();

        // The "Unsuitable for children" dialog should appear
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alertdialog']/h2")));
        var alert = driver().findElement(By.xpath("//div[@role='alertdialog']/h2"));

        var expectedWarningText = "Detta program är olämpligt för barn";
        assertEquals(expectedWarningText, alert.getText(),
                "Could play age restricted program although a code has been set");
    }

    @When("disables autoplay")
    public void disables_autoplay() {
        // Get hold of the LocalStorage implementation
        var webStorage = (WebStorage) new Augmenter().augment(driver());
        var localStorage = webStorage.getLocalStorage();

        var expectedInitialAutoplayEnabled = true;
        var autoplayEnabled = false;

        // Grab the autoplay setting from LocalStorage
        try {
            autoplayEnabled = (boolean) new org.json.JSONObject(localStorage.getItem("redux")).getJSONObject("settings").get("autoplay");
        } catch (JSONException e) {
            // Invalid json in the entry. We handle it in the assertion.
        }

        assertEquals(expectedInitialAutoplayEnabled, autoplayEnabled, "Wrong default autoplay setting");

        // Navigate to the Settings page.
        driver().findElement(By.linkText("Inställningar".toUpperCase())).click();

        // Toggle the autoplay switch
        var autoplaySwitchClassName = "jmdfsN";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className(autoplaySwitchClassName)));
        driver().findElement(By.className(autoplaySwitchClassName)).click();
    }

    @Then("autoplay should be disabled")
    public void autoplay_should_be_disabled() {
        // Get hold of the LocalStorage implementation
        var webStorage = (WebStorage) new Augmenter().augment(driver());
        var localStorage = webStorage.getLocalStorage();

        var expectedAutoplayEnabled = false;
        var autoplayEnabledAfterToggle = false;

        // Grab the autoplay setting from LocalStorage again
        try {
            autoplayEnabledAfterToggle = (boolean) new org.json.JSONObject(localStorage.getItem("redux")).getJSONObject("settings").get("autoplay");
        } catch (JSONException e) {
            // Invalid json in the entry. We handle it in the assertion.
            autoplayEnabledAfterToggle = true;
        }

        assertEquals(expectedAutoplayEnabled, autoplayEnabledAfterToggle,
                "Wrong autoplay setting after toggling");
    }

    @When("selects a program with visual aid")
    public void selects_a_program_with_visual_aid() {
        // Navigate to the Programs page
        driver().findElement(By.linkText("Program".toUpperCase())).click();

        // Find and click the "Visual aid" link
        var visualAidLinkText = "Syntolkat";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.linkText(visualAidLinkText)));
        driver().findElement(By.linkText(visualAidLinkText)).click();

        // Find the first available program and navigate to it
        var firstShowXpath = "//main/descendant::article[1]";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath((firstShowXpath))));
        driver().findElement(By.xpath(firstShowXpath)).click();
    }

    @Then("a link with the text {string} should be shown")
    public void a_link_with_the_text_should_be_shown(String linkText) {
        // Check that the option to view the program without aid id displayed.
        // This indicates that the current program is in "visual aid" mode.
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
        var noVisualAid = driver().findElement(By.linkText(linkText));

        assertTrue(noVisualAid.isDisplayed(),
                "No 'visual aid' available although we selected a program that has it");
    }

    @When("searches for without entering a search term")
    public void searches_for_without_entering_a_search_term() {
        // Click/submit an empty search
        var searchFormXpath = "//button[@type='submit']";
        driver().findElement(By.xpath(searchFormXpath)).click();
    }

    @Then("the result {string} should be shown")
    public void the_result_should_be_shown(String expectedResultText) {
        // Verify that no programs were found
        var mainElementId = "play_main-content";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(mainElementId)));
        var mainElement = driver().findElement(By.id(mainElementId));

        var paragraphXpath = "//section/div/p[1]";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(paragraphXpath)));
        var actualResultText = mainElement.findElement(By.xpath(paragraphXpath)).getText();

        assertEquals(expectedResultText, actualResultText, "Empty search criteria yielded results");
    }
}
