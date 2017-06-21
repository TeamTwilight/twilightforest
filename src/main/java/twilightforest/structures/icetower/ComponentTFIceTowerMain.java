package twilightforest.structures.icetower;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class ComponentTFIceTowerMain extends ComponentTFIceTowerWing {
	public boolean hasBossWing = false;

	public ComponentTFIceTowerMain() {
		super();
	}

	public ComponentTFIceTowerMain(World world, Random rand, int index, int x, int y, int z) {
		this(world, rand, index, x + SIZE, y + 40, z + SIZE, EnumFacing.NORTH);
	}

	public ComponentTFIceTowerMain(World world, Random rand, int index, int x, int y, int z, EnumFacing rotation) {
		super(index, x, y, z, SIZE, 31 + (rand.nextInt(3) * 10), rotation);

		// decorator
		if (this.deco == null) {
			this.deco = new StructureDecoratorIceTower();
		}
	}


	protected ComponentTFIceTowerMain(int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setBoolean("hasBossWing", this.hasBossWing);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);
		this.hasBossWing = par1NBTTagCompound.getBoolean("hasBossWing");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void buildComponent(StructureComponent parent, List list, Random rand) {
		super.buildComponent(parent, list, rand);

		// add entrance tower
		StructureBoundingBox towerBB = StructureBoundingBox.getNewBoundingBox();

		for (StructureComponent structurecomponent : (List<StructureComponent>) list) {
			towerBB.expandTo(structurecomponent.getBoundingBox());
		}

		// TODO: make this more general
		BlockPos myDoor = this.openings.get(0);
		BlockPos entranceDoor = new BlockPos(myDoor);


		if (myDoor.getX() == 0) {
			int length = this.getBoundingBox().minX - towerBB.minX;
			//System.out.println("Offset for door -x = " + length);
			if (length >= 0) {
				entranceDoor = entranceDoor.west(length);
				makeEntranceBridge(list, rand, this.getComponentType() + 1, myDoor.getX(), myDoor.getY(), myDoor.getZ(), length, Rotation.CLOCKWISE_180);
			}
		}
		if (myDoor.getX() == this.size - 1) {
			entranceDoor = entranceDoor.east(towerBB.maxX - this.getBoundingBox().maxX);
		}
		if (myDoor.getZ() == 0) {
			entranceDoor = entranceDoor.south(towerBB.minZ - this.getBoundingBox().minZ);
		}
		//FIXME: AtomicBlom I don't get it, should this not be getZ, and entranceDoor.north?
		if (myDoor.getX() == this.size - 1) {
			entranceDoor = entranceDoor.south(towerBB.maxZ - this.getBoundingBox().maxZ);
		}

		makeEntranceTower(list, rand, this.getComponentType() + 1, entranceDoor.getX(), entranceDoor.getY(), entranceDoor.getZ(), SIZE, 11, this.rotation);
	}

	private void makeEntranceBridge(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int length, Rotation rotation) {
		EnumFacing direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, 5, direction);

		ComponentTFIceTowerBridge bridge = new ComponentTFIceTowerBridge(index, dest.getX(), dest.getY(), dest.getZ(), length, direction);

		list.add(bridge);
		bridge.buildComponent(list.get(0), list, rand);
	}

	public boolean makeEntranceTower(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		ComponentTFIceTowerWing entrance = new ComponentTFIceTowerEntrance(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);

		list.add(entrance);
		entrance.buildComponent(list.get(0), list, rand);
		addOpening(x, y, z, rotation);
		return true;
	}

}
