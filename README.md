# Mobile Test Automation Framework (Android / iOS)

A team project focused on **building a mobile test automation framework** (Java, Appium) for Android and iOS — not on testing a specific product.
A demo shop app is used only as a test target to develop and debug the framework.

## Tech stack

- Java
- Appium (UiAutomator2) + Selenium WebDriver
- TestNG
- Gradle (Kotlin DSL)
- ADB

## Architecture

The framework follows the **Page Object Model**. Each screen is a page object; element locators are built as *page root locator + element locator*.

- **Core** — app-independent infrastructure:
  - Appium driver lifecycle (session start/stop)
  - device/emulator management (app install, state checks): platform-independent layer with Android (ADB) and iOS implementations
  - locator building (base page + base UI element, "root + child" composition)
  - gestures (scroll)
  - logging and configuration
- **Components** — reusable screen parts shared by several pages (e.g. product card and cart item share one base component)
- **Pages** — app screens: catalog, product details, etc.
- **Tests** — scenarios built on pages and components

## Starting point

The project started with a minimal base: the **locator building mechanism** (base view + locator composition) and **two-platform support** (Android / iOS) with a shared driver holder. Everything below was built on top of it.

## My contribution

- Built the **cross-platform core** of the framework:
  - `MobileConfig` — configuration foundation for both platforms (Android / iOS)
  - `DeviceManager` interface and `DeviceManagers` — platform-independent device management layer
  - `AndroidDeviceManager` — sends commands to Android devices via ADB (tested on emulator)
  - `IOSDeviceManager` — iOS implementation on the same interface (written, not tested: no iOS device available)
  - `DeviceCommandRunner` — runs device shell commands
  - platform-independent `DriverManager` — sets up the matching Appium driver for the selected platform
- Implemented **test run logging** (event log + screenshot on test failure)
- Implemented **gestures** (scroll) as part of the framework core
- Designed and implemented **pages and reusable components**: catalog page, product details page, product card, review dialog, and a shared product item base (also reused by the cart item component)
- Implemented **UI tests for the product flow**: catalog display and scroll → open product → product details (color, quantity, rating, add-to-cart button state)

## Guidelines for using the framework

Based on debugging the framework on the demo app.

The framework's architecture relies on one key rule: **every screen has a unique, stable root locator**. Without it, the "root + child" locator model doesn't work as intended.

**Writing pages**
- Each page needs a **stable root (parent) locator**: an element that is always on the screen and belongs to this screen only. Locators of page elements are built from this root.
- Don't use dynamic content (list items, data-dependent elements) as a root: in Android scrollable lists, elements that scroll off screen are removed from the UI tree.
**Requirements for the app under test**
- Each screen should have a root container with a **unique, stable ID** (resource-id / accessibility id). Element IDs inside can repeat across screens, since locators are resolved from the root.

## Running the tests

Prerequisites: JDK, Android SDK (ADB), Appium server with UiAutomator2 driver, a running emulator or connected device.

```
./gradlew test
```

On Windows: `.\gradlew.bat test`
