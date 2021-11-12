package twilightforest.world.components.structures.stronghold;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
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

public class StrongholdDeadEndComponent extends StructureTFStrongholdComponent {

	private boolean chestTrapped;

	public StrongholdDeadEndComponent(ServerLevel level, CompoundTag nbt) {
		super(StrongholdPieces.TFSDE, nbt);
		this.chestTrapped = nbt.getBoolean("chestTrapped");
	}

	public StrongholdDeadEndComponent(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(StrongholdPieces.TFSDE, feature, i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		super.addAdditionalSaveData(level, tagCompound);
		tagCompound.putBoolean("chestTrapped", this.chestTrapped);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 6, 9, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random random) {
		super.addChildren(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		this.chestTrapped = random.nextInt(3) == 0;
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 8, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 4, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 4, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 4, 1, 7, Rotation.NONE, sbb);

		// doors
		placeDoors(world, sbb);

		// treasure
		this.manualTreaurePlacement(world, 4, 1, 3, Direction.SOUTH, TFTreasure.STRONGHOLD_CACHE, this.chestTrapped, sbb);
		if (this.chestTrapped) {
			this.placeBlock(world, Blocks.TNT.defaultBlockState(), 4, 0, 3, sbb);
		}

		for (int z = 2; z < 5; z++) {
			this.placeBlock(world, deco.stairState.setValue(StairBlock.FACING, Direction.WEST), 3, 1, z, sbb);
			this.placeBlock(world, deco.stairState.setValue(StairBlock.FACING, Direction.EAST), 5, 1, z, sbb);
		}

		this.placeBlock(world, deco.stairState.setValue(StairBlock.FACING, Direction.NORTH), 4, 1, 2, sbb);
		this.placeBlock(world, deco.stairState.setValue(StairBlock.FACING, Direction.SOUTH), 4, 1, 4, sbb);
		this.placeBlock(world, deco.stairState.setValue(StairBlock.FACING, Direction.NORTH), 4, 2, 3, sbb);

		return true;
	}
}
