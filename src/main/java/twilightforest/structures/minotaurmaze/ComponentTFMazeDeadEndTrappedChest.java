package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockTripWireHook;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeDeadEndTrappedChest extends ComponentTFMazeDeadEndChest {

	public ComponentTFMazeDeadEndTrappedChest() {
		super();
	}

	public ComponentTFMazeDeadEndTrappedChest(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// normal chest room
		super.addComponentParts(world, rand, sbb);

		// add tripwire hooks
		this.setBlockState(world, Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.WEST), 1, 1, 2, sbb);
		this.setBlockState(world, Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.EAST), 4, 1, 2, sbb);

		// add string
		this.setBlockState(world, Blocks.TRIPWIRE.getDefaultState(), 2, 1, 2, sbb);
		this.setBlockState(world, Blocks.TRIPWIRE.getDefaultState(), 3, 1, 2, sbb);

		// TNT!
		this.setBlockState(world, Blocks.TNT.getDefaultState(), 0, 0, 2, sbb);
		this.setBlockState(world, AIR, 0, -1, 2, sbb);
		this.setBlockState(world, AIR, 1, -1, 2, sbb);
		this.setBlockState(world, Blocks.TNT.getDefaultState(), 2, 0, 4, sbb);
		this.setBlockState(world, Blocks.TNT.getDefaultState(), 3, 0, 4, sbb);
		this.setBlockState(world, Blocks.TNT.getDefaultState(), 2, 0, 3, sbb);
		this.setBlockState(world, Blocks.TNT.getDefaultState(), 3, 0, 3, sbb);

		return true;
	}
}
