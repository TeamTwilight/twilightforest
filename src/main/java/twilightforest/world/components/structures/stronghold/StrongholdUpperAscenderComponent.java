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
import twilightforest.world.registration.TFGenerationSettings;

import java.util.Random;

public class StrongholdUpperAscenderComponent extends StructureTFStrongholdComponent {

	boolean exitTop;

	public StrongholdUpperAscenderComponent(ServerLevel level, CompoundTag nbt) {
		super(StrongholdPieces.TFSUA, nbt);
		this.exitTop = nbt.getBoolean("exitTop");
	}

	public StrongholdUpperAscenderComponent(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(StrongholdPieces.TFSUA, feature, i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		super.addAdditionalSaveData(level, tagCompound);
		tagCompound.putBoolean("exitTop", this.exitTop);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		if (y < TFGenerationSettings.SEALEVEL - 7) { // FIXME Fix this when we overhaul this structure
			this.exitTop = true;
			return BoundingBox.orientBox(x, y, z, -2, -1, 0, 5, 10, 10, facing);
		} else /*if (y < -32)*/ { // FIXME world.minBuildHeight
			this.exitTop = false;
			return BoundingBox.orientBox(x, y, z, -2, -6, 0, 5, 10, 10, facing);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random random) {
		super.addChildren(parent, list, random);

		// make a random component on the other side
		addNewUpperComponent(parent, list, random, Rotation.NONE, 2, exitTop ? 6 : 1, 10);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		/*if (this.edgesLiquid(world, sbb)) {
			return false;
		} else */{
			placeUpperStrongholdWalls(world, sbb, 0, 0, 0, 4, 9, 9, rand, deco.randomBlocks);

			// entrance doorway
			placeSmallDoorwayAt(world, 2, 2, exitTop ? 1 : 6, 0, sbb);

			// exit doorway
			placeSmallDoorwayAt(world, 0, 2, exitTop ? 6 : 1, 9, sbb);

			// steps!
			if (exitTop) {
				makeStairsAt(world, 1, 3, Direction.NORTH, sbb);
				makeStairsAt(world, 2, 4, Direction.NORTH, sbb);
				makeStairsAt(world, 3, 5, Direction.NORTH, sbb);
				makeStairsAt(world, 4, 6, Direction.NORTH, sbb);
				makeStairsAt(world, 5, 7, Direction.NORTH, sbb);
				makePlatformAt(world, 5, 8, sbb);
			} else {
				makeStairsAt(world, 1, 6, Direction.NORTH, sbb);
				makeStairsAt(world, 2, 5, Direction.NORTH, sbb);
				makeStairsAt(world, 3, 4, Direction.NORTH, sbb);
				makeStairsAt(world, 4, 3, Direction.NORTH, sbb);
				makeStairsAt(world, 5, 2, Direction.NORTH, sbb);
				makePlatformAt(world, 5, 1, sbb);
			}
			return true;
		}
	}

	/**
	 * Check if we can find at least one wall, and if so, generate stairs
	 */
	private void makeStairsAt(WorldGenLevel world, int y, int z, Direction facing, BoundingBox sbb) {
		// check walls
		if (this.getBlock(world, 0, y, z, sbb).getBlock() != Blocks.AIR || this.getBlock(world, 4, y, z, sbb).getBlock() != Blocks.AIR) {
			for (int x = 1; x < 4; x++) {
				this.placeBlock(world, Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, facing.getOpposite()), x, y, z, sbb);
			}
		}
	}

	/**
	 * Check if we can find at least one wall, and if so, generate blocks
	 */
	private void makePlatformAt(WorldGenLevel world, int y, int z, BoundingBox sbb) {
		// check walls
		if (this.getBlock(world, 0, y, z, sbb).getBlock() != Blocks.AIR || this.getBlock(world, 4, y, z, sbb).getBlock() != Blocks.AIR) {
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
