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
import twilightforest.loot.TFTreasure;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdSmallStairs extends StructureTFStrongholdComponent {

	private boolean enterBottom;
	public boolean hasTreasure;
	public boolean chestTrapped;

	public ComponentTFStrongholdSmallStairs(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSSS, nbt);
		this.enterBottom = nbt.getBoolean("enterBottom");
		this.hasTreasure = nbt.getBoolean("hasTreasure");
		this.chestTrapped = nbt.getBoolean("chestTrapped");
	}

	public ComponentTFStrongholdSmallStairs(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSSS, feature, i, facing, x, y, z);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("enterBottom", this.enterBottom);
		tagCompound.putBoolean("hasTreasure", this.hasTreasure);
		tagCompound.putBoolean("chestTrapped", this.chestTrapped);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {

		if (y > 17) {
			this.enterBottom = false;
		} else if (y < 11) {
			this.enterBottom = true;
		} else {
			this.enterBottom = (z & 1) == 0;
		}

		if (enterBottom) {
			return MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 14, 9, facing);
		} else {
			// enter on the top
			return MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, -8, 0, 9, 14, 9, facing);
		}
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		if (this.enterBottom) {
			this.addDoor(4, 1, 0);
			addNewComponent(parent, list, random, Rotation.NONE, 4, 8, 9);
		} else {
			this.addDoor(4, 8, 0);
			addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 9);
		}

		this.hasTreasure = random.nextBoolean();
		this.chestTrapped = random.nextInt(3) == 0;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 13, 8, rand, deco.randomBlocks);

		// railing
		this.fillWithBlocks(world, sbb, 1, 7, 1, 7, 7, 7, deco.platformState, Blocks.AIR.getDefaultState(), false);
		this.fillWithAir(world, sbb, 2, 7, 2, 6, 7, 6);

		Rotation rotation = this.enterBottom ? Rotation.NONE : Rotation.CLOCKWISE_180;

		// stairs
		for (int y = 1; y < 8; y++) {
			for (int x = 3; x < 6; x++) {
				this.setBlockStateRotated(world, Blocks.AIR.getDefaultState(), x, y + 1, y, rotation, sbb);
				this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.NORTH), x, y, y, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, x, y - 1, y, rotation, sbb);
			}
		}

		// treasure
		if (this.hasTreasure) {
			this.placeTreasureRotated(world, 4, 1, 6, getCoordBaseMode().getOpposite(), rotation, TFTreasure.stronghold_cache, this.chestTrapped, sbb);

			if (this.chestTrapped) {
				this.setBlockStateRotated(world, Blocks.TNT.getDefaultState(), 4, 0, 6, rotation, sbb);
			}

			for (int z = 5; z < 8; z++) {
				this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.WEST), 3, 1, z, rotation, sbb);
				this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.EAST), 5, 1, z, rotation, sbb);
			}

			this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.NORTH), 4, 1, 5, rotation, sbb);
			this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.SOUTH), 4, 1, 7, rotation, sbb);
			this.setBlockStateRotated(world, deco.stairState.with(StairsBlock.FACING, Direction.NORTH), 4, 2, 6, rotation, sbb);
		}

		if (enterBottom) {
			this.placeWallStatue(world, 4, 8, 1, Rotation.CLOCKWISE_180, sbb);
		} else {
			this.placeWallStatue(world, 4, 8, 7, Rotation.NONE, sbb);
		}

		// doors
		placeDoors(world, sbb);

		return true;
	}
}
