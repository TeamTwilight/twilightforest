package twilightforest.structures.icetower;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;

public class ComponentTFIceTowerBeard extends StructureTFComponentOld {

	protected int size;
	protected int height;

	public ComponentTFIceTowerBeard() {
	}

	public ComponentTFIceTowerBeard(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i);

		// same alignment
		this.setCoordBaseMode(wing.getCoordBaseMode());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = Math.round(this.size * 1.414F);

		this.deco = wing.deco;

		// just hang out at the very bottom of the tower
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().minY - this.height, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX, wing.getBoundingBox().minY, wing.getBoundingBox().maxZ);
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
	 * Makes a dark tower type beard
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				//int rHeight = this.size - (int) MathHelper.sqrt_float(x * z); // interesting office building pattern
				int rHeight = Math.round(MathHelper.sqrt(x * x + z * z));
				//int rHeight = MathHelper.ceiling_float_int(Math.min(x * x / 9F, z * z / 9F));

				for (int y = 0; y < rHeight; y++) {
					this.setBlockState(world, deco.blockState, x, this.height - y, z, sbb);

				}
			}
		}
		return true;
	}

}
