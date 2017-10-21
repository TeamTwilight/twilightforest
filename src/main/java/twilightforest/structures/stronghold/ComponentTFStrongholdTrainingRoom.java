package twilightforest.structures.stronghold;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdTrainingRoom extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdTrainingRoom() {
	}

	public ComponentTFStrongholdTrainingRoom(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
		super(feature, i, facing, x, y, z);
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -1, 0, 18, 7, 18, facing);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		this.addDoor(13, 1, 0);
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 18);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 6, 17, rand, deco.randomBlocks);

		// statues
		placeCornerStatue(world, 2, 1, 2, 0, sbb);
		placeCornerStatue(world, 15, 1, 15, 3, sbb);

		// sand floor
		this.generateMaybeBox(world, sbb, rand, 0.7F, 4, 0, 4, 8, 0, 8, Blocks.SAND.getDefaultState(), Blocks.SAND.getDefaultState(), false, 0);
		this.generateMaybeBox(world, sbb, rand, 0.7F, 9, 0, 4, 13, 0, 8, Blocks.SAND.getDefaultState(), Blocks.SAND.getDefaultState(), false, 0);
		this.generateMaybeBox(world, sbb, rand, 0.7F, 9, 0, 9, 13, 0, 13, Blocks.SAND.getDefaultState(), Blocks.SAND.getDefaultState(), false, 0);

		// training dummies
		placeTrainingDummy(world, sbb, Rotation.NONE);
		placeTrainingDummy(world, sbb, Rotation.CLOCKWISE_90);
		placeTrainingDummy(world, sbb, Rotation.CLOCKWISE_180);

		// anvil pad
		this.fillWithBlocks(world, sbb, 5, 0, 10, 7, 0, 12, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);

		this.setBlockState(world, deco.pillarState, 5, 1, 12, sbb);
		this.setBlockState(world, deco.pillarState, 5, 2, 12, sbb);
		this.setBlockState(world, deco.pillarState, 6, 1, 12, sbb);
		this.setBlockState(world, deco.stairState.withProperty(BlockPumpkin.FACING, EnumFacing.EAST), 6, 2, 12, sbb);
		this.setBlockState(world, deco.stairState.withProperty(BlockPumpkin.FACING, EnumFacing.EAST), 7, 1, 12, sbb);
		this.setBlockState(world, deco.pillarState, 5, 1, 11, sbb);
		this.setBlockState(world, deco.stairState.withProperty(BlockPumpkin.FACING, EnumFacing.NORTH), 5, 2, 11, sbb);
		this.setBlockState(world, deco.stairState.withProperty(BlockPumpkin.FACING, EnumFacing.NORTH), 5, 1, 10, sbb);

		this.setBlockState(world, Blocks.ANVIL.getDefaultState(), 6, 1, 11, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	private void placeTrainingDummy(World world, StructureBoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 5, 0, 5, 7, 0, 7, Blocks.SAND.getDefaultState(), rotation);
		this.setBlockStateRotated(world, deco.fenceState, 6, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), 6, 2, 6, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.OAK_FENCE.getDefaultState(), 5, 2, 6, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.OAK_FENCE.getDefaultState(), 7, 2, 6, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, getStructureRelativeRotation(rotation)), 6, 3, 6, rotation, sbb);
	}

}
