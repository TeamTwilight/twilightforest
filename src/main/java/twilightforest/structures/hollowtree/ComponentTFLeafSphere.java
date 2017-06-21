package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponent;

import java.util.Random;


/**
 * A blob of leaves used to make trees
 *
 * @author Ben
 */
public class ComponentTFLeafSphere extends StructureTFComponent {

	int radius; // radius


	public ComponentTFLeafSphere() {
		super();
	}

	/**
	 * Make a blob of leaves
	 *
	 * @param index
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	protected ComponentTFLeafSphere(int index, int x, int y, int z, int radius) {
		super(index);

		this.setCoordBaseMode(EnumFacing.SOUTH);

		boundingBox = new StructureBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
		this.radius = radius;
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setInteger("leafRadius", this.radius);

	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);

		this.radius = par1NBTTagCompound.getInteger("leafRadius");
	}

	/**
	 * Draw a giant blob of whatevs (okay, it's going to be leaves).
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {


		int sx = radius;
		int sy = radius;
		int sz = radius;

		// then trace out a quadrant
		for (byte dx = 0; dx <= radius; dx++) {
			for (byte dy = 0; dy <= radius; dy++) {
				for (byte dz = 0; dz <= radius; dz++) {
					// determine how far we are from the center.
					byte dist = 0;

					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz) {
						dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}


					// if we're inside the blob, fill it
					if (dist <= radius) {
						// do eight at a time for easiness!
						final IBlockState leaves = TFBlocks.leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false);
						placeBlockIfEmpty(world, leaves, sx + dx, sy + dy, sz + dz, sbb);
						placeBlockIfEmpty(world, leaves, sx + dx, sy + dy, sz - dz, sbb);
						placeBlockIfEmpty(world, leaves, sx - dx, sy + dy, sz + dz, sbb);
						placeBlockIfEmpty(world, leaves, sx - dx, sy + dy, sz - dz, sbb);
						placeBlockIfEmpty(world, leaves, sx + dx, sy - dy, sz + dz, sbb);
						placeBlockIfEmpty(world, leaves, sx + dx, sy - dy, sz - dz, sbb);
						placeBlockIfEmpty(world, leaves, sx - dx, sy - dy, sz + dz, sbb);
						placeBlockIfEmpty(world, leaves, sx - dx, sy - dy, sz - dz, sbb);

					}
				}
			}
		}

		return true;
	}

	/**
	 * Puts a block only if the block that's there is air.
	 * <p>
	 * TODO: This could be more efficient by combining the duplicate logic of the getBlockStateFromPos and setBlockState functions.
	 */
	protected void placeBlockIfEmpty(World world, IBlockState blockState, int x, int y, int z, StructureBoundingBox sbb) {
		if (getBlockStateFromPos(world, x, y, z, sbb).getBlock() == Blocks.AIR) {
			setBlockState(world, blockState, x, y, z, sbb);
		}
	}

}
