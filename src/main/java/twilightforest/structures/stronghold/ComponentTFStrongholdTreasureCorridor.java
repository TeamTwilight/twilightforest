package twilightforest.structures.stronghold;

import net.minecraft.block.StairsBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.loot.TFTreasure;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdTreasureCorridor extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdTreasureCorridor(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSTC, nbt);
	}

	public ComponentTFStrongholdTreasureCorridor(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSTC, feature, i, facing, x, y, z);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 7, 27, facing);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		// make a random component at the end
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 27);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 26, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 9, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 1, 1, 17, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 9, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 17, Rotation.COUNTERCLOCKWISE_90, sbb);

		Rotation rotation = (this.boundingBox.minX ^ this.boundingBox.minZ) % 2 == 0 ? Rotation.NONE : Rotation.CLOCKWISE_180;

		// treasure!
		this.placeTreasureRotated(world, 8, 2, 13, rotation == Rotation.NONE ? getCoordBaseMode().rotateY() : getCoordBaseMode().rotateYCCW(), rotation, TFTreasure.stronghold_cache, sbb);

		// niche!

		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, rotation, true), 8, 3, 12, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, rotation, true), 8, 3, 13, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, rotation, true), 8, 3, 14, rotation, sbb);
		this.setBlockStateRotated(world, deco.fenceState, 8, 2, 12, rotation, sbb);
		this.setBlockStateRotated(world, deco.fenceState, 8, 2, 14, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.SOUTH), 7, 1, 12, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.WEST), 7, 1, 13, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.NORTH), 7, 1, 14, rotation, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}
}
