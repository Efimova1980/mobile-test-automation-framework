package com.mobile.framework.core;

import com.mobile.framework.core.scroll.ScrollDirection;
import com.mobile.framework.core.scroll.Scrollers;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePage {

    private final Locator rootLocator;

    protected BasePage(String androidRootXPath, String iosRootXPath) {
        this.rootLocator = Locator.of(androidRootXPath, iosRootXPath);
    }

    public Locator rootLocator() {
        return rootLocator;
    }

    protected View view(String androidXPath, String iosXPath) {
        return new View(rootLocator, Locator.of(androidXPath, iosXPath));
    }

    protected View view(String xpath) {
        return new View(rootLocator, Locator.same(xpath));
    }

    protected View view(Locator locator) {
        return new View(rootLocator, locator);
    }

    public String rootXPath() {
        return rootLocator.xpath();
    }

    /**
     * Scrollable container of the page. Override it when the page scrolls
     * inside a specific container; by default the whole screen is scrolled.
     */
    protected View scrollView() {
        return null;
    }

    /**
     * Scrolls the page for the current platform (Android / iOS).
     * If the page defines {@link #scrollView()}, scrolls inside that container;
     * otherwise scrolls the whole screen.
     *
     * @param direction direction of the content to reveal: DOWN shows content below,
     *                  RIGHT shows content on the right, etc.
     * @param percent   scroll distance as a share of the scroll area size
     *                  (height for UP/DOWN, width for LEFT/RIGHT), (0, 1]
     */
    public void scroll(ScrollDirection direction, double percent) {
        View scrollView = scrollView();
        if (scrollView != null) {
            scrollContainer(scrollView, direction, percent);
        } else {
            scrollScreen(direction, percent);
        }
    }

    /**
     * Always scrolls the whole screen, even if the page defines {@link #scrollView()}.
     * Use it when the gesture must not be bound to the page container.
     * Parameters are the same as in {@link #scroll(ScrollDirection, double)}.
     */
    public void scrollScreen(ScrollDirection direction, double percent) {
        Scrollers.current().scrollScreen(direction, percent);
    }

    /**
     * Scrolls inside the given container. Intended for pages with several scrollable
     * areas: {@link #scrollView()} describes only one (main) container, so each extra
     * container gets its own named page method built on this one, e.g.
     * <pre>{@code
     * public void scrollCarousel(ScrollDirection direction, double percent) {
     *     scrollContainer(carousel(), direction, percent);
     * }
     * }</pre>
     * Parameters are the same as in {@link #scroll(ScrollDirection, double)}.
     */
    protected void scrollContainer(View container, ScrollDirection direction, double percent) {
        Scrollers.current().scrollContainer(container, direction, percent);
    }

    public boolean isDisplayed() {
        try {
            return DriverHolder.driver().findElement(org.openqa.selenium.By.xpath(rootXPath())).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    protected List<View> views(String androidXPath, String iosXPath) {
        Locator childLocator = Locator.of(androidXPath, iosXPath);
        Locator resolvedLocator = rootLocator.child(childLocator);

        int countOfViews = DriverHolder.driver()
                .findElements(org.openqa.selenium.By.xpath(resolvedLocator.xpath()))
                .size();

        List<View> views = new ArrayList<>();

        for (int i = 1; i <= countOfViews; i++) {
            Locator indexedLocator = Locator.of(
                    "(" + resolvedLocator.androidXPath() + ")[" + i + "]",
                    "(" + resolvedLocator.iosXPath() + ")[" + i + "]"
            );

            views.add(new View(Locator.same(""), indexedLocator));
        }

        return views;
    }
}
