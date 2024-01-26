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
import twilightforest.init.TFStructurePieceTypes;


public class StrongholdBalconyRoomComponent extends KnightStrongholdComponent {

	boolean enterBottom;

	public StrongholdBalconyRoomComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSBalR.get(), nbt);
		this.enterBottom = nbt.getBoolean("enterBottom");
	}

	public StrongholdBalconyRoomComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSBalR.get(), i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putBoolean("enterBottom", this.enterBottom);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {

		if (y > -15) { // FIXME
			this.enterBottom = false;
		} else if (y < -21) {
			this.enterBottom = true;
		} else {
			this.enterBottom = (z & 1) == 0;
		}

		if (enterBottom) {
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 18, 14, 27, facing);
		} else {
			// enter on the top
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -8, 0, 18, 14, 27, facing);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		// lower left exit
		addNewComponent(parent, list, random, Rotation.NONE, 13, 1, 27);

		// lower middle right exit
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 13);

		// lower middle left exit
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 18, 1, 13);

		// upper left exit
		addNewComponent(parent, list, random, Rotation.NONE, 4, 8, 27);

		// upper close right exit
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 8, 4);

		// upper far left exit
		addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 8, 22);

		if (this.enterBottom) {
			this.addDoor(4, 1, 0);
			//this.addDoor(4, 1, 1);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 13, 8, -1);
		} else {
			this.addDoor(13, 8, 0);
			//this.addDoor(13, 8, 1);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 4, 1, -1);
		}
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 13, 26, rand, deco.randomBlocks);

		// balcony
		this.generateBox(world, sbb, 1, 6, 1, 16, 7, 25, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 4, 8, 4, 13, 8, 22, deco.fenceState, Blocks.AIR.defaultBlockState(), false);
		this.generateAirBox(world, sbb, 5, 6, 5, 12, 8, 21);

		// stairs & pillars
		placeStairsAndPillars(world, sbb, Rotation.NONE);
		placeStairsAndPillars(world, sbb, Rotation.CLOCKWISE_180);

		// doors
		placeDoors(world, sbb);
	}

	private void placeStairsAndPillars(WorldGenLevel world, BoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 4, 1, 4, 4, 12, 4, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), 4, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), 5, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 4, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 5, 5, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 4, 12, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 5, 12, 4, rotation, sbb);

		this.fillBlocksRotated(world, sbb, 13, 1, 4, 13, 12, 4, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), 13, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), false), 12, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 13, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), true), 12, 5, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 13, 12, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), true), 12, 12, 4, rotation, sbb);

		this.fillBlocksRotated(world, sbb, 13, 1, 8, 13, 12, 8, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), 13, 1, 9, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(Direction.WEST), false), 13, 1, 7, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), false), 12, 1, 8, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 13, 5, 9, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(Direction.WEST), true), 13, 5, 7, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 13, 12, 9, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(Direction.WEST), true), 13, 12, 7, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), true), 12, 12, 8, rotation, sbb);

		for (int y = 1; y < 8; y++) {
			for (int z = 5; z < 8; z++) {
				this.setBlockStateRotated(world, AIR, y + 6, y + 1, z, rotation, sbb);
				this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), false), y + 6, y, z, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, y + 6, y - 1, z, rotation, sbb);
			}
		}
	}
}
