package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import twilightforest.TwilightForestMod;

public final class TFContextIntProviders {

	public static final ResourceKey<ContextIntProvider> COMPOSTABLE_FALLEN_LEAVES = createKey("compostable/fallen_leaves");

	private static ResourceKey<ContextIntProvider> createKey(String name) {
		return ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, TwilightForestMod.prefix(name));
	}
}
