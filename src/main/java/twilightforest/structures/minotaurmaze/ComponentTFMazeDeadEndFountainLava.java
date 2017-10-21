package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeDeadEndFountainLava extends ComponentTFMazeDeadEndFountain {

	public ComponentTFMazeDeadEndFountainLava() {
		super();
	}

	public ComponentTFMazeDeadEndFountainLava(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// normal fountain
		super.addComponentParts(world, rand, sbb);

		// remove water
		this.setBlockState(world, AIR, 2, 3, 4, sbb);
		this.setBlockState(world, AIR, 3, 3, 4, sbb);

		// lava instead of water
		this.setBlockState(world, Blocks.FLOWING_LAVA.getDefaultState(), 2, 3, 4, sbb);
		this.setBlockState(world, Blocks.FLOWING_LAVA.getDefaultState(), 3, 3, 4, sbb);

		return true;
	}


}
