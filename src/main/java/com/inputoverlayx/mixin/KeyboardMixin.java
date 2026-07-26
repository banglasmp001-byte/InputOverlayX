package com.inputoverlayx.mixin;

import com.inputoverlayx.client.InputOverlayXClient;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts raw GLFW key events before they reach Minecraft's input pipeline.
 *
 * <p>We hook into {@code onKey} (the raw GLFW key callback) rather than
 * key-binding events so that <em>every</em> key — including those not bound to
 * any action — is captured and forwarded to {@link InputOverlayXClient#getInputHandler()}.
 *
 * <p>GLFW action constants:
 * <ul>
 *   <li>0 = GLFW_RELEASE</li>
 *   <li>1 = GLFW_PRESS</li>
 *   <li>2 = GLFW_REPEAT</li>
 * </ul>
 */
@Mixin(Keyboard.class)
public class KeyboardMixin {

    /**
     * Injected at the very start of the key callback method.
     *
     * @param window   GLFW window handle (unused)
     * @param key      GLFW key constant (e.g. {@code GLFW.GLFW_KEY_A})
     * @param scancode hardware scancode (unused)
     * @param action   0=release, 1=press, 2=repeat
     * @param modifiers bitmask of held modifier keys (unused)
     * @param ci       callback info (not cancelled — we are observers only)
     */
    @Inject(method = "onKey", at = @At("HEAD"))
    private void inputOverlayX$onKey(long window, int key, int scancode, int action, int modifiers,
                                     CallbackInfo ci) {
        if (InputOverlayXClient.getInputHandler() == null) return;

        if (action == 1) { // GLFW_PRESS
            InputOverlayXClient.getInputHandler().onKeyPress(key);
        } else if (action == 0) { // GLFW_RELEASE
            InputOverlayXClient.getInputHandler().onKeyRelease(key);
        }
        // GLFW_REPEAT (action == 2) is ignored — the key is already marked pressed
    }
}
