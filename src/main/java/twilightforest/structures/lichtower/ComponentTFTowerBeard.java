package twilightforest.structures.lichtower;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;


public class ComponentTFTowerBeard extends StructureTFComponentOld {

	int size;
	int height;

	public ComponentTFTowerBeard() {
		super();
	}

	public ComponentTFTowerBeard(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i);

		this.setCoordBaseMode(wing.getCoordBaseMode());
		this.size = wing.size - 2;
		this.height = size / 2;

		// just hang out at the very bottom of the tower
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX + 1, wing.getBoundingBox().minY - this.height - 1, wing.getBoundingBox().minZ + 1, wing.getBoundingBox().maxX - 1, wing.getBoundingBox().minY - 1, wing.getBoundingBox().maxZ - 1);

	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("beardSize", this.size);
		tagCompound.setInteger("beardHeight", this.height);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.size = tagCompound.getInteger("beardSize");
		this.height = tagCompound.getInteger("beardHeight");
	}

	/**
	 * Makes a pyramid-shaped beard
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		return makePyramidBeard(world, rand, sbb);
	}

	private boolean makePyramidBeard(World world, Random rand, StructureBoundingBox sbb) {
		for (int y = 0; y <= height; y++) {
			int min = y;
			int max = size - y - 1;

			fillWithRandomizedBlocks(world, sbb, min, height - y, min, max, height - y, max, false, rand, StructureTFComponentOld.getStrongholdStones());
		}
		return true;
	}


}
