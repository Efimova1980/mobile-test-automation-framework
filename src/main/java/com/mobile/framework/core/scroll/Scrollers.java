package com.mobile.framework.core.scroll;

import com.mobile.framework.core.Platform;
/**
 * Provides cached access to the scroller for the currently selected platform.
 */
public final class Scrollers {

    private static Scroller android;
    private static Scroller ios;

    private Scrollers() {
    }

    public static Scroller current() {
        if (Platform.current().isAndroid()) {
            if (android == null) {
                android = new AndroidScroller();
            }
            return android;
        }

        if (ios == null) {
            ios = new IosScroller();
        }
        return ios;
    }
}
