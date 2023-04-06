package com.example.lab3;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GTestsStepDefinitions {

    private static WebDriver driver;
    private static final int toWait = 5;

    //@BeforeAll
    public static void setup() {
        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--incognito");
        driver = new ChromeDriver(options);

        driver.get("https://www.svtplay.se/");
        driver.manage().window().maximize();

        acceptCookieConsentDialog();
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

    //@AfterAll
    public static void teardown() {
        driver.quit();
    }
    @Given("SVT Play is available")
    public void svt_play_is_available() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("a user visits SVT Play")
    public void a_user_visits_svt_play() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the title should be {string}")
    public void the_title_should_be(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the logo should be displayed")
    public void the_logo_should_be_displayed() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the main link texts should be {string}, {string} and {string}")
    public void the_main_link_texts_should_be_and(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the link to the availability page {string} should be visible")
    public void the_link_to_the_availability_page_should_be_visible(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the link text should be {string}")
    public void the_link_text_should_be(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("navigates to the availability page {string}")
    public void navigates_to_the_availability_page(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the heading should be {string}")
    public void the_heading_should_be(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("navigates to {string} page")
    public void navigates_to_page(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the number of categories displayed should be {int}")
    public void the_number_of_categories_displayed_should_be(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
