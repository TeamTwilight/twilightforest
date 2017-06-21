package twilightforest.structures.lichtower;

import net.minecraft.block.BlockPlanks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;


public class ComponentTFTowerRoofAttachedSlab extends ComponentTFTowerRoofSlab {

	public ComponentTFTowerRoofAttachedSlab() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComponentTFTowerRoofAttachedSlab(int i, ComponentTFTowerWing wing) {
		super(i, wing);
	}

	/**
	 * Makes a flat, pyramid-shaped roof that is connected to the parent tower
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		return makeConnectedCap(world, BlockPlanks.EnumType.BIRCH, sbb);
	}
}
