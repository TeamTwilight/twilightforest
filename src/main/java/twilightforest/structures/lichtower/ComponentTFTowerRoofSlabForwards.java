package twilightforest.structures.lichtower;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;


/**
 * A flat tower roof using slabs that is larger than the tower under it.
 *
 * @author Ben
 */
public class ComponentTFTowerRoofSlabForwards extends ComponentTFTowerRoofSlab {

	public ComponentTFTowerRoofSlabForwards() {
		super();
	}

	public ComponentTFTowerRoofSlabForwards(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// the overhang roof is like a cap roof that's 2 sizes bigger
		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size / 2;

		// bounding box
		makeAttachedOverhangBB(wing);

	}

	/**
	 * Makes flat hip roof
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		IBlockState birchSlab = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		IBlockState birchDoubleSlab = Blocks.DOUBLE_WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);

		for (int y = 0; y <= height; y++) {
			int min = 2 * y;
			int max = size - (2 * y) - 1;
			for (int x = 0; x <= max - 1; x++) {
				for (int z = min; z <= max; z++) {
					if (x == max - 1 || z == min || z == max) {
						setBlockState(world, birchSlab, x, y, z, sbb);
					} else {
						setBlockState(world, birchDoubleSlab, x, y, z, sbb);
					}
				}
			}
		}
		return true;
	}
}
