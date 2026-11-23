package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.init.TFBlocks;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public record UndergroundPlantFeature(BlockState state, int maxCount, boolean spawnInStructure) implements Feature {

	public static final MapCodec<UndergroundPlantFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("state").forGetter(UndergroundPlantFeature::state),
		Codec.INT.fieldOf("max_count").forGetter(UndergroundPlantFeature::maxCount),
		Codec.BOOL.fieldOf("spawn_in_structure").forGetter(UndergroundPlantFeature::spawnInStructure)
	).apply(instance, UndergroundPlantFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		int x = origin.getX();
		int z = origin.getZ();
		int placed = 0;

		for (int y = origin.getY(); y > level.getMinY(); y--) {
			if (placed >= maxCount)
				break;

			BlockPos pos = new BlockPos(x, y, z);
			if (!level.isEmptyBlock(pos) || random.nextInt(6) == 0) {
				x = origin.getX() + random.nextInt(4) - random.nextInt(4);
				z = origin.getZ() + random.nextInt(4) - random.nextInt(4);
				continue;
			}

			BlockState state = state();
			Optional<StructureStart> structureStart = LandmarkUtil.locateNearestLandmarkStart(level, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
			if (state.is(TFBlocks.TROLLVIDR) && random.nextInt(10) == 0)
				state = TFBlocks.UNRIPE_TROLLBER.get().defaultBlockState();
			if (state.canSurvive(level, pos) && (spawnInStructure || structureStart.isEmpty() || !structureStart.get().getBoundingBox().isInside(pos))) {
				level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				placed++;
			}
		}
		return placed > 0;
	}
}
