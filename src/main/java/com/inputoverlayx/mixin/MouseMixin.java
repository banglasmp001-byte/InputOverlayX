package com.inputoverlayx.mixin;

import com.inputoverlayx.client.InputOverlayXClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts raw GLFW mouse button and scroll-wheel events from Minecraft's
 * {@link Mouse} class and forwards them to {@link InputOverlayXClient#getInputHandler()}.
 *
 * <p>All injections are at HEAD and are non-cancelling — normal Minecraft
 * mouse handling is never affected.
 *
 * <p>Method descriptor notes:
 * <ul>
 *   <li>{@code onMouseButton} signature is stable across 1.21.x.</li>
 *   <li>{@code onMouseScroll} gained an extra {@code boolean} parameter in some
 *       1.21.x snapshots. We use a descriptor-less match so Mixin resolves it
 *       automatically regardless of the exact overload present.</li>
 * </ul>
 */
@Mixin(Mouse.class)
public class MouseMixin {

    /**
     * Captures mouse button press and release events.
     *
     * @param window GLFW window handle
     * @param button GLFW button index (0=left, 1=right, 2=middle)
     * @param action 0=release, 1=press
     * @param mods   modifier bitmask (unused)
     */
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void inputOverlayX$onMouseButton(long window, int button, int action, int mods,
                                              CallbackInfo ci) {
        if (InputOverlayXClient.getInputHandler() == null) return;

        if (action == 1) { // GLFW_PRESS
            InputOverlayXClient.getInputHandler().onMousePress(button);
        } else if (action == 0) { // GLFW_RELEASE
            InputOverlayXClient.getInputHandler().onMouseRelease(button);
        }
    }

    /**
     * Captures scroll-wheel events.
     *
     * <p>The extra {@code boolean} parameter present in some 1.21.x builds is
     * handled by Mixin's argument coercion — we declare only the parameters we
     * actually need, relying on the method name match.
     *
     * @param window   GLFW window handle
     * @param horizontal horizontal scroll (usually 0 for standard mice)
     * @param vertical  vertical scroll — positive = up, negative = down
     */
    @Inject(method = "onMouseScroll", at = @At("HEAD"))
    private void inputOverlayX$onMouseScroll(long window, double horizontal, double vertical,
                                              CallbackInfo ci) {
        if (InputOverlayXClient.getInputHandler() == null) return;
        InputOverlayXClient.getInputHandler().onScroll(vertical);
    }
}
