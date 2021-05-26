package twilightforest.structures.stronghold;

import net.minecraft.block.BlockState;
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

public class ComponentTFStrongholdCrossing extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdCrossing(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSCr, nbt);
	}

	public ComponentTFStrongholdCrossing(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSCr, feature, i, facing, x, y, z);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -1, 0, 18, 7, 18, facing);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		this.addDoor(13, 1, 0);
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 18);
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 13);
		addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 1, 4);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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

		return true;
	}

	private void placeTableAndChairs(ISeedReader world, MutableBoundingBox sbb, Rotation rotation) {
		// table
		BlockState oakStairs = Blocks.OAK_STAIRS.getDefaultState();

		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.NONE.rotate(Direction.WEST), true), 5, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 5, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.CLOCKWISE_90.rotate(Direction.WEST), true), 6, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(oakStairs, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 6, 1, 4, rotation, sbb);
		// chairs
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST)), 5, 1, 2, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Rotation.NONE.rotate(Direction.WEST)), 7, 1, 3, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Rotation.CLOCKWISE_90.rotate(Direction.WEST)), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Rotation.CLOCKWISE_180.rotate(Direction.WEST)), 4, 1, 4, rotation, sbb);
	}
}
