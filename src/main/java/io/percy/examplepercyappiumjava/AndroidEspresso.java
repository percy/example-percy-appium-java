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

public class AndroidEspresso {
  private static AppPercy percy;

  // Hub Url to connect to Automation session
  private static String HUB_URL = "https://hub.browserstack.com/wd/hub";

  public static void main(String[] args) throws MalformedURLException {
    // W3C capabilities format for Espresso driver
    HashMap<String, Object> browserstackOptions = new HashMap<>();
    browserstackOptions.put("userName", "<USER>");
    browserstackOptions.put("accessKey", "<ACCESS_KEY>");
    browserstackOptions.put("projectName", "First Java Project");
    browserstackOptions.put("buildName", "Espresso Driver Test");
    browserstackOptions.put("espressoServer", "<ESPRESSO_SERVER_URL>");

    HashMap<String, Object> percyOptions = new HashMap<>();
    percyOptions.put("enabled", true);
    percyOptions.put("ignoreErrors", true);

    DesiredCapabilities capabilities = new DesiredCapabilities();

    // Appium W3C capabilities
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("appium:automationName", "Espresso");
    capabilities.setCapability("appium:app", "<APP_URL>");
    capabilities.setCapability("appium:deviceName", "Samsung Galaxy S22 Ultra");
    capabilities.setCapability("appium:platformVersion", "12.0");

    // BrowserStack options
    capabilities.setCapability("bstack:options", browserstackOptions);

    // Percy options
    capabilities.setCapability("percy:options", percyOptions);

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
