package twilightforest.structures.darktower;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFTowerWood;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerWoodVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;


public class ComponentTFDarkTowerBeard extends StructureTFComponentOld {

	protected int size;
	protected int height;

	public ComponentTFDarkTowerBeard() {
		super();
	}

	public ComponentTFDarkTowerBeard(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i);

		this.setCoordBaseMode(wing.getCoordBaseMode());
		this.size = wing.size;
		this.height = size / 2;

		// just hang out at the very bottom of the tower
		this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().minY - this.height, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX, wing.getBoundingBox().minY, wing.getBoundingBox().maxZ);

	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("beardSize", this.size);
		tagCompound.setInteger("beardHeight", this.height);
	}

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
		makeDarkBeard(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

		return true;
	}


	protected void makeDarkBeard(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		IBlockState frameState = TFBlocks.tower_wood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.ENCASED);

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (x == minX || x == maxX || z == minZ || z == maxZ) {
					int length = Math.min(Math.abs(x - height) - 1, Math.abs(z - height) - 1);

					if (length == height - 1) {
						length++;
					}

					if (length == -1) {
						length = 1;
					}

					for (int y = maxY; y >= height - length; y--) {
						// wall
						this.setBlockState(world, frameState, x, y, z, sbb);
					}
				}
			}
		}
	}
}
