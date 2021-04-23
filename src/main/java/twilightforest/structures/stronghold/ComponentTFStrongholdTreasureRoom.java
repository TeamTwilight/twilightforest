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
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdTreasureRoom extends StructureTFStrongholdComponent {

	private boolean enterBottom;

	public ComponentTFStrongholdTreasureRoom(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFTreaR, nbt);
		this.enterBottom = nbt.getBoolean("enterBottom");
	}

	public ComponentTFStrongholdTreasureRoom(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFTreaR, feature, i, facing, x, y, z);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("enterBottom", this.enterBottom);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 7, 18, facing);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		this.addDoor(4, 1, 0);
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 17, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 4, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 1, 1, 13, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 4, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 13, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 4, 1, 16, Rotation.NONE, sbb);

		this.fillWithRandomizedBlocks(world, sbb, 1, 1, 8, 7, 5, 9, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 3, 1, 8, 5, 4, 9, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);

		// spawnwers
		this.setSpawner(world, 4, 1, 4, sbb, TFEntities.helmet_crab);

		this.setSpawner(world, 4, 4, 15, sbb, TFEntities.helmet_crab);

		// treasure!
		this.manualTreaurePlacement(world, 2, 4, 13, Direction.WEST, TFTreasure.stronghold_room, false, sbb);
		this.manualTreaurePlacement(world, 6, 4, 13, Direction.EAST, TFTreasure.stronghold_room, false, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	/**
	 * Make a doorway
	 */
	@Override
	protected void placeDoorwayAt(ISeedReader world, Random rand, int x, int y, int z, MutableBoundingBox sbb) {
		if (x == 0 || x == getXSize()) {
			this.fillWithBlocks(world, sbb, x, y, z - 1, x, y + 3, z + 1, Blocks.IRON_BARS.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		} else {
			this.fillWithBlocks(world, sbb, x - 1, y, z, x + 1, y + 3, z, Blocks.IRON_BARS.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		}
	}
}
