package twilightforest.datagen.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import twilightforest.init.TFContextIntProviders;

public class TFContextIntProviderGenerator {

	public static void bootstrap(BootstrapContext<ContextIntProvider> context) {
		HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
		context.register(TFContextIntProviders.COMPOSTABLE_FALLEN_LEAVES, ContextIntProviders.compostable(blocks, 10));
	}
}
