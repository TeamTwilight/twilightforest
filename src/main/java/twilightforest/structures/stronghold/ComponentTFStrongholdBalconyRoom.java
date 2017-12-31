package twilightforest.structures.stronghold;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdBalconyRoom extends StructureTFStrongholdComponent {

	boolean enterBottom;

	public ComponentTFStrongholdBalconyRoom() {
	}

	public ComponentTFStrongholdBalconyRoom(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
		super(feature, i, facing, x, y, z);
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setBoolean("enterBottom", this.enterBottom);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.enterBottom = tagCompound.getBoolean("enterBottom");
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
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 18, 14, 27, facing);
		} else {
			// enter on the top
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -8, 0, 18, 14, 27, facing);
		}
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
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
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 13, 26, rand, deco.randomBlocks);

		// balcony
		this.fillWithRandomizedBlocks(world, sbb, 1, 6, 1, 16, 7, 25, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 4, 8, 4, 13, 8, 22, deco.fenceState, Blocks.AIR.getDefaultState(), false);
		this.fillWithAir(world, sbb, 5, 6, 5, 12, 8, 21);

		// stairs & pillars
		placeStairsAndPillars(world, sbb, Rotation.NONE);
		placeStairsAndPillars(world, sbb, Rotation.CLOCKWISE_180);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	private void placeStairsAndPillars(World world, StructureBoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 4, 1, 4, 4, 12, 4, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 4, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), 5, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 4, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 5, 5, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 4, 12, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 5, 12, 4, rotation, sbb);

		this.fillBlocksRotated(world, sbb, 13, 1, 4, 13, 12, 4, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 13, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, false), 12, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 13, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, true), 12, 5, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 13, 12, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, true), 12, 12, 4, rotation, sbb);

		this.fillBlocksRotated(world, sbb, 13, 1, 8, 13, 12, 8, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 13, 1, 9, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 13, 1, 7, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, false), 12, 1, 8, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 13, 5, 9, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 13, 5, 7, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 13, 12, 9, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 13, 12, 7, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, true), 12, 12, 8, rotation, sbb);

		for (int y = 1; y < 8; y++) {
			for (int z = 5; z < 8; z++) {
				this.setBlockStateRotated(world, AIR, y + 6, y + 1, z, rotation, sbb);
				this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, false), y + 6, y, z, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, y + 6, y - 1, z, rotation, sbb);
			}
		}
	}


}
