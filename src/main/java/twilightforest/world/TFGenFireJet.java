package twilightforest.world;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFFireJet;
import twilightforest.block.TFBlocks;
import twilightforest.enums.FireJetVariant;

import java.util.Random;

public class TFGenFireJet extends TFGenerator {
	private final FireJetVariant variant;

	public TFGenFireJet(FireJetVariant variant) {
		this.variant = variant;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		for (int var6 = 0; var6 < 4; ++var6) {
			BlockPos dPos = pos.add(
					rand.nextInt(8) - rand.nextInt(8),
					rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8)
			);

			if (world.isAirBlock(dPos) && world.canSeeSky(dPos) && world.getBlockState(dPos.down()).getMaterial() == Material.GRASS
					&& world.getBlockState(dPos.east().down()).getMaterial() == Material.GRASS && world.getBlockState(dPos.west().down()).getMaterial() == Material.GRASS
					&& world.getBlockState(dPos.south().down()).getMaterial() == Material.GRASS && world.getBlockState(dPos.north().down()).getMaterial() == Material.GRASS) {
				// jet
				world.setBlockState(dPos.down(), TFBlocks.fire_jet.getDefaultState().withProperty(BlockTFFireJet.VARIANT, variant), 0);

				// create reservoir with stone walls
				for (int rx = -2; rx <= 2; rx++) {
					for (int rz = -2; rz <= 2; rz++) {
						BlockPos dPos2 = dPos.add(rx, -2, rz);
						if ((rx == 1 || rx == 0 || rx == -1) && (rz == 1 || rz == 0 || rz == -1)) {
							// lava reservoir
							world.setBlockState(dPos2, Blocks.FLOWING_LAVA.getDefaultState(), 0);
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
