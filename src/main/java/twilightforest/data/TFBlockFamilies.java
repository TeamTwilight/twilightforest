package twilightforest.data;

import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// For Datagen only! Avoid referencing this class too early, or the DeferredHolders will return null!
public class TFBlockFamilies {
	public static final BlockFamily TWILIGHT_OAK = familyBuilder(TFBlocks.TWILIGHT_OAK_PLANKS.value())
		.stairs(TFBlocks.TWILIGHT_OAK_STAIRS.value())
		.slab(TFBlocks.TWILIGHT_OAK_SLAB.value())
		.button(TFBlocks.TWILIGHT_OAK_BUTTON.value())
		.fence(TFBlocks.TWILIGHT_OAK_FENCE.value())
		.fenceGate(TFBlocks.TWILIGHT_OAK_GATE.value())
		.pressurePlate(TFBlocks.TWILIGHT_OAK_PLATE.value())
		.door(TFBlocks.TWILIGHT_OAK_DOOR.value())
		.trapdoor(TFBlocks.TWILIGHT_OAK_TRAPDOOR.value())
		.sign(TFBlocks.TWILIGHT_OAK_SIGN.value(), TFBlocks.TWILIGHT_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily CANOPY = familyBuilder(TFBlocks.CANOPY_PLANKS.value())
		.stairs(TFBlocks.CANOPY_STAIRS.value())
		.slab(TFBlocks.CANOPY_SLAB.value())
		.button(TFBlocks.CANOPY_BUTTON.value())
		.fence(TFBlocks.CANOPY_FENCE.value())
		.fenceGate(TFBlocks.CANOPY_GATE.value())
		.pressurePlate(TFBlocks.CANOPY_PLATE.value())
		.door(TFBlocks.CANOPY_DOOR.value())
		.trapdoor(TFBlocks.CANOPY_TRAPDOOR.value())
		.sign(TFBlocks.CANOPY_SIGN.value(), TFBlocks.CANOPY_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily MANGROVE = familyBuilder(TFBlocks.MANGROVE_PLANKS.value())
		.stairs(TFBlocks.MANGROVE_STAIRS.value())
		.slab(TFBlocks.MANGROVE_SLAB.value())
		.button(TFBlocks.MANGROVE_BUTTON.value())
		.fence(TFBlocks.MANGROVE_FENCE.value())
		.fenceGate(TFBlocks.MANGROVE_GATE.value())
		.pressurePlate(TFBlocks.MANGROVE_PLATE.value())
		.door(TFBlocks.MANGROVE_DOOR.value())
		.trapdoor(TFBlocks.MANGROVE_TRAPDOOR.value())
		.sign(TFBlocks.MANGROVE_SIGN.value(), TFBlocks.MANGROVE_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily DARKWOOD = familyBuilder(TFBlocks.DARK_PLANKS.value())
		.stairs(TFBlocks.DARK_STAIRS.value())
		.slab(TFBlocks.DARK_SLAB.value())
		.button(TFBlocks.DARK_BUTTON.value())
		.fence(TFBlocks.DARK_FENCE.value())
		.fenceGate(TFBlocks.DARK_GATE.value())
		.pressurePlate(TFBlocks.DARK_PLATE.value())
		.door(TFBlocks.DARK_DOOR.value())
		.trapdoor(TFBlocks.DARK_TRAPDOOR.value())
		.sign(TFBlocks.DARK_SIGN.value(), TFBlocks.DARK_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily TIMEWOOD = familyBuilder(TFBlocks.TIME_PLANKS.value())
		.stairs(TFBlocks.TIME_STAIRS.value())
		.slab(TFBlocks.TIME_SLAB.value())
		.button(TFBlocks.TIME_BUTTON.value())
		.fence(TFBlocks.TIME_FENCE.value())
		.fenceGate(TFBlocks.TIME_GATE.value())
		.pressurePlate(TFBlocks.TIME_PLATE.value())
		.door(TFBlocks.TIME_DOOR.value())
		.trapdoor(TFBlocks.TIME_TRAPDOOR.value())
		.sign(TFBlocks.TIME_SIGN.value(), TFBlocks.TIME_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily TRANSWOOD = familyBuilder(TFBlocks.TRANSFORMATION_PLANKS.value())
		.stairs(TFBlocks.TRANSFORMATION_STAIRS.value())
		.slab(TFBlocks.TRANSFORMATION_SLAB.value())
		.button(TFBlocks.TRANSFORMATION_BUTTON.value())
		.fence(TFBlocks.TRANSFORMATION_FENCE.value())
		.fenceGate(TFBlocks.TRANSFORMATION_GATE.value())
		.pressurePlate(TFBlocks.TRANSFORMATION_PLATE.value())
		.door(TFBlocks.TRANSFORMATION_DOOR.value())
		.trapdoor(TFBlocks.TRANSFORMATION_TRAPDOOR.value())
		.sign(TFBlocks.TRANSFORMATION_SIGN.value(), TFBlocks.TRANSFORMATION_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily MINEWOOD = familyBuilder(TFBlocks.MINING_PLANKS.value())
		.stairs(TFBlocks.MINING_STAIRS.value())
		.slab(TFBlocks.MINING_SLAB.value())
		.button(TFBlocks.MINING_BUTTON.value())
		.fence(TFBlocks.MINING_FENCE.value())
		.fenceGate(TFBlocks.MINING_GATE.value())
		.pressurePlate(TFBlocks.MINING_PLATE.value())
		.door(TFBlocks.MINING_DOOR.value())
		.trapdoor(TFBlocks.MINING_TRAPDOOR.value())
		.sign(TFBlocks.MINING_SIGN.value(), TFBlocks.MINING_WALL_SIGN.value())
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily SORTWOOD = familyBuilder(TFBlocks.SORTING_PLANKS.value())
		.stairs(TFBlocks.SORTING_STAIRS.value())
		.slab(TFBlocks.SORTING_SLAB.value())
		.button(TFBlocks.SORTING_BUTTON.value())
		.fence(TFBlocks.SORTING_FENCE.value())
		.fenceGate(TFBlocks.SORTING_GATE.value())
		.pressurePlate(TFBlocks.SORTING_PLATE.value())
		.door(TFBlocks.SORTING_DOOR.value())
		.trapdoor(TFBlocks.SORTING_TRAPDOOR.value())
		.sign(TFBlocks.SORTING_SIGN.value(), TFBlocks.SORTING_WALL_SIGN.value())
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

	@NotNull
	private static BlockFamily.Builder familyBuilder(Block baseBlock) {
		return new BlockFamily.Builder(baseBlock);
	}
}
