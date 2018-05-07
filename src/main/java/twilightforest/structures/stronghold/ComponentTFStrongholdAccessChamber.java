package twilightforest.structures.stronghold;

import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFTrophyPedestal;
import twilightforest.block.TFBlocks;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdAccessChamber extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdAccessChamber() {
	}

	public ComponentTFStrongholdAccessChamber(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
		super(feature, i, facing, x, y, z);
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {
		return StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, 1, 0, 9, 5, 9, facing);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		// make a random component in each direction
		addNewUpperComponent(parent, list, random, Rotation.NONE, 4, 1, 9);
		addNewUpperComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 4);
		addNewUpperComponent(parent, list, random, Rotation.CLOCKWISE_180, 4, 1, -1);
		addNewUpperComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 9, 1, 4);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, 8, 4, 8, true, rand, deco.randomBlocks);

		// doors
		placeSmallDoorwayAt(world, rand, 0, 4, 1, 8, sbb);
		placeSmallDoorwayAt(world, rand, 1, 0, 1, 4, sbb);
		placeSmallDoorwayAt(world, rand, 2, 4, 1, 0, sbb);
		placeSmallDoorwayAt(world, rand, 3, 8, 1, 4, sbb);

		// shaft down
		final IBlockState defaultState = Blocks.STONEBRICK
				.getDefaultState()
				.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
		this.fillWithBlocks(world, sbb, 2, -2, 2, 6, 0, 6, defaultState, AIR, false);

		this.fillWithAir(world, sbb, 3, -2, 3, 5, 2, 5);

		// stairs surrounding shaft
		this.fillWithBlocks(world, sbb, 2, 0, 3, 2, 0, 6, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), AIR, false);
		this.fillWithBlocks(world, sbb, 6, 0, 2, 6, 0, 6, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, false), AIR, false);
		this.fillWithBlocks(world, sbb, 3, 0, 2, 5, 0, 2, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), AIR, false);
		this.fillWithBlocks(world, sbb, 3, 0, 6, 5, 0, 6, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), AIR, false);

		// pillar
		this.setBlockState(world, deco.pillarState, 2, 0, 2, sbb);

		// pedestal
		final IBlockState blockstateIn = TFBlocks.trophy_pedestal
				.getDefaultState()
				.withProperty(BlockTFTrophyPedestal.LATENT, true)
				//TODO: Atomic: Verify this facing.
				.withProperty(BlockTFTrophyPedestal.FACING, EnumFacing.EAST);
		this.setBlockState(world, blockstateIn, 2, 1, 2, sbb);

		// block point
		this.fillWithBlocks(world, sbb, 2, -1, 2, 6, -1, 6, TFBlocks.stronghold_shield.getDefaultState(), AIR, false);

		return true;
	}

	/**
	 * Does this component fall under block protection when progression is turned on, normally true
	 */
	@Override
	public boolean isComponentProtected() {
		return false;
	}
}
