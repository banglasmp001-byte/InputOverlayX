package com.inputoverlayx.keybind;

import com.inputoverlayx.config.InputOverlayXConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Manages all custom key bindings for InputOverlayX.
 *
 * <p>Currently registers:
 * <ul>
 *   <li><b>Open Menu</b> — default: Right Shift</li>
 * </ul>
 *
 * <p>The keybind triggers the control panel / quick-access menu to open
 * without pausing the game.
 */
public class KeybindManager {

    private static final String CATEGORY = "key.categories.inputoverlayx";

    /** The keybind that opens the quick-access floating menu (default: Right Shift). */
    private KeyBinding openMenuKey;

    private final InputOverlayXConfig config;

    public KeybindManager(InputOverlayXConfig config) {
        this.config = config;
    }

    /**
     * Registers all key bindings with the Fabric keybinding API.
     * Must be called during client initialization.
     */
    public void register() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.inputoverlayx.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBindingHelper.createCategory(CATEGORY)
        ));
    }

    /**
     * Checks for keybind presses each client tick and triggers the appropriate action.
     *
     * @param client the current Minecraft client instance
     */
    public void tick(MinecraftClient client) {
        if (openMenuKey != null && openMenuKey.wasPressed()) {
            openMenu(client);
        }
    }

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    private void openMenu(MinecraftClient client) {
        if (client.player == null) return;
        // Open the quick menu as a non-pausing overlay screen
        client.setScreen(new com.inputoverlayx.ui.QuickMenuScreen(config));
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public KeyBinding getOpenMenuKey() { return openMenuKey; }
}
