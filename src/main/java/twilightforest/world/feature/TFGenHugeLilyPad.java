package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;

import java.util.Random;

import static twilightforest.block.BlockTFHugeLilyPad.FACING;
import static twilightforest.block.BlockTFHugeLilyPad.PIECE;
import static twilightforest.enums.HugeLilypadPiece.NE;
import static twilightforest.enums.HugeLilypadPiece.NW;
import static twilightforest.enums.HugeLilypadPiece.SE;
import static twilightforest.enums.HugeLilypadPiece.SW;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class TFGenHugeLilyPad extends Feature<NoFeatureConfig> {

	public TFGenHugeLilyPad(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		for (int i = 0; i < 10; i++) {
			BlockPos dPos = pos.add(
					random.nextInt(8) - random.nextInt(8),
					random.nextInt(4) - random.nextInt(4),
					random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, dPos) && world.isAreaLoaded(dPos, 1)) {
				final Direction horizontal = Direction.byHorizontalIndex(random.nextInt(4));
				final BlockState lilypad = TFBlocks.huge_lilypad.get().getDefaultState().with(FACING, horizontal);

				world.setBlockState(dPos, lilypad.with(PIECE, NW), 16 | 2);
				world.setBlockState(dPos.east(), lilypad.with(PIECE, NE), 16 | 2);
				world.setBlockState(dPos.east().south(), lilypad.with(PIECE, SE), 16 | 2);
				world.setBlockState(dPos.south(), lilypad.with(PIECE, SW), 16 | 2);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(IWorld world, BlockPos pos) {
		return world.isAirBlock(pos) && world.getBlockState(pos.down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.east()) && world.getBlockState(pos.east().down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.south()) && world.getBlockState(pos.south().down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.east().south()) && world.getBlockState(pos.east().south().down()).getMaterial() == Material.WATER;
	}
}
