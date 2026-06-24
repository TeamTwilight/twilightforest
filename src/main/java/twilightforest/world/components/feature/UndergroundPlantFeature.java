package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.init.TFBlocks;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public class UndergroundPlantFeature extends Feature<BlockStateConfiguration> {
	int maxCount;
	boolean spawnInStructure;

	public UndergroundPlantFeature(Codec<BlockStateConfiguration> config, int maxCount) {
		super(config);
		this.maxCount = maxCount;
		this.spawnInStructure = false;

	}

	public UndergroundPlantFeature(Codec<BlockStateConfiguration> config) {
		super(config);
		this.maxCount = Integer.MAX_VALUE;
		this.spawnInStructure = false;

	}

	public UndergroundPlantFeature(Codec<BlockStateConfiguration> config, int maxCount, boolean spawnInStructure) {
		super(config);
		this.maxCount = maxCount;
		this.spawnInStructure = spawnInStructure;
	}

	public UndergroundPlantFeature(Codec<BlockStateConfiguration> config, boolean spawnInStructure) {
		super(config);
		this.maxCount = Integer.MAX_VALUE;
		this.spawnInStructure = spawnInStructure;
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos origin = ctx.origin();
		RandomSource random = ctx.random();

		int x = origin.getX();
		int z = origin.getZ();
		int placed = 0;

		for (int y = origin.getY(); y > world.getMinY(); y--) {
			if (placed >= maxCount)
				break;

			BlockPos pos = new BlockPos(x, y, z);
			if (!world.isEmptyBlock(pos) || random.nextInt(6) == 0) {
				x = origin.getX() + random.nextInt(4) - random.nextInt(4);
				z = origin.getZ() + random.nextInt(4) - random.nextInt(4);
				continue;
			}

			BlockState state = ctx.config().state;
			Optional<StructureStart> structureStart = LandmarkUtil.locateNearestLandmarkStart(world, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
			if (state.is(TFBlocks.TROLLVIDR) && random.nextInt(10) == 0)
				state = TFBlocks.UNRIPE_TROLLBER.get().defaultBlockState();
			if (state.canSurvive(world, pos) && (spawnInStructure || structureStart.isEmpty() || !structureStart.get().getBoundingBox().isInside(pos))) {
				world.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				placed++;
			}
		}
		return placed > 0;
	}
}
