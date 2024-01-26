package twilightforest.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.forge.REIPluginDedicatedServer;
import twilightforest.TwilightForestMod;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;

@REIPluginDedicatedServer
public class TFREIServerPlugin implements REIServerPlugin {
	public static final CategoryIdentifier<REIUncraftingDisplay> UNCRAFTING = CategoryIdentifier.of(TwilightForestMod.ID, "uncrafting");

	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		registry.register(UNCRAFTING, REIUncraftingDisplay.Serializer.INSTANCE);
	}
}
