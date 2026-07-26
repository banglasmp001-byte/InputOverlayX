package com.inputoverlayx.client;

import com.inputoverlayx.config.InputOverlayXConfig;
import com.inputoverlayx.input.InputHandler;
import com.inputoverlayx.keybind.KeybindManager;
import com.inputoverlayx.render.OverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main client entrypoint for InputOverlayX.
 * Initializes config, input handling, keybinds, and rendering.
 */
@Environment(EnvType.CLIENT)
public class InputOverlayXClient implements ClientModInitializer {

    public static final String MOD_ID   = "inputoverlayx";
    public static final String MOD_NAME = "InputOverlayX";
    public static final Logger LOGGER   = LoggerFactory.getLogger(MOD_NAME);

    private static InputOverlayXConfig config;
    private static InputHandler        inputHandler;
    private static OverlayRenderer     overlayRenderer;
    private static KeybindManager      keybindManager;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] Initializing...", MOD_NAME);

        config         = InputOverlayXConfig.load();
        inputHandler   = new InputHandler();
        overlayRenderer = new OverlayRenderer(config, inputHandler);
        keybindManager = new KeybindManager(config);
        keybindManager.register();

        // HUD render — partial tick is omitted for cross-version compatibility.
        // Animations run at 20Hz from the tick event below, which is imperceptibly
        // smooth for an input overlay. Using 1.0f avoids RenderTickCounter API drift
        // between 1.21.1 and 1.21.11.
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (config.isEnabled()) {
                overlayRenderer.render(drawContext, 1.0f);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            keybindManager.tick(client);
            overlayRenderer.tick();
        });

        LOGGER.info("[{}] Initialized successfully.", MOD_NAME);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------
    public static InputOverlayXConfig getConfig()          { return config; }
    public static InputHandler        getInputHandler()    { return inputHandler; }
    public static OverlayRenderer     getOverlayRenderer() { return overlayRenderer; }
    public static KeybindManager      getKeybindManager()  { return keybindManager; }
}
