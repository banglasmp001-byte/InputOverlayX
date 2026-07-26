package com.inputoverlayx.render.keyboard;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines the physical layout of a full 104/105-key ANSI keyboard.
 *
 * <p>Coordinates are expressed in a normalized unit system where one standard
 * key unit = {@link #UNIT} pixels at scale 1.0.  The layout maps directly to a
 * real desktop keyboard (ISO-like proportions).
 *
 * <p>A separate compact mode scales everything down uniformly; no keys are
 * removed — only the size changes.
 */
public final class KeyboardLayout {

    /** Size of one standard key in logical pixels (scale 1.0). */
    public static final float UNIT = 18.0f;
    /** Gap between adjacent keys. */
    public static final float GAP  = 1.5f;

    // Row Y positions (top of each row group, relative to keyboard origin)
    private static final float ROW_FN      = 0.0f;
    private static final float ROW_NUM     = UNIT + GAP * 3;
    private static final float ROW_TAB     = ROW_NUM  + UNIT + GAP;
    private static final float ROW_CAPS    = ROW_TAB  + UNIT + GAP;
    private static final float ROW_LSHIFT  = ROW_CAPS + UNIT + GAP;
    private static final float ROW_CTRL    = ROW_LSHIFT + UNIT + GAP;

    // Navigation cluster X offset (after main keys + gap)
    private static final float NAV_X = (UNIT + GAP) * 14.5f + GAP * 4;
    // Numpad X offset (after nav cluster)
    private static final float NUM_X = NAV_X + (UNIT + GAP) * 3 + GAP * 3;

    private KeyboardLayout() {}

    /**
     * Builds and returns the full list of {@link KeyData} objects representing
     * the complete 104-key layout.  The list is immutable.
     */
    public static List<KeyData> buildFullLayout() {
        List<KeyData> keys = new ArrayList<>(110);

        float u = UNIT;
        float g = GAP;

        // -----------------------------------------------------------------------
        // Fn row: Escape + F1-F12 + Print Screen / Scroll Lock / Pause Break
        // -----------------------------------------------------------------------
        float rx = 0;
        keys.add(new KeyData("Esc",   GLFW.GLFW_KEY_ESCAPE,        rx, ROW_FN, u, u)); rx += u + g * 2;
        keys.add(new KeyData("F1",    GLFW.GLFW_KEY_F1,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F2",    GLFW.GLFW_KEY_F2,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F3",    GLFW.GLFW_KEY_F3,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F4",    GLFW.GLFW_KEY_F4,            rx, ROW_FN, u, u)); rx += u + g * 2;
        keys.add(new KeyData("F5",    GLFW.GLFW_KEY_F5,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F6",    GLFW.GLFW_KEY_F6,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F7",    GLFW.GLFW_KEY_F7,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F8",    GLFW.GLFW_KEY_F8,            rx, ROW_FN, u, u)); rx += u + g * 2;
        keys.add(new KeyData("F9",    GLFW.GLFW_KEY_F9,            rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F10",   GLFW.GLFW_KEY_F10,           rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F11",   GLFW.GLFW_KEY_F11,           rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("F12",   GLFW.GLFW_KEY_F12,           rx, ROW_FN, u, u)); rx += u + g * 2;
        keys.add(new KeyData("PrtSc", GLFW.GLFW_KEY_PRINT_SCREEN,  rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("Scr",   GLFW.GLFW_KEY_SCROLL_LOCK,   rx, ROW_FN, u, u)); rx += u + g;
        keys.add(new KeyData("Brk",   GLFW.GLFW_KEY_PAUSE,         rx, ROW_FN, u, u));

        // -----------------------------------------------------------------------
        // Number row: ` 1-0 - = Backspace
        // -----------------------------------------------------------------------
        rx = 0;
        keys.add(new KeyData("`",   GLFW.GLFW_KEY_GRAVE_ACCENT,     rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("1",   GLFW.GLFW_KEY_1,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("2",   GLFW.GLFW_KEY_2,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("3",   GLFW.GLFW_KEY_3,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("4",   GLFW.GLFW_KEY_4,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("5",   GLFW.GLFW_KEY_5,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("6",   GLFW.GLFW_KEY_6,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("7",   GLFW.GLFW_KEY_7,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("8",   GLFW.GLFW_KEY_8,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("9",   GLFW.GLFW_KEY_9,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("0",   GLFW.GLFW_KEY_0,                rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("-",   GLFW.GLFW_KEY_MINUS,            rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("=",   GLFW.GLFW_KEY_EQUAL,            rx, ROW_NUM, u,   u)); rx += u + g;
        keys.add(new KeyData("Bksp",GLFW.GLFW_KEY_BACKSPACE,        rx, ROW_NUM, u * 2.0f - g, u));

        // -----------------------------------------------------------------------
        // Tab row: Tab Q-P [ ] \
        // -----------------------------------------------------------------------
        rx = 0;
        keys.add(new KeyData("Tab", GLFW.GLFW_KEY_TAB,             rx, ROW_TAB, u * 1.5f - g * 0.5f, u)); rx += u * 1.5f + g * 0.5f;
        keys.add(new KeyData("Q",   GLFW.GLFW_KEY_Q,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("W",   GLFW.GLFW_KEY_W,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("E",   GLFW.GLFW_KEY_E,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("R",   GLFW.GLFW_KEY_R,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("T",   GLFW.GLFW_KEY_T,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("Y",   GLFW.GLFW_KEY_Y,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("U",   GLFW.GLFW_KEY_U,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("I",   GLFW.GLFW_KEY_I,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("O",   GLFW.GLFW_KEY_O,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("P",   GLFW.GLFW_KEY_P,               rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("[",   GLFW.GLFW_KEY_LEFT_BRACKET,    rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("]",   GLFW.GLFW_KEY_RIGHT_BRACKET,   rx, ROW_TAB, u, u)); rx += u + g;
        keys.add(new KeyData("\\",  GLFW.GLFW_KEY_BACKSLASH,       rx, ROW_TAB, u * 1.5f - g * 0.5f, u));

        // -----------------------------------------------------------------------
        // Caps row: Caps A-L ; ' Enter
        // -----------------------------------------------------------------------
        rx = 0;
        keys.add(new KeyData("Caps", GLFW.GLFW_KEY_CAPS_LOCK,      rx, ROW_CAPS, u * 1.75f - g * 0.75f, u)); rx += u * 1.75f + g * 0.25f;
        keys.add(new KeyData("A",   GLFW.GLFW_KEY_A,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("S",   GLFW.GLFW_KEY_S,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("D",   GLFW.GLFW_KEY_D,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("F",   GLFW.GLFW_KEY_F,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("G",   GLFW.GLFW_KEY_G,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("H",   GLFW.GLFW_KEY_H,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("J",   GLFW.GLFW_KEY_J,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("K",   GLFW.GLFW_KEY_K,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("L",   GLFW.GLFW_KEY_L,               rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData(";",   GLFW.GLFW_KEY_SEMICOLON,       rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("'",   GLFW.GLFW_KEY_APOSTROPHE,      rx, ROW_CAPS, u, u)); rx += u + g;
        keys.add(new KeyData("Enter",GLFW.GLFW_KEY_ENTER,           rx, ROW_CAPS, u * 2.25f - g * 0.25f, u));

        // -----------------------------------------------------------------------
        // Shift row: LShift Z-M , . / RShift
        // -----------------------------------------------------------------------
        rx = 0;
        keys.add(new KeyData("⇧ Shift", GLFW.GLFW_KEY_LEFT_SHIFT,  rx, ROW_LSHIFT, u * 2.25f - g * 0.25f, u)); rx += u * 2.25f + g * 0.75f;
        keys.add(new KeyData("Z",   GLFW.GLFW_KEY_Z,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("X",   GLFW.GLFW_KEY_X,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("C",   GLFW.GLFW_KEY_C,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("V",   GLFW.GLFW_KEY_V,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("B",   GLFW.GLFW_KEY_B,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("N",   GLFW.GLFW_KEY_N,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("M",   GLFW.GLFW_KEY_M,               rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData(",",   GLFW.GLFW_KEY_COMMA,           rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData(".",   GLFW.GLFW_KEY_PERIOD,          rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("/",   GLFW.GLFW_KEY_SLASH,           rx, ROW_LSHIFT, u, u)); rx += u + g;
        keys.add(new KeyData("Shift ⇧", GLFW.GLFW_KEY_RIGHT_SHIFT, rx, ROW_LSHIFT, u * 2.75f - g * 0.75f, u));

        // -----------------------------------------------------------------------
        // Bottom row: LCtrl Win LAlt Space RAlt Menu RCtrl
        // -----------------------------------------------------------------------
        rx = 0;
        keys.add(new KeyData("Ctrl",  GLFW.GLFW_KEY_LEFT_CONTROL,  rx, ROW_CTRL, u * 1.25f - g * 0.25f, u)); rx += u * 1.25f + g * 0.75f;
        keys.add(new KeyData("Win",   GLFW.GLFW_KEY_LEFT_SUPER,    rx, ROW_CTRL, u, u)); rx += u + g;
        keys.add(new KeyData("Alt",   GLFW.GLFW_KEY_LEFT_ALT,      rx, ROW_CTRL, u * 1.25f - g * 0.25f, u)); rx += u * 1.25f + g * 0.75f;
        // Space bar spans ~6.25 units
        float spaceW = u * 6.25f - g * 5.25f;
        keys.add(new KeyData("Space", GLFW.GLFW_KEY_SPACE,         rx, ROW_CTRL, spaceW, u)); rx += spaceW + g;
        keys.add(new KeyData("Alt",   GLFW.GLFW_KEY_RIGHT_ALT,     rx, ROW_CTRL, u * 1.25f - g * 0.25f, u)); rx += u * 1.25f + g * 0.75f;
        keys.add(new KeyData("Menu",  GLFW.GLFW_KEY_MENU,          rx, ROW_CTRL, u, u)); rx += u + g;
        keys.add(new KeyData("Ctrl",  GLFW.GLFW_KEY_RIGHT_CONTROL, rx, ROW_CTRL, u * 1.25f - g * 0.25f, u));

        // -----------------------------------------------------------------------
        // Navigation cluster (Insert / Home / PgUp / Delete / End / PgDn)
        // -----------------------------------------------------------------------
        float ny = ROW_NUM;
        keys.add(new KeyData("Ins",  GLFW.GLFW_KEY_INSERT,         NAV_X,           ny, u, u));
        keys.add(new KeyData("Home", GLFW.GLFW_KEY_HOME,           NAV_X + u + g,   ny, u, u));
        keys.add(new KeyData("PgUp", GLFW.GLFW_KEY_PAGE_UP,        NAV_X + (u+g)*2, ny, u, u));
        ny += u + g;
        keys.add(new KeyData("Del",  GLFW.GLFW_KEY_DELETE,         NAV_X,           ny, u, u));
        keys.add(new KeyData("End",  GLFW.GLFW_KEY_END,            NAV_X + u + g,   ny, u, u));
        keys.add(new KeyData("PgDn", GLFW.GLFW_KEY_PAGE_DOWN,      NAV_X + (u+g)*2, ny, u, u));

        // Arrow keys
        float ay = ROW_LSHIFT;
        keys.add(new KeyData("↑",   GLFW.GLFW_KEY_UP,              NAV_X + u + g,   ay, u, u));
        ay = ROW_CTRL;
        keys.add(new KeyData("←",   GLFW.GLFW_KEY_LEFT,            NAV_X,           ay, u, u));
        keys.add(new KeyData("↓",   GLFW.GLFW_KEY_DOWN,            NAV_X + u + g,   ay, u, u));
        keys.add(new KeyData("→",   GLFW.GLFW_KEY_RIGHT,           NAV_X + (u+g)*2, ay, u, u));

        // -----------------------------------------------------------------------
        // Numpad
        // -----------------------------------------------------------------------
        float npY = ROW_NUM;
        keys.add(new KeyData("NmLk", GLFW.GLFW_KEY_NUM_LOCK,       NUM_X,           npY, u, u));
        keys.add(new KeyData("NP/",  GLFW.GLFW_KEY_KP_DIVIDE,      NUM_X + u + g,   npY, u, u));
        keys.add(new KeyData("NP*",  GLFW.GLFW_KEY_KP_MULTIPLY,    NUM_X + (u+g)*2, npY, u, u));
        keys.add(new KeyData("NP-",  GLFW.GLFW_KEY_KP_SUBTRACT,    NUM_X + (u+g)*3, npY, u, u));
        npY += u + g;
        keys.add(new KeyData("7",    GLFW.GLFW_KEY_KP_7,           NUM_X,           npY, u, u));
        keys.add(new KeyData("8",    GLFW.GLFW_KEY_KP_8,           NUM_X + u + g,   npY, u, u));
        keys.add(new KeyData("9",    GLFW.GLFW_KEY_KP_9,           NUM_X + (u+g)*2, npY, u, u));
        // NP+ spans two rows
        keys.add(new KeyData("NP+",  GLFW.GLFW_KEY_KP_ADD,         NUM_X + (u+g)*3, npY, u, u * 2 + g));
        npY += u + g;
        keys.add(new KeyData("4",    GLFW.GLFW_KEY_KP_4,           NUM_X,           npY, u, u));
        keys.add(new KeyData("5",    GLFW.GLFW_KEY_KP_5,           NUM_X + u + g,   npY, u, u));
        keys.add(new KeyData("6",    GLFW.GLFW_KEY_KP_6,           NUM_X + (u+g)*2, npY, u, u));
        npY += u + g;
        keys.add(new KeyData("1",    GLFW.GLFW_KEY_KP_1,           NUM_X,           npY, u, u));
        keys.add(new KeyData("2",    GLFW.GLFW_KEY_KP_2,           NUM_X + u + g,   npY, u, u));
        keys.add(new KeyData("3",    GLFW.GLFW_KEY_KP_3,           NUM_X + (u+g)*2, npY, u, u));
        // NP Enter spans two rows
        keys.add(new KeyData("Enter",GLFW.GLFW_KEY_KP_ENTER,       NUM_X + (u+g)*3, npY, u, u * 2 + g));
        npY += u + g;
        // NP 0 is double-wide
        keys.add(new KeyData("0",    GLFW.GLFW_KEY_KP_0,           NUM_X,           npY, u * 2 + g, u));
        keys.add(new KeyData(".",    GLFW.GLFW_KEY_KP_DECIMAL,     NUM_X + (u+g)*2, npY, u, u));

        return Collections.unmodifiableList(keys);
    }

    // -----------------------------------------------------------------------
    // Total keyboard dimensions (for background panel sizing)
    // -----------------------------------------------------------------------

    /**
     * Returns the total width of the full keyboard layout in logical pixels.
     */
    public static float totalWidth() {
        // Numpad right edge: NUM_X + 4 key columns
        return NUM_X + (UNIT + GAP) * 4;
    }

    /**
     * Returns the total height of the full keyboard layout in logical pixels.
     */
    public static float totalHeight() {
        return ROW_CTRL + UNIT;
    }

    /**
     * Returns the compact scale factor (0.0–1.0) relative to the full layout.
     */
    public static float compactScale() {
        return 0.65f;
    }
}
