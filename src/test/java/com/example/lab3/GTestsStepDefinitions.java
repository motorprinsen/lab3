package com.example.lab3;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GTestsStepDefinitions {

    static WebDriver driver;

    @Before
    public void setupSelenium() {

    }

    @After
    public void teardownSelenium() {

    }

    @Given("SVT Play is available")
    public void svt_play_is_available() {
        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--incognito");
        driver = new ChromeDriver(options);

        driver.get("https://www.svtplay.se/");
    }

    @When("User visits SVT Play")
    public void user_visits_svt_play() {
        driver.manage().window().maximize();
    }

    @Then("The title should be {string}")
    public void the_title_should_be(String expectedTitle) {
        var actualTitle = driver.getTitle();

        assertEquals(expectedTitle, actualTitle, "Unexpected title");
    }

    @Then("The logo should be displayed")
    public void the_logo_should_be_displayed() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("The main link texts should be {string}, {string} and {string}")
    public void the_main_link_texts_should_be_and(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("The link to the availability site {string} should be visible")
    public void the_link_to_the_availability_site_should_be_visible(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("The link text should be {string}")
    public void the_link_text_should_be(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("User navigates to the availability site")
    public void user_navigates_to_the_availability_site() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("The heading should be {string}")
    public void the_heading_should_be(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("User navigates to {string} page")
    public void user_navigates_to_page(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("The number of categories should be {int}")
    public void the_number_of_categories_should_be(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
