package twilightforest.structures.stronghold;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdCrossing extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdCrossing() {
	}

	public ComponentTFStrongholdCrossing(int i, EnumFacing facing, int x, int y, int z) {
		super(i, facing, x, y, z);
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
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 13);
		addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 1, 4);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 6, 17, rand, deco.randomBlocks);

		// statues
		placeCornerStatue(world, 2, 1, 2, 0, sbb);
		placeCornerStatue(world, 15, 1, 15, 3, sbb);

		// center pillar
		this.fillBlocksRotated(world, sbb, 8, 1, 8, 9, 5, 9, deco.pillarState, Rotation.NONE);

		// statues
		placeWallStatue(world, 8, 1, 7, Rotation.NONE, sbb);
		placeWallStatue(world, 7, 1, 9, Rotation.COUNTERCLOCKWISE_90, sbb);
		placeWallStatue(world, 9, 1, 10, Rotation.CLOCKWISE_180, sbb);
		placeWallStatue(world, 10, 1, 8, Rotation.CLOCKWISE_90, sbb);


		// tables
		placeTableAndChairs(world, sbb, Rotation.NONE);
		placeTableAndChairs(world, sbb, Rotation.CLOCKWISE_90);
		placeTableAndChairs(world, sbb, Rotation.CLOCKWISE_180);
		placeTableAndChairs(world, sbb, Rotation.COUNTERCLOCKWISE_90);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	private void placeTableAndChairs(World world, StructureBoundingBox sbb, Rotation rotation) {
		// table
		IBlockState oakStairs = Blocks.OAK_STAIRS.getDefaultState();
		;
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.NONE.rotate(EnumFacing.WEST), rotation, true), 5, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 5, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 6, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 6, 1, 4, rotation, sbb);
		// chairs
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST)), 5, 1, 2, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Rotation.NONE.rotate(EnumFacing.WEST)), 7, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST)), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST)), 4, 1, 4, rotation, sbb);
	}


}
