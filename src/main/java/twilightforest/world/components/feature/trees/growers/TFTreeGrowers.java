package twilightforest.world.components.feature.trees.growers;

import net.minecraft.world.level.block.grower.TreeGrower;
import twilightforest.init.TFConfiguredFeatures;

import java.util.Optional;

public class TFTreeGrowers {

	public static final TreeGrower TWILIGHT_OAK = new TreeGrower("twilight_oak", 0.1F,
		Optional.of(TFConfiguredFeatures.FOREST_MEGA_OAK_TREE),
		Optional.of(TFConfiguredFeatures.SAVANNAH_MEGA_OAK_TREE),
		Optional.of(TFConfiguredFeatures.TWILIGHT_OAK_TREE),
		Optional.of(TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE),
		Optional.empty(),
		Optional.empty());

	public static final TreeGrower CANOPY = new TreeGrower("canopy",
		Optional.of(TFConfiguredFeatures.MEGA_CANOPY_TREE),
		Optional.of(TFConfiguredFeatures.CANOPY_TREE),
		Optional.empty());

	public static final TreeGrower MANGROVE = new TreeGrower("mangrove",
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.MANGROVE_TREE),
		Optional.empty());

	public static final TreeGrower DARK = new TreeGrower("dark",
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.HOMEGROWN_DARKWOOD_TREE),
		Optional.empty());

	public static final TreeGrower TIME = new TreeGrower("time",
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.TIME_TREE),
		Optional.empty());

	public static final TreeGrower TRANSFORMATION = new TreeGrower("transformation",
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.TRANSFORMATION_TREE),
		Optional.empty());

	public static final TreeGrower MINING = new TreeGrower("mining",
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.MINING_TREE),
		Optional.empty());

	public static final TreeGrower SORTING = new TreeGrower("sorting",
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.SORTING_TREE),
		Optional.empty());

	public static final TreeGrower HOLLOW_OAK = new StructureTreeGrower("hollow_oak");

	public static final TreeGrower RAINBOW_OAK = new TreeGrower("rainbow_oak", 0.1F,
		Optional.empty(),
		Optional.empty(),
		Optional.of(TFConfiguredFeatures.RAINBOW_OAK_TREE),
		Optional.of(TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE),
		Optional.empty(),
		Optional.empty());
}
