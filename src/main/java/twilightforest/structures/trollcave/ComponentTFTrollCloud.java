package twilightforest.structures.trollcave;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.structures.StructureTFComponent;

import java.util.Random;

public class ComponentTFTrollCloud extends StructureTFComponent {

	private int size;
	private int height;

	public ComponentTFTrollCloud() {
	}

	public ComponentTFTrollCloud(int index, int x, int y, int z) {
		super(index);
		this.setCoordBaseMode(EnumFacing.SOUTH);

		this.size = 40;
		this.height = 20;

		int radius = this.size / 2;
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, -radius, -this.height, -radius, this.size, this.height, this.size, EnumFacing.SOUTH);
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setInteger("size", this.size);
		par1NBTTagCompound.setInteger("height", this.height);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);
		this.size = par1NBTTagCompound.getInteger("size");
		this.height = par1NBTTagCompound.getInteger("height");
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeCloud(world, sbb, 0, 0, 0, this.size - 1, 6, this.size - 1);

		return true;
	}

	protected void placeCloud(World world, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, Blocks.STAINED_GLASS.getDefaultState(), Blocks.STAINED_GLASS.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, minX + 2, minY + 2, minZ + 2, maxX - 2, maxY - 1, maxZ - 2, Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.QUARTZ_BLOCK.getDefaultState(), false);

	}

}
