package twilightforest.structures.stronghold;

import net.minecraft.block.Blocks;
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

public class ComponentTFStrongholdBalconyRoom extends StructureTFStrongholdComponent {

	boolean enterBottom;

	public ComponentTFStrongholdBalconyRoom(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSBalR, nbt);
		this.enterBottom = nbt.getBoolean("enterBottom");
	}

	public ComponentTFStrongholdBalconyRoom(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSBalR, feature, i, facing, x, y, z);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("enterBottom", this.enterBottom);
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
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 18, 14, 27, facing);
		} else {
			// enter on the top
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -8, 0, 18, 14, 27, facing);
		}
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

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
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 13, 26, rand, deco.randomBlocks);

		// balcony
		this.fillWithRandomizedBlocks(world, sbb, 1, 6, 1, 16, 7, 25, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 4, 8, 4, 13, 8, 22, deco.fenceState, Blocks.AIR.getDefaultState(), false);
		this.fillWithAir(world, sbb, 5, 6, 5, 12, 8, 21);

		// stairs & pillars
		placeStairsAndPillars(world, sbb, Rotation.NONE);
		placeStairsAndPillars(world, sbb, Rotation.CLOCKWISE_180);

		// doors
		placeDoors(world, sbb);

		return true;
	}

	private void placeStairsAndPillars(ISeedReader world, MutableBoundingBox sbb, Rotation rotation) {
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
