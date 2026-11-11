package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModdedBlockTagGenerator extends TagsProvider<Block> {

	public ModdedBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.BLOCK, future, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBlockTags.AC_FERROMAGNETIC_BLOCKS).addTag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).addTag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).addTag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.CANDELABRA.getKey()).add(TFBlocks.WROUGHT_IRON_FENCE.getKey());
		this.tag(TFBlockTags.AC_GLOOMOTH_LIGHT_SOURCES).addAll(List.of(TFBlocks.FIREFLY_SPAWNER.getKey(), TFBlocks.FIREFLY_JAR.getKey()));
		this.tag(TFBlockTags.AC_UNDERZEALOT_LIGHT_SOURCES).addAll(List.of(TFBlocks.FIREFLY.getKey(), TFBlocks.MOONWORM.getKey()));

		this.tag(TFBlockTags.ARTIFACTS_CAMPSITE_CHESTS).addTag(TFBlockTags.TF_CHESTS);

		this.tag(TFBlockTags.CHEST_MOUNTED_STORAGE).addTag(TFBlockTags.TF_CHESTS).addAll(List.of(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.getKey(),
			TFBlocks.CANOPY_TRAPPED_CHEST.getKey(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.getKey(),
			TFBlocks.DARK_TRAPPED_CHEST.getKey(),
			TFBlocks.TIME_TRAPPED_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.getKey(),
			TFBlocks.MINING_TRAPPED_CHEST.getKey(),
			TFBlocks.SORTING_TRAPPED_CHEST.getKey()));
		this.tag(TFBlockTags.PASSIVE_BOILER_HEATERS).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);
		this.tag(TFBlockTags.TREE_ATTACHMENTS).addAll(List.of(
				TFBlocks.TIME_LOG_CORE.getKey(), TFBlocks.TRANSFORMATION_LOG_CORE.getKey(),
				TFBlocks.MINING_LOG_CORE.getKey(), TFBlocks.SORTING_LOG_CORE.getKey(),
				TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(),
				TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.FIREFLY.getKey(), TFBlocks.CICADA.getKey()));

		this.tag(TFBlockTags.FD_COMPOST_ACTIVATORS).addAll(List.of(TFBlocks.UBEROUS_SOIL.getKey(), TFBlocks.MUSHGLOOM.getKey()));
		this.tag(TFBlockTags.FD_HEAT_SOURCES).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);

		this.tag(TFBlockTags.SUPPLEMENTARIES_MAP_FOLIAGE).addAll(List.of(TFBlocks.TWILIGHT_OAK_LEAVES.getKey(), TFBlocks.DARK_LEAVES.getKey(), TFBlocks.HARDENED_DARK_LEAVES.getKey(), TFBlocks.GIANT_LEAVES.getKey(), TFBlocks.FALLEN_LEAVES.getKey()));
		this.tag(TFBlockTags.SUPPLEMENTARIES_MAP_GRASS).add(TFBlocks.FIDDLEHEAD.getKey());
	}
}
