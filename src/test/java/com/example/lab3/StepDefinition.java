package com.example.lab3;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinition {

    static WebDriver driver;

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
}
