package twilightforest.world.feature.tree;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import twilightforest.worldgen.ConfiguredFeatures;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

public class MinersTree extends TFTree {

	@Override
	public ConfiguredFeature<TFTreeFeatureConfig, ?> createTreeFeature() {
		return ConfiguredFeatures.MINING_TREE_BASE;
	}
}
