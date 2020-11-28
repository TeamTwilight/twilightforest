package twilightforest.world.feature.tree;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import twilightforest.features.TwilightFeatures;
import javax.annotation.Nullable;
import java.util.Random;

public class SmallOakTree extends Tree {

	@Nullable
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean b) {
		return TwilightFeatures.ConfiguredFeatures.TWILIGHT_OAK;
	}
}
