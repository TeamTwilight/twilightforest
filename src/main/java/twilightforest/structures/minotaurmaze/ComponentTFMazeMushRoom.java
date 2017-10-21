package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeMushRoom extends ComponentTFMazeRoom {

	public ComponentTFMazeMushRoom() {
		super();
	}


	public ComponentTFMazeMushRoom(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(feature, i, rand, x, y, z);

		this.setCoordBaseMode(EnumFacing.SOUTH); // let's just make this easy on us?
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		for (int x = 1; x < 14; x++) {
			for (int z = 1; z < 14; z++) {
				// calculate distance from middle
				int dist = (int) Math.round(7 / Math.sqrt((7.5 - x) * (7.5 - x) + (7.5 - z) * (7.5 - z)));

				// make part of the floor mycelium
				if (rand.nextInt(dist + 1) > 0) {
					this.setBlockState(world, Blocks.MYCELIUM.getDefaultState(), x, 0, z, sbb);
				}
				// add small mushrooms all over
				if (rand.nextInt(dist) > 0) {
					this.setBlockState(world, (rand.nextBoolean() ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM).getDefaultState(), x, 1, z, sbb);
				}
			}
		}

		final IBlockState redMushroomBlock = Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
		final IBlockState brownMushroomBlock = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();

		// add our medium mushrooms

		makeMediumMushroom(world, sbb, 5, 2, 9, redMushroomBlock);
		makeMediumMushroom(world, sbb, 5, 3, 9, redMushroomBlock);
		makeMediumMushroom(world, sbb, 9, 2, 5, redMushroomBlock);
		makeMediumMushroom(world, sbb, 6, 3, 4, brownMushroomBlock);
		makeMediumMushroom(world, sbb, 10, 1, 9, brownMushroomBlock);

		// bracket mushrooms on the wall
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM), 1, 2, 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER), 1, 3, 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST), 2, 3, 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST), 1, 3, 2, sbb);

		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM), 14, 3, 1, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER), 14, 4, 1, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST), 13, 4, 1, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST), 14, 4, 2, sbb);

		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM), 1, 1, 14, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER), 1, 2, 14, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST), 2, 2, 14, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST), 1, 2, 13, sbb);

		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER), 14, 1, 14, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST), 13, 1, 14, sbb);
		this.setBlockState(world, brownMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST), 14, 1, 13, sbb);

		// mushroom ceiling spots?

		return true;
	}

	/**
	 * Make a 3x3 square mushroom centered on the specified coords.
	 */
	private void makeMediumMushroom(World world, StructureBoundingBox sbb, int mx, int my, int mz, IBlockState redMushroomBlock) {
		// cap
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER), mx + 0, my, mz + 0, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.WEST), mx + 1, my, mz + 0, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST), mx + 1, my, mz + 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH), mx + 0, my, mz + 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST), mx - 1, my, mz + 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.EAST), mx - 1, my, mz + 0, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST), mx - 1, my, mz - 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH), mx + 0, my, mz - 1, sbb);
		this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST), mx + 1, my, mz - 1, sbb);

		// stem
		for (int y = 1; y < my; y++) {
			this.setBlockState(world, redMushroomBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), mx + 0, y, mz + 0, sbb);
		}
	}
}
