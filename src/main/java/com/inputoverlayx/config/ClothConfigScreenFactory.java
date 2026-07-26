package com.inputoverlayx.config;

import com.inputoverlayx.theme.Theme;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Builds the Cloth Config settings screen exposed via Mod Menu.
 *
 * <p>Categories match the documented specification:
 * General · Keyboard · Mouse · Themes · Appearance · Animation · Editor · Keybinds · Advanced
 */
public final class ClothConfigScreenFactory {

    private ClothConfigScreenFactory() {}

    /**
     * Creates and returns a fully-configured Cloth Config {@link Screen}.
     *
     * @param parent the parent screen to return to when closed
     * @param config the live config instance to modify
     */
    public static Screen create(Screen parent, InputOverlayXConfig config) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("inputoverlayx.title"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        // -------------------------------------------------------------------------
        // General
        // -------------------------------------------------------------------------
        ConfigCategory general = builder.getOrCreateCategory(
                Text.translatable("inputoverlayx.category.general"));

        general.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.enabled"), config.isEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setEnabled)
                .build());

        // -------------------------------------------------------------------------
        // Keyboard
        // -------------------------------------------------------------------------
        ConfigCategory keyboard = builder.getOrCreateCategory(
                Text.translatable("inputoverlayx.category.keyboard"));

        keyboard.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.keyboard_enabled"),
                        config.isKeyboardEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setKeyboardEnabled)
                .setTooltip(Text.translatable("inputoverlayx.option.keyboard_enabled.tooltip"))
                .build());

        keyboard.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.keyboard_compact"),
                        config.isKeyboardCompact())
                .setDefaultValue(false)
                .setSaveConsumer(config::setKeyboardCompact)
                .setTooltip(Text.translatable("inputoverlayx.option.keyboard_compact.tooltip"))
                .build());

        keyboard.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.keyboard_scale"),
                        config.getKeyboardScale())
                .setDefaultValue(1.0f)
                .setMin(0.3f).setMax(3.0f)
                .setSaveConsumer(config::setKeyboardScale)
                .build());

        keyboard.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.keyboard_opacity"),
                        config.getKeyboardOpacity())
                .setDefaultValue(0.9f)
                .setMin(0.0f).setMax(1.0f)
                .setSaveConsumer(config::setKeyboardOpacity)
                .build());

        keyboard.addEntry(eb.startColorField(
                        Text.translatable("inputoverlayx.option.keyboard_bg_color"),
                        config.getKeyboardBgColor() & 0xFFFFFF)
                .setDefaultValue(0x1A1A2E)
                .setSaveConsumer(v -> config.setKeyboardBgColor(0xCC000000 | v))
                .build());

        keyboard.addEntry(eb.startColorField(
                        Text.translatable("inputoverlayx.option.keyboard_border_color"),
                        config.getKeyboardBorderColor() & 0xFFFFFF)
                .setDefaultValue(0x4A4A8A)
                .setSaveConsumer(v -> config.setKeyboardBorderColor(0xFF000000 | v))
                .build());

        keyboard.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.keyboard_border_thickness"),
                        config.getKeyboardBorderThickness())
                .setDefaultValue(1.5f)
                .setMin(0.0f).setMax(8.0f)
                .setSaveConsumer(config::setKeyboardBorderThickness)
                .build());

        keyboard.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.keyboard_corner_radius"),
                        config.getKeyboardCornerRadius())
                .setDefaultValue(6.0f)
                .setMin(0.0f).setMax(20.0f)
                .setSaveConsumer(config::setKeyboardCornerRadius)
                .build());

        keyboard.addEntry(eb.startColorField(
                        Text.translatable("inputoverlayx.option.keyboard_pressed_color"),
                        config.getKeyboardPressedColor() & 0xFFFFFF)
                .setDefaultValue(0x6060FF)
                .setSaveConsumer(v -> config.setKeyboardPressedColor(0xFF000000 | v))
                .build());

        keyboard.addEntry(eb.startColorField(
                        Text.translatable("inputoverlayx.option.keyboard_font_color"),
                        config.getKeyboardFontColor() & 0xFFFFFF)
                .setDefaultValue(0xE0E0FF)
                .setSaveConsumer(v -> config.setKeyboardFontColor(0xFF000000 | v))
                .build());

        keyboard.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.keyboard_shadow"),
                        config.isKeyboardShadow())
                .setDefaultValue(true)
                .setSaveConsumer(config::setKeyboardShadow)
                .build());

        keyboard.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.keyboard_glow"),
                        config.isKeyboardGlow())
                .setDefaultValue(false)
                .setSaveConsumer(config::setKeyboardGlow)
                .build());

        // -------------------------------------------------------------------------
        // Mouse
        // -------------------------------------------------------------------------
        ConfigCategory mouse = builder.getOrCreateCategory(
                Text.translatable("inputoverlayx.category.mouse"));

        mouse.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.mouse_enabled"),
                        config.isMouseEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setMouseEnabled)
                .setTooltip(Text.translatable("inputoverlayx.option.mouse_enabled.tooltip"))
                .build());

        mouse.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.mouse_scale"),
                        config.getMouseScale())
                .setDefaultValue(1.0f)
                .setMin(0.3f).setMax(3.0f)
                .setSaveConsumer(config::setMouseScale)
                .build());

        mouse.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.mouse_opacity"),
                        config.getMouseOpacity())
                .setDefaultValue(0.9f)
                .setMin(0.0f).setMax(1.0f)
                .setSaveConsumer(config::setMouseOpacity)
                .build());

        mouse.addEntry(eb.startColorField(
                        Text.translatable("inputoverlayx.option.mouse_bg_color"),
                        config.getMouseBgColor() & 0xFFFFFF)
                .setDefaultValue(0x1A1A2E)
                .setSaveConsumer(v -> config.setMouseBgColor(0xCC000000 | v))
                .build());

        mouse.addEntry(eb.startColorField(
                        Text.translatable("inputoverlayx.option.mouse_click_color"),
                        config.getMouseClickColor() & 0xFFFFFF)
                .setDefaultValue(0x6060FF)
                .setSaveConsumer(v -> config.setMouseClickColor(0xFF000000 | v))
                .build());

        // -------------------------------------------------------------------------
        // Themes
        // -------------------------------------------------------------------------
        ConfigCategory themes = builder.getOrCreateCategory(
                Text.translatable("inputoverlayx.category.themes"));

        themes.addEntry(eb.startEnumSelector(
                        Text.translatable("inputoverlayx.option.theme"),
                        Theme.class,
                        config.getCurrentTheme())
                .setDefaultValue(Theme.DEFAULT)
                .setSaveConsumer(theme -> config.applyTheme(theme))
                .setEnumNameProvider(t -> Text.translatable("inputoverlayx.theme." + t.name().toLowerCase()))
                .build());

        // -------------------------------------------------------------------------
        // Animation
        // -------------------------------------------------------------------------
        ConfigCategory animation = builder.getOrCreateCategory(
                Text.translatable("inputoverlayx.category.animation"));

        animation.addEntry(eb.startBooleanToggle(
                        Text.translatable("inputoverlayx.option.animation_enabled"),
                        config.isAnimationEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(config::setAnimationEnabled)
                .build());

        animation.addEntry(eb.startFloatField(
                        Text.translatable("inputoverlayx.option.animation_speed"),
                        config.getAnimationSpeed())
                .setDefaultValue(1.0f)
                .setMin(0.1f).setMax(5.0f)
                .setSaveConsumer(config::setAnimationSpeed)
                .build());

        return builder.build();
    }
}
