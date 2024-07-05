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
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class StrongholdCrossingComponent extends KnightStrongholdComponent {

	public StrongholdCrossingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSCr.get(), nbt);
	}

	public StrongholdCrossingComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSCr.get(), i, facing, x, y, z);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -1, 0, 18, 7, 18, facing);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		this.addDoor(13, 1, 0);
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 18);
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 13);
		addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 1, 4);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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
		placeDoors(world, sbb);
	}

	private void placeTableAndChairs(WorldGenLevel world, BoundingBox sbb, Rotation rotation) {
		// table
		BlockState oakStairs = Blocks.OAK_STAIRS.defaultBlockState();

		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.NONE.rotate(Direction.WEST), true), 5, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 5, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.CLOCKWISE_90.rotate(Direction.WEST), true), 6, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 6, 1, 4, rotation, sbb);
		// chairs
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST)), 5, 1, 2, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Rotation.NONE.rotate(Direction.WEST)), 7, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Rotation.CLOCKWISE_90.rotate(Direction.WEST)), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Rotation.CLOCKWISE_180.rotate(Direction.WEST)), 4, 1, 4, rotation, sbb);
	}
}
