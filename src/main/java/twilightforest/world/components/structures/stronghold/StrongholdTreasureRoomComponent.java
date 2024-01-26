package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;


public class StrongholdTreasureRoomComponent extends KnightStrongholdComponent {

	private boolean enterBottom;

	public StrongholdTreasureRoomComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFTreaR.get(), nbt);
		this.enterBottom = nbt.getBoolean("enterBottom");
	}

	public StrongholdTreasureRoomComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFTreaR.get(), i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putBoolean("enterBottom", this.enterBottom);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return BoundingBox.orientBox(x, y, z, -4, -1, 0, 9, 7, 18, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		this.addDoor(4, 1, 0);
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 17, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 4, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 1, 1, 13, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 4, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 13, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 4, 1, 16, Rotation.NONE, sbb);

		this.generateBox(world, sbb, 1, 1, 8, 7, 5, 9, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 3, 1, 8, 5, 4, 9, Blocks.IRON_BARS.defaultBlockState(), Blocks.IRON_BARS.defaultBlockState(), false);

		// spawnwers
		this.setSpawner(world, 4, 1, 4, sbb, TFEntities.HELMET_CRAB.get());

		this.setSpawner(world, 4, 4, 15, sbb, TFEntities.HELMET_CRAB.get());

		// treasure!
		this.manualTreaurePlacement(world, 2, 4, 13, Direction.WEST, TFLootTables.STRONGHOLD_ROOM, false, sbb);
		this.manualTreaurePlacement(world, 6, 4, 13, Direction.EAST, TFLootTables.STRONGHOLD_ROOM, false, sbb);

		// doors
		placeDoors(world, sbb);
	}

	/**
	 * Make a doorway
	 */
	@Override
	protected void placeDoorwayAt(WorldGenLevel world, int x, int y, int z, BoundingBox sbb) {
		if (x == 0 || x == getXSize()) {
			this.generateBox(world, sbb, x, y, z - 1, x, y + 3, z + 1, Blocks.IRON_BARS.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		} else {
			this.generateBox(world, sbb, x - 1, y, z, x + 1, y + 3, z, Blocks.IRON_BARS.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		}
	}
}
