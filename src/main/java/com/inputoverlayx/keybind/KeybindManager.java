package com.inputoverlayx.keybind;

import com.inputoverlayx.config.InputOverlayXConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Manages all custom key bindings for InputOverlayX.
 * Compatible with MC 1.21.1 through 1.21.11.
 *
 * <p>Currently registers:
 * <ul>
 *   <li><b>Open Menu</b> — default: Right Shift (changeable in Controls settings)</li>
 *   <li><b>Toggle Overlay</b> — default: none (changeable in Controls settings)</li>
 * </ul>
 */
public class KeybindManager {

    private static final String CATEGORY = "key.categories.inputoverlayx";

    private KeyBinding openMenuKey;
    private KeyBinding toggleOverlayKey;

    private final InputOverlayXConfig config;

    public KeybindManager(InputOverlayXConfig config) {
        this.config = config;
    }

    /**
     * Registers all key bindings with the Fabric keybinding API.
     * Must be called during client initialization.
     * Uses reflection to support both old (String) and new (Category) KeyBinding API.
     */
    public void register() {
        openMenuKey    = KeyBindingHelper.registerKeyBinding(createKeyBinding(
                "key.inputoverlayx.open_menu",
                GLFW.GLFW_KEY_RIGHT_SHIFT
        ));
        toggleOverlayKey = KeyBindingHelper.registerKeyBinding(createKeyBinding(
                "key.inputoverlayx.toggle_overlay",
                GLFW.GLFW_KEY_UNKNOWN
        ));
    }

    /**
     * Creates a KeyBinding compatible with both old and new MC APIs.
     * MC 1.21.1–1.21.8: KeyBinding(String, Type, int, String)
     * MC 1.21.9+:        KeyBinding(String, Type, int, KeyBinding.Category)
     */
    private KeyBinding createKeyBinding(String translationKey, int defaultKey) {
        try {
            // Try old API first (MC 1.21.1 – 1.21.8): category is a String
            return new KeyBinding(translationKey, InputUtil.Type.KEYSYM, defaultKey, CATEGORY);
        } catch (Exception | Error e) {
            // New API (MC 1.21.9+): category must be a KeyBinding.Category object
            // Use reflection to call KeyBinding.Category.of(String) or similar
            try {
                Class<?> categoryClass = Class.forName("net.minecraft.client.option.KeyBinding$Category");
                java.lang.reflect.Method ofMethod = categoryClass.getMethod("of", String.class);
                Object category = ofMethod.invoke(null, CATEGORY);
                return (KeyBinding) KeyBinding.class
                        .getConstructor(String.class, InputUtil.Type.class, int.class, categoryClass)
                        .newInstance(translationKey, InputUtil.Type.KEYSYM, defaultKey, category);
            } catch (Exception ex) {
                // Final fallback: use "misc" category
                return new KeyBinding(translationKey, InputUtil.Type.KEYSYM, defaultKey, "key.categories.misc");
            }
        }
    }

    /**
     * Checks for keybind presses each client tick and triggers the appropriate action.
     */
    public void tick(MinecraftClient client) {
        if (openMenuKey != null && openMenuKey.wasPressed()) {
            openMenu(client);
        }
        if (toggleOverlayKey != null && toggleOverlayKey.wasPressed()) {
            toggleOverlay();
        }
    }

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    private void openMenu(MinecraftClient client) {
        if (client.player == null) return;
        client.setScreen(new com.inputoverlayx.ui.QuickMenuScreen(config));
    }

    private void toggleOverlay() {
        config.setEnabled(!config.isEnabled());
        config.save();
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public KeyBinding getOpenMenuKey()      { return openMenuKey;     }
    public KeyBinding getToggleOverlayKey() { return toggleOverlayKey; }
}
