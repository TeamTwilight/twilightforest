package twilightforest.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;

import java.util.concurrent.CompletableFuture;

public class ModdedBlockTagGenerator extends IntrinsicHolderTagsProvider<Block> {

	public static final TagKey<Block> AC_FERROMAGNETIC_BLOCKS = createTagFor("alexscaves", "ferromagnetic_blocks");
	public static final TagKey<Block> AC_GLOOMOTH_LIGHT_SOURCES = createTagFor("alexscaves", "gloomoth_light_sources");
	public static final TagKey<Block> AC_UNDERZEALOT_LIGHT_SOURCES = createTagFor("alexscaves", "underzealot_light_sources");

	public static final TagKey<Block> ARTIFACTS_CAMPSITE_CHESTS = createTagFor("artifacts", "campsite_chests");

	public static final TagKey<Block> FD_COMPOST_ACTIVATORS = createTagFor("farmersdelight", "compost_activators");
	public static final TagKey<Block> FD_HEAT_SOURCES = createTagFor("farmersdelight", "heat_sources");

	public ModdedBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
		super(output, Registries.BLOCK, future, block -> block.builtInRegistryHolder().key(), TwilightForestMod.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(AC_FERROMAGNETIC_BLOCKS).addTag(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD).addTag(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF).addTag(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.CANDELABRA.get()).add(TFBlocks.WROUGHT_IRON_FENCE.get());
		tag(AC_GLOOMOTH_LIGHT_SOURCES).add(TFBlocks.FIREFLY_SPAWNER.get(), TFBlocks.FIREFLY_JAR.get());
		tag(AC_UNDERZEALOT_LIGHT_SOURCES).add(TFBlocks.FIREFLY.get(), TFBlocks.MOONWORM.get());

		tag(ARTIFACTS_CAMPSITE_CHESTS).addTag(BlockTagGenerator.TF_CHESTS);

		tag(FD_COMPOST_ACTIVATORS).add(TFBlocks.UBEROUS_SOIL.get(), TFBlocks.MUSHGLOOM.get());
		tag(FD_HEAT_SOURCES).addTag(BlockTagGenerator.STORAGE_BLOCKS_FIERY);
	}

	private static TagKey<Block> createTagFor(String modid, String tagName) {
		return BlockTags.create(new ResourceLocation(modid, tagName));
	}
}
