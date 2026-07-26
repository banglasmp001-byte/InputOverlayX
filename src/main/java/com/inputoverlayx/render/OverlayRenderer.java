package com.inputoverlayx.render;

import com.inputoverlayx.animation.AnimationSystem;
import com.inputoverlayx.config.InputOverlayXConfig;
import com.inputoverlayx.input.InputHandler;
import com.inputoverlayx.render.keyboard.KeyboardRenderer;
import com.inputoverlayx.render.mouse.MouseRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;

/**
 * Top-level coordinator for all overlay rendering.
 *
 * <p>Owns the shared {@link AnimationSystem} and delegates to
 * {@link KeyboardRenderer} and {@link MouseRenderer}. Tick updates propagate
 * from here so callers only need to reference this single object.
 */
@Environment(EnvType.CLIENT)
public class OverlayRenderer {

    private final InputOverlayXConfig config;
    private final AnimationSystem animation;
    private final KeyboardRenderer keyboardRenderer;
    private final MouseRenderer mouseRenderer;

    public OverlayRenderer(InputOverlayXConfig config, InputHandler inputHandler) {
        this.config    = config;
        this.animation = new AnimationSystem(config);
        this.keyboardRenderer = new KeyboardRenderer(config, inputHandler, animation);
        this.mouseRenderer    = new MouseRenderer(config, inputHandler, animation);
    }

    // -------------------------------------------------------------------------
    // Lifecycle hooks
    // -------------------------------------------------------------------------

    /**
     * Must be called once per client tick (20 Hz) to advance animation state.
     */
    public void tick() {
        animation.tick();
        keyboardRenderer.tick();
        mouseRenderer.tick();
    }

    /**
     * Renders the overlay onto the HUD.
     *
     * @param ctx         Minecraft draw context
     * @param partialTick fractional tick value for sub-tick interpolation
     */
    public void render(DrawContext ctx, float partialTick) {
        if (config.isKeyboardEnabled()) {
            keyboardRenderer.render(ctx, partialTick);
        }
        if (config.isMouseEnabled()) {
            mouseRenderer.render(ctx, partialTick);
        }
    }

    // -------------------------------------------------------------------------
    // Accessors (used by editor and menu)
    // -------------------------------------------------------------------------

    public KeyboardRenderer getKeyboardRenderer() { return keyboardRenderer; }
    public MouseRenderer    getMouseRenderer()    { return mouseRenderer;    }
    public AnimationSystem  getAnimation()        { return animation;        }
}
