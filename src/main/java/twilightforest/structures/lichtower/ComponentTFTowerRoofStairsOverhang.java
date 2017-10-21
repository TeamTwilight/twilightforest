package twilightforest.structures.lichtower;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;


public class ComponentTFTowerRoofStairsOverhang extends ComponentTFTowerRoof {

	public ComponentTFTowerRoofStairsOverhang() {
		super();
	}

	public ComponentTFTowerRoofStairsOverhang(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// always facing = 0.  This roof cannot rotate, due to stair facing issues.
		this.setCoordBaseMode(EnumFacing.SOUTH);

		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size / 2;

		// just hang out at the very top of the tower
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);

	}

	/**
	 * Makes a pyramid-shaped roof out of stairs
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		IBlockState woodenSlab = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		IBlockState woodenPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);

		IBlockState birchStairsNorth = Blocks.BIRCH_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		IBlockState birchStairsSouth = Blocks.BIRCH_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		IBlockState birchStairsEast = Blocks.BIRCH_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
		IBlockState birchStairsWest = Blocks.BIRCH_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);

		for (int y = 0; y <= height; y++) {
			int min = y;
			int max = size - y - 1;
			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					if (x == min) {
						if (z == min || z == max) {
							setBlockState(world, woodenSlab, x, y, z, sbb);
						} else {
							setBlockState(world, birchStairsWest, x, y, z, sbb);
						}
					} else if (x == max) {
						if (z == min || z == max) {
							setBlockState(world, woodenSlab, x, y, z, sbb);
						} else {
							setBlockState(world, birchStairsEast, x, y, z, sbb);
						}
					} else if (z == max) {
						setBlockState(world, birchStairsSouth, x, y, z, sbb);
					} else if (z == min) {
						setBlockState(world, birchStairsNorth, x, y, z, sbb);
					} else {
						setBlockState(world, woodenPlanks, x, y, z, sbb);
					}
				}
			}
		}
		return true;
	}

}
