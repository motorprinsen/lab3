package com.example.lab3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Driver {
    private static WebDriver driver;
    private static boolean isConfigured = false;

    public static WebDriver createDriver() {
        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--incognito");
        driver = new ChromeDriver(options);
        isConfigured = true;

        return driver;
    }
    public static WebDriver driver() {
        return isConfigured ? driver : createDriver();
    }

    public static void quit() {
        driver.quit();
        isConfigured = false;
    }
}
