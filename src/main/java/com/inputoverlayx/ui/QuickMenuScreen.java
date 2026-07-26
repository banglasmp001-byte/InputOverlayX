package com.inputoverlayx.ui;

import com.inputoverlayx.config.InputOverlayXConfig;
import com.inputoverlayx.theme.Theme;
import com.inputoverlayx.util.ColorUtil;
import com.inputoverlayx.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Floating quick-access control panel, opened by pressing Right Shift.
 *
 * <p>The game world continues rendering behind this screen — no pause, no blur.
 * Every change applies immediately and is auto-saved.
 */
public class QuickMenuScreen extends Screen {

    // Layout constants
    private static final int PANEL_W  = 204;
    private static final int BTN_H    = 16;
    private static final int BTN_GAP  = 2;
    private static final int HEADER_H = 14;
    private static final int PAD      = 6;
    private static final int TITLE_H  = 22;

    private final InputOverlayXConfig config;

    // Computed layout
    private int panelX;
    private int panelY;
    private int panelH;

    public QuickMenuScreen(InputOverlayXConfig config) {
        super(Text.translatable("inputoverlayx.menu.title"));
        this.config = config;
    }

    // -------------------------------------------------------------------------
    // Init — build buttons with a running Y tracker
    // -------------------------------------------------------------------------

    @Override
    protected void init() {
        // Calculate total panel height first by simulating the layout pass
        int innerW = PANEL_W - PAD * 2;
        int halfW  = innerW / 2 - 1;

        // We place buttons relative to panel origin; compute panelH from the
        // number of rows we'll add.
        int rows = 4   // overlay section: 2 toggles + 1 layout row + spacing
                 + 2   // appearance section: theme row + opacity row
                 + 2   // animation section: toggle + speed row
                 + 1   // editor section: open editor
                 + 2   // utilities section: reset position + reset settings
                 + 1;  // close button
        int headers = 5; // 5 section headers

        panelH = TITLE_H
                + headers * (HEADER_H + BTN_GAP)
                + rows * (BTN_H + BTN_GAP)
                + PAD * 2;

        panelX = (width  - PANEL_W) / 2;
        panelY = Math.max(4, (height - panelH) / 2);

        // Running Y cursor inside the panel
        int cx = panelX + PAD;
        int cy = panelY + TITLE_H + PAD;

        // ── Overlay ──────────────────────────────────────
        cy += HEADER_H + BTN_GAP;

        // Enable Keyboard
        addToggleButton(cx, cy, innerW, "inputoverlayx.menu.enable_keyboard",
                config.isKeyboardEnabled(),
                () -> { config.setKeyboardEnabled(!config.isKeyboardEnabled()); config.save(); });
        cy += BTN_H + BTN_GAP;

        // Enable Mouse
        addToggleButton(cx, cy, innerW, "inputoverlayx.menu.enable_mouse",
                config.isMouseEnabled(),
                () -> { config.setMouseEnabled(!config.isMouseEnabled()); config.save(); });
        cy += BTN_H + BTN_GAP;

        // Full / Compact split
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.menu.full_keyboard"),
                btn -> { config.setKeyboardCompact(false); config.save(); })
                .dimensions(cx, cy, halfW, BTN_H).build());
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.menu.compact_keyboard"),
                btn -> { config.setKeyboardCompact(true); config.save(); })
                .dimensions(cx + halfW + 2, cy, halfW, BTN_H).build());
        cy += BTN_H + BTN_GAP;

        cy += BTN_GAP; // extra spacing

        // ── Appearance ───────────────────────────────────
        cy += HEADER_H + BTN_GAP;

        // Theme ◀ ▶
        addDrawableChild(ButtonWidget.builder(Text.literal("◀ Theme"),
                btn -> cycleTheme(-1))
                .dimensions(cx, cy, halfW, BTN_H).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Theme ▶"),
                btn -> cycleTheme(+1))
                .dimensions(cx + halfW + 2, cy, halfW, BTN_H).build());
        cy += BTN_H + BTN_GAP;

        // Opacity − +
        addDrawableChild(ButtonWidget.builder(Text.literal("Opacity  −"),
                btn -> { config.setKeyboardOpacity(config.getKeyboardOpacity() - 0.1f); config.save(); })
                .dimensions(cx, cy, halfW, BTN_H).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Opacity  +"),
                btn -> { config.setKeyboardOpacity(config.getKeyboardOpacity() + 0.1f); config.save(); })
                .dimensions(cx + halfW + 2, cy, halfW, BTN_H).build());
        cy += BTN_H + BTN_GAP;

        cy += BTN_GAP;

        // ── Animation ────────────────────────────────────
        cy += HEADER_H + BTN_GAP;

        addToggleButton(cx, cy, innerW, "inputoverlayx.menu.enable_animation",
                config.isAnimationEnabled(),
                () -> { config.setAnimationEnabled(!config.isAnimationEnabled()); config.save(); });
        cy += BTN_H + BTN_GAP;

        addDrawableChild(ButtonWidget.builder(Text.literal("Speed  −"),
                btn -> { config.setAnimationSpeed(config.getAnimationSpeed() - 0.25f); config.save(); })
                .dimensions(cx, cy, halfW, BTN_H).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Speed  +"),
                btn -> { config.setAnimationSpeed(config.getAnimationSpeed() + 0.25f); config.save(); })
                .dimensions(cx + halfW + 2, cy, halfW, BTN_H).build());
        cy += BTN_H + BTN_GAP;

        cy += BTN_GAP;

        // ── Editor ───────────────────────────────────────
        cy += HEADER_H + BTN_GAP;

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.menu.open_editor"),
                btn -> openEditor())
                .dimensions(cx, cy, innerW, BTN_H).build());
        cy += BTN_H + BTN_GAP;

        cy += BTN_GAP;

        // ── Utilities ────────────────────────────────────
        cy += HEADER_H + BTN_GAP;

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.menu.reset_position"),
                btn -> config.resetPositions())
                .dimensions(cx, cy, innerW, BTN_H).build());
        cy += BTN_H + BTN_GAP;

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.menu.reset_settings"),
                btn -> config.resetToDefaults())
                .dimensions(cx, cy, innerW, BTN_H).build());
        cy += BTN_H + BTN_GAP + BTN_GAP;

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("inputoverlayx.menu.close"),
                btn -> close())
                .dimensions(cx, cy, innerW, BTN_H).build());
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Panel shadow + background
        RenderUtil.drawShadow(ctx, panelX - 3, panelY - 3, PANEL_W + 6, panelH + 6, 10.0f, 6);
        RenderUtil.fillRoundRect(ctx, panelX, panelY, PANEL_W, panelH, 9.0f,
                ColorUtil.argb(225, 12, 12, 24));
        RenderUtil.drawRoundRectBorder(ctx, panelX, panelY, PANEL_W, panelH, 9.0f, 1.5f,
                ColorUtil.argb(200, 70, 70, 150));

        // Title bar area
        RenderUtil.fillRoundRect(ctx, panelX, panelY, PANEL_W, TITLE_H, 9.0f,
                ColorUtil.argb(120, 30, 30, 80));
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.translatable("inputoverlayx.menu.title"),
                panelX + PANEL_W / 2, panelY + 6,
                0xFFDDDDFF);

        // Section headers — re-trace the Y positions
        int innerW = PANEL_W - PAD * 2;
        int sectionColor = 0xFF8888BB;
        int cy = panelY + TITLE_H + PAD;

        renderSectionHeader(ctx, "── Overlay ──",    panelX + PAD, cy); cy += HEADER_H + BTN_GAP + (BTN_H + BTN_GAP) * 3 + BTN_GAP;
        renderSectionHeader(ctx, "── Appearance ──", panelX + PAD, cy); cy += HEADER_H + BTN_GAP + (BTN_H + BTN_GAP) * 2 + BTN_GAP;
        renderSectionHeader(ctx, "── Animation ──",  panelX + PAD, cy); cy += HEADER_H + BTN_GAP + (BTN_H + BTN_GAP) * 2 + BTN_GAP;
        renderSectionHeader(ctx, "── Editor ──",     panelX + PAD, cy); cy += HEADER_H + BTN_GAP + (BTN_H + BTN_GAP) * 1 + BTN_GAP;
        renderSectionHeader(ctx, "── Utilities ──",  panelX + PAD, cy);

        // Status line (theme + opacity) — just below title
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§7Theme: §a" + config.getCurrentTheme().getDisplayName() +
                        "  §7Opacity: §a" + (int)(config.getKeyboardOpacity() * 100) + "%"),
                panelX + PANEL_W / 2, panelY + TITLE_H + 2,
                0xFF888888);

        // Buttons render on top
        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderSectionHeader(DrawContext ctx, String text, int x, int y) {
        ctx.drawText(textRenderer, text, x, y + 2, 0xFF7777AA, false);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /** Adds a toggle button that shows the current on/off state in its label. */
    private void addToggleButton(int x, int y, int w, String key, boolean current,
                                 Runnable action) {
        final boolean[] state = {current};
        ButtonWidget btn = ButtonWidget.builder(toggleText(key, state[0]), b -> {
            action.run();
            state[0] = !state[0];
            b.setMessage(toggleText(key, state[0]));
        }).dimensions(x, y, w, BTN_H).build();
        addDrawableChild(btn);
    }

    private Text toggleText(String key, boolean on) {
        return Text.translatable(key).append(Text.literal(on ? " §a[ON]" : " §c[OFF]"));
    }

    private void cycleTheme(int direction) {
        Theme[] themes = Theme.values();
        int next = (config.getCurrentTheme().ordinal() + direction + themes.length) % themes.length;
        config.applyTheme(themes[next]);
    }

    private void openEditor() {
        close();
        if (client != null) {
            client.setScreen(new EditorScreen(config));
        }
    }

    // -------------------------------------------------------------------------
    // No game pause
    // -------------------------------------------------------------------------
    // shouldPause() removed in MC 1.21.3 — screens no longer pause the game by default

    // -------------------------------------------------------------------------
    // Keybinds — Escape or Right Shift closes
    // -------------------------------------------------------------------------
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE ||
            keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }
        try { return super.keyPressed(keyCode, scanCode, modifiers); } catch (Exception e) { return false; }
    }
}
