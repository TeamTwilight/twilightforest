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

public class ComponentTFStrongholdDeadEnd extends StructureTFStrongholdComponent {

	private boolean chestTrapped;

	public ComponentTFStrongholdDeadEnd(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSDE, nbt);
		this.chestTrapped = nbt.getBoolean("chestTrapped");
	}

	public ComponentTFStrongholdDeadEnd(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSDE, feature, i, facing, x, y, z);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("chestTrapped", this.chestTrapped);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 6, 9, facing);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		this.chestTrapped = random.nextInt(3) == 0;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 8, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 4, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 4, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 4, 1, 7, Rotation.NONE, sbb);

		// doors
		placeDoors(world, rand, sbb);

		// treasure
		this.placeTreasureAtCurrentPosition(world, 4, 1, 3, TFTreasure.stronghold_cache, this.chestTrapped, sbb);
		if (this.chestTrapped) {
			this.setBlockState(world, Blocks.TNT.getDefaultState(), 4, 0, 3, sbb);
		}

		for (int z = 2; z < 5; z++) {
			this.setBlockState(world, deco.stairState.with(StairsBlock.FACING, Direction.WEST), 3, 1, z, sbb);
			this.setBlockState(world, deco.stairState.with(StairsBlock.FACING, Direction.EAST), 5, 1, z, sbb);
		}

		this.setBlockState(world, deco.stairState.with(StairsBlock.FACING, Direction.NORTH), 4, 1, 2, sbb);
		this.setBlockState(world, deco.stairState.with(StairsBlock.FACING, Direction.SOUTH), 4, 1, 4, sbb);
		this.setBlockState(world, deco.stairState.with(StairsBlock.FACING, Direction.NORTH), 4, 2, 3, sbb);

		return true;
	}
}
