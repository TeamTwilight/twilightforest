package twilightforest.world.components.feature.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

/**
 * Follows similar structure to HugeTreeFeatureConfig
 */
public class TFTreeFeatureConfig implements FeatureConfiguration {
	public static final Codec<TFTreeFeatureConfig> codecTFTreeConfig = RecordCodecBuilder.create(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(obj -> obj.trunkProvider),
		BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(obj -> obj.leavesProvider),
		BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(obj -> obj.branchProvider),
		BlockStateProvider.CODEC.fieldOf("roots_provider").forGetter(obj -> obj.rootsProvider),
		Codec.INT.fieldOf("minimum_size").orElse(20).forGetter(obj -> obj.minHeight),
		Codec.INT.fieldOf("add_first_five_chance").orElse(1).forGetter(obj -> obj.chanceAddFiveFirst),
		Codec.INT.fieldOf("add_second_five_chance").orElse(1).forGetter(obj -> obj.chanceAddFiveSecond),
		Codec.BOOL.fieldOf("has_leaves").orElse(true).forGetter(obj -> obj.hasLeaves),
		Codec.BOOL.fieldOf("check_water").orElse(false).forGetter(obj -> obj.checkWater),
		TreeDecorator.CODEC.listOf().fieldOf("decorators").orElseGet(ImmutableList::of).forGetter(obj -> obj.decorators)
	).apply(instance, TFTreeFeatureConfig::new));

	public final BlockStateProvider trunkProvider;
	public final BlockStateProvider leavesProvider;
	public final BlockStateProvider branchProvider;
	public final BlockStateProvider rootsProvider;
	public final int minHeight;
	public final int chanceAddFiveFirst;
	public final int chanceAddFiveSecond;
	public final boolean hasLeaves;
	public final boolean checkWater;
	public transient boolean forcePlacement;
	public final List<TreeDecorator> decorators;

	public TFTreeFeatureConfig(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider branch, BlockStateProvider roots, int height, int chanceFiveFirst, int chanceFiveSecond, boolean hasLeaves, boolean checkWater, List<TreeDecorator> decorators) {
		this.trunkProvider = trunk;
		this.leavesProvider = leaves;
		this.branchProvider = branch;
		this.rootsProvider = roots;
		this.minHeight = height;
		// For some dumb reason this keeps getting -1 so you get `Math.max(x, 0)` for punishment
		this.chanceAddFiveFirst = Math.max(chanceFiveFirst, 1);
		this.chanceAddFiveSecond = Math.max(chanceFiveSecond, 1);
		this.hasLeaves = hasLeaves;
		this.checkWater = checkWater;
		this.decorators = decorators;
	}

	public void forcePlacement() {
		this.forcePlacement = true;
	}

	public static class Builder {
		private final BlockStateProvider trunkProvider;
		private final BlockStateProvider leavesProvider;
		private final BlockStateProvider branchProvider;
		private final BlockStateProvider rootsProvider;
		private int baseHeight;
		private int chanceFirstFive;
		private int chanceSecondFive;
		private boolean hasLeaves;
		private boolean checkWater;
		private final List<TreeDecorator> decorators = Lists.newArrayList();

		public Builder(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider branch, BlockStateProvider roots) {
			this.trunkProvider = trunk;
			this.leavesProvider = leaves;
			this.branchProvider = branch;
			this.rootsProvider = roots;
		}

		public TFTreeFeatureConfig.Builder minHeight(int height) {
			this.baseHeight = height;
			return this;
		}

		public TFTreeFeatureConfig.Builder chanceFirstFive(int chance) {
			this.chanceFirstFive = chance;
			return this;
		}

		public TFTreeFeatureConfig.Builder chanceSecondFive(int chance) {
			this.chanceSecondFive = chance;
			return this;
		}

		public TFTreeFeatureConfig.Builder noLeaves() {
			this.hasLeaves = false;
			return this;
		}

		public TFTreeFeatureConfig.Builder checksWater() {
			this.checkWater = true;
			return this;
		}

		public TFTreeFeatureConfig.Builder addDecorator(TreeDecorator deco) {
			decorators.add(deco);
			return this;
		}

		public TFTreeFeatureConfig build() {
			return new TFTreeFeatureConfig(trunkProvider, leavesProvider, branchProvider, rootsProvider, baseHeight, chanceFirstFive, chanceSecondFive, hasLeaves, checkWater, decorators);
		}
	}
}
