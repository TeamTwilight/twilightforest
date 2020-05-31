package twilightforest.world.feature.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraftforge.common.IPlantable;

import java.util.List;

/**
 * Follows similar structure to HugeTreeFeatureConfig
 */
public class TFTreeFeatureConfig extends BaseTreeFeatureConfig {
	public final BlockStateProvider branchProvider;
	public final BlockStateProvider rootsProvider;
	public final int chanceAddFiveFirst;
	public final int chanceAddFiveSecond;
	public final boolean hasLeaves;
	public final boolean checkWater;

	public TFTreeFeatureConfig(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider branch, BlockStateProvider roots, List<TreeDecorator> decorators, int height, int chanceFiveFirst, int chanceFiveSecond, boolean hasLeaves, boolean checkWater) {
		super (trunk, leaves, decorators, height);
		this.branchProvider = branch;
		this.rootsProvider = roots;
		this.chanceAddFiveFirst = chanceFiveFirst;
		this.chanceAddFiveSecond = chanceFiveSecond;
		this.hasLeaves = hasLeaves;
		this.checkWater = checkWater;
	}

	@Override
	protected TFTreeFeatureConfig setSapling(IPlantable sapling) {
		super.setSapling(sapling);
		return this;
	}

	@Override
	public <T> Dynamic<T> serialize(DynamicOps<T> dynOps) {
		ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();

		builder.put(dynOps.createString("branch_provider"), this.branchProvider.serialize(dynOps))
				.put(dynOps.createString("roots_provider"), this.rootsProvider.serialize(dynOps))
				.put(dynOps.createString("add_first_five_chance"), dynOps.createInt(chanceAddFiveFirst))
				.put(dynOps.createString("add_second_five_chance"), dynOps.createInt(chanceAddFiveSecond))
				.put(dynOps.createString("has_leaves"), dynOps.createBoolean(hasLeaves))
				.put(dynOps.createString("check_water"), dynOps.createBoolean(checkWater));
		Dynamic<T> dynamic = new Dynamic<>(dynOps, dynOps.createMap(builder.build()));
		return dynamic.merge(super.serialize(dynOps));
	}

	public static <T> TFTreeFeatureConfig deserialize(Dynamic<T> data) {
		BlockStateProviderType<?> branchState = Registry.field_229387_t_.getOrDefault(new ResourceLocation(data.get("branch_provider").get("type").asString().orElseThrow(RuntimeException::new)));
		BlockStateProviderType<?> rootsState = Registry.field_229387_t_.getOrDefault(new ResourceLocation(data.get("roots_provider").get("type").asString().orElseThrow(RuntimeException::new)));
		int chanceFiveFirst = data.get("add_first_five_chance").asInt(-1);
		int chanceFiveSecond = data.get("add_second_five_chance").asInt(-1);
		boolean genLeaves = data.get("has_leaves").asBoolean(true);
		boolean checkWater = data.get("check_water").asBoolean(false);

		BaseTreeFeatureConfig config = BaseTreeFeatureConfig.deserialize(data);
		return new TFTreeFeatureConfig(
				config.trunkProvider,
				config.leavesProvider,
				branchState.deserialize(data.get("branch_provider").orElseEmptyMap()),
				rootsState.deserialize(data.get("roots_provider").orElseEmptyMap()),
				config.decorators,
				config.baseHeight,
				chanceFiveFirst,
				chanceFiveSecond,
				genLeaves,
				checkWater
		);
	}

	public static class Builder extends BaseTreeFeatureConfig.Builder {
		private BlockStateProvider branchProvider;
		private BlockStateProvider rootsProvider;
		private List<TreeDecorator> decorators = ImmutableList.of();
		private int baseHeight;
		private int chanceFirstFive;
		private int chanceSecondFive;
		private boolean hasLeaves = true;
		private boolean checkWater = false;

		public Builder(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider branch, BlockStateProvider roots) {
			super(trunk, leaves);
			this.branchProvider = branch;
			this.rootsProvider = roots;
		}

		@Override
		public TFTreeFeatureConfig.Builder baseHeight(int height) {
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

		@Override
		public TFTreeFeatureConfig.Builder setSapling(IPlantable sapling) {
			super.setSapling(sapling);
			return this;
		}

		@Override
		public TFTreeFeatureConfig build() {
			return new TFTreeFeatureConfig(trunkProvider, leavesProvider, branchProvider, rootsProvider, decorators, baseHeight, chanceFirstFive, chanceSecondFive, hasLeaves, checkWater);
		}
	}
}
