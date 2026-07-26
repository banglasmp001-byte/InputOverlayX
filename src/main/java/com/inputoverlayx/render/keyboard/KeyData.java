package com.inputoverlayx.render.keyboard;

/**
 * Immutable data record representing a single physical key on the keyboard overlay.
 *
 * <p>Coordinates are in the keyboard's local coordinate system (before scale is applied).
 * They are relative to the top-left corner of the keyboard panel.
 */
public final class KeyData {

    /** Label displayed on the key cap (may use Unicode arrows like ↑). */
    public final String label;

    /** GLFW key constant used for input detection. */
    public final int glfwKey;

    /** X position within the keyboard panel (local units). */
    public final float x;

    /** Y position within the keyboard panel (local units). */
    public final float y;

    /** Width of the key in local units. */
    public final float width;

    /** Height of the key in local units. */
    public final float height;

    /**
     * Constructs a new key definition.
     *
     * @param label    text displayed on the key
     * @param glfwKey  GLFW_KEY_* constant
     * @param x        local X offset from keyboard origin
     * @param y        local Y offset from keyboard origin
     * @param width    key width in local units
     * @param height   key height in local units
     */
    public KeyData(String label, int glfwKey, float x, float y, float width, float height) {
        this.label   = label;
        this.glfwKey = glfwKey;
        this.x       = x;
        this.y       = y;
        this.width   = width;
        this.height  = height;
    }

    @Override
    public String toString() {
        return "KeyData{label='" + label + "', glfw=" + glfwKey + "}";
    }
}
