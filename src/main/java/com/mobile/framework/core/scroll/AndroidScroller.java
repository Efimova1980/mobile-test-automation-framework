package com.mobile.framework.core.scroll;

import com.mobile.framework.core.View;
import com.mobile.framework.core.DriverHolder;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Map;

/**
 * Android scroller based on the UiAutomator2 "mobile: scrollGesture" command.
 *
 * <p>A W3C finger drag (as in {@link IosScroller}) would also work on Android,
 * but the native command is used on purpose:
 * <ul>
 *     <li>it runs inside UiAutomator2 on the device, which handles touch slop and
 *     gesture speed, so the scroll distance is more accurate and there is no fling;</li>
 *     <li>it returns whether the area can be scrolled further, which allows
 *     "scroll until element is found" / "scroll to the end" helpers.</li>
 * </ul>
 */
public class AndroidScroller implements Scroller {

    @Override
    public void scrollContainer(View scrollableView, ScrollDirection direction, double percent) {
        ScrollSupport.validatePercent(percent);

        WebElement element = ScrollSupport.waitForVisible(scrollableView);

        DriverHolder.driver().executeScript(
                "mobile: scrollGesture",
                Map.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "direction", direction.value(),
                        "percent", percent
                )
        );
    }

    @Override
    public void scrollScreen(ScrollDirection direction, double percent) {
        ScrollSupport.validatePercent(percent);

        Rectangle area = ScrollSupport.screenArea();

        DriverHolder.driver().executeScript(
                "mobile: scrollGesture",
                Map.of(
                        "left", area.getX(),
                        "top", area.getY(),
                        "width", area.getWidth(),
                        "height", area.getHeight(),
                        "direction", direction.value(),
                        "percent", percent
                )
        );
    }
}
