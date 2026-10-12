package io.percy.examplepercyappiumjava;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;

import io.percy.appium.AppPercy;

public class Ios {
    private static AppPercy percy;

    // Hub Url to connect to Automation session
    private static String HUB_URL = "https://hub.browserstack.com/wd/hub";

    public static void main(String[] args) throws MalformedURLException {
        // Browserstack specific capabiilities
        HashMap<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("userName", "<USER>");
        browserstackOptions.put("accessKey", "<USER_AUTH_KEY>");
        browserstackOptions.put("appiumVersion", "2.19.0");
        browserstackOptions.put("deviceName", "iPhone 14");
        browserstackOptions.put("osVersion", "16");
        browserstackOptions.put("projectName", "First Java Project");

        // Percy Options
        HashMap<String, Object> percyOptions = new HashMap<>();
        percyOptions.put("enabled", true);
        percyOptions.put("ignoreErrors", true);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        // App url we get post uploading in response
        capabilities.setCapability("appium:app", "<APP_URL>");
        capabilities.setCapability("appium:percyOptions", percyOptions);
        capabilities.setCapability("bstack:options", browserstackOptions);

        // Create sessioin
        IOSDriver driver = new IOSDriver(new URL(HUB_URL), capabilities);

        // Initialize AppPercy
        percy = new AppPercy(driver);

        // Take First Screenshot
        percy.screenshot("First Screenshot");

        // Find element and click to change screen
        WebElement textButton = new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Text Button")));
        textButton.click();

        // Find textInput and send some data to it
        WebElement textInput = new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Text Input")));
        textInput.sendKeys("hello@percy.io\n");

        // Take Second Screenshot Post screen update
        percy.screenshot("Second Screenshot");
        driver.quit();
    }
}
