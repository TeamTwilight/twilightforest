package twilightforest.structures;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.TFBlockProperties;
import twilightforest.block.TFBlocks;

import java.util.Random;

import static twilightforest.block.BlockTFNagastone.VARIANT;
import static twilightforest.block.enums.NagastoneVariant.*;


public class ComponentTFNagaCourtyard extends StructureTFComponent {

	static int RADIUS = 46;
	static int DIAMETER = 2 * RADIUS + 1;


	public ComponentTFNagaCourtyard() {
		super();
	}

	public ComponentTFNagaCourtyard(TFFeature feature, World world, Random rand, int i, int x, int y, int z) {
		super(feature, i);

		this.setCoordBaseMode(EnumFacing.SOUTH);

		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, -RADIUS, -1, -RADIUS, RADIUS * 2, 10, RADIUS * 2, EnumFacing.SOUTH);

	}

	/**
	 * At the moment, this just draws the whole courtyard as a giant 93x93 block every time.  We may break this up properly later.
	 * <p>
	 * TODO: what I just said
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		IBlockState nagaStone = TFBlocks.nagastone.getDefaultState();
		// add a broken up courtyard
		for (int fx = 0; fx <= DIAMETER; fx++) {
			for (int fz = 0; fz <= DIAMETER; fz++) {
				if (rand.nextInt(3) == 0) {
					setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), fx, 0, fz, sbb);

					// put some half slabs around
					if (rand.nextInt(20) == 0) {
						setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), fx, 1, fz, sbb);
					} else {
						setBlockState(world, Blocks.AIR.getDefaultState(), fx, 1, fz, sbb);  // clear out grass or flowers
					}
				} else {
					setBlockState(world, Blocks.GRASS.getDefaultState(), fx, 0, fz, sbb);
				}
			}
		}

		// fence!  except not a fence, more of a wall
		for (int fx = 0; fx <= DIAMETER; fx++) {
			randomBrick(world, rand, fx, 0, DIAMETER, sbb);
			randomBrick(world, rand, fx, 0, 0, sbb);
			randomBrick(world, rand, fx, 1, DIAMETER, sbb);
			randomBrick(world, rand, fx, 1, 0, sbb);
			randomBrick(world, rand, fx, 2, DIAMETER, sbb);
			randomBrick(world, rand, fx, 2, 0, sbb);
			randomBrick(world, rand, fx, 3, DIAMETER, sbb);
			randomBrick(world, rand, fx, 3, 0, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), fx, 4, DIAMETER, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), fx, 4, 0, sbb);


			// make nagastone pattern!
			switch (fx % 23) {
				case 2:
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_DOWN), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_DOWN), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_UP), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_UP), fx, 1, 0, sbb);
					break;
				case 3:
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_HEAD), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_HEAD), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 1, 0, sbb);
					break;
				case 4:
				case 8:
				case 16:
				case 20:
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 1, 0, sbb);
					break;
				case 5:
				case 9:
				case 17:
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_DOWN), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_DOWN), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_UP), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_UP), fx, 1, 0, sbb);
					break;
				case 6:
				case 10:
				case 14:
				case 18:
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 3, 0, sbb);
					break;
				case 7:
				case 15:
				case 19:
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_DOWN), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_DOWN), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_UP), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_UP), fx, 1, 0, sbb);
					break;
				case 11:
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_DOWN), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_DOWN), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 1, 0, sbb);
					break;
				case 13:
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_DOWN), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_DOWN), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 1, 0, sbb);
					break;
				case 21:
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_HEAD), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_HEAD), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_X), fx, 1, 0, sbb);
					break;
				case 22:
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_DOWN), fx, 3, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, WEST_DOWN), fx, 3, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), fx, 2, 0, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_UP), fx, 1, DIAMETER, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, EAST_UP), fx, 1, 0, sbb);
					break;
			}

//			if (fx % 2 == 0) {
//				setBlockState(world, Blocks.STONE_SLAB, 5, fx, 4, DIAMETER, sbb);
//				setBlockState(world, Blocks.STONEBRICK, 0, fx, 4, 0, sbb);
//			}
//			else
//			{
//				setBlockState(world, Blocks.STONE_SLAB, 5, fx, 4, 0, sbb);
//				setBlockState(world, Blocks.STONEBRICK, 0, fx, 4, DIAMETER, sbb);
//			}
		}

		for (int fz = 0; fz <= DIAMETER; fz++) {
			randomBrick(world, rand, DIAMETER, 0, fz, sbb);
			randomBrick(world, rand, 0, 0, fz, sbb);
			randomBrick(world, rand, DIAMETER, 1, fz, sbb);
			randomBrick(world, rand, 0, 1, fz, sbb);
			randomBrick(world, rand, DIAMETER, 2, fz, sbb);
			randomBrick(world, rand, 0, 2, fz, sbb);
			randomBrick(world, rand, DIAMETER, 3, fz, sbb);
			randomBrick(world, rand, 0, 3, fz, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), DIAMETER, 4, fz, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), 0, 4, fz, sbb);

			// make nagastone pattern!
			switch (fz % 23) {
				case 2:
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_DOWN), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_DOWN), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_UP), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_UP), 0, 1, fz, sbb);
					break;
				case 3:
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_HEAD), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_HEAD), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), 0, 1, fz, sbb);
					break;
				case 4:
				case 8:
				case 16:
				case 20:
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), 0, 1, fz, sbb);
					break;
				case 5:
				case 9:
				case 17:
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_DOWN), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_DOWN), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_UP), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_UP), 0, 1, fz, sbb);
					break;
				case 6:
				case 10:
				case 14:
				case 18:
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), 0, 3, fz, sbb);
					break;
				case 7:
				case 15:
				case 19:
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_DOWN), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_DOWN), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_UP), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_UP), 0, 1, fz, sbb);
					break;
				case 11:
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_DOWN), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_DOWN), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 1, fz, sbb);
					break;
				case 13:
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_DOWN), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, SOUTH_DOWN), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 1, fz, sbb);
					break;
				case 21:
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_HEAD), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_HEAD), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Y), 0, 1, fz, sbb);
					break;
				case 22:
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_DOWN), DIAMETER, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_DOWN), 0, 3, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), DIAMETER, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, AXIS_Z), 0, 2, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_UP), DIAMETER, 1, fz, sbb);
					setBlockState(world, nagaStone.withProperty(VARIANT, NORTH_UP), 0, 1, fz, sbb);
					break;
			}

//			if (fz % 2 == 0) {
//				setBlockState(world, Blocks.STONE_SLAB, 5, DIAMETER, 4, fz, sbb);
//				setBlockState(world, Blocks.STONEBRICK, 0, 0, 4, fz, sbb);
//			}
//			else
//			{
//				setBlockState(world, Blocks.STONE_SLAB, 5, 0, 4, fz, sbb);
//				setBlockState(world, Blocks.STONEBRICK, 0, DIAMETER, 4, fz, sbb);
//			}
		}

		// make a new rand here because we keep getting different results and this actually matters... or should the pillars be different StructureComponents?
		Random pillarRand = new Random(world.getSeed() + this.boundingBox.minX * this.boundingBox.minZ);

		// pick a few spots and make pillars
		for (int i = 0; i < 20; i++) {
			int rx = 2 + pillarRand.nextInt(DIAMETER - 4);
			int rz = 2 + pillarRand.nextInt(DIAMETER - 4);

			makePillar(world, pillarRand, rx, 1, rz, sbb);
		}


		// naga spawner seems important
		setBlockState(world, TFBlocks.bossSpawner.getDefaultState(), RADIUS + 1, 2, RADIUS + 1, sbb);

		return true;
	}

	/**
	 * Make a pillar out of stone brick
	 */
	public boolean makePillar(World world, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
		int height = 8;

		// make the base
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x - 1, y + 0, z - 1, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 0, y + 0, z - 1, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 1, y + 0, z - 1, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x - 1, y + 0, z + 0, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 1, y + 0, z + 0, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x - 1, y + 0, z + 1, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 0, y + 0, z + 1, sbb);
		setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 1, y + 0, z + 1, sbb);

		// make the pillar
		for (int i = 0; i < height; i++) {
			randomBrick(world, rand, x, y + i, z, sbb);
			if (i > 0 && rand.nextInt(2) == 0) {
				// vines?
				switch (rand.nextInt(4)) {
					case 0:
						setBlockState(world, Blocks.VINE.getDefaultState().withProperty(BlockVine.WEST, true), x - 1, y + i, z + 0, sbb);
						break;
					case 1:
						setBlockState(world, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, true), x + 1, y + i, z + 0, sbb);
						break;
					case 2:
						setBlockState(world, Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, true), x + 0, y + i, z - 1, sbb);
						break;
					case 3:
						setBlockState(world, Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, true), x + 0, y + i, z + 1, sbb);
						break;
				}
			} else if (i > 0 && rand.nextInt(4) == 0) {
				// fireflies!
				switch (rand.nextInt(4)) {
					case 0:
						setBlockState(world, TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.WEST), x - 1, y + i, z + 0, sbb);
						break;
					case 1:
						setBlockState(world, TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.EAST), x + 1, y + i, z + 0, sbb);
						break;
					case 2:
						setBlockState(world, TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.NORTH), x + 0, y + i, z - 1, sbb);
						break;
					case 3:
						setBlockState(world, TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.SOUTH), x + 0, y + i, z + 1, sbb);
						break;
				}
			}
		}

		// top?
		if (height == 8) {
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x - 1, y + 8, z - 1, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 0, y + 8, z - 1, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 1, y + 8, z - 1, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x - 1, y + 8, z + 0, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), x + 0, y + 8, z + 0, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 1, y + 8, z + 0, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x - 1, y + 8, z + 1, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 0, y + 8, z + 1, sbb);
			setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), x + 1, y + 8, z + 1, sbb);


		}

		return true;
	}

	/**
	 * Places a random stone brick at the specified location
	 */
	private void randomBrick(World world, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
		BlockStoneBrick.EnumType variant = BlockStoneBrick.EnumType.values()[rand.nextInt(3)];
		setBlockState(world, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, variant), x, y, z, sbb);
	}


}
