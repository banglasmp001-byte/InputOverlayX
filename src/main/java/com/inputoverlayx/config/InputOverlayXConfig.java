package com.inputoverlayx.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inputoverlayx.client.InputOverlayXClient;
import com.inputoverlayx.theme.Theme;
import com.inputoverlayx.theme.ThemePresets;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Central configuration object for InputOverlayX.
 * All fields have sensible defaults and are persisted via Gson to a JSON file
 * in the Minecraft config directory.
 */
public class InputOverlayXConfig {

    // -------------------------------------------------------------------------
    // Config file location
    // -------------------------------------------------------------------------
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("inputoverlayx.json");

    // -------------------------------------------------------------------------
    // General
    // -------------------------------------------------------------------------
    private boolean enabled = true;

    // -------------------------------------------------------------------------
    // Keyboard
    // -------------------------------------------------------------------------
    private boolean keyboardEnabled = true;
    private boolean keyboardCompact = false;
    private float keyboardX = 10.0f;
    private float keyboardY = 10.0f;
    private float keyboardScale = 1.0f;
    private float keyboardOpacity = 0.9f;
    private int keyboardBgColor = 0xCC1A1A2E;
    private int keyboardBorderColor = 0xFF4A4A8A;
    private float keyboardBorderThickness = 1.5f;
    private float keyboardCornerRadius = 6.0f;
    private float keyboardFontSize = 6.0f;
    private int keyboardFontColor = 0xFFE0E0FF;
    private int keyboardPressedColor = 0xFF6060FF;
    private int keyboardHoverColor = 0xFF3A3A6A;
    private boolean keyboardShadow = true;
    private boolean keyboardGlow = false;

    // -------------------------------------------------------------------------
    // Mouse
    // -------------------------------------------------------------------------
    private boolean mouseEnabled = true;
    private float mouseX = 10.0f;
    private float mouseY = 200.0f;
    private float mouseScale = 1.0f;
    private float mouseOpacity = 0.9f;
    private int mouseBgColor = 0xCC1A1A2E;
    private int mouseBorderColor = 0xFF4A4A8A;
    private int mouseClickColor = 0xFF6060FF;
    private boolean mouseScrollAnimation = true;

    // -------------------------------------------------------------------------
    // Theme
    // -------------------------------------------------------------------------
    private Theme currentTheme = Theme.DEFAULT;

    // -------------------------------------------------------------------------
    // Animation
    // -------------------------------------------------------------------------
    private boolean animationEnabled = true;
    private float animationSpeed = 1.0f;

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /**
     * Loads configuration from disk, or returns defaults if no file exists.
     */
    public static InputOverlayXConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                InputOverlayXConfig cfg = GSON.fromJson(reader, InputOverlayXConfig.class);
                if (cfg != null) {
                    InputOverlayXClient.LOGGER.info("[InputOverlayX] Config loaded.");
                    return cfg;
                }
            } catch (IOException | com.google.gson.JsonParseException e) {
                InputOverlayXClient.LOGGER.warn("[InputOverlayX] Failed to load config, using defaults.", e);
            }
        }
        InputOverlayXConfig defaults = new InputOverlayXConfig();
        defaults.save();
        return defaults;
    }

    /**
     * Saves the current configuration to disk.
     */
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            InputOverlayXClient.LOGGER.error("[InputOverlayX] Failed to save config.", e);
        }
    }

    /**
     * Applies a theme preset, overriding color fields but preserving positions/scales.
     */
    public void applyTheme(Theme theme) {
        this.currentTheme = theme;
        ThemePresets.apply(theme, this);
        save();
    }

    /**
     * Resets all settings to their defaults.
     */
    public void resetToDefaults() {
        InputOverlayXConfig defaults = new InputOverlayXConfig();
        this.enabled = defaults.enabled;
        this.keyboardEnabled = defaults.keyboardEnabled;
        this.keyboardCompact = defaults.keyboardCompact;
        this.keyboardScale = defaults.keyboardScale;
        this.keyboardOpacity = defaults.keyboardOpacity;
        this.keyboardBgColor = defaults.keyboardBgColor;
        this.keyboardBorderColor = defaults.keyboardBorderColor;
        this.keyboardBorderThickness = defaults.keyboardBorderThickness;
        this.keyboardCornerRadius = defaults.keyboardCornerRadius;
        this.keyboardFontSize = defaults.keyboardFontSize;
        this.keyboardFontColor = defaults.keyboardFontColor;
        this.keyboardPressedColor = defaults.keyboardPressedColor;
        this.keyboardHoverColor = defaults.keyboardHoverColor;
        this.keyboardShadow = defaults.keyboardShadow;
        this.keyboardGlow = defaults.keyboardGlow;
        this.mouseEnabled = defaults.mouseEnabled;
        this.mouseScale = defaults.mouseScale;
        this.mouseOpacity = defaults.mouseOpacity;
        this.mouseBgColor = defaults.mouseBgColor;
        this.mouseBorderColor = defaults.mouseBorderColor;
        this.mouseClickColor = defaults.mouseClickColor;
        this.mouseScrollAnimation = defaults.mouseScrollAnimation;
        this.currentTheme = defaults.currentTheme;
        this.animationEnabled = defaults.animationEnabled;
        this.animationSpeed = defaults.animationSpeed;
        save();
    }

    /**
     * Resets only keyboard and mouse positions.
     */
    public void resetPositions() {
        this.keyboardX = 10.0f;
        this.keyboardY = 10.0f;
        this.mouseX = 10.0f;
        this.mouseY = 200.0f;
        save();
    }

    // -------------------------------------------------------------------------
    // Getters and setters (boilerplate — kept readable for IDE navigation)
    // -------------------------------------------------------------------------

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean v) { enabled = v; }

    public boolean isKeyboardEnabled() { return keyboardEnabled; }
    public void setKeyboardEnabled(boolean v) { keyboardEnabled = v; }

    public boolean isKeyboardCompact() { return keyboardCompact; }
    public void setKeyboardCompact(boolean v) { keyboardCompact = v; }

    public float getKeyboardX() { return keyboardX; }
    public void setKeyboardX(float v) { keyboardX = v; }

    public float getKeyboardY() { return keyboardY; }
    public void setKeyboardY(float v) { keyboardY = v; }

    public float getKeyboardScale() { return keyboardScale; }
    public void setKeyboardScale(float v) { keyboardScale = Math.max(0.3f, Math.min(3.0f, v)); }

    public float getKeyboardOpacity() { return keyboardOpacity; }
    public void setKeyboardOpacity(float v) { keyboardOpacity = Math.max(0.0f, Math.min(1.0f, v)); }

    public int getKeyboardBgColor() { return keyboardBgColor; }
    public void setKeyboardBgColor(int v) { keyboardBgColor = v; }

    public int getKeyboardBorderColor() { return keyboardBorderColor; }
    public void setKeyboardBorderColor(int v) { keyboardBorderColor = v; }

    public float getKeyboardBorderThickness() { return keyboardBorderThickness; }
    public void setKeyboardBorderThickness(float v) { keyboardBorderThickness = Math.max(0.0f, Math.min(8.0f, v)); }

    public float getKeyboardCornerRadius() { return keyboardCornerRadius; }
    public void setKeyboardCornerRadius(float v) { keyboardCornerRadius = Math.max(0.0f, Math.min(20.0f, v)); }

    public float getKeyboardFontSize() { return keyboardFontSize; }
    public void setKeyboardFontSize(float v) { keyboardFontSize = Math.max(4.0f, Math.min(14.0f, v)); }

    public int getKeyboardFontColor() { return keyboardFontColor; }
    public void setKeyboardFontColor(int v) { keyboardFontColor = v; }

    public int getKeyboardPressedColor() { return keyboardPressedColor; }
    public void setKeyboardPressedColor(int v) { keyboardPressedColor = v; }

    public int getKeyboardHoverColor() { return keyboardHoverColor; }
    public void setKeyboardHoverColor(int v) { keyboardHoverColor = v; }

    public boolean isKeyboardShadow() { return keyboardShadow; }
    public void setKeyboardShadow(boolean v) { keyboardShadow = v; }

    public boolean isKeyboardGlow() { return keyboardGlow; }
    public void setKeyboardGlow(boolean v) { keyboardGlow = v; }

    public boolean isMouseEnabled() { return mouseEnabled; }
    public void setMouseEnabled(boolean v) { mouseEnabled = v; }

    public float getMouseX() { return mouseX; }
    public void setMouseX(float v) { mouseX = v; }

    public float getMouseY() { return mouseY; }
    public void setMouseY(float v) { mouseY = v; }

    public float getMouseScale() { return mouseScale; }
    public void setMouseScale(float v) { mouseScale = Math.max(0.3f, Math.min(3.0f, v)); }

    public float getMouseOpacity() { return mouseOpacity; }
    public void setMouseOpacity(float v) { mouseOpacity = Math.max(0.0f, Math.min(1.0f, v)); }

    public int getMouseBgColor() { return mouseBgColor; }
    public void setMouseBgColor(int v) { mouseBgColor = v; }

    public int getMouseBorderColor() { return mouseBorderColor; }
    public void setMouseBorderColor(int v) { mouseBorderColor = v; }

    public int getMouseClickColor() { return mouseClickColor; }
    public void setMouseClickColor(int v) { mouseClickColor = v; }

    public boolean isMouseScrollAnimation() { return mouseScrollAnimation; }
    public void setMouseScrollAnimation(boolean v) { mouseScrollAnimation = v; }

    public Theme getCurrentTheme() { return currentTheme; }
    public void setCurrentTheme(Theme v) { currentTheme = v; }

    public boolean isAnimationEnabled() { return animationEnabled; }
    public void setAnimationEnabled(boolean v) { animationEnabled = v; }

    public float getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(float v) { animationSpeed = Math.max(0.1f, Math.min(5.0f, v)); }
}
