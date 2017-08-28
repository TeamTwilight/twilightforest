package twilightforest.structures.stronghold;

import net.minecraft.block.BlockStairs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFTreasure;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdTreasureCorridor extends StructureTFStrongholdComponent {


	public ComponentTFStrongholdTreasureCorridor() {
	}

	public ComponentTFStrongholdTreasureCorridor(int i, EnumFacing facing, int x, int y, int z) {
		super(i, facing, x, y, z);
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 7, 27, facing);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		// make a random component at the end
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 27);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 26, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 9, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 1, 1, 17, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 9, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 17, Rotation.COUNTERCLOCKWISE_90, sbb);

		Rotation rotation = (this.boundingBox.minX ^ this.boundingBox.minZ) % 2 == 0 ? Rotation.NONE : Rotation.CLOCKWISE_180;

		// treasure!
		this.placeTreasureRotated(world, 8, 2, 13, rotation, TFTreasure.stronghold_cache, sbb);

		// niche!

		this.setBlockStateRotated(world, getStairState(deco.stairState, EnumFacing.SOUTH, rotation, true), 8, 3, 12, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, EnumFacing.WEST, rotation, true), 8, 3, 13, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, EnumFacing.NORTH, rotation, true), 8, 3, 14, rotation, sbb);
		this.setBlockStateRotated(world, deco.fenceState, 8, 2, 12, rotation, sbb);
		this.setBlockStateRotated(world, deco.fenceState, 8, 2, 14, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 7, 1, 12, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.WEST), 7, 1, 13, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 7, 1, 14, rotation, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}


}
