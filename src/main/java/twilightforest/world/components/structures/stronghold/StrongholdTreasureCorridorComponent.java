package twilightforest.world.components.structures.stronghold;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class StrongholdTreasureCorridorComponent extends StructureTFStrongholdComponent {

	public StrongholdTreasureCorridorComponent(ServerLevel level, CompoundTag nbt) {
		super(StrongholdPieces.TFSTC, nbt);
	}

	public StrongholdTreasureCorridorComponent(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(StrongholdPieces.TFSTC, feature, i, facing, x, y, z);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 7, 27, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random random) {
		super.addChildren(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		// make a random component at the end
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 27);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 26, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 9, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 1, 1, 17, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 9, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 17, Rotation.COUNTERCLOCKWISE_90, sbb);

		Rotation rotation = (this.boundingBox.minX() ^ this.boundingBox.minZ()) % 2 == 0 ? Rotation.NONE : Rotation.CLOCKWISE_180;

		// treasure!
		this.placeTreasureRotated(world, 8, 2, 13, rotation == Rotation.NONE ? getOrientation().getClockWise() : getOrientation().getCounterClockWise(), rotation, TFTreasure.STRONGHOLD_CACHE, sbb);

		// niche!

		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, true), 8, 3, 12, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, true), 8, 3, 13, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, true), 8, 3, 14, rotation, sbb);
		this.setBlockStateRotated(world, deco.fenceState, 8, 2, 12, rotation, sbb);
		this.setBlockStateRotated(world, deco.fenceState, 8, 2, 14, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.setValue(StairBlock.FACING, Direction.SOUTH), 7, 1, 12, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.setValue(StairBlock.FACING, Direction.WEST), 7, 1, 13, rotation, sbb);
		this.setBlockStateRotated(world, deco.stairState.setValue(StairBlock.FACING, Direction.NORTH), 7, 1, 14, rotation, sbb);

		// doors
		placeDoors(world, sbb);

		return true;
	}
}
