package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;

import java.util.Random;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class TFGenHugeWaterLily extends Feature<NoFeatureConfig> {

	public TFGenHugeWaterLily(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		for (int i = 0; i < 4; i++) {
			BlockPos pos_ = pos.add(
					random.nextInt(8) - random.nextInt(8),
					random.nextInt(4) - random.nextInt(4),
					random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, pos_)) {
				world.setBlockState(pos_, TFBlocks.huge_waterlily.get().getDefaultState(), 16 | 2);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(IWorld world, BlockPos pos) {
		return world.isAirBlock(pos) && world.getBlockState(pos.down()).getMaterial() == Material.WATER;
	}
}
