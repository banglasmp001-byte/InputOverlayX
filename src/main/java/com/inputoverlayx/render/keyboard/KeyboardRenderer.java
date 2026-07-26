package com.inputoverlayx.render.keyboard;

import com.inputoverlayx.animation.AnimationSystem;
import com.inputoverlayx.config.InputOverlayXConfig;
import com.inputoverlayx.input.InputHandler;
import com.inputoverlayx.util.ColorUtil;
import com.inputoverlayx.util.RenderUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/**
 * Renders the full 104-key (or compact) keyboard overlay onto the game HUD.
 *
 * <p>Rendering pipeline per frame:
 * <ol>
 *   <li>Optional drop shadow</li>
 *   <li>Keyboard background panel (rounded rectangle)</li>
 *   <li>Optional border</li>
 *   <li>Optional glow overlay (for keys with active glow)</li>
 *   <li>Per-key backgrounds with animated pressed color</li>
 *   <li>Per-key labels (centered text)</li>
 * </ol>
 *
 * <p>All rendering is done through {@link DrawContext} so it integrates
 * cleanly with Minecraft's scissor/blend state management.
 */
@Environment(EnvType.CLIENT)
public class KeyboardRenderer {

    /** Padding around the key area inside the keyboard panel (logical pixels). */
    private static final float PANEL_PADDING = 4.0f;

    private final InputOverlayXConfig config;
    private final InputHandler inputHandler;
    private final AnimationSystem animation;
    private final List<KeyData> fullLayout;

    public KeyboardRenderer(InputOverlayXConfig config, InputHandler inputHandler,
                            AnimationSystem animation) {
        this.config      = config;
        this.inputHandler = inputHandler;
        this.animation   = animation;
        this.fullLayout  = KeyboardLayout.buildFullLayout();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Updates animation state — must be called once per client tick.
     */
    public void tick() {
        for (KeyData key : fullLayout) {
            boolean pressed = inputHandler.isKeyPressed(key.glfwKey);
            animation.setPressed(key.glfwKey, pressed);
        }
    }

    /**
     * Renders the keyboard overlay at its configured position.
     *
     * @param ctx         Minecraft draw context
     * @param partialTick fractional tick for smooth animation interpolation
     */
    public void render(DrawContext ctx, float partialTick) {
        float scale  = config.isKeyboardCompact()
                ? KeyboardLayout.compactScale() * config.getKeyboardScale()
                : config.getKeyboardScale();
        float ox     = config.getKeyboardX();
        float oy     = config.getKeyboardY();
        float kw     = (KeyboardLayout.totalWidth()  + PANEL_PADDING * 2) * scale;
        float kh     = (KeyboardLayout.totalHeight() + PANEL_PADDING * 2) * scale;
        float opacity = config.getKeyboardOpacity();
        float radius  = config.getKeyboardCornerRadius();

        // --- Drop shadow ---
        if (config.isKeyboardShadow()) {
            RenderUtil.drawShadow(ctx, ox, oy, kw, kh, radius, 4);
        }

        // --- Panel background ---
        int bgColor = ColorUtil.withOpacity(config.getKeyboardBgColor(), opacity);
        RenderUtil.fillRoundRect(ctx, ox, oy, kw, kh, radius, bgColor);

        // --- Border ---
        if (config.getKeyboardBorderThickness() > 0.0f) {
            int borderColor = ColorUtil.withOpacity(config.getKeyboardBorderColor(), opacity);
            RenderUtil.drawRoundRectBorder(ctx, ox, oy, kw, kh, radius,
                    config.getKeyboardBorderThickness(), borderColor);
        }

        // --- Keys ---
        for (KeyData key : fullLayout) {
            renderKey(ctx, key, ox, oy, scale, partialTick, opacity);
        }
    }

    // -------------------------------------------------------------------------
    // Private — per-key rendering
    // -------------------------------------------------------------------------

    private void renderKey(DrawContext ctx, KeyData key, float ox, float oy,
                           float scale, float partialTick, float opacity) {
        float kx = ox + (PANEL_PADDING + key.x) * scale;
        float ky = oy + (PANEL_PADDING + key.y) * scale;
        float kw = key.width  * scale;
        float kh = key.height * scale;

        float progress = animation.getProgress(key.glfwKey, partialTick);

        // Lerp from unpressed key color (transparent/subtle) to pressed color
        int unpressedColor = ColorUtil.withOpacity(config.getKeyboardHoverColor(), opacity * 0.5f);
        int pressedColor   = ColorUtil.withOpacity(config.getKeyboardPressedColor(), opacity);
        int keyColor       = ColorUtil.lerp(unpressedColor, pressedColor, progress);

        // Key background
        RenderUtil.fillRoundRect(ctx, kx, ky, kw, kh, 2.0f, keyColor);

        // Glow effect when fully pressed and glow is enabled
        if (config.isKeyboardGlow() && progress > 0.05f) {
            int glowColor = ColorUtil.glowColor(config.getKeyboardPressedColor(), progress * 0.7f);
            RenderUtil.fillRoundRect(ctx, kx - 1, ky - 1, kw + 2, kh + 2, 3.0f, glowColor);
        }

        // Key label text
        renderKeyLabel(ctx, key.label, kx, ky, kw, kh, opacity);
    }

    private void renderKeyLabel(DrawContext ctx, String label, float kx, float ky,
                                float kw, float kh, float opacity) {
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer == null) return;

        int fontColor = ColorUtil.withOpacity(config.getKeyboardFontColor(), opacity);

        // Determine display label (shorten multi-character labels if key is small)
        String display = label;
        int textWidth  = textRenderer.getWidth(display);

        // Scale-aware centering — always draw at native scale (1:1 font)
        // The key might be smaller than the text; clip display to fit
        float maxTextW = kw - 2;
        while (textWidth > maxTextW && display.length() > 1) {
            display   = display.substring(0, display.length() - 1);
            textWidth = textRenderer.getWidth(display);
        }

        int textX = (int) (kx + (kw - textWidth) / 2.0f);
        int textY = (int) (ky + (kh - textRenderer.fontHeight) / 2.0f);

        if (config.isKeyboardShadow()) {
            ctx.drawText(textRenderer, display, textX + 1, textY + 1,
                    ColorUtil.argb(100, 0, 0, 0), false);
        }
        ctx.drawText(textRenderer, display, textX, textY, fontColor, false);
    }

    // -------------------------------------------------------------------------
    // Bounds check (used by editor drag logic)
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the given screen coordinate is within the
     * keyboard panel bounds.
     */
    public boolean isHovered(float mouseX, float mouseY) {
        float scale = config.isKeyboardCompact()
                ? KeyboardLayout.compactScale() * config.getKeyboardScale()
                : config.getKeyboardScale();
        float kw = (KeyboardLayout.totalWidth()  + PANEL_PADDING * 2) * scale;
        float kh = (KeyboardLayout.totalHeight() + PANEL_PADDING * 2) * scale;

        float ox = config.getKeyboardX();
        float oy = config.getKeyboardY();
        return mouseX >= ox && mouseX <= ox + kw && mouseY >= oy && mouseY <= oy + kh;
    }

    /** Returns the scaled panel width. */
    public float getPanelWidth() {
        float scale = config.isKeyboardCompact()
                ? KeyboardLayout.compactScale() * config.getKeyboardScale()
                : config.getKeyboardScale();
        return (KeyboardLayout.totalWidth() + PANEL_PADDING * 2) * scale;
    }

    /** Returns the scaled panel height. */
    public float getPanelHeight() {
        float scale = config.isKeyboardCompact()
                ? KeyboardLayout.compactScale() * config.getKeyboardScale()
                : config.getKeyboardScale();
        return (KeyboardLayout.totalHeight() + PANEL_PADDING * 2) * scale;
    }
}
