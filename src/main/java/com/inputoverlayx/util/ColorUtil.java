package com.inputoverlayx.util;

/**
 * Utility methods for working with ARGB packed-int colors.
 *
 * <p>All colors are stored as {@code 0xAARRGGBB} 32-bit integers, matching
 * Minecraft's convention for HUD rendering.
 */
public final class ColorUtil {

    private ColorUtil() {}

    // -------------------------------------------------------------------------
    // Component extraction
    // -------------------------------------------------------------------------

    public static int alpha(int argb) { return (argb >>> 24) & 0xFF; }
    public static int red(int argb)   { return (argb >>> 16) & 0xFF; }
    public static int green(int argb) { return (argb >>>  8) & 0xFF; }
    public static int blue(int argb)  { return  argb         & 0xFF; }

    // -------------------------------------------------------------------------
    // Component packing
    // -------------------------------------------------------------------------

    public static int argb(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    // -------------------------------------------------------------------------
    // Opacity scaling
    // -------------------------------------------------------------------------

    /**
     * Returns the given ARGB color with its alpha channel multiplied by
     * {@code opacity} (clamped to [0, 1]).
     */
    public static int withOpacity(int argb, float opacity) {
        int a = (int) (alpha(argb) * Math.max(0.0f, Math.min(1.0f, opacity)));
        return argb(a, red(argb), green(argb), blue(argb));
    }

    // -------------------------------------------------------------------------
    // Linear interpolation
    // -------------------------------------------------------------------------

    /**
     * Linearly interpolates between two ARGB colors component-wise.
     *
     * @param from  start color
     * @param to    end color
     * @param t     interpolation factor [0, 1] — 0 returns {@code from}
     */
    public static int lerp(int from, int to, float t) {
        t = Math.max(0.0f, Math.min(1.0f, t));
        int a = (int) (alpha(from) + (alpha(to) - alpha(from)) * t);
        int r = (int) (red(from)   + (red(to)   - red(from))   * t);
        int g = (int) (green(from) + (green(to) - green(from)) * t);
        int b = (int) (blue(from)  + (blue(to)  - blue(from))  * t);
        return argb(a, r, g, b);
    }

    // -------------------------------------------------------------------------
    // Additive brightening (for glow simulation)
    // -------------------------------------------------------------------------

    /**
     * Brightens each RGB channel by {@code amount} (0–255), clamping at 255.
     * Alpha is preserved.
     */
    public static int brighten(int argb, int amount) {
        int r = Math.min(255, red(argb)   + amount);
        int g = Math.min(255, green(argb) + amount);
        int b = Math.min(255, blue(argb)  + amount);
        return argb(alpha(argb), r, g, b);
    }

    // -------------------------------------------------------------------------
    // Glow helper
    // -------------------------------------------------------------------------

    /**
     * Returns a color suitable for a "glow" render pass — same hue as the
     * source color but with reduced alpha so it can be drawn slightly oversized
     * behind the real element.
     */
    public static int glowColor(int argb, float intensity) {
        int a = (int) (alpha(argb) * Math.max(0.0f, Math.min(1.0f, intensity)) * 0.6f);
        return argb(a, red(argb), green(argb), blue(argb));
    }
}
