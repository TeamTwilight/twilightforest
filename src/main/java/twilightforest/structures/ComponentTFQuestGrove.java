package twilightforest.structures;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.entity.TFEntities;
import twilightforest.entity.passive.EntityTFQuestRam;
import twilightforest.util.ColorUtil;

import java.util.Random;

public class ComponentTFQuestGrove extends StructureTFComponentOld {

	private static final int RADIUS = 13;
	private static final BlockState MOSSY_STONEBRICK = Blocks.MOSSY_STONE_BRICKS.getDefaultState();
	private static final BlockState CHISELED_STONEBRICK = Blocks.CHISELED_STONE_BRICKS.getDefaultState();

	protected boolean beastPlaced = false;
	protected boolean dispenserPlaced = false;

	public ComponentTFQuestGrove(TemplateManager manager, CompoundNBT nbt) {
		super(TFFeature.TFQuest1, nbt);
	}

	//TODO: Parameter "rand" is unused. Remove?
	public ComponentTFQuestGrove(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFeature.TFQuest1, feature, i);

		this.setCoordBaseMode(Direction.SOUTH);

		// the maze is 25 x 25 for now
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -RADIUS, 0, -RADIUS, RADIUS * 2, 10, RADIUS * 2, Direction.SOUTH);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		for (Direction e : Direction.Plane.HORIZONTAL) {
			// make the rings
			makeWallSide(world, rand, e, sbb);
		}

		// a small platform
		for (int x = 10; x < 17; x++) {
			for (int z = 10; z < 17; z++) {
				if (x == 10 || x == 16 || z == 10 || z == 16) {
					if (rand.nextInt(2) > 0) {
						setBlockState(world, MOSSY_STONEBRICK, x, -1, z, sbb);
					}
				} else if (x == 11 || x == 15 || z == 11 || z == 15) {
					if (rand.nextInt(3) > 0) {
						setBlockState(world, MOSSY_STONEBRICK, x, -1, z, sbb);
					}
				} else {
					setBlockState(world, MOSSY_STONEBRICK, x, -1, z, sbb);
				}
			}
		}

		// dispenser frame and button
		setBlockState(world, Blocks.STONE_BUTTON.getDefaultState().with(HorizontalFaceBlock.FACE, AttachFace.WALL).with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH), 13, 5, 19, sbb);

		setBlockState(world, MOSSY_STONEBRICK, 12, 7, 20, sbb);
		setBlockState(world, MOSSY_STONEBRICK, 13, 7, 20, sbb);
		setBlockState(world, MOSSY_STONEBRICK, 14, 7, 20, sbb);
		setBlockState(world, MOSSY_STONEBRICK, 12, 7, 21, sbb);
		setBlockState(world, MOSSY_STONEBRICK, 13, 7, 21, sbb);
		setBlockState(world, MOSSY_STONEBRICK, 14, 7, 21, sbb);

		// the dispenser
		if (!dispenserPlaced) {
			int bx = this.getXWithOffset(13, 20);
			int by = this.getYWithOffset(6);
			int bz = this.getZWithOffset(13, 20);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isVecInside(pos)) {
				dispenserPlaced = true;

				world.setBlockState(pos, Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, Direction.NORTH), 4);
				DispenserTileEntity ted = (DispenserTileEntity) world.getTileEntity(pos);

				// add 4 random wool blocks
				for (int i = 0; i < 4; i++) {
					ted.setInventorySlotContents(i, new ItemStack(ColorUtil.WOOL.getRandomColor(rand), 1));
				}
			}
		}

		// the quest beast!
		if (!beastPlaced) {
			int bx = this.getXWithOffset(13, 13);
			int by = this.getYWithOffset(0);
			int bz = this.getZWithOffset(13, 13);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isVecInside(pos)) {
				beastPlaced = true;

				EntityTFQuestRam ram = new EntityTFQuestRam(TFEntities.quest_ram, world.getWorld());
				ram.setPosition(bx, by, bz);
				ram.setHomePosAndDistance(pos, 13);
				ram.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);

				world.addEntity(ram);
			}
		}

		return true;
	}

	//TODO: Parameter "rand" is unused. Remove?
	private void makeWallSide(ISeedReader world, Random rand, Direction direction, MutableBoundingBox sbb) {
		Direction temp = this.getCoordBaseMode();
		this.setCoordBaseMode(direction);

		// arches
		placeOuterArch(world, 3, -1, sbb);
		placeOuterArch(world, 11, -1, sbb);
		placeOuterArch(world, 19, -1, sbb);

		// connecting thingers
		setBlockState(world, CHISELED_STONEBRICK, 0, 0, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 0, 1, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 0, 2, 0, sbb);

		setBlockState(world, CHISELED_STONEBRICK, 0, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 1, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 2, 3, 0, sbb);

		setBlockState(world, CHISELED_STONEBRICK, 8, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 9, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 10, 3, 0, sbb);

		setBlockState(world, CHISELED_STONEBRICK, 16, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 17, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 18, 3, 0, sbb);

		setBlockState(world, CHISELED_STONEBRICK, 24, 3, 0, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 25, 3, 0, sbb);

		// inner arch
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				for (int z = 0; z < 2; z++) {
					if (x == 0 || x == 1 || x == 7 || x == 8 || y == 0 || y == 1 || y == 7 || y == 8) {
						setBlockState(world, MOSSY_STONEBRICK, x + 9, y - 2, z + 5, sbb);
					}
				}
			}
		}

		// connecting thingers
		setBlockState(world, CHISELED_STONEBRICK, 6, 0, 6, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 6, 1, 6, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 6, 2, 6, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 6, 3, 6, sbb);

		setBlockState(world, CHISELED_STONEBRICK, 6, 4, 6, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 7, 4, 6, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 8, 4, 6, sbb);

		setBlockState(world, CHISELED_STONEBRICK, 18, 4, 6, sbb);
		setBlockState(world, CHISELED_STONEBRICK, 19, 4, 6, sbb);

		this.setCoordBaseMode(temp);
	}

	private void placeOuterArch(ISeedReader world, int ox, int oy, MutableBoundingBox sbb) {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 6; y++) {
				if (x == 0 || x == 4 || y == 0 || y == 5) {
					setBlockState(world, MOSSY_STONEBRICK, x + ox, y + oy, 0, sbb);
				}
			}
		}
	}
}
