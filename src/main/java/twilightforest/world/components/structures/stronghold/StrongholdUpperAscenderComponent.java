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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.registration.TFGenerationSettings;


public class StrongholdUpperAscenderComponent extends KnightStrongholdComponent {

	boolean exitTop;

	public StrongholdUpperAscenderComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSUA.get(), nbt);
		this.exitTop = nbt.getBoolean("exitTop");
	}

	public StrongholdUpperAscenderComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSUA.get(), i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putBoolean("exitTop", this.exitTop);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		if (y < TFGenerationSettings.SEALEVEL + 5) { // FIXME Fix this when we overhaul this structure
			this.exitTop = true;
			return BoundingBox.orientBox(x, y, z, -2, -1, 0, 5, 10, 10, facing);
		} else /*if (y < -32)*/ { // FIXME world.minBuildHeight
			this.exitTop = false;
			return BoundingBox.orientBox(x, y, z, -2, -6, 0, 5, 10, 10, facing);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		// make a random component on the other side
		this.addNewUpperComponent(parent, list, random, Rotation.NONE, 2, exitTop ? 6 : 1, 10);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		this.placeUpperStrongholdWalls(world, sbb, 0, 0, 0, 4, 9, 9, rand, this.deco.randomBlocks);

		// entrance doorway
		this.placeSmallDoorwayAt(world, 2, 2, this.exitTop ? 1 : 6, 0, sbb);

		// exit doorway
		this.placeSmallDoorwayAt(world, 0, 2, this.exitTop ? 6 : 1, 9, sbb);

		// steps!
		if (this.exitTop) {
			this.makeStairsAt(world, 1, 3, Direction.NORTH, sbb);
			this.makeStairsAt(world, 2, 4, Direction.NORTH, sbb);
			this.makeStairsAt(world, 3, 5, Direction.NORTH, sbb);
			this.makeStairsAt(world, 4, 6, Direction.NORTH, sbb);
			this.makeStairsAt(world, 5, 7, Direction.NORTH, sbb);
			this.makePlatformAt(world, 5, 8, sbb);
		} else {
			this.makeStairsAt(world, 1, 6, Direction.SOUTH, sbb);
			this.makeStairsAt(world, 2, 5, Direction.SOUTH, sbb);
			this.makeStairsAt(world, 3, 4, Direction.SOUTH, sbb);
			this.makeStairsAt(world, 4, 3, Direction.SOUTH, sbb);
			this.makeStairsAt(world, 5, 2, Direction.SOUTH, sbb);
			this.makePlatformAt(world, 5, 1, sbb);
		}
	}

	/**
	 * Check if we can find at least one wall, and if so, generate stairs
	 */
	private void makeStairsAt(WorldGenLevel world, int y, int z, Direction facing, BoundingBox sbb) {
		// check walls
		if (!this.getBlock(world, 0, y, z, sbb).isAir() || !this.getBlock(world, 4, y, z, sbb).isAir()) {
			for (int x = 1; x < 4; x++) {
				this.placeBlock(world, Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, facing), x, y, z, sbb);
			}
		}
	}

	/**
	 * Check if we can find at least one wall, and if so, generate blocks
	 */
	private void makePlatformAt(WorldGenLevel world, int y, int z, BoundingBox sbb) {
		// check walls
		if (!this.getBlock(world, 0, y, z, sbb).isAir() || !this.getBlock(world, 4, y, z, sbb).isAir()) {
			for (int x = 1; x < 4; x++) {
				this.placeBlock(world, Blocks.STONE_BRICKS.defaultBlockState(), x, y, z, sbb);
			}
		}
	}

	@Override
	public boolean isComponentProtected() {
		return false;
	}
}
