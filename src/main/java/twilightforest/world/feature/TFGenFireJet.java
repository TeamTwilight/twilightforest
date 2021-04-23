package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import twilightforest.util.FeatureUtil;

import java.util.Random;

public class TFGenFireJet extends Feature<BlockStateFeatureConfig> {

	public TFGenFireJet(Codec<BlockStateFeatureConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config) {

		if(!FeatureUtil.isAreaSuitable(world, rand, pos, 5, 2, 5)) return false;

		for (int i = 0; i < 4; ++i) {
			BlockPos dPos = pos.add(
					rand.nextInt(8) - rand.nextInt(8),
					rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8)
			);

			if (world.isAirBlock(dPos) && world.canBlockSeeSky(dPos) && world.getBlockState(dPos.down()).getMaterial() == Material.ORGANIC
					&& world.getBlockState(dPos.east().down()).getMaterial() == Material.ORGANIC && world.getBlockState(dPos.west().down()).getMaterial() == Material.ORGANIC
					&& world.getBlockState(dPos.south().down()).getMaterial() == Material.ORGANIC && world.getBlockState(dPos.north().down()).getMaterial() == Material.ORGANIC) {

				//create blocks around the jet/smoker, just in case
				for (int gx = -2; gx <= 2; gx++) {
					for (int gz = -2; gz <= 2; gz++) {
						BlockPos grassPos = dPos.add(gx, -1, gz);
						world.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 0);
					}
				}

				// jet
				world.setBlockState(dPos.down(), config.state, 0);

				// create reservoir with stone walls
				for (int rx = -2; rx <= 2; rx++) {
					for (int rz = -2; rz <= 2; rz++) {
						BlockPos dPos2 = dPos.add(rx, -2, rz);
						if ((rx == 1 || rx == 0 || rx == -1) && (rz == 1 || rz == 0 || rz == -1)) {
							// lava reservoir
							world.setBlockState(dPos2, Blocks.LAVA.getDefaultState(), 0);
						} else if (world.getBlockState(dPos2).getMaterial() != Material.LAVA) {
							// only stone where there is no lava
							world.setBlockState(dPos2, Blocks.STONE.getDefaultState(), 0);
						}
						world.setBlockState(dPos2.down(), Blocks.STONE.getDefaultState(), 0);
					}
				}
			}
		}

		return true;
	}
}
