package twilightforest.structures.lichtower;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;


public class ComponentTFTowerRoofSlab extends ComponentTFTowerRoof {

	public ComponentTFTowerRoofSlab() {
		super();
	}

	public ComponentTFTowerRoofSlab(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = size / 2;

		// just hang out at the very top of the tower
		makeCapBB(wing);

	}

	/**
	 * Makes a flat, pyramid-shaped roof
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		BlockPlanks.EnumType woodType = BlockPlanks.EnumType.BIRCH;

		return makePyramidCap(world, woodType, sbb);
	}

	protected boolean makePyramidCap(World world, BlockPlanks.EnumType woodType, StructureBoundingBox sbb) {
		IBlockState woodenSlab = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, woodType);
		IBlockState woodenPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, woodType);
		for (int y = 0; y <= height; y++) {
			int min = 2 * y;
			int max = size - (2 * y) - 1;
			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					if (x == min || x == max || z == min || z == max) {

						setBlockState(world, woodenSlab, x, y, z, sbb);
					} else {
						setBlockState(world, woodenPlanks, x, y, z, sbb);
					}
				}
			}
		}
		return true;
	}

	protected boolean makeConnectedCap(World world, BlockPlanks.EnumType woodType, StructureBoundingBox sbb) {
		IBlockState woodenSlab = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, woodType);
		IBlockState woodenPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, woodType);

		for (int y = 0; y < height; y++) {
			int min = 2 * y;
			int max = size - (2 * y) - 1;
			for (int x = 0; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					if (x == max || z == min || z == max) {
						setBlockState(world, woodenSlab, x, y, z, sbb);
					} else {
						setBlockState(world, woodenPlanks, x, y, z, sbb);
					}
				}
			}
		}
		return true;
	}

}
