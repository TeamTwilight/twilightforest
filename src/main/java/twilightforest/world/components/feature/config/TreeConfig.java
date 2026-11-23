package twilightforest.world.components.feature.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

public record TreeConfig(
	BlockStateProvider trunkProvider,
	BlockStateProvider leavesProvider,
	BlockStateProvider branchProvider,
	BlockStateProvider rootsProvider,
	int minHeight,
	int chanceAddFiveFirst,
	int chanceAddFiveSecond,
	boolean hasLeaves,
	boolean checkWater,
	List<TreeDecorator> decorators
) {
	public static final MapCodec<TreeConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("trunk_provider")
			.forGetter(TreeConfig::trunkProvider),
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("leaves_provider")
			.forGetter(TreeConfig::leavesProvider),
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("branch_provider")
			.forGetter(TreeConfig::branchProvider),
		BlockStateProvider.DIRECT_CODEC
			.fieldOf("roots_provider")
			.forGetter(TreeConfig::rootsProvider),
		Codec.INT
			.fieldOf("minimum_size")
			.orElse(20)
			.forGetter(TreeConfig::minHeight),
		Codec.INT
			.fieldOf("add_first_five_chance")
			.orElse(1)
			.forGetter(TreeConfig::chanceAddFiveFirst),
		Codec.INT
			.fieldOf("add_second_five_chance")
			.orElse(1)
			.forGetter(TreeConfig::chanceAddFiveSecond),
		Codec.BOOL
			.fieldOf("has_leaves")
			.orElse(true)
			.forGetter(TreeConfig::hasLeaves),
		Codec.BOOL
			.fieldOf("check_water")
			.orElse(false)
			.forGetter(TreeConfig::checkWater),
		TreeDecorator.CODEC
			.listOf()
			.fieldOf("decorators")
			.orElseGet(ImmutableList::of)
			.forGetter(TreeConfig::decorators)
	).apply(instance, TreeConfig::new));

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

		public Builder minHeight(int height) {
			this.baseHeight = height;
			return this;
		}

		public Builder chanceFirstFive(int chance) {
			this.chanceFirstFive = chance;
			return this;
		}

		public Builder chanceSecondFive(int chance) {
			this.chanceSecondFive = chance;
			return this;
		}

		public Builder noLeaves() {
			this.hasLeaves = false;
			return this;
		}

		public Builder checksWater() {
			this.checkWater = true;
			return this;
		}

		public Builder addDecorator(TreeDecorator deco) {
			decorators.add(deco);
			return this;
		}

		public TreeConfig build() {
			return new TreeConfig(trunkProvider, leavesProvider, branchProvider, rootsProvider, baseHeight, chanceFirstFive, chanceSecondFive, hasLeaves, checkWater, decorators);
		}
	}
}
