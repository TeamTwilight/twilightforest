package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.entity.passive.Raven;
import twilightforest.init.TFEntities;
import twilightforest.util.features.FeatureUtil;

/**
 * A 2x2 monolith of obsidian
 *
 * @author Ben
 */
public record MonolithFeature() implements Feature {

	public static final MapCodec<MonolithFeature> MAP_CODEC = MapCodec.unit(MonolithFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		int ht = randomSource.nextInt(10) + 10;
		int dir = randomSource.nextInt(4);
		int h0, h1, h2, h3;

		if (!FeatureUtil.isAreaSuitable(worldGenLevel, blockPos, 2, ht, 2)) {
			return false;
		}

		switch (dir) {
			case 0 -> {
				h0 = ht;
				h1 = (int) (ht * .75);
				h2 = (int) (ht * .75);
				h3 = (int) (ht * .5);
			}
			case 1 -> {
				h0 = (int) (ht * .5);
				h1 = ht;
				h2 = (int) (ht * .75);
				h3 = (int) (ht * .75);
			}
			case 2 -> {
				h0 = (int) (ht * .75);
				h1 = (int) (ht * .5);
				h2 = ht;
				h3 = (int) (ht * .75);
			}
			default -> {
				h0 = (int) (ht * .75);
				h1 = (int) (ht * .75);
				h2 = (int) (ht * .5);
				h3 = ht;
			}
		}

		for (int cy = 0; cy <= h0; cy++) {
			worldGenLevel.setBlock(blockPos.offset(0, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}
		for (int cy = 0; cy <= h1; cy++) {
			worldGenLevel.setBlock(blockPos.offset(1, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}
		for (int cy = 0; cy <= h2; cy++) {
			worldGenLevel.setBlock(blockPos.offset(0, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}
		for (int cy = 0; cy <= h3; cy++) {
			worldGenLevel.setBlock(blockPos.offset(1, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}

		// spawn a few ravens nearby
		for (int i = 0; i < 2; i++) {
			BlockPos dPos = blockPos.offset(
				randomSource.nextInt(8) - randomSource.nextInt(8),
				0,
				randomSource.nextInt(8) - randomSource.nextInt(8)
			);
			dPos = worldGenLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dPos);

			if (dPos.getY() > 0) {
				Raven raven = TFEntities.RAVEN.get().create(worldGenLevel.getLevel(), EntitySpawnReason.STRUCTURE);
				raven.snapTo(dPos, randomSource.nextFloat() * 360.0F, 0.0F);

				worldGenLevel.addFreshEntity(raven);
			}
		}

		return true;
	}
}
