package twilightforest.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;
import twilightforest.util.MushroomUtil;
import twilightforest.world.TFWorld;

import java.util.Random;
import java.util.function.Function;

/**
 * Makes large mushrooms with flat mushroom tops that provide a canopy for the forest
 *
 * @author Ben
 */
public class TFGenCanopyMushroom<T extends NoFeatureConfig> extends Feature<T> {

	public BlockState treeState   = Blocks.MUSHROOM_STEM.getDefaultState().with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.UP, false);
	public BlockState branchState = Blocks.MUSHROOM_STEM.getDefaultState();
	public BlockState leafState   = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.NORTH, false).with(HugeMushroomBlock.SOUTH, false).with(HugeMushroomBlock.EAST, false).with(HugeMushroomBlock.WEST, false);
	public Block source = Blocks.RED_MUSHROOM;

//	public TFGenCanopyMushroom() {
//		this(false);
//	}
//
//	public TFGenCanopyMushroom(boolean notify) {
//		super(notify);
//		treeState   = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().with(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
//		branchState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().with(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM);
//		leafState   = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().with(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER);
//		source = Blocks.RED_MUSHROOM;
//	}

	public TFGenCanopyMushroom(Function<Dynamic<?>, T> config) {
		super(config);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos pos, T config) {
		// determine a height
		int treeHeight = 12;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		if (pos.getY() >= TFWorld.MAXHEIGHT - treeHeight - 1) {
			return false;
		}

		// check if we're on dirt or grass
		Block blockUnder = world.getBlockState(pos.down()).getBlock();
		if (blockUnder != Blocks.GRASS && blockUnder != Blocks.DIRT && blockUnder != Blocks.MYCELIUM) {
			return false;
		}

		BlockState baseState = (random.nextInt(3) == 0 ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).getDefaultState();

		leafState = baseState.with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.NORTH, false).with(HugeMushroomBlock.SOUTH, false).with(HugeMushroomBlock.EAST, false).with(HugeMushroomBlock.WEST, false);

		//okay build a tree!  Go up to the height
		buildBranch(world.getWorld(), pos, 0, treeHeight, 0, 0, true, random);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		double offset = random.nextDouble();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world.getWorld(), pos, treeHeight - 5 + b, 9, 0.3 * b + offset, 0.2, false, random);
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
	private void buildBranch(World world, BlockPos pos, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG) {
		BlockPos src = pos.up(height);
		BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 5)) {
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
	}

	/**
	 * Draw a flat blob (a circle?) of mushroom pieces
	 * <p>
	 * This assumes that the baseState you've passed in is the center variant
	 */
	private void drawMushroomCircle(World world, BlockPos pos, int rad, BlockState baseState) {
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
						world.setBlockState(pos.add(0, 0, dz), baseState);
						world.setBlockState(pos.add(0, 0, -dz), baseState);
					} else {
						world.setBlockState(pos.add(0, 0, dz), MushroomUtil.getState(MushroomUtil.Type.SOUTH, baseState));
						world.setBlockState(pos.add(0, 0, -dz), MushroomUtil.getState(MushroomUtil.Type.NORTH, baseState));
					}
				} else if (dz == 0) {
					// two!
					if (dx < rad) {
						world.setBlockState(pos.add(dx, 0, 0), baseState);
						world.setBlockState(pos.add(-dx, 0, 0), baseState);
					} else {
						world.setBlockState(pos.add(dx, 0, 0), MushroomUtil.getState(MushroomUtil.Type.EAST, baseState));
						world.setBlockState(pos.add(-dx, 0, 0), MushroomUtil.getState(MushroomUtil.Type.WEST, baseState));
					}
				} else if (dist < rad) {
					// do four at a time for easiness!
					world.setBlockState(pos.add(dx, 0, dz), baseState);
					world.setBlockState(pos.add(dx, 0, -dz), baseState);
					world.setBlockState(pos.add(-dx, 0, dz), baseState);
					world.setBlockState(pos.add(-dx, 0, -dz), baseState);
				} else if (dist == rad) {
					// do four at a time for easiness!
					world.setBlockState(pos.add(dx, 0, dz), MushroomUtil.getState(MushroomUtil.Type.SOUTH_EAST, baseState));
					world.setBlockState(pos.add(dx, 0, -dz), MushroomUtil.getState(MushroomUtil.Type.NORTH_EAST, baseState));
					world.setBlockState(pos.add(-dx, 0, dz), MushroomUtil.getState(MushroomUtil.Type.SOUTH_WEST, baseState));
					world.setBlockState(pos.add(-dx, 0, -dz), MushroomUtil.getState(MushroomUtil.Type.NORTH_WEST, baseState));
				}
			}
		}
	}

	protected void addFirefly(World world, BlockPos pos, int height, double angle) {
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

	private void setIfEmpty(World world, BlockPos pos, BlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state);
		}
	}

	/**
	 * Copied over from FeatureUtil, as this is the only place this will ever be used for now
	 */
	public static void drawBresehnam(World world, BlockPos from, BlockPos to, BlockState state) {
		for (BlockPos pixel : FeatureUtil.getBresehnamArrays(from, to)) {
			world.setBlockState(pixel, state);
		}
	}
}
