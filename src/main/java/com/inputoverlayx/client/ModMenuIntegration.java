package com.inputoverlayx.client;

import com.inputoverlayx.config.ClothConfigScreenFactory;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Mod Menu integration — registers the config screen factory so users can open
 * the Cloth Config settings directly from the Mod Menu screen.
 */
@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ClothConfigScreenFactory.create(parent, InputOverlayXClient.getConfig());
    }
}
