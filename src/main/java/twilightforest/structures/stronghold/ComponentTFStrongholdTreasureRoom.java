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
import twilightforest.TFTreasure;
import twilightforest.util.TFEntityNames;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdTreasureRoom extends StructureTFStrongholdComponent {

	private boolean enterBottom;

	public ComponentTFStrongholdTreasureRoom() {
	}

	public ComponentTFStrongholdTreasureRoom(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
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
		return StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 7, 18, facing);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		this.addDoor(4, 1, 0);
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
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
		this.setSpawner(world, 4, 1, 4, sbb, TFEntityNames.HELMET_CRAB);

		this.setSpawner(world, 4, 4, 15, sbb, TFEntityNames.HELMET_CRAB);

		// treasure!
		this.placeTreasureAtCurrentPosition(world, rand, 2, 4, 13, TFTreasure.stronghold_room, sbb);
		this.placeTreasureAtCurrentPosition(world, rand, 6, 4, 13, TFTreasure.stronghold_room, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}

	/**
	 * Make a doorway
	 */
	@Override
	protected void placeDoorwayAt(World world, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
		if (x == 0 || x == getXSize()) {
			this.fillWithBlocks(world, sbb, x, y, z - 1, x, y + 3, z + 1, Blocks.IRON_BARS.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		} else {
			this.fillWithBlocks(world, sbb, x - 1, y, z, x + 1, y + 3, z, Blocks.IRON_BARS.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		}
	}


}
