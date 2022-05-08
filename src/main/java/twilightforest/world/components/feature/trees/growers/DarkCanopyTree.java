package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import twilightforest.world.registration.features.TFConfiguredFeatures;

import java.util.Random;

public class DarkCanopyTree extends AbstractTreeGrower {

	@Override
	public Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random rand, boolean largeHive) {
		return TFConfiguredFeatures.HOMEGROWN_DARKWOOD_TREE;
	}
}
