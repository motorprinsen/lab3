package com.example.lab3;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.example.lab3.Driver.driver;

public class Background {
    private static final int toWait = 5;
    private static final String svtPlayUrl = "https://www.svtplay.se/";

    private static void acceptCookieConsentDialog() {
        // The modal takes a couple of seconds to show up
        var modalXpath = "//div[@data-rt='cookie-consent-modal']";
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(modalXpath)));

        // Accept the default settings
        var modal = driver().findElement(By.xpath(modalXpath));
        var consentButton = modal.findElement(By.xpath(".//button[text() = 'Acceptera alla']"));
        consentButton.click();

        // The modal takes a couple of seconds to close after accepting
        new WebDriverWait(driver(), Duration.ofSeconds(toWait))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(modalXpath)));
    }

    @After
    public static void teardown() {
        // A new driver is created when it is needed, so we don't have to hook @Before.

        // Invoke our "custom" instance method that quits the driver and flags
        // that a new instance must be created next time someone uses it
        Driver.quit();
    }

    @Given("SVT Play is available")
    public void svt_play_is_available() {
        driver().get(svtPlayUrl);
        driver().manage().window().maximize();
    }

    @Given("a user navigates to start page")
    public void a_user_navigates_to_start_page() {
        driver().get(svtPlayUrl);
    }

    @Given("accepts the cookie consent dialog")
    public void accepts_the_cookie_consent_dialog() {
        acceptCookieConsentDialog();
    }
}
