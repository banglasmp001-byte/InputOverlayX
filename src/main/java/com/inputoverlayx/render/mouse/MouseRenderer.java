package com.inputoverlayx.render.mouse;

import com.inputoverlayx.animation.AnimationSystem;
import com.inputoverlayx.config.InputOverlayXConfig;
import com.inputoverlayx.input.InputHandler;
import com.inputoverlayx.util.ColorUtil;
import com.inputoverlayx.util.RenderUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Renders a realistic mouse overlay showing left button, right button, middle
 * button, scroll wheel, and a scroll indicator.
 *
 * <p>The mouse silhouette is drawn using rectangular primitives assembled to
 * approximate a real mouse shape:
 * <pre>
 *   ┌───┬───┐
 *   │ L │ R │   ← Button area (top 60% of body)
 *   ├───┼───┤
 *   │   M   │   ← Middle button strip
 *   │  ┌─┐  │
 *   │  │W│  │   ← Scroll wheel
 *   │  └─┘  │
 *   │       │   ← Lower body
 *   └───────┘
 * </pre>
 *
 * <p>All colors support opacity and animate smoothly on click/scroll events.
 */
@Environment(EnvType.CLIENT)
public class MouseRenderer {

    // Mouse body dimensions in logical units (before config.mouseScale is applied)
    private static final float BODY_W  = 40.0f;
    private static final float BODY_H  = 60.0f;
    private static final float RADIUS  = 10.0f;
    // Split line Y (separates left/right button area from lower body)
    private static final float SPLIT_Y = 22.0f;
    // Scroll wheel
    private static final float WHEEL_W = 8.0f;
    private static final float WHEEL_H = 14.0f;

    // Mouse button GLFW indices
    private static final int BTN_LEFT   = 0;
    private static final int BTN_RIGHT  = 1;
    private static final int BTN_MIDDLE = 2;

    private final InputOverlayXConfig config;
    private final InputHandler inputHandler;
    private final AnimationSystem animation;

    // Scroll state (ephemeral — decays each frame)
    private float scrollUpDecay   = 0.0f;
    private float scrollDownDecay = 0.0f;

    public MouseRenderer(InputOverlayXConfig config, InputHandler inputHandler,
                         AnimationSystem animation) {
        this.config      = config;
        this.inputHandler = inputHandler;
        this.animation   = animation;
    }

    // -------------------------------------------------------------------------
    // Tick
    // -------------------------------------------------------------------------

    /**
     * Updates animation state and scroll decay — called once per client tick.
     */
    public void tick() {
        animation.setPressed(AnimationSystem.MOUSE_LEFT,   inputHandler.isMouseButtonPressed(BTN_LEFT));
        animation.setPressed(AnimationSystem.MOUSE_RIGHT,  inputHandler.isMouseButtonPressed(BTN_RIGHT));
        animation.setPressed(AnimationSystem.MOUSE_MIDDLE, inputHandler.isMouseButtonPressed(BTN_MIDDLE));

        // Consume scroll delta
        double scrollDelta = inputHandler.consumeScrollDelta();
        if (scrollDelta > 0) {
            animation.triggerScrollUp();
        } else if (scrollDelta < 0) {
            animation.triggerScrollDown();
        }
    }

    // -------------------------------------------------------------------------
    // Render
    // -------------------------------------------------------------------------

    /**
     * Renders the mouse overlay at the configured position.
     */
    public void render(DrawContext ctx, float partialTick) {
        float scale   = config.getMouseScale();
        float ox      = config.getMouseX();
        float oy      = config.getMouseY();
        float opacity = config.getMouseOpacity();
        float w       = BODY_W * scale;
        float h       = BODY_H * scale;
        float r       = RADIUS * scale;

        // --- Shadow ---
        RenderUtil.drawShadow(ctx, ox, oy, w, h, r, 3);

        // --- Mouse body background ---
        int bgColor = ColorUtil.withOpacity(config.getMouseBgColor(), opacity);
        RenderUtil.fillRoundRect(ctx, ox, oy, w, h, r, bgColor);

        // --- Border ---
        int borderColor = ColorUtil.withOpacity(config.getMouseBorderColor(), opacity);
        RenderUtil.drawRoundRectBorder(ctx, ox, oy, w, h, r, 1.5f, borderColor);

        // --- Left button (left half, top portion) ---
        float leftProgress = animation.getProgress(AnimationSystem.MOUSE_LEFT, partialTick);
        if (leftProgress > 0.01f) {
            int lColor = ColorUtil.withOpacity(config.getMouseClickColor(), leftProgress * opacity);
            // Clamp to left half of button area
            RenderUtil.fillRoundRect(ctx, ox + 1, oy + 1,
                    w / 2.0f - 1.5f, SPLIT_Y * scale - 1, r * 0.8f, lColor);
        }

        // --- Right button (right half, top portion) ---
        float rightProgress = animation.getProgress(AnimationSystem.MOUSE_RIGHT, partialTick);
        if (rightProgress > 0.01f) {
            int rColor = ColorUtil.withOpacity(config.getMouseClickColor(), rightProgress * opacity);
            RenderUtil.fillRoundRect(ctx, ox + w / 2.0f + 0.5f, oy + 1,
                    w / 2.0f - 1.5f, SPLIT_Y * scale - 1, r * 0.8f, rColor);
        }

        // Center divider line
        int dividerColor = ColorUtil.withOpacity(config.getMouseBorderColor(), opacity * 0.6f);
        // Horizontal split
        RenderUtil.fillRect(ctx, ox + 1, oy + SPLIT_Y * scale,
                w - 2, 1.0f, dividerColor);
        // Vertical split
        RenderUtil.fillRect(ctx, ox + w / 2.0f - 0.5f, oy + 1,
                1.0f, SPLIT_Y * scale - 1, dividerColor);

        // --- Middle button ---
        float midProgress = animation.getProgress(AnimationSystem.MOUSE_MIDDLE, partialTick);
        renderMiddleButton(ctx, ox, oy, scale, opacity, midProgress, partialTick);

        // --- Scroll wheel ---
        renderScrollWheel(ctx, ox, oy, scale, opacity, partialTick);

        // --- Labels ---
        renderMouseLabels(ctx, ox, oy, scale, opacity, leftProgress, rightProgress, midProgress);
    }

    // -------------------------------------------------------------------------
    // Private rendering sub-methods
    // -------------------------------------------------------------------------

    private void renderMiddleButton(DrawContext ctx, float ox, float oy, float scale,
                                    float opacity, float midProgress, float partialTick) {
        float mx = ox + BODY_W * scale / 2.0f - (WHEEL_W * scale) / 2.0f;
        float my = oy + (SPLIT_Y + 3) * scale;
        float mw = WHEEL_W * scale;
        float mh = 5 * scale;

        int midBase  = ColorUtil.withOpacity(config.getMouseBorderColor(), opacity * 0.5f);
        int midClick = ColorUtil.withOpacity(config.getMouseClickColor(), midProgress * opacity);
        int midColor = ColorUtil.lerp(midBase, midClick, midProgress);

        RenderUtil.fillRoundRect(ctx, mx, my, mw, mh, 1.5f, midColor);
    }

    private void renderScrollWheel(DrawContext ctx, float ox, float oy, float scale, float opacity,
                                   float partialTick) {
        float wx = ox + BODY_W * scale / 2.0f - (WHEEL_W * scale) / 2.0f;
        float wy = oy + (SPLIT_Y + 10) * scale;
        float ww = WHEEL_W * scale;
        float wh = WHEEL_H * scale;

        float upProg   = animation.getScrollUpProgress();
        float downProg = animation.getScrollDownProgress();

        // Wheel base
        int wheelBase = ColorUtil.withOpacity(config.getMouseBorderColor(), opacity * 0.4f);
        RenderUtil.fillRoundRect(ctx, wx, wy, ww, wh, 2.0f, wheelBase);

        // Scroll up indicator (top half of wheel glows)
        if (upProg > 0.01f && config.isMouseScrollAnimation()) {
            int upColor = ColorUtil.withOpacity(config.getMouseClickColor(), upProg * opacity);
            RenderUtil.fillRoundRect(ctx, wx, wy, ww, wh / 2.0f, 2.0f, upColor);
        }
        // Scroll down indicator (bottom half of wheel glows)
        if (downProg > 0.01f && config.isMouseScrollAnimation()) {
            int downColor = ColorUtil.withOpacity(config.getMouseClickColor(), downProg * opacity);
            RenderUtil.fillRoundRect(ctx, wx, wy + wh / 2.0f, ww, wh / 2.0f, 2.0f, downColor);
        }

        // Scroll arrows (minimal text indicators)
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null && config.isMouseScrollAnimation()) {
            int fontColor = ColorUtil.withOpacity(config.getMouseBorderColor(), opacity * 0.8f);
            int upTextColor   = upProg   > 0.1f ? ColorUtil.withOpacity(0xFFFFFFFF, upProg   * opacity) : fontColor;
            int downTextColor = downProg > 0.1f ? ColorUtil.withOpacity(0xFFFFFFFF, downProg * opacity) : fontColor;

            // "↑" above wheel
            ctx.drawText(textRenderer, "↑",
                    (int)(wx + ww / 2 - 3), (int)(wy - 8 * scale), upTextColor, false);
            // "↓" below wheel
            ctx.drawText(textRenderer, "↓",
                    (int)(wx + ww / 2 - 3), (int)(wy + wh + 1), downTextColor, false);
        }
    }

    private void renderMouseLabels(DrawContext ctx, float ox, float oy, float scale, float opacity,
                                   float leftProgress, float rightProgress, float midProgress) {
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer == null) return;

        float splitY = SPLIT_Y * scale;
        int baseColor = ColorUtil.withOpacity(config.getMouseBorderColor(), opacity * 0.8f);

        // "L" label center of left half
        int lColor = leftProgress > 0.3f
                ? ColorUtil.withOpacity(0xFFFFFFFF, leftProgress * opacity)
                : baseColor;
        ctx.drawText(textRenderer, "L",
                (int)(ox + BODY_W * scale / 4.0f - 3), (int)(oy + splitY / 2.0f - 4), lColor, false);

        // "R" label center of right half
        int rColor = rightProgress > 0.3f
                ? ColorUtil.withOpacity(0xFFFFFFFF, rightProgress * opacity)
                : baseColor;
        ctx.drawText(textRenderer, "R",
                (int)(ox + BODY_W * scale * 3.0f / 4.0f - 3), (int)(oy + splitY / 2.0f - 4), rColor, false);
    }

    // -------------------------------------------------------------------------
    // Bounds check
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the given screen coordinate is within the mouse
     * overlay bounds.
     */
    public boolean isHovered(float mouseX, float mouseY) {
        float scale = config.getMouseScale();
        float ox = config.getMouseX();
        float oy = config.getMouseY();
        float w  = BODY_W * scale;
        float h  = BODY_H * scale;
        return mouseX >= ox && mouseX <= ox + w && mouseY >= oy && mouseY <= oy + h;
    }

    public float getPanelWidth()  { return BODY_W * config.getMouseScale(); }
    public float getPanelHeight() { return BODY_H * config.getMouseScale(); }
}
