package twilightforest.structures.lichtower;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;


public abstract class ComponentTFTowerRoof extends StructureTFComponentOld {

	protected int size;
	protected int height;

	public ComponentTFTowerRoof() {
		super();
	}

	public ComponentTFTowerRoof(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i);

		this.spawnListIndex = -1;

		// inheritors need to add a bounding box or die~!
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("roofSize", this.size);
		tagCompound.setInteger("roofHeight", this.height);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.size = tagCompound.getInteger("roofSize");
		this.height = tagCompound.getInteger("roofHeight");
	}

	/**
	 * Makes a bounding box that hangs forwards off of the tower wing we are on.  This is for attached roofs.
	 *
	 * @param wing
	 */
	protected void makeAttachedOverhangBB(ComponentTFTowerWing wing) {
		// just hang out at the very top of the tower
		switch (getCoordBaseMode()) {
			case SOUTH:
				this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
				break;
			case WEST:
				this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
				break;
			case EAST:
				this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
				break;
			case NORTH:
				this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ);
				break;
			default:
				break;
		}
	}


	/**
	 * Makes a bounding box that sits at the top of the tower.  Works for attached or freestanding roofs.
	 *
	 * @param wing
	 */
	protected void makeCapBB(ComponentTFTowerWing wing) {
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX, wing.getBoundingBox().maxY + this.height, wing.getBoundingBox().maxZ);
	}


	/**
	 * Make a bounding box that hangs over the sides of the tower 1 block.  Freestanding towers only.
	 *
	 * @param wing
	 */
	protected void makeOverhangBB(ComponentTFTowerWing wing) {
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
	}


	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		return false;
	}

	/**
	 * Does this roof intersect anything except the parent tower?
	 */
	public boolean fits(ComponentTFTowerWing parent, List<StructureComponent> list, Random rand) {
		return StructureComponent.findIntersecting(list, this.boundingBox) == parent;
	}

}
