package twilightforest.structures.stronghold;

import net.minecraft.block.Blocks;
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

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdUpperAscender extends StructureTFStrongholdComponent {

	boolean exitTop;

	public ComponentTFStrongholdUpperAscender(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSUA, nbt);
		this.exitTop = nbt.getBoolean("exitTop");
	}

	public ComponentTFStrongholdUpperAscender(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSUA, feature, i, facing, x, y, z);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("exitTop", this.exitTop);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		if (y < 36) {
			this.exitTop = true;
			return MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, -2, -1, 0, 5, 10, 10, facing);
		} else {
			this.exitTop = false;
			return MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, -2, -6, 0, 5, 10, 10, facing);
		}
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		// make a random component on the other side
		addNewUpperComponent(parent, list, random, Rotation.NONE, 2, exitTop ? 6 : 1, 10);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		if (this.isLiquidInStructureBoundingBox(world, sbb)) {
			return false;
		} else {
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
	private void makeStairsAt(ISeedReader world, int y, int z, Direction facing, MutableBoundingBox sbb) {
		// check walls
		if (this.getBlockStateFromPos(world, 0, y, z, sbb).getBlock() != Blocks.AIR || this.getBlockStateFromPos(world, 4, y, z, sbb).getBlock() != Blocks.AIR) {
			for (int x = 1; x < 4; x++) {
				this.setBlockState(world, Blocks.STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, facing), x, y, z, sbb);
			}
		}
	}

	/**
	 * Check if we can find at least one wall, and if so, generate blocks
	 */
	private void makePlatformAt(ISeedReader world, int y, int z, MutableBoundingBox sbb) {
		// check walls
		if (this.getBlockStateFromPos(world, 0, y, z, sbb).getBlock() != Blocks.AIR || this.getBlockStateFromPos(world, 4, y, z, sbb).getBlock() != Blocks.AIR) {
			for (int x = 1; x < 4; x++) {
				this.setBlockState(world, Blocks.STONE_BRICKS.getDefaultState(), x, y, z, sbb);
			}
		}
	}

	@Override
	public boolean isComponentProtected() {
		return false;
	}
}
