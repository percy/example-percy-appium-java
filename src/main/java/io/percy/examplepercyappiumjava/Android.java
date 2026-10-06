package io.percy.examplepercyappiumjava;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;

import io.appium.java_client.android.AndroidDriver;

import io.percy.appium.AppPercy;

public class Android {
    private static AppPercy percy;

    // Hub Url to connect to Automation session
    private static String HUB_URL = "https://hub.browserstack.com/wd/hub";

    public static void main(String[] args) throws MalformedURLException {
        // Browserstack specific capabiilities
        HashMap<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("userName", "<USER>");
        browserstackOptions.put("accessKey", "<USER_AUTH_KEY>");
        browserstackOptions.put("appiumVersion", "2.19.0");
        browserstackOptions.put("deviceName", "Google Pixel 6");
        browserstackOptions.put("osVersion", "12.0");
        browserstackOptions.put("projectName", "First Java Project");

        // Percy Options
        HashMap<String, Object> percyOptions = new HashMap<>();
        percyOptions.put("enabled", true);
        percyOptions.put("ignoreErrors", true);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        // App url we get post uploading in response
        capabilities.setCapability("appium:app", "<APP_URL>");
        capabilities.setCapability("appium:percyOptions", percyOptions);
        capabilities.setCapability("bstack:options", browserstackOptions);

        // Create sessioin
        AndroidDriver driver = new AndroidDriver(new URL(HUB_URL), capabilities);

        // Initialize AppPercy
        percy = new AppPercy(driver);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Take First Screenshot
        percy.screenshot("First Screenshot");


        WebElement searchElement = new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Search Wikipedia")));
        searchElement.click();

        WebElement textInput = new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.elementToBeClickable(AppiumBy.id("org.wikipedia.alpha:id/search_src_text")));
        textInput.sendKeys("Browserstack\n");

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Take Second Screenshot post scrolling
        percy.screenshot("Second Screenshot");

        driver.quit();
    }
}
