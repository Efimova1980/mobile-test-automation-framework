# Mobile Test Automation Framework (Android / iOS)

A team project focused on **building a mobile test automation framework** (Java, Appium) for Android and iOS — not on testing a specific product.
A demo shop app is used only as a test target to develop and debug the framework.

## Tech stack

- Java
- Appium (UiAutomator2 / XCUITest) + Selenium WebDriver
- TestNG
- Gradle (Kotlin DSL)
- ADB / xcrun simctl

## Platform status

- **Android** — implemented and tested on an emulator.
- **iOS** — the framework layer is implemented (driver setup, device management, scrolling, test group)
  but not tested: no macOS / iOS device available. Page locators for iOS are not filled in yet.

## Application under test

[Sauce Labs My Demo App](https://github.com/saucelabs/my-demo-app-android) — a demo shop app.

- **Android**: [source](https://github.com/saucelabs/my-demo-app-android),
  [releases](https://github.com/saucelabs/my-demo-app-android/releases).
  The APK (`mda-2.2.0-25.apk`, release 2.2.0) is included in the repository.
- **iOS**: [source](https://github.com/saucelabs/my-demo-app-ios),
  [releases](https://github.com/saucelabs/my-demo-app-ios/releases).
  To run on iOS, download a simulator build and set `IOS_APP_PATH`, `IOS_BUNDLE_ID`
  and `IOS_DEVICE_NAME` in `MobileConfig`, then fill in the iOS page locators.

## Architecture

The framework follows the **Page Object Model**. Each screen is a page object; element locators are built as *page root locator + element locator*.

- **Core** — app-independent infrastructure:
  - Appium driver lifecycle (session start/stop) for the selected platform
  - locator building (base page + base UI element, "root + child" composition)
  - waits: every action waits for its element; a page can define what "loaded" means (e.g. the first item of a list that loads asynchronously)
  - `core.device` — device management before a driver session (app install/uninstall, state checks): platform-independent `DeviceManager` with Android (ADB) and iOS (simctl) implementations
  - `core.scroll` — scrolling: platform-independent `Scroller` with Android (UiAutomator2 `scrollGesture`) and iOS (W3C finger drag) implementations; scrolls a container or the whole screen in four directions by a given percent
  - logging and configuration
- **Components** — reusable screen parts (e.g. product card built on a shared product item base)
- **Pages** — app screens: catalog, product details, etc.
- **Tests** — scenarios built on pages and components; a platform-independent base test runs them on the platform selected for the run

## Starting point

The project started with a minimal base: the **locator building mechanism** (base view + locator composition) and **two-platform support** (Android / iOS) with a shared driver holder. Everything below was built on top of it.

## My contribution

- Built the **cross-platform core** of the framework:
  - `MobileConfig` — configuration foundation for both platforms (Android / iOS)
  - `DeviceManager` interface and `DeviceManagers` — platform-independent device management layer
  - `AndroidDeviceManager` — sends commands to Android devices via ADB (tested on emulator)
  - `IosDeviceManager` — iOS implementation on the same interface (written, not tested: no iOS device available)
  - `DeviceCommandRunner` — runs device shell commands with a timeout, without pipe deadlocks
  - platform-independent `DriverManager` — sets up the matching Appium driver for the selected platform
- Implemented **scrolling** as a platform-independent `Scroller` with Android and iOS implementations (container or whole screen, four directions)
- Implemented **waits**: actions wait for their elements, pages wait until their content is loaded
- Implemented **test run logging** (event log, device logs and a driver screenshot on test failure)
- Designed and implemented **pages and reusable components**: catalog page, product details page, product card, review dialog, and a shared product item base
- Organized **test runs**: platform-independent `BaseTest`, test groups per platform, framework self-tests that run before UI tests
- Implemented **UI tests for the product flow**: catalog display and scroll → open product → product details (color, quantity, rating, add-to-cart button state, screen scroll)

## Guidelines for using the framework

Based on debugging the framework on the demo app.

The framework's architecture relies on one key rule: **every screen has a unique, stable root locator**. Without it, the "root + child" locator model doesn't work as intended.

**Writing pages**
- Each page needs a **stable root (parent) locator**: an element that is always on the screen and belongs to this screen only. Locators of page elements are built from this root.
- Don't use dynamic content (list items, data-dependent elements) as a root: in Android scrollable lists, elements that scroll off screen are removed from the UI tree.
- If the page content loads after the screen opens (e.g. a list), override `loadedIndicator()` so the page can wait for it.
- If the page scrolls inside a container, override `scrollView()`; otherwise `scroll()` scrolls the whole screen. For extra containers, add named page methods built on `scrollContainer()`.

**Requirements for the app under test**
- Each screen should have a root container with a **unique, stable ID** (resource-id / accessibility id). Element IDs inside can repeat across screens, since locators are resolved from the root.

## Running the tests

Prerequisites: JDK, Android SDK (ADB), Appium server with UiAutomator2 driver, a running emulator or connected device. For iOS: macOS with Xcode, a booted simulator and the XCUITest driver.

Run from the project root (macOS / Linux: `./gradlew`, Windows PowerShell: `.\gradlew.bat`):

| Command | What runs |
|---|---|
| `./gradlew test` | framework self-tests first, then Android tests |
| `./gradlew test -Pplatform=ios` | framework self-tests first, then iOS tests |
| `./gradlew frameworkTest` | framework self-tests only (no device needed) |

UI tests run only if the framework self-tests pass.

On failure, a screenshot is saved to `build/screenshots/`; test reports are in `build/reports/tests/`.
