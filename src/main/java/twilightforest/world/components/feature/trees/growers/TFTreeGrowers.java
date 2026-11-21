package twilightforest.world.components.feature.trees.growers;

import net.minecraft.world.level.block.grower.TreeGrower;
import twilightforest.init.TFFeatures;

import java.util.Optional;

public class TFTreeGrowers {

	public static final TreeGrower TWILIGHT_OAK = new TreeGrower("twilight_oak", 0.1F,
		Optional.of(TFFeatures.FOREST_MEGA_OAK_TREE),
		Optional.of(TFFeatures.SAVANNAH_MEGA_OAK_TREE),
		Optional.of(TFFeatures.TWILIGHT_OAK_TREE),
		Optional.of(TFFeatures.LARGE_TWILIGHT_OAK_TREE),
		Optional.empty(),
		Optional.empty());

	public static final TreeGrower CANOPY = new TreeGrower("canopy",
		Optional.of(TFFeatures.MEGA_CANOPY_TREE),
		Optional.of(TFFeatures.CANOPY_TREE),
		Optional.empty());

	public static final TreeGrower MANGROVE = new TreeGrower("mangrove",
		Optional.empty(),
		Optional.of(TFFeatures.MANGROVE_TREE),
		Optional.empty());

	public static final TreeGrower DARK = new TreeGrower("dark",
		Optional.empty(),
		Optional.of(TFFeatures.HOMEGROWN_DARKWOOD_TREE),
		Optional.empty());

	public static final TreeGrower TIME = new TreeGrower("time",
		Optional.empty(),
		Optional.of(TFFeatures.TIME_TREE),
		Optional.empty());

	public static final TreeGrower TRANSFORMATION = new TreeGrower("transformation",
		Optional.empty(),
		Optional.of(TFFeatures.TRANSFORMATION_TREE),
		Optional.empty());

	public static final TreeGrower MINING = new TreeGrower("mining",
		Optional.empty(),
		Optional.of(TFFeatures.MINING_TREE),
		Optional.empty());

	public static final TreeGrower SORTING = new TreeGrower("sorting",
		Optional.empty(),
		Optional.of(TFFeatures.SORTING_TREE),
		Optional.empty());

	public static final TreeGrower HOLLOW_OAK = new StructureTreeGrower("hollow_oak");

	public static final TreeGrower RAINBOW_OAK = new TreeGrower("rainbow_oak", 0.1F,
		Optional.empty(),
		Optional.empty(),
		Optional.of(TFFeatures.RAINBOW_OAK_TREE),
		Optional.of(TFFeatures.LARGE_RAINBOW_OAK_TREE),
		Optional.empty(),
		Optional.empty());
}
