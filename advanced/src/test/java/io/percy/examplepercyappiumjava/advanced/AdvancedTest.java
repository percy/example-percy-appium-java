package io.percy.examplepercyappiumjava.advanced;

// PER-8195 Phase 2 — appium-java advanced example.
// Each @Test exercises one row of the App Percy / Appium Native matrix.
// See ../../../../matrix.yml for the canonical mapping.
//
// Run against the BrowserStack App Automate hub. Requires AA_USERNAME,
// AA_ACCESS_KEY, APP env vars. See ../README.md.

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.percy.appium.AppPercy;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdvancedTest {
  private static final String HUB_URL = "https://hub-cloud.browserstack.com/wd/hub";
  private static AndroidDriver<AndroidElement> driver;
  private static AppPercy percy;

  @BeforeAll
  static void setUp() throws Exception {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("browserstack.user", System.getenv("AA_USERNAME"));
    caps.setCapability("browserstack.key", System.getenv("AA_ACCESS_KEY"));
    caps.setCapability("app", System.getenv("APP"));
    caps.setCapability("device", System.getenv().getOrDefault("DEVICE", "Google Pixel 6"));
    caps.setCapability("os_version", System.getenv().getOrDefault("OS_VERSION", "12.0"));
    caps.setCapability("project", System.getenv().getOrDefault("BROWSERSTACK_PROJECT_NAME", "Percy Appium Java Advanced"));
    caps.setCapability("build", System.getenv().getOrDefault("BROWSERSTACK_BUILD_NAME", "Advanced Java Appium"));
    caps.setCapability("percy.enabled", "true");
    caps.setCapability("percy.ignoreErrors", "true");

    driver = new AndroidDriver<>(new URL(HUB_URL), caps);
    percy = new AppPercy(driver);
    Thread.sleep(5000);
  }

  @AfterAll
  static void tearDown() {
    if (driver != null) driver.quit();
  }

  @Test
  void exercisesBaselineScreenshot() {
    percy.screenshot("Wikipedia Home");
  }

  @Test
  void exercisesDeviceNameAndOrientation() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("device_name", System.getenv().getOrDefault("DEVICE", "Google Pixel 6"));
    opts.put("orientation", "landscape");
    percy.screenshot("Wikipedia Home — landscape", opts);
  }

  @Test
  void exercisesFullscreenAndBars() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("fullscreen", true);
    opts.put("status_bar_height", 24);
    opts.put("nav_bar_height", 0);
    percy.screenshot("Wikipedia Home — fullscreen", opts);
  }

  @Test
  void exercisesIgnoreRegionsViaXpath() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("ignore_regions_xpaths",
        Arrays.asList("//android.widget.TextView[@text=\"Search Wikipedia\"]"));
    percy.screenshot("Wikipedia Home — ignore via xpath", opts);
  }

  @Test
  void exercisesIgnoreRegionsViaAppiumElements() {
    AndroidElement el = (AndroidElement) new WebDriverWait(driver, 30).until(
        ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Search Wikipedia")));
    Map<String, Object> opts = new HashMap<>();
    opts.put("ignore_region_appium_elements", Arrays.asList(el));
    percy.screenshot("Wikipedia Home — ignore via appium element", opts);
  }

  @Test
  void exercisesCustomIgnoreRegions() {
    Map<String, Object> region = new HashMap<>();
    region.put("top", 0);
    region.put("bottom", 100);
    region.put("left", 0);
    region.put("right", 300);
    Map<String, Object> opts = new HashMap<>();
    opts.put("custom_ignore_regions", Arrays.asList(region));
    percy.screenshot("Wikipedia Home — custom ignore region", opts);
  }

  @Test
  void exercisesConsiderRegionsViaXpath() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("consider_regions_xpaths",
        Arrays.asList("//android.widget.TextView[@text=\"Search Wikipedia\"]"));
    percy.screenshot("Wikipedia Home — consider via xpath", opts);
  }

  @Test
  void exercisesSyncMode() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("sync", true);
    // sync blocks until Percy returns the snapshot comparison result, so the
    // SDK hands back a non-null JSONObject (unlike the fire-and-forget async path).
    JSONObject result = percy.screenshot("Wikipedia Home — sync", opts);
    System.out.println("[advanced] sync comparison result: " + result);
    assertNotNull(result, "sync screenshot should return the comparison result");
  }

  @Test
  void exercisesTestCaseAndLabels() {
    Map<String, Object> opts = new HashMap<>();
    opts.put("test_case", "home-smoke");
    opts.put("labels", "smoke,appium-java");
    percy.screenshot("Wikipedia Home — test_case + labels", opts);
  }
}
