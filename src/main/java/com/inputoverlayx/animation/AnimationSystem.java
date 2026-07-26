package com.inputoverlayx.animation;

import com.inputoverlayx.config.InputOverlayXConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages smooth animation progress values for key-press and mouse-click highlights.
 *
 * <p>Each animated target is identified by an integer key (GLFW key codes for
 * keyboard keys, or negative sentinel values for mouse buttons and scroll).
 * The progress value ranges from 0.0 (fully unpressed) to 1.0 (fully pressed).
 *
 * <p>Animation is driven by delta-time so it runs at the same perceived speed
 * regardless of frame rate.  The {@link #tick()} method is called once per
 * client tick (20/s) and {@link #getProgress(int)} is called each render frame
 * with the partial tick interpolated value for smooth rendering.
 */
@Environment(EnvType.CLIENT)
public class AnimationSystem {

    // Sentinel IDs for non-keyboard animated elements
    public static final int MOUSE_LEFT   = -1;
    public static final int MOUSE_RIGHT  = -2;
    public static final int MOUSE_MIDDLE = -3;
    public static final int MOUSE_SCROLL_UP   = -4;
    public static final int MOUSE_SCROLL_DOWN = -5;

    // Progress maps: current and previous tick values for lerp
    private final Map<Integer, Float> currentProgress  = new HashMap<>();
    private final Map<Integer, Float> previousProgress = new HashMap<>();
    // Whether each target is in a "pressed" (increasing) or "releasing" state
    private final Map<Integer, Boolean> pressed = new HashMap<>();

    // Scroll animation decays on its own regardless of button state
    private float scrollUpDecay   = 0.0f;
    private float scrollDownDecay = 0.0f;

    private final InputOverlayXConfig config;

    public AnimationSystem(InputOverlayXConfig config) {
        this.config = config;
    }

    // -------------------------------------------------------------------------
    // State updates (called by the renderer / input handler consumers)
    // -------------------------------------------------------------------------

    /**
     * Marks a target as pressed, so its progress ramps toward 1.0.
     */
    public void setPressed(int id, boolean isPressed) {
        pressed.put(id, isPressed);
        if (!currentProgress.containsKey(id)) {
            currentProgress.put(id, 0.0f);
        }
    }

    /**
     * Triggers a scroll-up animation pulse.
     */
    public void triggerScrollUp() {
        scrollUpDecay = 1.0f;
    }

    /**
     * Triggers a scroll-down animation pulse.
     */
    public void triggerScrollDown() {
        scrollDownDecay = 1.0f;
    }

    // -------------------------------------------------------------------------
    // Tick (called 20× per second from the client tick event)
    // -------------------------------------------------------------------------

    /**
     * Advances all animation values by one server tick (1/20 s ≈ 50 ms).
     * The actual step size is scaled by {@link InputOverlayXConfig#getAnimationSpeed()}.
     */
    public void tick() {
        // Save previous progress for interpolation
        previousProgress.clear();
        previousProgress.putAll(currentProgress);

        if (!config.isAnimationEnabled()) {
            // Snap instantly — no smooth transitions
            for (Map.Entry<Integer, Boolean> entry : pressed.entrySet()) {
                currentProgress.put(entry.getKey(), entry.getValue() ? 1.0f : 0.0f);
            }
            scrollUpDecay   = 0.0f;
            scrollDownDecay = 0.0f;
            return;
        }

        float speed = Math.max(0.1f, config.getAnimationSpeed());
        // Step per tick: base 0.25 means 4 ticks (200 ms) for full press at speed=1
        float step = 0.25f * speed;

        // Advance each tracked key/button
        for (Integer id : pressed.keySet()) {
            boolean isPressed = pressed.get(id);
            float current = currentProgress.getOrDefault(id, 0.0f);
            float target  = isPressed ? 1.0f : 0.0f;
            float next    = moveToward(current, target, step);
            currentProgress.put(id, next);
        }

        // Decay scroll pulses
        float scrollDecayStep = 0.15f * speed;
        scrollUpDecay   = Math.max(0.0f, scrollUpDecay   - scrollDecayStep);
        scrollDownDecay = Math.max(0.0f, scrollDownDecay - scrollDecayStep);

        // Clean up targets that have fully settled at 0 and are not pressed
        currentProgress.entrySet().removeIf(e ->
                e.getValue() <= 0.0f &&
                        !Boolean.TRUE.equals(pressed.get(e.getKey()))
        );
    }

    // -------------------------------------------------------------------------
    // Query (called each render frame)
    // -------------------------------------------------------------------------

    /**
     * Returns the interpolated progress value for a target in [0, 1].
     *
     * @param id       the GLFW key code or mouse sentinel constant
     * @param partialTick fractional tick progress for sub-tick interpolation
     */
    public float getProgress(int id, float partialTick) {
        float current  = currentProgress.getOrDefault(id, 0.0f);
        float previous = previousProgress.getOrDefault(id, 0.0f);
        return lerp(previous, current, partialTick);
    }

    /**
     * Returns the interpolated progress without needing a partial tick — uses
     * the current value directly (acceptable for non-critical callers).
     */
    public float getProgress(int id) {
        return currentProgress.getOrDefault(id, 0.0f);
    }

    public float getScrollUpProgress() { return scrollUpDecay; }
    public float getScrollDownProgress() { return scrollDownDecay; }

    // -------------------------------------------------------------------------
    // Math helpers
    // -------------------------------------------------------------------------

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float moveToward(float current, float target, float step) {
        if (current < target) return Math.min(current + step, target);
        if (current > target) return Math.max(current - step, target);
        return current;
    }
}
