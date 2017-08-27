package twilightforest.structures.stronghold;

import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFTreasure;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdSmallStairs extends StructureTFStrongholdComponent {

	private boolean enterBottom;
	public boolean hasTreasure;
	public boolean chestTrapped;

	public ComponentTFStrongholdSmallStairs() {
	}

	public ComponentTFStrongholdSmallStairs(int i, EnumFacing facing, int x, int y, int z) {
		super(i, facing, x, y, z);
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setBoolean("enterBottom", this.enterBottom);
		par1NBTTagCompound.setBoolean("hasTreasure", this.hasTreasure);
		par1NBTTagCompound.setBoolean("chestTrapped", this.chestTrapped);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);
		this.enterBottom = par1NBTTagCompound.getBoolean("enterBottom");
		this.hasTreasure = par1NBTTagCompound.getBoolean("hasTreasure");
		this.chestTrapped = par1NBTTagCompound.getBoolean("chestTrapped");
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
			return StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 14, 9, facing);
		} else {
			// enter on the top
			return StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, -8, 0, 9, 14, 9, facing);
		}
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
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
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 13, 8, rand, deco.randomBlocks);

		// railing
		this.fillWithBlocks(world, sbb, 1, 7, 1, 7, 7, 7, deco.platformState, Blocks.AIR.getDefaultState(), false);
		this.fillWithAir(world, sbb, 2, 7, 2, 6, 7, 6);

		Rotation rotation = this.enterBottom ? Rotation.NONE : Rotation.CLOCKWISE_180;

		// stairs
		for (int y = 1; y < 8; y++) {
			for (int x = 3; x < 6; x++) {
				this.setBlockStateRotated(world, Blocks.AIR.getDefaultState(), x, y + 1, y, rotation, sbb);
				this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.NORTH), x, y, y, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, x, y - 1, y, rotation, sbb);
			}
		}

		// treasure
		if (this.hasTreasure) {
			this.placeTreasureRotated(world, 4, 1, 6, rotation, TFTreasure.stronghold_cache, this.chestTrapped, sbb);

			if (this.chestTrapped) {
				this.setBlockStateRotated(world, Blocks.TNT.getDefaultState(), 4, 0, 6, rotation, sbb);
			}

			for (int z = 5; z < 8; z++) {
				this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.WEST), 3, 1, z, rotation, sbb);
				this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.EAST), 5, 1, z, rotation, sbb);
			}

			this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 4, 1, 5, rotation, sbb);
			this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 4, 1, 7, rotation, sbb);
			this.setBlockStateRotated(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 4, 2, 6, rotation, sbb);
		}

		if (enterBottom) {
			this.placeWallStatue(world, 4, 8, 1, Rotation.CLOCKWISE_180, sbb);
		} else {
			this.placeWallStatue(world, 4, 8, 7, Rotation.NONE, sbb);
		}

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

}
