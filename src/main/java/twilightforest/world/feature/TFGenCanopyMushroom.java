package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;
import twilightforest.util.MushroomUtil;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

/**
 * Makes large mushrooms with flat mushroom tops that provide a canopy for the forest
 *
 * @author Ben
 */

// TODO Split into Separate Brown and Red Mushrooms
public class TFGenCanopyMushroom extends Feature<NoFeatureConfig> {
	public BlockState treeState   = Blocks.MUSHROOM_STEM.getDefaultState().with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.UP, false);
	public BlockState branchState = Blocks.MUSHROOM_STEM.getDefaultState();
	public BlockState leafState   = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.NORTH, false).with(HugeMushroomBlock.SOUTH, false).with(HugeMushroomBlock.EAST, false).with(HugeMushroomBlock.WEST, false);
	public Block source = Blocks.RED_MUSHROOM;

	public TFGenCanopyMushroom(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		// determine a height
		int treeHeight = 12;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		if (pos.getY() >= TFGenerationSettings.MAXHEIGHT - treeHeight - 1) {
			return false;
		}

		// check if we're on dirt or grass
		Block blockUnder = world.getBlockState(pos.down()).getBlock();
		if (blockUnder != Blocks.GRASS_BLOCK && blockUnder != Blocks.DIRT && blockUnder != Blocks.MYCELIUM) {
			return false;
		}

		BlockState baseState = (random.nextInt(3) == 0 ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).getDefaultState();

		leafState = baseState.with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.NORTH, false).with(HugeMushroomBlock.SOUTH, false).with(HugeMushroomBlock.EAST, false).with(HugeMushroomBlock.WEST, false);

		//okay build a tree!  Go up to the height
		buildBranch(world, pos, 0, treeHeight, 0, 0, true, random);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		double offset = random.nextDouble();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, pos, treeHeight - 5 + b, 9, 0.3 * b + offset, 0.2, false, random);
		}

		return true;
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 *
	 * @param height
	 * @param length
	 * @param angle
	 * @param tilt
	 */
	private void buildBranch(ISeedReader world, BlockPos pos, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG) {
		BlockPos src = pos.up(height);
		BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

		if (src.getX() != dest.getX() || src.getZ() != dest.getZ()) {
			// branch
			drawBresehnam(world, src, new BlockPos(dest.getX(), src.getY(), dest.getZ()), branchState);
			drawBresehnam(world, new BlockPos(dest.getX(), src.getY() + 1, dest.getZ()), dest.down(), treeState);
		} else {
			// trunk
			drawBresehnam(world, src, dest.down(), treeState);
		}

		if (trunk) {
			// add a firefly (torch) to the trunk
			addFirefly(world, pos, 3 + treeRNG.nextInt(7), treeRNG.nextDouble());
		}

		drawMushroomCircle(world, dest, 4, leafState);
	}

	/**
	 * Draw a flat blob (a circle?) of mushroom pieces
	 * <p>
	 * This assumes that the baseState you've passed in is the center variant
	 */
	private void drawMushroomCircle(ISeedReader world, BlockPos pos, int rad, BlockState baseState) {
		// trace out a quadrant
		for (byte dx = 0; dx <= rad; dx++) {
			for (byte dz = 0; dz <= rad; dz++) {
				int dist = (int) (Math.max(dx, dz) + (Math.min(dx, dz) * 0.5));

				//hack!  I keep getting failing leaves at a certain position.
				if (dx == 3 && dz == 3) {
					dist = 6;
				}

				// if we're inside the blob, fill it
				if (dx == 0) {
					// two!
					if (dz < rad) {
						world.setBlockState(pos.add(0, 0, dz), baseState, 3);
						world.setBlockState(pos.add(0, 0, -dz), baseState, 3);
					} else {
						world.setBlockState(pos.add(0, 0, dz), MushroomUtil.getState(MushroomUtil.Type.SOUTH, baseState), 3);
						world.setBlockState(pos.add(0, 0, -dz), MushroomUtil.getState(MushroomUtil.Type.NORTH, baseState), 3);
					}
				} else if (dz == 0) {
					// two!
					if (dx < rad) {
						world.setBlockState(pos.add(dx, 0, 0), baseState, 3);
						world.setBlockState(pos.add(-dx, 0, 0), baseState, 3);
					} else {
						world.setBlockState(pos.add(dx, 0, 0), MushroomUtil.getState(MushroomUtil.Type.EAST, baseState), 3);
						world.setBlockState(pos.add(-dx, 0, 0), MushroomUtil.getState(MushroomUtil.Type.WEST, baseState), 3);
					}
				} else if (dist < rad) {
					// do four at a time for easiness!
					world.setBlockState(pos.add(dx, 0, dz), baseState, 3);
					world.setBlockState(pos.add(dx, 0, -dz), baseState, 3);
					world.setBlockState(pos.add(-dx, 0, dz), baseState, 3);
					world.setBlockState(pos.add(-dx, 0, -dz), baseState, 3);
				} else if (dist == rad) {
					// do four at a time for easiness!
					world.setBlockState(pos.add(dx, 0, dz), MushroomUtil.getState(MushroomUtil.Type.SOUTH_EAST, baseState), 3);
					world.setBlockState(pos.add(dx, 0, -dz), MushroomUtil.getState(MushroomUtil.Type.NORTH_EAST, baseState), 3);
					world.setBlockState(pos.add(-dx, 0, dz), MushroomUtil.getState(MushroomUtil.Type.SOUTH_WEST, baseState), 3);
					world.setBlockState(pos.add(-dx, 0, -dz), MushroomUtil.getState(MushroomUtil.Type.NORTH_WEST, baseState), 3);
				}
			}
		}
	}

	protected void addFirefly(ISeedReader world, BlockPos pos, int height, double angle) {
		int iAngle = (int) (angle * 4.0);
		if (iAngle == 0) {
			setIfEmpty(world, pos.add( 1, height,  0), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.EAST));
		} else if (iAngle == 1) {
			setIfEmpty(world, pos.add(-1, height,  0), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.WEST));
		} else if (iAngle == 2) {
			setIfEmpty(world, pos.add( 0, height,  1), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.SOUTH));
		} else if (iAngle == 3) {
			setIfEmpty(world, pos.add( 0, height, -1), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.NORTH));
		}
	}

	private void setIfEmpty(ISeedReader world, BlockPos pos, BlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state, 3);
		}
	}

	/**
	 * Copied over from FeatureUtil, as this is the only place this will ever be used for now
	 */
	public static void drawBresehnam(ISeedReader world, BlockPos from, BlockPos to, BlockState state) {
		for (BlockPos pixel : FeatureUtil.getBresenhamArrays(from, to)) {
			world.setBlockState(pixel, state, 3);
		}
	}
}
