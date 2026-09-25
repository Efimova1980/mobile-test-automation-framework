package com.mobile.framework.core.scroll;

import com.mobile.framework.core.View;
/**
 * Defines scroll gestures for platform-specific scrollers.
 *
 * <p>{@code direction} is the direction of the content to reveal: DOWN shows content below,
 * RIGHT shows content on the right, etc.
 * <p>{@code percent} is the scroll distance as a share of the scroll area size
 * (height for UP/DOWN, width for LEFT/RIGHT), (0, 1].
 */
public interface Scroller {

    /**
     * Scrolls inside the given scrollable container.
     */
    void scrollContainer(View scrollableView, ScrollDirection direction, double percent);

    /**
     * Scrolls the whole screen (no scrollable element needed).
     */
    void scrollScreen(ScrollDirection direction, double percent);
}
