package com.inputoverlayx.theme;

import com.inputoverlayx.config.InputOverlayXConfig;

/**
 * Applies a {@link Theme} preset to an {@link InputOverlayXConfig} instance,
 * overriding the relevant color and visual style fields.
 *
 * <p>Each theme defines:
 * <ul>
 *   <li>Background color (ARGB)</li>
 *   <li>Border color (ARGB)</li>
 *   <li>Pressed key color (ARGB)</li>
 *   <li>Font color (ARGB)</li>
 *   <li>Hover color (ARGB)</li>
 *   <li>Mouse click color (ARGB)</li>
 *   <li>Shadow / glow toggles</li>
 * </ul>
 */
public final class ThemePresets {

    private ThemePresets() {}

    /**
     * Applies the given theme to the config object in-place.
     * The caller is responsible for persisting the config afterwards.
     */
    public static void apply(Theme theme, InputOverlayXConfig cfg) {
        switch (theme) {
            case DEFAULT -> applyDefault(cfg);
            case DARK    -> applyDark(cfg);
            case LIGHT   -> applyLight(cfg);
            case PURPLE  -> applyPurple(cfg);
            case BLUE    -> applyBlue(cfg);
            case EMERALD -> applyEmerald(cfg);
            case RED     -> applyRed(cfg);
            case GLASS   -> applyGlass(cfg);
            case NEON    -> applyNeon(cfg);
            case FROST   -> applyFrost(cfg);
            case CARBON  -> applyCarbon(cfg);
            case MINIMAL -> applyMinimal(cfg);
            case TRANSPARENT -> applyTransparent(cfg);
        }
    }

    // -------------------------------------------------------------------------
    // Theme definitions
    // -------------------------------------------------------------------------

    private static void applyDefault(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xCC1A1A2E);
        c.setKeyboardBorderColor(0xFF4A4A8A);
        c.setKeyboardPressedColor(0xFF6060FF);
        c.setKeyboardHoverColor(0xFF3A3A6A);
        c.setKeyboardFontColor(0xFFE0E0FF);
        c.setMouseBgColor(0xCC1A1A2E);
        c.setMouseBorderColor(0xFF4A4A8A);
        c.setMouseClickColor(0xFF6060FF);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(1.5f);
        c.setKeyboardCornerRadius(6.0f);
    }

    private static void applyDark(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xE5080808);
        c.setKeyboardBorderColor(0xFF222222);
        c.setKeyboardPressedColor(0xFF444444);
        c.setKeyboardHoverColor(0xFF1A1A1A);
        c.setKeyboardFontColor(0xFFCCCCCC);
        c.setMouseBgColor(0xE5080808);
        c.setMouseBorderColor(0xFF222222);
        c.setMouseClickColor(0xFF555555);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(1.0f);
        c.setKeyboardCornerRadius(4.0f);
    }

    private static void applyLight(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xEFF5F5F5);
        c.setKeyboardBorderColor(0xFFCCCCCC);
        c.setKeyboardPressedColor(0xFF999999);
        c.setKeyboardHoverColor(0xFFE0E0E0);
        c.setKeyboardFontColor(0xFF222222);
        c.setMouseBgColor(0xEFF5F5F5);
        c.setMouseBorderColor(0xFFCCCCCC);
        c.setMouseClickColor(0xFF888888);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(1.0f);
        c.setKeyboardCornerRadius(6.0f);
    }

    private static void applyPurple(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xCC1A0A2E);
        c.setKeyboardBorderColor(0xFF8A00FF);
        c.setKeyboardPressedColor(0xFFAA44FF);
        c.setKeyboardHoverColor(0xFF3A1A5A);
        c.setKeyboardFontColor(0xFFEECCFF);
        c.setMouseBgColor(0xCC1A0A2E);
        c.setMouseBorderColor(0xFF8A00FF);
        c.setMouseClickColor(0xFFAA44FF);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(true);
        c.setKeyboardBorderThickness(2.0f);
        c.setKeyboardCornerRadius(8.0f);
    }

    private static void applyBlue(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xCC0A1A3A);
        c.setKeyboardBorderColor(0xFF0080FF);
        c.setKeyboardPressedColor(0xFF44AAFF);
        c.setKeyboardHoverColor(0xFF1A2A5A);
        c.setKeyboardFontColor(0xFFCCEEFF);
        c.setMouseBgColor(0xCC0A1A3A);
        c.setMouseBorderColor(0xFF0080FF);
        c.setMouseClickColor(0xFF44AAFF);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(true);
        c.setKeyboardBorderThickness(2.0f);
        c.setKeyboardCornerRadius(7.0f);
    }

    private static void applyEmerald(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xCC0A2E1A);
        c.setKeyboardBorderColor(0xFF00CC66);
        c.setKeyboardPressedColor(0xFF44FF99);
        c.setKeyboardHoverColor(0xFF1A4A2A);
        c.setKeyboardFontColor(0xFFCCFFEE);
        c.setMouseBgColor(0xCC0A2E1A);
        c.setMouseBorderColor(0xFF00CC66);
        c.setMouseClickColor(0xFF44FF99);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(true);
        c.setKeyboardBorderThickness(2.0f);
        c.setKeyboardCornerRadius(7.0f);
    }

    private static void applyRed(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xCC2E0A0A);
        c.setKeyboardBorderColor(0xFFCC0022);
        c.setKeyboardPressedColor(0xFFFF4455);
        c.setKeyboardHoverColor(0xFF4A1A1A);
        c.setKeyboardFontColor(0xFFFFCCCC);
        c.setMouseBgColor(0xCC2E0A0A);
        c.setMouseBorderColor(0xFFCC0022);
        c.setMouseClickColor(0xFFFF4455);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(true);
        c.setKeyboardBorderThickness(2.0f);
        c.setKeyboardCornerRadius(5.0f);
    }

    private static void applyGlass(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0x40FFFFFF);
        c.setKeyboardBorderColor(0x80FFFFFF);
        c.setKeyboardPressedColor(0x99FFFFFF);
        c.setKeyboardHoverColor(0x55FFFFFF);
        c.setKeyboardFontColor(0xFFFFFFFF);
        c.setMouseBgColor(0x40FFFFFF);
        c.setMouseBorderColor(0x80FFFFFF);
        c.setMouseClickColor(0xAAFFFFFF);
        c.setKeyboardShadow(false);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(1.0f);
        c.setKeyboardCornerRadius(10.0f);
    }

    private static void applyNeon(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xCC000011);
        c.setKeyboardBorderColor(0xFF00FFCC);
        c.setKeyboardPressedColor(0xFF00FFFF);
        c.setKeyboardHoverColor(0xFF001122);
        c.setKeyboardFontColor(0xFF00FFCC);
        c.setMouseBgColor(0xCC000011);
        c.setMouseBorderColor(0xFF00FFCC);
        c.setMouseClickColor(0xFF00FFFF);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(true);
        c.setKeyboardBorderThickness(2.0f);
        c.setKeyboardCornerRadius(3.0f);
    }

    private static void applyFrost(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0x99DDEEFF);
        c.setKeyboardBorderColor(0xFFAADDFF);
        c.setKeyboardPressedColor(0xFFBBEEFF);
        c.setKeyboardHoverColor(0x77CCEEFF);
        c.setKeyboardFontColor(0xFF113355);
        c.setMouseBgColor(0x99DDEEFF);
        c.setMouseBorderColor(0xFFAADDFF);
        c.setMouseClickColor(0xFFBBEEFF);
        c.setKeyboardShadow(false);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(1.5f);
        c.setKeyboardCornerRadius(10.0f);
    }

    private static void applyCarbon(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0xEE111111);
        c.setKeyboardBorderColor(0xFF333333);
        c.setKeyboardPressedColor(0xFF666666);
        c.setKeyboardHoverColor(0xFF222222);
        c.setKeyboardFontColor(0xFFAAAAAA);
        c.setMouseBgColor(0xEE111111);
        c.setMouseBorderColor(0xFF333333);
        c.setMouseClickColor(0xFF777777);
        c.setKeyboardShadow(true);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(1.0f);
        c.setKeyboardCornerRadius(4.0f);
    }

    private static void applyMinimal(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0x00000000);
        c.setKeyboardBorderColor(0x44FFFFFF);
        c.setKeyboardPressedColor(0x88FFFFFF);
        c.setKeyboardHoverColor(0x22FFFFFF);
        c.setKeyboardFontColor(0xCCFFFFFF);
        c.setMouseBgColor(0x00000000);
        c.setMouseBorderColor(0x44FFFFFF);
        c.setMouseClickColor(0x88FFFFFF);
        c.setKeyboardShadow(false);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(0.5f);
        c.setKeyboardCornerRadius(2.0f);
    }

    private static void applyTransparent(InputOverlayXConfig c) {
        c.setKeyboardBgColor(0x00000000);
        c.setKeyboardBorderColor(0x00000000);
        c.setKeyboardPressedColor(0x66FFFFFF);
        c.setKeyboardHoverColor(0x11FFFFFF);
        c.setKeyboardFontColor(0xAAFFFFFF);
        c.setMouseBgColor(0x00000000);
        c.setMouseBorderColor(0x00000000);
        c.setMouseClickColor(0x66FFFFFF);
        c.setKeyboardShadow(false);
        c.setKeyboardGlow(false);
        c.setKeyboardBorderThickness(0.0f);
        c.setKeyboardCornerRadius(0.0f);
    }
}
