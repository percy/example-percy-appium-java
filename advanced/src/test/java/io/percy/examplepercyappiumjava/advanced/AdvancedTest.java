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
import io.percy.appium.lib.Region;
import io.percy.appium.lib.ScreenshotOptions;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdvancedTest {
  private static final String HUB_URL = "https://hub-cloud.browserstack.com/wd/hub";

  // Stable on-screen element on the Wikipedia Android sample app's Explore
  // feed — the "Featured article" lead card. Used as the target for
  // ignore/consider region selectors so the regions are non-empty and
  // visible on the Percy diff. The previous selector
  // (TextView[@text="Search Wikipedia"]) never matched because that string
  // lives on an EditText, not a TextView, in this app.
  private static final String WIKIPEDIA_LEAD_XPATH =
      "//android.widget.TextView[@text=\"Featured article\"]";

  private static AndroidDriver<AndroidElement> driver;
  private static AppPercy percy;

  @BeforeAll
  static void setUp() throws Exception {
    String platform = System.getenv().getOrDefault("PLATFORM", "android").toLowerCase();
    if (platform.equals("ios")) {
      // TODO(PER-8195): iOS pathway not yet implemented for the advanced
      // example. Add an IOSDriver<IOSElement> branch once the iOS sample app
      // + capabilities are wired up.
      throw new IllegalStateException(
          "PLATFORM=ios is not yet supported in advanced AdvancedTest; "
              + "Android pathway is the current focus. See PER-8195.");
    }

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
    // Actually rotate the device so the snapshot reflects landscape pixels
    // instead of just being tagged "landscape" in metadata.
    driver.rotate(ScreenOrientation.LANDSCAPE);
    try {
      ScreenshotOptions opts = new ScreenshotOptions();
      opts.setDeviceName(System.getenv().getOrDefault("DEVICE", "Google Pixel 6"));
      opts.setOrientation("landscape");
      percy.screenshot("Wikipedia Home — landscape", opts);
    } finally {
      driver.rotate(ScreenOrientation.PORTRAIT);
    }
  }

  @Test
  void exercisesFullscreenAndBars() {
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setFullScreen(true);
    opts.setStatusBarHeight(24);
    opts.setNavBarHeight(0);
    percy.screenshot("Wikipedia Home — fullscreen", opts);
  }

  @Test
  void exercisesIgnoreRegionsViaXpath() {
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setIgnoreRegionXpaths(Arrays.asList(WIKIPEDIA_LEAD_XPATH));
    percy.screenshot("Wikipedia Home — ignore via xpath", opts);
  }

  @Test
  void exercisesIgnoreRegionsViaAppiumElements() {
    AndroidElement el = (AndroidElement) new WebDriverWait(driver, 30).until(
        ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Search Wikipedia")));
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setIgnoreRegionAppiumElements(Arrays.<Object>asList(el));
    percy.screenshot("Wikipedia Home — ignore via appium element", opts);
  }

  @Test
  void exercisesCustomIgnoreRegions() {
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setCustomIgnoreRegions(Arrays.asList(new Region(0, 100, 0, 300)));
    percy.screenshot("Wikipedia Home — custom ignore region", opts);
  }

  @Test
  void exercisesConsiderRegionsViaXpath() {
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setConsiderRegionXpaths(Arrays.asList(WIKIPEDIA_LEAD_XPATH));
    percy.screenshot("Wikipedia Home — consider via xpath", opts);
  }

  @Test
  void exercisesSyncMode() {
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setSync(true);
    // sync blocks until Percy returns the snapshot comparison result, so the
    // SDK hands back a non-null JSONObject (unlike the fire-and-forget async path).
    JSONObject result = percy.screenshot("Wikipedia Home — sync", opts);
    System.out.println("[advanced] sync comparison result: " + result);
    assertNotNull(result, "sync screenshot should return the comparison result");
  }

  @Test
  void exercisesTestCaseAndLabels() {
    ScreenshotOptions opts = new ScreenshotOptions();
    opts.setTestCase("home-smoke");
    opts.setLabels("smoke,appium-java");
    percy.screenshot("Wikipedia Home — test_case + labels", opts);
  }
}
