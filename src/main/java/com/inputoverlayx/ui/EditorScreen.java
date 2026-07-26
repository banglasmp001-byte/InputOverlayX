package com.inputoverlayx.ui;

import com.inputoverlayx.client.InputOverlayXClient;
import com.inputoverlayx.config.InputOverlayXConfig;
import com.inputoverlayx.render.keyboard.KeyboardRenderer;
import com.inputoverlayx.render.mouse.MouseRenderer;
import com.inputoverlayx.util.ColorUtil;
import com.inputoverlayx.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Interactive drag-and-drop editor screen for repositioning and resizing
 * the keyboard and mouse overlays.
 *
 * <p>Design principles:
 * <ul>
 *   <li>No background blur or dark overlay — game world stays fully visible.</li>
 *   <li>Overlays can be dragged independently with the mouse.</li>
 *   <li>Save persists all positions immediately; Cancel reverts to pre-edit state.</li>
 *   <li>A thin, non-intrusive toolbar sits at the top of the screen.</li>
 * </ul>
 *
 * <p>Dragging works by tracking the mouse delta from the initial click position
 * within an overlay's panel bounds.
 */
public class EditorScreen extends Screen {

    // Toolbar dimensions
    private static final int TOOLBAR_H = 28;
    private static final int BTN_W     = 70;
    private static final int BTN_H     = 18;
    private static final int BTN_Y     = 5;

    private final InputOverlayXConfig config;

    // Snapshot of positions before editing (for cancel)
    private final float savedKeyboardX;
    private final float savedKeyboardY;
    private final float savedMouseX;
    private final float savedMouseY;

    // Drag state
    private boolean draggingKeyboard = false;
    private boolean draggingMouse    = false;
    private float   dragOffsetX      = 0;
    private float   dragOffsetY      = 0;

    // Resize state (right-drag changes scale)
    private boolean resizingKeyboard = false;
    private boolean resizingMouse    = false;
    private float   resizeStartX     = 0;
    private float   resizeStartScale = 1.0f;

    public EditorScreen(InputOverlayXConfig config) {
        super(Text.translatable("inputoverlayx.editor.title"));
        this.config = config;

        // Store pre-edit positions for Cancel
        this.savedKeyboardX = config.getKeyboardX();
        this.savedKeyboardY = config.getKeyboardY();
        this.savedMouseX    = config.getMouseX();
        this.savedMouseY    = config.getMouseY();
    }

    // -------------------------------------------------------------------------
    // Screen lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void init() {
        int centerX = width / 2;
        int bx      = centerX - (BTN_W * 3 + 12) / 2;

        // Save button
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.editor.save"),
                btn -> saveAndClose()
        ).dimensions(bx, BTN_Y, BTN_W, BTN_H).build());
        bx += BTN_W + 6;

        // Cancel button
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.editor.cancel"),
                btn -> cancelAndClose()
        ).dimensions(bx, BTN_Y, BTN_W, BTN_H).build());
        bx += BTN_W + 6;

        // Reset button
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.editor.reset"),
                btn -> {
                    config.resetPositions();
                }
        ).dimensions(bx, BTN_Y, BTN_W, BTN_H).build());
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // --- Toolbar background (semi-transparent, top of screen) ---
        ctx.fill(0, 0, width, TOOLBAR_H, ColorUtil.argb(180, 10, 10, 20));
        RenderUtil.drawRoundRectBorder(ctx, 0, TOOLBAR_H - 1, width, 1, 0, 1.0f,
                ColorUtil.argb(100, 80, 80, 160));

        // Title text in toolbar
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("inputoverlayx.editor.title"),
                width / 2, BTN_Y + BTN_H + 3, 0xFFAAAAAA);

        // Hint text
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("inputoverlayx.editor.hint"),
                width / 2, TOOLBAR_H + 4, 0xFF666699);

        // Draw highlight borders around draggable overlays
        drawOverlayBorders(ctx, mouseX, mouseY);

        // Render overlay on top of editor UI (the real overlays from config)
        var overlayRenderer = InputOverlayXClient.getOverlayRenderer();
        if (overlayRenderer != null && config.isEnabled()) {
            if (config.isKeyboardEnabled()) {
                overlayRenderer.getKeyboardRenderer().render(ctx, delta);
            }
            if (config.isMouseEnabled()) {
                overlayRenderer.getMouseRenderer().render(ctx, delta);
            }
        }

        // Render buttons (toolbar)
        super.render(ctx, mouseX, mouseY, delta);

        // Position readout
        renderPositionInfo(ctx);
    }

    private void drawOverlayBorders(DrawContext ctx, int mouseX, int mouseY) {
        var overlayRenderer = InputOverlayXClient.getOverlayRenderer();
        if (overlayRenderer == null) return;

        KeyboardRenderer kr = overlayRenderer.getKeyboardRenderer();
        MouseRenderer    mr = overlayRenderer.getMouseRenderer();

        // Keyboard border
        if (config.isKeyboardEnabled()) {
            float kx = config.getKeyboardX();
            float ky = config.getKeyboardY();
            float kw = kr.getPanelWidth();
            float kh = kr.getPanelHeight();
            boolean hovered = draggingKeyboard || kr.isHovered(mouseX, mouseY);
            int borderColor = hovered
                    ? ColorUtil.argb(220, 100, 200, 255)
                    : ColorUtil.argb(120, 60, 120, 200);
            RenderUtil.drawRoundRectBorder(ctx, kx - 2, ky - 2, kw + 4, kh + 4,
                    config.getKeyboardCornerRadius() + 2, 2.0f, borderColor);
            if (hovered) {
                ctx.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("⟵ drag to move ⟶"),
                        (int)(kx + kw / 2), (int)(ky - 14), 0xFF88BBFF);
            }
        }

        // Mouse border
        if (config.isMouseEnabled()) {
            float mx = config.getMouseX();
            float my = config.getMouseY();
            float mw = mr.getPanelWidth();
            float mh = mr.getPanelHeight();
            boolean hovered = draggingMouse || mr.isHovered(mouseX, mouseY);
            int borderColor = hovered
                    ? ColorUtil.argb(220, 100, 255, 200)
                    : ColorUtil.argb(120, 60, 200, 120);
            RenderUtil.drawRoundRectBorder(ctx, mx - 2, my - 2, mw + 4, mh + 4,
                    8.0f + 2, 2.0f, borderColor);
            if (hovered) {
                ctx.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("⟵ drag ⟶"),
                        (int)(mx + mw / 2), (int)(my - 14), 0xFF88FFBB);
            }
        }
    }

    private void renderPositionInfo(DrawContext ctx) {
        String kbInfo = String.format("Keyboard: (%.0f, %.0f)  Scale: %.2f",
                config.getKeyboardX(), config.getKeyboardY(), config.getKeyboardScale());
        String msInfo = String.format("Mouse: (%.0f, %.0f)  Scale: %.2f",
                config.getMouseX(), config.getMouseY(), config.getMouseScale());

        ctx.drawText(textRenderer, kbInfo, 4, height - 20, 0xFF666699, false);
        ctx.drawText(textRenderer, msInfo, 4, height - 10, 0xFF669966, false);
    }

    // -------------------------------------------------------------------------
    // Mouse events — drag logic
    // -------------------------------------------------------------------------

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, boolean bl) {
        // Let buttons handle their own clicks first
        if (super.mouseClicked(mouseX, mouseY, button, bl)) return true;

        var overlayRenderer = InputOverlayXClient.getOverlayRenderer();
        if (overlayRenderer == null) return false;

        float mx = (float) mouseX;
        float my = (float) mouseY;

        if (button == 0) { // Left click — drag
            // Test keyboard first (higher z-order visually)
            if (config.isKeyboardEnabled() &&
                    overlayRenderer.getKeyboardRenderer().isHovered(mx, my)) {
                draggingKeyboard = true;
                dragOffsetX = mx - config.getKeyboardX();
                dragOffsetY = my - config.getKeyboardY();
                return true;
            }
            if (config.isMouseEnabled() &&
                    overlayRenderer.getMouseRenderer().isHovered(mx, my)) {
                draggingMouse = true;
                dragOffsetX = mx - config.getMouseX();
                dragOffsetY = my - config.getMouseY();
                return true;
            }
        } else if (button == 1) { // Right click — resize
            if (config.isKeyboardEnabled() &&
                    overlayRenderer.getKeyboardRenderer().isHovered(mx, my)) {
                resizingKeyboard = true;
                resizeStartX     = mx;
                resizeStartScale = config.getKeyboardScale();
                return true;
            }
            if (config.isMouseEnabled() &&
                    overlayRenderer.getMouseRenderer().isHovered(mx, my)) {
                resizingMouse    = true;
                resizeStartX     = mx;
                resizeStartScale = config.getMouseScale();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button, boolean bl) {
        draggingKeyboard = false;
        draggingMouse    = false;
        resizingKeyboard = false;
        resizingMouse    = false;
        return super.mouseReleased(mouseX, mouseY, button, bl);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button,
                                double deltaX, double deltaY, boolean bl) {
        float mx = (float) mouseX;
        float my = (float) mouseY;

        if (draggingKeyboard && button == 0) {
            config.setKeyboardX(Math.max(0, mx - dragOffsetX));
            config.setKeyboardY(Math.max(TOOLBAR_H + 10, my - dragOffsetY));
            return true;
        }
        if (draggingMouse && button == 0) {
            config.setMouseX(Math.max(0, mx - dragOffsetX));
            config.setMouseY(Math.max(TOOLBAR_H + 10, my - dragOffsetY));
            return true;
        }
        if (resizingKeyboard && button == 1) {
            float delta = (mx - resizeStartX) / 200.0f;
            config.setKeyboardScale(resizeStartScale + delta);
            return true;
        }
        if (resizingMouse && button == 1) {
            float delta = (mx - resizeStartX) / 200.0f;
            config.setMouseScale(resizeStartScale + delta);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY, bl);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount,
                                 double verticalAmount) {
        var overlayRenderer = InputOverlayXClient.getOverlayRenderer();
        if (overlayRenderer == null) return false;

        float mx = (float) mouseX;
        float my = (float) mouseY;

        // Scroll to resize keyboard
        if (config.isKeyboardEnabled() &&
                overlayRenderer.getKeyboardRenderer().isHovered(mx, my)) {
            float delta = (float) verticalAmount * 0.05f;
            config.setKeyboardScale(config.getKeyboardScale() + delta);
            return true;
        }
        // Scroll to resize mouse
        if (config.isMouseEnabled() &&
                overlayRenderer.getMouseRenderer().isHovered(mx, my)) {
            float delta = (float) verticalAmount * 0.05f;
            config.setMouseScale(config.getMouseScale() + delta);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    // -------------------------------------------------------------------------
    // Keyboard events
    // -------------------------------------------------------------------------

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
            cancelAndClose();
            return true;
        }
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_S &&
                (modifiers & org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL) != 0) {
            saveAndClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    // -------------------------------------------------------------------------
    // Game world — no pause
    // -------------------------------------------------------------------------

    // shouldPause() removed in MC 1.21.3 — screens no longer pause the game by default

    // -------------------------------------------------------------------------
    // Save / Cancel
    // -------------------------------------------------------------------------

    private void saveAndClose() {
        config.save();
        close();
    }

    private void cancelAndClose() {
        // Restore original positions
        config.setKeyboardX(savedKeyboardX);
        config.setKeyboardY(savedKeyboardY);
        config.setMouseX(savedMouseX);
        config.setMouseY(savedMouseY);
        // Don't save — just close
        close();
    }
}
