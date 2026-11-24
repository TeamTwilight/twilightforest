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
		this.tag(TFBlockTags.AC_FERROMAGNETIC_BLOCKS).addTag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).addTag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).addTag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.CANDELABRA.key()).add(TFBlocks.WROUGHT_IRON_FENCE.key());
		this.tag(TFBlockTags.AC_GLOOMOTH_LIGHT_SOURCES).addAll(List.of(TFBlocks.FIREFLY_SPAWNER.key(), TFBlocks.FIREFLY_JAR.key()));
		this.tag(TFBlockTags.AC_UNDERZEALOT_LIGHT_SOURCES).addAll(List.of(TFBlocks.FIREFLY.key(), TFBlocks.MOONWORM.key()));

		this.tag(TFBlockTags.ARTIFACTS_CAMPSITE_CHESTS).addTag(TFBlockTags.TF_CHESTS);

		this.tag(TFBlockTags.CHEST_MOUNTED_STORAGE).addTag(TFBlockTags.TF_CHESTS).addAll(List.of(
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.key(),
			TFBlocks.CANOPY_TRAPPED_CHEST.key(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.key(),
			TFBlocks.DARK_TRAPPED_CHEST.key(),
			TFBlocks.TIME_TRAPPED_CHEST.key(),
			TFBlocks.TRANSFORMATION_TRAPPED_CHEST.key(),
			TFBlocks.MINING_TRAPPED_CHEST.key(),
			TFBlocks.SORTING_TRAPPED_CHEST.key()));
		this.tag(TFBlockTags.PASSIVE_BOILER_HEATERS).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);
		this.tag(TFBlockTags.TREE_ATTACHMENTS).addAll(List.of(
				TFBlocks.TIME_LOG_CORE.key(), TFBlocks.TRANSFORMATION_LOG_CORE.key(),
				TFBlocks.MINING_LOG_CORE.key(), TFBlocks.SORTING_LOG_CORE.key(),
				TFBlocks.ROOT_BLOCK.key(), TFBlocks.LIVEROOT_BLOCK.key(),
				TFBlocks.MANGROVE_ROOT.key(), TFBlocks.FIREFLY.key(), TFBlocks.CICADA.key()));

		this.tag(TFBlockTags.FD_COMPOST_ACTIVATORS).addAll(List.of(TFBlocks.UBEROUS_SOIL.key(), TFBlocks.MUSHGLOOM.key()));
		this.tag(TFBlockTags.FD_HEAT_SOURCES).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);

		this.tag(TFBlockTags.SUPPLEMENTARIES_MAP_FOLIAGE).addAll(List.of(TFBlocks.TWILIGHT_OAK_LEAVES.key(), TFBlocks.DARK_LEAVES.key(), TFBlocks.HARDENED_DARK_LEAVES.key(), TFBlocks.GIANT_LEAVES.key(), TFBlocks.FALLEN_LEAVES.key()));
		this.tag(TFBlockTags.SUPPLEMENTARIES_MAP_GRASS).add(TFBlocks.FIDDLEHEAD.key());
	}
}
