package twilightforest.world.components.feature.trees.growers;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import twilightforest.world.registration.features.TFConfiguredFeatures;

import javax.annotation.Nullable;
import java.util.Random;

public class SmallOakTree extends AbstractTreeGrower {

	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean b) {
		return TFConfiguredFeatures.TWILIGHT_OAK_TREE;
	}
}
