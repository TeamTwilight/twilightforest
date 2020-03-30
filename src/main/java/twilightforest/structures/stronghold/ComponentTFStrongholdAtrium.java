package twilightforest.structures.stronghold;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdAtrium extends StructureTFStrongholdComponent {

	private boolean enterBottom;

	public ComponentTFStrongholdAtrium(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSAt, nbt);
	}

	public ComponentTFStrongholdAtrium(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(feature, i, facing, x, y, z);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(CompoundNBT tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.putBoolean("enterBottom", this.enterBottom);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		this.enterBottom = tagCompound.getBoolean("enterBottom");
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
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 18, 14, 18, facing);
		} else {
			// enter on the top
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -8, 0, 18, 14, 18, facing);
		}
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		if (this.enterBottom) {
			this.addDoor(4, 1, 0);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 13, 8, -1);
		} else {
			this.addDoor(13, 8, 0);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 4, 1, -1);
		}

		addNewComponent(parent, list, random, Rotation.NONE, 13, 1, 18);
		addNewComponent(parent, list, random, Rotation.NONE, 4, 8, 18);
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public boolean generate(IWorld world, ChunkGenerator<?> generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn) {
		placeStrongholdWalls(world.getWorld(), sbb, 0, 0, 0, 17, 13, 17, rand, deco.randomBlocks);


		// balcony
		this.fillWithRandomizedBlocks(world, sbb, 1, 6, 1, 16, 7, 16, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 5, 8, 5, 12, 8, 12, deco.fenceState, AIR, false);
		this.fillWithAir(world, sbb, 6, 6, 6, 11, 8, 11);

		// balcony pillars
		placeBalconyPillar(world.getWorld(), sbb, Rotation.NONE);
		placeBalconyPillar(world.getWorld(), sbb, Rotation.CLOCKWISE_90);
		placeBalconyPillar(world.getWorld(), sbb, Rotation.CLOCKWISE_180);
		placeBalconyPillar(world.getWorld(), sbb, Rotation.COUNTERCLOCKWISE_90);

		// corner pillars
		this.fillWithRandomizedBlocks(world, sbb, 1, 1, 1, 1, 12, 2, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 2, 1, 1, 2, 12, 1, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 16, 1, 1, 16, 12, 2, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 15, 1, 1, 15, 12, 1, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 1, 1, 15, 1, 12, 16, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 2, 1, 16, 2, 12, 16, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 16, 1, 15, 16, 12, 16, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 15, 1, 16, 15, 12, 16, false, rand, deco.randomBlocks);

		// grass
		BlockState grass = Blocks.GRASS.getDefaultState();
		this.generateMaybeBox(world, sbb, rand, 0.5F, 6, 0, 6, 11, 0, 11, grass, grass, false, 0);
		this.fillWithBlocks(world, sbb, 7, 0, 7, 10, 0, 10, grass, AIR, false);

		// tree
		this.spawnATree(world.getWorld(), rand.nextInt(5), 8, 1, 8, sbb);

		// statues
		placeCornerStatue(world.getWorld(), 2, 8, 2, 0, sbb);
		placeCornerStatue(world.getWorld(), 2, 1, 15, 1, sbb);
		placeCornerStatue(world.getWorld(), 15, 1, 2, 2, sbb);
		placeCornerStatue(world.getWorld(), 15, 8, 15, 3, sbb);

		// doors
		placeDoors(world.getWorld(), rand, sbb);

		return true;
	}

	private void spawnATree(World world, int treeNum, int x, int y, int z, MutableBoundingBox sbb) {
		BlockPos pos = getBlockPosWithOffset(x, y, z);

		if (sbb.isVecInside(pos)) {
			WorldGenerator treeGen;
			// grow a tree
			int minHeight = 8;

			switch (treeNum) {
				case 0:
				default:
					// oak tree
					BlockState oakWood = Blocks.OAK_LOG.getDefaultState();
					BlockState oakLeaves = Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.CHECK_DECAY, false);
					treeGen = new WorldGenTrees(true, minHeight, oakWood, oakLeaves, false);
					break;
				case 1:
					// jungle tree
					BlockState jungleWood = Blocks.JUNGLE_LOG.getDefaultState();
					BlockState jungleLeaves = Blocks.JUNGLE_LEAVES.getDefaultState().with(LeavesBlock.CHECK_DECAY, false);
					treeGen = new WorldGenTrees(true, minHeight, jungleWood, jungleLeaves, false);
					break;
				case 2:
					// birch
					BlockState birchWood = Blocks.BIRCH_LOG.getDefaultState();
					BlockState birchLeaves = Blocks.BIRCH_LEAVES.getDefaultState().with(LeavesBlock.CHECK_DECAY, false);
					treeGen = new WorldGenTrees(true, minHeight, birchWood, birchLeaves, false);
					break;
				case 3:
					treeGen = new TFGenSmallTwilightOak(false, minHeight);
					break;
				case 4:
					treeGen = new TFGenSmallRainboak(false);
					break;
			}

			for (int i = 0; i < 100; i++) {
				if (treeGen.generate(world, world.rand, pos)) {
					break;
				}
			}
		}
	}

	private void placeBalconyPillar(World world, MutableBoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 5, 1, 5, 5, 12, 5, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), rotation, false), 5, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), rotation, false), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), rotation, true), 5, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), rotation, true), 6, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), rotation, true), 5, 12, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), rotation, true), 6, 12, 5, rotation, sbb);
	}
}
