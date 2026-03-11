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
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;


public class StrongholdBossRoomComponent extends KnightStrongholdComponent {

	public StrongholdBossRoomComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSBR.get(), nbt);
	}

	public StrongholdBossRoomComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSBR.get(), i, facing, x, y, z);
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -1, 0, 27, 7, 27, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		this.addDoor(13, 1, 0);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 26, 6, 26, rand, deco.randomBlocks);

		// inner walls
		this.generateBox(world, sbb, 1, 1, 1, 3, 5, 25, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 23, 1, 1, 25, 5, 25, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 4, 1, 1, 22, 5, 3, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 4, 1, 23, 22, 5, 25, false, rand, deco.randomBlocks);

		// obsidian filler
		this.generateBox(world, sbb, 1, 1, 1, 2, 5, 25, Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState(), false);
		this.generateBox(world, sbb, 24, 1, 1, 25, 5, 25, Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState(), false);
		this.generateBox(world, sbb, 4, 1, 1, 22, 5, 2, Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState(), false);
		this.generateBox(world, sbb, 4, 1, 24, 22, 5, 25, Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState(), false);

		// corner pillars
		this.generateBox(world, sbb, 4, 1, 4, 4, 5, 7, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 5, 1, 4, 5, 5, 5, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 6, 1, 4, 7, 5, 4, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 4, 1, 19, 4, 5, 22, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 5, 1, 21, 5, 5, 22, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 6, 1, 22, 7, 5, 22, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 22, 1, 4, 22, 5, 7, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 21, 1, 4, 21, 5, 5, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 19, 1, 4, 20, 5, 4, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 22, 1, 19, 22, 5, 22, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 21, 1, 21, 21, 5, 22, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 19, 1, 22, 20, 5, 22, false, rand, deco.randomBlocks);

		// pillar decorations (stairs)
		placePillarDecorations(world, sbb, Rotation.NONE);
		placePillarDecorations(world, sbb, Rotation.CLOCKWISE_90);
		placePillarDecorations(world, sbb, Rotation.CLOCKWISE_180);
		placePillarDecorations(world, sbb, Rotation.COUNTERCLOCKWISE_90);

		// sarcophagi
		placeSarcophagus(world, sbb, 8, 1, 8, Rotation.NONE);
		placeSarcophagus(world, sbb, 13, 1, 8, Rotation.NONE);
		placeSarcophagus(world, sbb, 18, 1, 8, Rotation.NONE);

		placeSarcophagus(world, sbb, 8, 1, 15, Rotation.NONE);
		placeSarcophagus(world, sbb, 13, 1, 15, Rotation.NONE);
		placeSarcophagus(world, sbb, 18, 1, 15, Rotation.NONE);

		// doorway
		this.generateAirBox(world, sbb, 12, 1, 1, 14, 4, 2);
		this.generateBox(world, sbb, 12, 1, 3, 14, 4, 3, Blocks.IRON_BARS.defaultBlockState(), Blocks.IRON_BARS.defaultBlockState(), false);

		//spawner
		placeBlock(world, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get().defaultBlockState(), 13, 2, 13, sbb);

		// doors
		placeDoors(world, sbb);
	}

	private void placeSarcophagus(WorldGenLevel world, BoundingBox sbb, int x, int y, int z, Rotation rotation) {

		this.setBlockStateRotated(world, deco.pillarState, x - 1, y, z, rotation, sbb);
		this.setBlockStateRotated(world, deco.pillarState, x + 1, y, z + 3, rotation, sbb);
		this.setBlockStateRotated(world, deco.pillarState, x + 1, y, z, rotation, sbb);
		this.setBlockStateRotated(world, deco.pillarState, x - 1, y, z + 3, rotation, sbb);

		// make either torches or fence posts

		if (world.getRandom().nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.defaultBlockState(), x + 1, y + 1, z, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x + 1, y + 1, z, rotation, sbb);
		}
		if (world.getRandom().nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.defaultBlockState(), x - 1, y + 1, z, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x - 1, y + 1, z, rotation, sbb);
		}
		if (world.getRandom().nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.defaultBlockState(), x + 1, y + 1, z + 3, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x + 1, y + 1, z + 3, rotation, sbb);
		}
		if (world.getRandom().nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.defaultBlockState(), x - 1, y + 1, z + 3, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x - 1, y + 1, z + 3, rotation, sbb);
		}

		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(Direction.WEST), false), x, y, z, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), x, y, z + 3, rotation, sbb);

		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), x + 1, y, z + 1, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), x + 1, y, z + 2, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), false), x - 1, y, z + 1, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(Direction.WEST), false), x - 1, y, z + 2, rotation, sbb);

		this.setBlockStateRotated(world, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), x, y + 1, z + 1, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), x, y + 1, z + 2, rotation, sbb);
	}

	protected void placePillarDecorations(WorldGenLevel world, BoundingBox sbb, Rotation rotation) {
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), 4, 1, 8, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), 8, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 4, 5, 8, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 8, 5, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), 5, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), 6, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 5, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 6, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 6, 5, 5, rotation, sbb);
	}

	@Override
	protected void placeDoorwayAt(WorldGenLevel world, int x, int y, int z, BoundingBox sbb) {
		if (x == 0 || x == getXSize()) {
			this.generateBox(world, sbb, x, y, z - 1, x, y + 3, z + 1, Blocks.IRON_BARS.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		} else {
			this.generateBox(world, sbb, x - 1, y, z, x + 1, y + 3, z, Blocks.IRON_BARS.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		}
	}

	@Override
	protected boolean isValidBreakInPoint(int wx, int wy, int wz) {
		return false;
	}
}
