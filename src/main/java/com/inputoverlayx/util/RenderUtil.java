package com.inputoverlayx.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;

/**
 * Lightweight rendering helpers for the overlay system.
 *
 * <p>All coordinate units are in Minecraft GUI pixels (scaled screen pixels).
 * The rendering is done via {@link DrawContext} to stay compatible with the
 * standard Minecraft rendering pipeline without bypassing any RenderSystem
 * state management.
 */
public final class RenderUtil {

    private RenderUtil() {}

    // -------------------------------------------------------------------------
    // Filled rectangles
    // -------------------------------------------------------------------------

    /**
     * Draws a solid filled rectangle.
     */
    public static void fillRect(DrawContext ctx, float x, float y, float w, float h, int color) {
        ctx.fill((int) x, (int) y, (int) (x + w), (int) (y + h), color);
    }

    /**
     * Draws a filled rectangle with rounded corners using a step approximation.
     * Renders up to {@code segments} horizontal slices to simulate corner rounding.
     *
     * @param radius corner radius in GUI pixels (clamped to half of min dimension)
     */
    public static void fillRoundRect(DrawContext ctx, float x, float y, float w, float h,
                                     float radius, int color) {
        if (radius <= 0.0f) {
            fillRect(ctx, x, y, w, h, color);
            return;
        }

        radius = Math.min(radius, Math.min(w, h) / 2.0f);

        // Central rectangle (no corner rounding needed)
        fillRect(ctx, x + radius, y, w - radius * 2, h, color);
        // Left strip
        fillRect(ctx, x, y + radius, radius, h - radius * 2, color);
        // Right strip
        fillRect(ctx, x + w - radius, y + radius, radius, h - radius * 2, color);

        // Fill corner arcs with pixel-by-pixel approximation
        int iRadius = (int) Math.ceil(radius);
        for (int i = 0; i < iRadius; i++) {
            for (int j = 0; j < iRadius; j++) {
                float cx = i + 0.5f;
                float cy = j + 0.5f;
                if (cx * cx + cy * cy <= radius * radius) {
                    // Top-left corner
                    ctx.fill((int)(x + iRadius - i - 1), (int)(y + iRadius - j - 1),
                             (int)(x + iRadius - i),     (int)(y + iRadius - j), color);
                    // Top-right corner
                    ctx.fill((int)(x + w - iRadius + i), (int)(y + iRadius - j - 1),
                             (int)(x + w - iRadius + i + 1), (int)(y + iRadius - j), color);
                    // Bottom-left corner
                    ctx.fill((int)(x + iRadius - i - 1), (int)(y + h - iRadius + j),
                             (int)(x + iRadius - i),     (int)(y + h - iRadius + j + 1), color);
                    // Bottom-right corner
                    ctx.fill((int)(x + w - iRadius + i), (int)(y + h - iRadius + j),
                             (int)(x + w - iRadius + i + 1), (int)(y + h - iRadius + j + 1), color);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Borders
    // -------------------------------------------------------------------------

    /**
     * Draws a hollow rounded-rectangle border (outline only).
     *
     * @param thickness border line thickness in GUI pixels
     */
    public static void drawRoundRectBorder(DrawContext ctx, float x, float y, float w, float h,
                                           float radius, float thickness, int color) {
        if (thickness <= 0.0f) return;
        int t = Math.max(1, (int) Math.ceil(thickness));

        // Top edge
        fillRect(ctx, x + radius, y, w - radius * 2, t, color);
        // Bottom edge
        fillRect(ctx, x + radius, y + h - t, w - radius * 2, t, color);
        // Left edge
        fillRect(ctx, x, y + radius, t, h - radius * 2, color);
        // Right edge
        fillRect(ctx, x + w - t, y + radius, t, h - radius * 2, color);

        // Corner arcs (approximate with circular pixel test)
        float r2 = radius * radius;
        float ri2 = (radius - t) * (radius - t);
        int iRadius = (int) Math.ceil(radius);
        for (int i = 0; i < iRadius; i++) {
            for (int j = 0; j < iRadius; j++) {
                float cx = i + 0.5f;
                float cy = j + 0.5f;
                float dist2 = cx * cx + cy * cy;
                if (dist2 <= r2 && dist2 >= ri2) {
                    ctx.fill((int)(x + iRadius - i - 1), (int)(y + iRadius - j - 1),
                             (int)(x + iRadius - i),     (int)(y + iRadius - j), color);
                    ctx.fill((int)(x + w - iRadius + i), (int)(y + iRadius - j - 1),
                             (int)(x + w - iRadius + i + 1), (int)(y + iRadius - j), color);
                    ctx.fill((int)(x + iRadius - i - 1), (int)(y + h - iRadius + j),
                             (int)(x + iRadius - i),     (int)(y + h - iRadius + j + 1), color);
                    ctx.fill((int)(x + w - iRadius + i), (int)(y + h - iRadius + j),
                             (int)(x + w - iRadius + i + 1), (int)(y + h - iRadius + j + 1), color);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Shadow
    // -------------------------------------------------------------------------

    /**
     * Draws a simple drop-shadow beneath a rectangle by rendering a series of
     * progressively more transparent, slightly offset rectangles.
     */
    public static void drawShadow(DrawContext ctx, float x, float y, float w, float h,
                                  float radius, int depth) {
        for (int i = depth; i > 0; i--) {
            int alpha = (int) (60.0f * (1.0f - (float) i / depth));
            int shadowColor = ColorUtil.argb(alpha, 0, 0, 0);
            fillRoundRect(ctx, x + i, y + i, w, h, radius, shadowColor);
        }
    }
}
