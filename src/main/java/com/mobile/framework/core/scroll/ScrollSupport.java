package com.mobile.framework.core.scroll;

import com.mobile.framework.core.View;
import com.mobile.framework.core.DriverHolder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Shared helpers for platform-specific scrollers.
 */
final class ScrollSupport {

    /**
     * Share of the screen size skipped at each edge, so the gesture does not hit
     * the status bar, the navigation bar or the edge "back" gesture area.
     */
    private static final double SCREEN_EDGE_INSET = 0.1;

    private ScrollSupport() {
    }

    static void validatePercent(double percent) {
        if (percent <= 0 || percent > 1) {
            throw new IllegalArgumentException(
                    "percent must be between 0 (exclusive) and 1 (inclusive), was " + percent
            );
        }
    }

    static WebElement waitForVisible(View view) {
        return new WebDriverWait(
                DriverHolder.driver(),
                Duration.ofSeconds(20)
        )
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        view.by()
                ));
    }

    /**
     * Screen rectangle without the edge insets.
     */
    static Rectangle screenArea() {
        Dimension size = DriverHolder.driver().manage().window().getSize();
        int left = (int) (size.getWidth() * SCREEN_EDGE_INSET);
        int top = (int) (size.getHeight() * SCREEN_EDGE_INSET);
        int width = size.getWidth() - 2 * left;
        int height = size.getHeight() - 2 * top;
        // Selenium Rectangle takes (x, y, height, width)
        return new Rectangle(left, top, height, width);
    }
}
