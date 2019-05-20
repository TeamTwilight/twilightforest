package twilightforest.structures.minotaurmaze;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeDeadEndTripwireChest extends ComponentTFMazeDeadEndChest {

	public ComponentTFMazeDeadEndTripwireChest() {
		super();
	}

	public ComponentTFMazeDeadEndTripwireChest(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// normal chest room
		super.addComponentParts(world, rand, sbb);

		// add tripwire
		this.placeTripwire(world, 1, 1, 2, 3, EnumFacing.EAST, sbb);

		// TNT!
		IBlockState tnt = Blocks.TNT.getDefaultState();
		this.setBlockState(world, tnt, 0,  0, 2, sbb);

		// Air blocks are required underneath to maximize TNT destruction of chest
		this.setBlockState(world, AIR, 0, -1, 2, sbb);
		this.setBlockState(world, AIR, 1, -1, 2, sbb);

		this.setBlockState(world, tnt, 2,  0, 4, sbb);
		this.setBlockState(world, tnt, 3,  0, 4, sbb);
		this.setBlockState(world, tnt, 2,  0, 3, sbb);
		this.setBlockState(world, tnt, 3,  0, 3, sbb);

		return true;
	}
}
