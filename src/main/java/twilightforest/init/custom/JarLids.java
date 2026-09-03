package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import twilightforest.TFRegistries;
import twilightforest.block.JarLid;
import twilightforest.init.TFBlocks;

public final class JarLids {
	public static void bootstrap(BootstrapContext<JarLid> context) {
		register(context, Items.ACACIA_LOG);
		register(context, Items.BIRCH_LOG);
		register(context, Items.CHERRY_LOG);
		register(context, Items.DARK_OAK_LOG);
		register(context, Items.JUNGLE_LOG);
		register(context, Items.MANGROVE_LOG);
		register(context, Items.OAK_LOG);
		register(context, Items.SPRUCE_LOG);
		register(context, Items.CRIMSON_STEM);
		register(context, Items.WARPED_STEM);
		register(context, Items.PALE_OAK_LOG);
		register(context, Items.STRIPPED_ACACIA_LOG);
		register(context, Items.STRIPPED_BIRCH_LOG);
		register(context, Items.STRIPPED_CHERRY_LOG);
		register(context, Items.STRIPPED_DARK_OAK_LOG);
		register(context, Items.STRIPPED_JUNGLE_LOG);
		register(context, Items.STRIPPED_MANGROVE_LOG);
		register(context, Items.STRIPPED_OAK_LOG);
		register(context, Items.STRIPPED_SPRUCE_LOG);
		register(context, Items.STRIPPED_CRIMSON_STEM);
		register(context, Items.STRIPPED_WARPED_STEM);
		register(context, Items.STRIPPED_PALE_OAK_LOG);

		register(context, Items.PUMPKIN);
		register(context, Items.BAMBOO_BLOCK);
		register(context, Items.STRIPPED_BAMBOO_BLOCK);

		register(context, TFBlocks.MANGROVE_LOG.asItem());
		register(context, TFBlocks.CANOPY_LOG.asItem());
		register(context, TFBlocks.DARK_LOG.asItem());
		register(context, TFBlocks.MINING_LOG.asItem());
		register(context, TFBlocks.SORTING_LOG.asItem());
		register(context, TFBlocks.TIME_LOG.asItem());
		register(context, TFBlocks.TRANSFORMATION_LOG.asItem());
		register(context, TFBlocks.TWILIGHT_OAK_LOG.asItem());
		register(context, TFBlocks.CINDER_LOG.asItem());
		register(context, TFBlocks.STRIPPED_MANGROVE_LOG.asItem());
		register(context, TFBlocks.STRIPPED_CANOPY_LOG.asItem());
		register(context, TFBlocks.STRIPPED_DARK_LOG.asItem());
		register(context, TFBlocks.STRIPPED_MINING_LOG.asItem());
		register(context, TFBlocks.STRIPPED_SORTING_LOG.asItem());
		register(context, TFBlocks.STRIPPED_TIME_LOG.asItem());
		register(context, TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem());
		register(context, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem());
	}

	private static ResourceKey<JarLid> makeKey(Item item) {
		return ResourceKey.create(
			TFRegistries.Keys.JAR_LIDS,
			item.builtInRegistryHolder().key().identifier()
		);
	}

	private static void register(BootstrapContext<JarLid> context, Item item) {
		ResourceKey<Item> itemKey = item.builtInRegistryHolder().key();
		context.register(
			makeKey(item),
			new JarLid(
				itemKey
			)
		);
	}
}