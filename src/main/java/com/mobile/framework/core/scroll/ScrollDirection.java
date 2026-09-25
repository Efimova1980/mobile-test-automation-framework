package com.mobile.framework.core.scroll;

public enum ScrollDirection {
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right");

    private final String value;

    ScrollDirection(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
