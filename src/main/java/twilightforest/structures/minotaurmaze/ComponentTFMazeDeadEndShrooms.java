package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeDeadEndShrooms extends ComponentTFMazeDeadEndRoots {

	public ComponentTFMazeDeadEndShrooms() {
		super();
	}

	public ComponentTFMazeDeadEndShrooms(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// no door

		// mycelium & mushrooms going back
		for (int x = 1; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				if (rand.nextInt(z + 2) > 0) {
					this.setBlockState(world, Blocks.MYCELIUM.getDefaultState(), x, 0, z, sbb);
				}
				if (rand.nextInt(z + 2) > 0) {
					this.setBlockState(world, rand.nextBoolean() ? Blocks.RED_MUSHROOM.getDefaultState() : Blocks.BROWN_MUSHROOM.getDefaultState(), x, 1, z, sbb);
				}
			}
		}

		// brackets?
		IBlockState mushType = (rand.nextBoolean() ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).getDefaultState();
		int mushY = rand.nextInt(4) + 1;
		int mushZ = rand.nextInt(3) + 1;
		this.setBlockState(world, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM), 1, mushY - 1, mushZ, sbb);
		this.fillWithBlocks(world, sbb, 1, 1, mushZ, 1, mushY, mushZ, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), AIR, false);
		this.fillWithBlocks(world, sbb, 1, mushY, mushZ - 1, 2, mushY, mushZ + 1, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE), AIR, false);

		mushType = (mushType == Blocks.BROWN_MUSHROOM_BLOCK ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).getDefaultState();
		mushY = rand.nextInt(4) + 1;
		mushZ = rand.nextInt(3) + 1;
		this.fillWithBlocks(world, sbb, 4, 1, mushZ, 4, mushY, mushZ, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), AIR, false);
		this.fillWithBlocks(world, sbb, 3, mushY, mushZ - 1, 4, mushY, mushZ + 1, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE), AIR, false);

		mushType = (rand.nextBoolean() ? Blocks.RED_MUSHROOM_BLOCK : Blocks.BROWN_MUSHROOM_BLOCK).getDefaultState();
		mushY = rand.nextInt(4) + 1;
		int mushX = rand.nextInt(3) + 2;
		this.fillWithBlocks(world, sbb, mushX, 1, 4, mushX, mushY, 4, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), AIR, false);
		this.fillWithBlocks(world, sbb, mushX - 1, mushY, 3, mushX + 1, mushY, 4, mushType.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE), AIR, false);

		return true;
	}
}
