package com.inputoverlayx.theme;

/**
 * Enumeration of all built-in themes available in InputOverlayX.
 */
public enum Theme {
    DEFAULT("Default"),
    DARK("Dark"),
    LIGHT("Light"),
    PURPLE("Purple"),
    BLUE("Blue"),
    EMERALD("Emerald"),
    RED("Red"),
    GLASS("Glass"),
    NEON("Neon"),
    FROST("Frost"),
    CARBON("Carbon"),
    MINIMAL("Minimal"),
    TRANSPARENT("Transparent");

    private final String displayName;

    Theme(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
