package twilightforest.world.components.feature.trees.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import twilightforest.init.TFFeatureModifiers;

import java.util.List;
import java.util.function.BiConsumer;

public class TrunkRiser extends TrunkPlacer {
	public static final MapCodec<TrunkRiser> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.intRange(0, 16).fieldOf("offset_up").forGetter(o -> o.offset),
		TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(o -> o.placer)
	).apply(instance, TrunkRiser::new));

	private final int offset;
	private final TrunkPlacer placer;

	public TrunkRiser(int baseHeight, TrunkPlacer placer) {
		super(placer.baseHeight, placer.heightRandA, placer.heightRandB);

		this.offset = baseHeight;
		this.placer = placer;
	}

	@Override
	protected TrunkPlacerType<TrunkRiser> type() {
		return TFFeatureModifiers.TRUNK_RISER.get();
	}

	@Override
	public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader worldReader, BiConsumer<BlockPos, BlockState> worldPlacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration treeConfig) {
		return this.placer.placeTrunk(worldReader, worldPlacer, random, height, startPos.above(this.offset), treeConfig);
	}
}
