package com.inputoverlayx.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Central input state tracker.
 *
 * <p>This class is updated by the Mixin hooks ({@code KeyboardMixin} and
 * {@code MouseMixin}) and read by the renderer. All public methods are
 * thread-safe via synchronization on the internal state sets.
 *
 * <p>Design goals:
 * <ul>
 *   <li>Minimal allocations per tick — reuse {@link HashSet} instances.</li>
 *   <li>O(1) pressed-key queries.</li>
 *   <li>Scroll state is ephemeral (cleared each render tick).</li>
 * </ul>
 */
@Environment(EnvType.CLIENT)
public class InputHandler {

    // GLFW key codes that are currently held down
    private final Set<Integer> pressedKeys = Collections.synchronizedSet(new HashSet<>());

    // Mouse button states (indices 0=left, 1=right, 2=middle)
    private final boolean[] mouseButtons = new boolean[8];

    // Scroll wheel state — positive = up, negative = down, 0 = idle
    private volatile double scrollDelta = 0.0;
    private final AtomicBoolean scrollDirty = new AtomicBoolean(false);

    // Timestamp of last press per key, for animation timing
    private final java.util.Map<Integer, Long> pressTimestamps =
            Collections.synchronizedMap(new java.util.HashMap<>());

    // Mouse button press timestamps
    private final long[] mouseButtonTimestamps = new long[8];

    // -------------------------------------------------------------------------
    // Keyboard
    // -------------------------------------------------------------------------

    /**
     * Called by {@code KeyboardMixin} when a key is pressed.
     *
     * @param keyCode GLFW key constant
     */
    public void onKeyPress(int keyCode) {
        pressedKeys.add(keyCode);
        pressTimestamps.put(keyCode, System.currentTimeMillis());
    }

    /**
     * Called by {@code KeyboardMixin} when a key is released.
     *
     * @param keyCode GLFW key constant
     */
    public void onKeyRelease(int keyCode) {
        pressedKeys.remove(keyCode);
        // Keep timestamp so renderer can still animate the release
    }

    /**
     * Returns {@code true} if the given GLFW key is currently held.
     */
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /**
     * Returns the number of milliseconds since the given key was last pressed,
     * or {@code Long.MAX_VALUE} if it has never been pressed.
     */
    public long getKeyPressDuration(int keyCode) {
        Long ts = pressTimestamps.get(keyCode);
        if (ts == null) return Long.MAX_VALUE;
        return System.currentTimeMillis() - ts;
    }

    // -------------------------------------------------------------------------
    // Mouse buttons
    // -------------------------------------------------------------------------

    /**
     * Called by {@code MouseMixin} when a mouse button is pressed.
     *
     * @param button GLFW button index (0 = left, 1 = right, 2 = middle)
     */
    public void onMousePress(int button) {
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = true;
            mouseButtonTimestamps[button] = System.currentTimeMillis();
        }
    }

    /**
     * Called by {@code MouseMixin} when a mouse button is released.
     */
    public void onMouseRelease(int button) {
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = false;
        }
    }

    /**
     * Returns {@code true} if the given mouse button is currently held.
     */
    public boolean isMouseButtonPressed(int button) {
        return button >= 0 && button < mouseButtons.length && mouseButtons[button];
    }

    /**
     * Returns the time in milliseconds since a mouse button was last pressed.
     */
    public long getMouseButtonPressDuration(int button) {
        if (button < 0 || button >= mouseButtonTimestamps.length) return Long.MAX_VALUE;
        if (mouseButtonTimestamps[button] == 0) return Long.MAX_VALUE;
        return System.currentTimeMillis() - mouseButtonTimestamps[button];
    }

    // -------------------------------------------------------------------------
    // Scroll wheel
    // -------------------------------------------------------------------------

    /**
     * Called by {@code MouseMixin} when the scroll wheel moves.
     *
     * @param delta positive = scroll up, negative = scroll down
     */
    public void onScroll(double delta) {
        this.scrollDelta = delta;
        this.scrollDirty.set(true);
    }

    /**
     * Consumes the current scroll delta and resets it to zero.
     * Should be called once per render frame to avoid accumulation.
     */
    public double consumeScrollDelta() {
        if (scrollDirty.compareAndSet(true, false)) {
            double val = scrollDelta;
            scrollDelta = 0.0;
            return val;
        }
        return 0.0;
    }

    /**
     * Returns the raw scroll delta without consuming it.
     */
    public double getScrollDelta() {
        return scrollDelta;
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /**
     * Returns an unmodifiable snapshot of all currently pressed GLFW key codes.
     */
    public Set<Integer> getPressedKeys() {
        synchronized (pressedKeys) {
            return Set.copyOf(pressedKeys);
        }
    }
}
