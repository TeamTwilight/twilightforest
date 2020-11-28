package twilightforest.world.feature.tree;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import twilightforest.features.TwilightFeatures;
import java.util.Random;

public class TransformationTree extends Tree {

	@Override
	public ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random rand, boolean largeHive) {
		return TwilightFeatures.ConfiguredFeatures.TRANSFORM_TREE;
	}
}
