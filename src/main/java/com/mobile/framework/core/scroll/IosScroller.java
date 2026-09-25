package com.mobile.framework.core.scroll;

import com.mobile.framework.core.View;
import com.mobile.framework.core.DriverHolder;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

/**
 * iOS scroller based on a W3C finger drag.
 * XCUITest "mobile: scroll" has no distance parameter, so the drag is used instead.
 */
public class IosScroller implements Scroller {

    /**
     * Duration of the finger movement.
     * Slow enough to avoid a fling (inertial scrolling).
     */
    private static final Duration DRAG_DURATION = Duration.ofMillis(600);

    @Override
    public void scrollContainer(View scrollableView, ScrollDirection direction, double percent) {
        ScrollSupport.validatePercent(percent);

        Rectangle area = ScrollSupport.insetArea(ScrollSupport.waitForVisible(scrollableView).getRect());
        drag(area, direction, percent);
    }

    @Override
    public void scrollScreen(ScrollDirection direction, double percent) {
        ScrollSupport.validatePercent(percent);

        drag(ScrollSupport.screenArea(), direction, percent);
    }

    /**
     * Drags a finger through the center of the area. The finger moves opposite
     * to the scroll direction: to scroll DOWN (see content below) it moves up.
     */
    private static void drag(Rectangle area, ScrollDirection direction, double percent) {
        int centerX = area.getX() + area.getWidth() / 2;
        int centerY = area.getY() + area.getHeight() / 2;
        int halfDistanceX = (int) (area.getWidth() * percent / 2);
        int halfDistanceY = (int) (area.getHeight() * percent / 2);

        int startX = centerX;
        int startY = centerY;
        int endX = centerX;
        int endY = centerY;

        switch (direction) {
            case DOWN -> {
                startY = centerY + halfDistanceY;
                endY = centerY - halfDistanceY;
            }
            case UP -> {
                startY = centerY - halfDistanceY;
                endY = centerY + halfDistanceY;
            }
            case RIGHT -> {
                startX = centerX + halfDistanceX;
                endX = centerX - halfDistanceX;
            }
            case LEFT -> {
                startX = centerX - halfDistanceX;
                endX = centerX + halfDistanceX;
            }
        }

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drag = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(
                        Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(
                        DRAG_DURATION, PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        DriverHolder.driver().perform(List.of(drag));
    }
}
