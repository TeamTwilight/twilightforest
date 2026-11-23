package twilightforest.world.components.feature.trees.growers;

import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import twilightforest.init.TFFeatures;

public class TFTreeGrowers {

	public static final TreeGrower TWILIGHT_OAK = new TreeGrower(
		"twilight_oak",
		WeightedList.of(
			new Weighted<>(TFFeatures.TWILIGHT_OAK_TREE, 9),
			new Weighted<>(TFFeatures.LARGE_TWILIGHT_OAK_TREE, 1)
		),
		WeightedList.of(
			new Weighted<>(TFFeatures.FOREST_MEGA_OAK_TREE, 9),
			new Weighted<>(TFFeatures.SAVANNAH_MEGA_OAK_TREE, 1)
		),
		WeightedList.of(),
		TFFeatures.TWILIGHT_OAK_TREE
	);

	public static final TreeGrower CANOPY = new TreeGrower(
		"canopy",
		WeightedList.of(TFFeatures.CANOPY_TREE),
		WeightedList.of(TFFeatures.MEGA_CANOPY_TREE),
		WeightedList.of(),
		TFFeatures.CANOPY_TREE
	);

	public static final TreeGrower MANGROVE = new TreeGrower(
		"mangrove",
		WeightedList.of(TFFeatures.MANGROVE_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.MANGROVE_TREE
	);

	public static final TreeGrower DARK = new TreeGrower(
		"dark",
		WeightedList.of(TFFeatures.HOMEGROWN_DARKWOOD_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.HOMEGROWN_DARKWOOD_TREE
	);

	public static final TreeGrower TIME = new TreeGrower(
		"time",
		WeightedList.of(TFFeatures.TIME_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.TIME_TREE
	);

	public static final TreeGrower TRANSFORMATION = new TreeGrower(
		"transformation",
		WeightedList.of(TFFeatures.TRANSFORMATION_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.TRANSFORMATION_TREE
	);

	public static final TreeGrower MINING = new TreeGrower(
		"mining",
		WeightedList.of(TFFeatures.MINING_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.MINING_TREE
	);

	public static final TreeGrower SORTING = new TreeGrower(
		"sorting",
		WeightedList.of(TFFeatures.SORTING_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.SORTING_TREE
	);

	public static final TreeGrower HOLLOW_OAK = new StructureTreeGrower("hollow_oak");

	public static final TreeGrower RAINBOW_OAK = new TreeGrower(
		"rainbow_oak",
		WeightedList.of(
			new Weighted<>(TFFeatures.RAINBOW_OAK_TREE, 9),
			new Weighted<>(TFFeatures.LARGE_RAINBOW_OAK_TREE, 1)
		),
		WeightedList.of(),
		WeightedList.of(),
		TFFeatures.RAINBOW_OAK_TREE
	);
}
