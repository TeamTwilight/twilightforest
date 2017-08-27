package twilightforest.structures.stronghold;

import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.world.TFGenSmallRainboak;
import twilightforest.world.TFGenSmallTwilightOak;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdAtrium extends StructureTFStrongholdComponent {

	private boolean enterBottom;

	public ComponentTFStrongholdAtrium() {
	}

	public ComponentTFStrongholdAtrium(int i, EnumFacing facing, int x, int y, int z) {
		super(i, facing, x, y, z);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setBoolean("enterBottom", this.enterBottom);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);
		this.enterBottom = par1NBTTagCompound.getBoolean("enterBottom");
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {

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
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
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
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 13, 17, rand, deco.randomBlocks);


		// balcony
		this.fillWithRandomizedBlocks(world, sbb, 1, 6, 1, 16, 7, 16, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 5, 8, 5, 12, 8, 12, deco.fenceState, AIR, false);
		this.fillWithAir(world, sbb, 6, 6, 6, 11, 8, 11);

		// balcony pillars
		placeBalconyPillar(world, sbb, Rotation.NONE);
		placeBalconyPillar(world, sbb, Rotation.CLOCKWISE_90);
		placeBalconyPillar(world, sbb, Rotation.CLOCKWISE_180);
		placeBalconyPillar(world, sbb, Rotation.COUNTERCLOCKWISE_90);

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
		IBlockState grass = Blocks.GRASS.getDefaultState();
		this.generateMaybeBox(world, sbb, rand, 0.5F, 6, 0, 6, 11, 0, 11, grass, grass, false, 0);
		this.fillWithBlocks(world, sbb, 7, 0, 7, 10, 0, 10, grass, AIR, false);

		// tree
		this.spawnATree(world, rand.nextInt(5), 8, 1, 8, sbb);

		// statues
		placeCornerStatue(world, 2, 8, 2, 0, sbb);
		placeCornerStatue(world, 2, 1, 15, 1, sbb);
		placeCornerStatue(world, 15, 1, 2, 2, sbb);
		placeCornerStatue(world, 15, 8, 15, 3, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	private void spawnATree(World world, int treeNum, int x, int y, int z, StructureBoundingBox sbb) {
		BlockPos pos = getBlockPosWithOffset(x, y, z);

		if (sbb.isVecInside(pos)) {
			WorldGenerator treeGen;
			// grow a tree
			int minHeight = 8;

			switch (treeNum) {
				case 0:
				default:
					// oak tree
					IBlockState oakWood = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
					IBlockState oakLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK);
					treeGen = new WorldGenTrees(true, minHeight, oakWood, oakLeaves, false);
					break;
				case 1:
					// jungle tree
					IBlockState jungleWood = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
					IBlockState jungleLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE);
					treeGen = new WorldGenTrees(true, minHeight, jungleWood, jungleLeaves, false);
					break;
				case 2:
					// birch
					IBlockState birchWood = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
					IBlockState birchLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH);
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


	private void placeBalconyPillar(World world, StructureBoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 5, 1, 5, 5, 12, 5, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 5, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 5, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 6, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 5, 12, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 6, 12, 5, rotation, sbb);
	}
}
