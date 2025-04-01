package twilightforest.util;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// For Datagen only! Avoid referencing this class too early, or the DeferredHolders will return null!
public class TFBlockFamilies {

	private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

	public static final BlockFamily TWILIGHT_OAK = familyBuilder(TFBlocks.TWILIGHT_OAK_PLANKS.get())
		.stairs(TFBlocks.TWILIGHT_OAK_STAIRS.get())
		.slab(TFBlocks.TWILIGHT_OAK_SLAB.get())
		.button(TFBlocks.TWILIGHT_OAK_BUTTON.get())
		.fence(TFBlocks.TWILIGHT_OAK_FENCE.get())
		.fenceGate(TFBlocks.TWILIGHT_OAK_GATE.get())
		.pressurePlate(TFBlocks.TWILIGHT_OAK_PLATE.get())
		.door(TFBlocks.TWILIGHT_OAK_DOOR.get())
		.trapdoor(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get())
		.sign(TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.TWILIGHT_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily CANOPY = familyBuilder(TFBlocks.CANOPY_PLANKS.get())
		.stairs(TFBlocks.CANOPY_STAIRS.get())
		.slab(TFBlocks.CANOPY_SLAB.get())
		.button(TFBlocks.CANOPY_BUTTON.get())
		.fence(TFBlocks.CANOPY_FENCE.get())
		.fenceGate(TFBlocks.CANOPY_GATE.get())
		.pressurePlate(TFBlocks.CANOPY_PLATE.get())
		.door(TFBlocks.CANOPY_DOOR.get())
		.trapdoor(TFBlocks.CANOPY_TRAPDOOR.get())
		.sign(TFBlocks.CANOPY_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily MANGROVE = familyBuilder(TFBlocks.MANGROVE_PLANKS.get())
		.stairs(TFBlocks.MANGROVE_STAIRS.get())
		.slab(TFBlocks.MANGROVE_SLAB.get())
		.button(TFBlocks.MANGROVE_BUTTON.get())
		.fence(TFBlocks.MANGROVE_FENCE.get())
		.fenceGate(TFBlocks.MANGROVE_GATE.get())
		.pressurePlate(TFBlocks.MANGROVE_PLATE.get())
		.door(TFBlocks.MANGROVE_DOOR.get())
		.trapdoor(TFBlocks.MANGROVE_TRAPDOOR.get())
		.sign(TFBlocks.MANGROVE_SIGN.get(), TFBlocks.MANGROVE_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily DARKWOOD = familyBuilder(TFBlocks.DARK_PLANKS.get())
		.stairs(TFBlocks.DARK_STAIRS.get())
		.slab(TFBlocks.DARK_SLAB.get())
		.button(TFBlocks.DARK_BUTTON.get())
		.fence(TFBlocks.DARK_FENCE.get())
		.fenceGate(TFBlocks.DARK_GATE.get())
		.pressurePlate(TFBlocks.DARK_PLATE.get())
		.door(TFBlocks.DARK_DOOR.get())
		.trapdoor(TFBlocks.DARK_TRAPDOOR.get())
		.sign(TFBlocks.DARK_SIGN.get(), TFBlocks.DARK_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily TIMEWOOD = familyBuilder(TFBlocks.TIME_PLANKS.get())
		.stairs(TFBlocks.TIME_STAIRS.get())
		.slab(TFBlocks.TIME_SLAB.get())
		.button(TFBlocks.TIME_BUTTON.get())
		.fence(TFBlocks.TIME_FENCE.get())
		.fenceGate(TFBlocks.TIME_GATE.get())
		.pressurePlate(TFBlocks.TIME_PLATE.get())
		.door(TFBlocks.TIME_DOOR.get())
		.trapdoor(TFBlocks.TIME_TRAPDOOR.get())
		.sign(TFBlocks.TIME_SIGN.get(), TFBlocks.TIME_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily TRANSWOOD = familyBuilder(TFBlocks.TRANSFORMATION_PLANKS.get())
		.stairs(TFBlocks.TRANSFORMATION_STAIRS.get())
		.slab(TFBlocks.TRANSFORMATION_SLAB.get())
		.button(TFBlocks.TRANSFORMATION_BUTTON.get())
		.fence(TFBlocks.TRANSFORMATION_FENCE.get())
		.fenceGate(TFBlocks.TRANSFORMATION_GATE.get())
		.pressurePlate(TFBlocks.TRANSFORMATION_PLATE.get())
		.door(TFBlocks.TRANSFORMATION_DOOR.get())
		.trapdoor(TFBlocks.TRANSFORMATION_TRAPDOOR.get())
		.sign(TFBlocks.TRANSFORMATION_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily MINEWOOD = familyBuilder(TFBlocks.MINING_PLANKS.get())
		.stairs(TFBlocks.MINING_STAIRS.get())
		.slab(TFBlocks.MINING_SLAB.get())
		.button(TFBlocks.MINING_BUTTON.get())
		.fence(TFBlocks.MINING_FENCE.get())
		.fenceGate(TFBlocks.MINING_GATE.get())
		.pressurePlate(TFBlocks.MINING_PLATE.get())
		.door(TFBlocks.MINING_DOOR.get())
		.trapdoor(TFBlocks.MINING_TRAPDOOR.get())
		.sign(TFBlocks.MINING_SIGN.get(), TFBlocks.MINING_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily SORTWOOD = familyBuilder(TFBlocks.SORTING_PLANKS.get())
		.stairs(TFBlocks.SORTING_STAIRS.get())
		.slab(TFBlocks.SORTING_SLAB.get())
		.button(TFBlocks.SORTING_BUTTON.get())
		.fence(TFBlocks.SORTING_FENCE.get())
		.fenceGate(TFBlocks.SORTING_GATE.get())
		.pressurePlate(TFBlocks.SORTING_PLATE.get())
		.door(TFBlocks.SORTING_DOOR.get())
		.trapdoor(TFBlocks.SORTING_TRAPDOOR.get())
		.sign(TFBlocks.SORTING_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static void verifyFamilyShapes(BlockFamily family, BlockFamily.Variant... required) {
		List<BlockFamily.Variant> missing = findMissingFamilyShapes(family, required);

		if (!missing.isEmpty())
			TwilightForestMod.LOGGER.warn("BlockFamily " + family + " for " + family.getBaseBlock() + " is missing variants for " + missing);
	}

	public static List<BlockFamily.Variant> findMissingFamilyShapes(BlockFamily family, BlockFamily.Variant... required) {
		ArrayList<BlockFamily.Variant> available = new ArrayList<>(Arrays.asList(required));
		available.removeAll(family.getVariants().keySet());
		return available;
	}

	private static BlockFamily.Builder familyBuilder(Block baseBlock) {
		BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
		BlockFamily blockfamily = MAP.put(baseBlock, builder.getFamily());
		if (blockfamily != null) {
			throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(baseBlock));
		} else {
			return builder;
		}
	}

	public static Stream<BlockFamily> getAllFamilies() {
		return MAP.values().stream();
	}
}
