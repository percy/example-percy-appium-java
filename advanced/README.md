# Advanced Percy + Appium-Java

This directory exercises the full applicable Percy SDK feature surface for `io.percy:percy-appium-app`. See the basic example at the repo root for the minimum integration.

## What this example covers

A JUnit 5 suite (`src/test/java/io/percy/examplepercyappiumjava/advanced/AdvancedTest.java`) where each `@Test` exercises one row of the App Percy / Appium Native matrix (source of truth: [`matrix.yml`](./matrix.yml)): device_name override, orientation, fullscreen + status_bar_height + nav_bar_height, ignore regions via xpath / appium element / custom bounding box, consider regions via xpath, sync mode, test_case + labels, build metadata via env, PERCY_BRANCH / PERCY_COMMIT override.

Web-only options (widths, percyCSS, minHeight, scope, discovery, domTransformation, responsiveSnapshotCapture, readiness preset, devicePixelRatio, browsers) marked `N/A` in `matrix.yml` — there's no DOM in native App Percy.

## Run locally

Requires BrowserStack App Automate hub credentials and an app uploaded to the BrowserStack cloud:

```bash
cd advanced
make install
export AA_USERNAME="<browserstack username>"
export AA_ACCESS_KEY="<browserstack access key>"
export APP="bs://<your hashed app id>"
export PERCY_TOKEN="<your project token>"      # do NOT commit this
make test
```

## CI note

The advanced CI job is `workflow_dispatch`-only — App Percy CI requires a real BrowserStack device session, which forks/Dependabot cannot access. See `.github/workflows/advanced.yml`.

## Coverage matrix

States: `Covered` / `N/A — <reason>` / `Planned` / `Deprecated`. Source of truth is [`matrix.yml`](./matrix.yml).
