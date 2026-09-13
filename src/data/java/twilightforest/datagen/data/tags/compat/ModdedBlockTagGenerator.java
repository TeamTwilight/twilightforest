package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.concurrent.CompletableFuture;

public class ModdedBlockTagGenerator extends IntrinsicHolderTagsProvider<Block> {

	public ModdedBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.BLOCK, future, block -> block.builtInRegistryHolder().key(), TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBlockTags.AC_FERROMAGNETIC_BLOCKS).addTag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).addTag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).addTag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.CANDELABRA.get()).add(TFBlocks.WROUGHT_IRON_FENCE.get());
		this.tag(TFBlockTags.AC_GLOOMOTH_LIGHT_SOURCES).add(TFBlocks.FIREFLY_SPAWNER.get(), TFBlocks.FIREFLY_JAR.get());
		this.tag(TFBlockTags.AC_UNDERZEALOT_LIGHT_SOURCES).add(TFBlocks.FIREFLY.get(), TFBlocks.MOONWORM.get());

		this.tag(TFBlockTags.ARTIFACTS_CAMPSITE_CHESTS).addTag(TFBlockTags.TF_CHESTS);

		this.tag(TFBlockTags.CHEST_MOUNTED_STORAGE).addTag(TFBlockTags.TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(),
			TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(),
			TFBlocks.DARK_TRAPPED_CHEST.get(),
			TFBlocks.TIME_TRAPPED_CHEST.get(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(),
			TFBlocks.SORTING_TRAPPED_CHEST.get());
		this.tag(TFBlockTags.PASSIVE_BOILER_HEATERS).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);
		this.tag(TFBlockTags.TREE_ATTACHMENTS).add(
				TFBlocks.TIME_LOG_CORE.get(), TFBlocks.TRANSFORMATION_LOG_CORE.get(),
				TFBlocks.MINING_LOG_CORE.get(), TFBlocks.SORTING_LOG_CORE.get(),
				TFBlocks.ROOT_BLOCK.get(), TFBlocks.LIVEROOT_BLOCK.get(),
				TFBlocks.MANGROVE_ROOT.get(), TFBlocks.FIREFLY.get(), TFBlocks.CICADA.get());

		this.tag(TFBlockTags.FD_COMPOST_ACTIVATORS).add(TFBlocks.UBEROUS_SOIL.get(), TFBlocks.MUSHGLOOM.get());
		this.tag(TFBlockTags.FD_HEAT_SOURCES).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);
	}
}
