package twilightforest.structures.hollowtree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFCreatures;
import twilightforest.structures.StructureTFComponent;


/**
 * A blob of leaves used to make trees 
 * 
 * @author Ben
 *
 */
public class ComponentTFHollowTreeLeafDungeon extends StructureTFComponent {
	
	int radius; // radius

	
	public ComponentTFHollowTreeLeafDungeon() {
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
	protected ComponentTFHollowTreeLeafDungeon(int index, int x, int y, int z, int radius) {
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
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readStructureFromNBT(par1NBTTagCompound);

        this.radius = par1NBTTagCompound.getInteger("leafRadius");
	}
	
	/**
	 * Add other structure components to this one if needed
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List list, Random rand) {
		// the bounding box should be cubical, so we can rotate freely
		this.setCoordBaseMode(EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)]);
	}

	/**
	 * Draw a giant blob of whatevs (okay, it's going to be leaves).
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		
		// leaves on the outside
		drawBlockBlob(world, sbb, radius, radius, radius, 4, TFBlocks.leaves.getDefaultState(), true);
		// then wood
		drawBlockBlob(world, sbb, radius, radius, radius, 3, TFBlocks.log.getDefaultState().withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE), false);
		// then air
		drawBlockBlob(world, sbb, radius, radius, radius, 2, AIR, false);
		
		// then treasure chest
		// which direction is this chest in?
		this.placeTreasureAtCurrentPosition(world, rand, radius + 2, radius - 1, radius, TFTreasure.tree_cache, sbb);
			
		// then spawner
		setSpawner(world, radius, radius, radius, sbb, TFCreatures.getSpawnerNameFor("Swarm Spider"));
		
		return true;
	}

	private void drawBlockBlob(World world, StructureBoundingBox sbb, int sx, int sy, int sz, int blobRadius, IBlockState blockState, boolean isLeaves) {
		// then trace out a quadrant
		for (byte dx = 0; dx <= blobRadius; dx++)
		{
			for (byte dy = 0; dy <= blobRadius; dy++)
			{
				for (byte dz = 0; dz <= blobRadius; dz++)
				{
					// determine how far we are from the center.
					byte dist = 0;

					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte)((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz)
					{
						dist = (byte) (dy + (byte)((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte)((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}


					// if we're inside the blob, fill it
					if (dist <= blobRadius) {
						// do eight at a time for easiness!
						if (isLeaves)
						{
							placeLeafBlock(world, blockState, sx + dx, sy + dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx + dx, sy + dy, sz - dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy + dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy + dy, sz - dz, sbb);
							placeLeafBlock(world, blockState, sx + dx, sy - dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx + dx, sy - dy, sz - dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy - dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy - dy, sz - dz, sbb);
						}
						else
						{
							this.setBlockState(world, blockState, sx + dx, sy + dy, sz + dz, sbb);
							this.setBlockState(world, blockState, sx + dx, sy + dy, sz - dz, sbb);
							this.setBlockState(world, blockState, sx - dx, sy + dy, sz + dz, sbb);
							this.setBlockState(world, blockState, sx - dx, sy + dy, sz - dz, sbb);
							this.setBlockState(world, blockState, sx + dx, sy - dy, sz + dz, sbb);
							this.setBlockState(world, blockState, sx + dx, sy - dy, sz - dz, sbb);
							this.setBlockState(world, blockState, sx - dx, sy - dy, sz + dz, sbb);
							this.setBlockState(world, blockState, sx - dx, sy - dy, sz - dz, sbb);

						}

					}
				}
			}
		}
	}

	/**
	 * Puts a block only if leaves can go there.
	 */
	protected void placeLeafBlock(World world, IBlockState blockState, int x, int y, int z, StructureBoundingBox sbb) {

		final BlockPos blockPosWithOffset = this.getBlockPosWithOffset(x, y, z);

		if (sbb.isVecInside(blockPosWithOffset))
		{
			IBlockState whatsThere = world.getBlockState(blockPosWithOffset);

			if (whatsThere.getBlock().canBeReplacedByLeaves(blockState, world, blockPosWithOffset))
			{
				world.setBlockState(blockPosWithOffset, blockState, 2);
			}
		}
	}

}
