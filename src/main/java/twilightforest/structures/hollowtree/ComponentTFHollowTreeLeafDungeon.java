package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFSwarmSpider;
import java.util.Random;


/**
 * A blob of leaves used to make trees
 *
 * @author Ben
 */
public class ComponentTFHollowTreeLeafDungeon extends StructureTFTreeComponent
{
	int radius;

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
	protected ComponentTFHollowTreeLeafDungeon(TFFeature feature, int index, int x, int y, int z, int radius) {
		super(feature, index);
		this.setCoordBaseMode(EnumFacing.SOUTH);
		boundingBox = new StructureBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
		this.radius = radius;
	}


	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("leafRadius", this.radius);

	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);

		this.radius = tagCompound.getInteger("leafRadius");
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb)
	{
		return this.addComponentParts(world, random, sbb, false);
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb, boolean drawLeaves) {
		if (!drawLeaves) {
			// wood
			drawHollowBlob(world, sbb, radius, radius, radius, 3, 2, TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE), false);
			// then treasure chest
			// which direction is this chest in?
			this.placeTreasureAtCurrentPosition(world, random, radius + 2, radius - 1, radius, TFTreasure.tree_cache, sbb);

			// then spawner
			setSpawner(world, radius, radius, radius, sbb, EntityList.getKey(EntityTFSwarmSpider.class));
		} else {
			// hollow sphere of leaves on the outside
			drawHollowBlob(world, sbb, radius, radius, radius, 4, 2, TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false), true);
		}
		return true;
	}

	private void drawHollowBlob(World world, StructureBoundingBox sbb, int sx, int sy, int sz, int blobRadius, int hollowRadius, IBlockState blockState, boolean isLeaves) {
		// then trace out a quadrant
		for (byte dx = 0; dx <= blobRadius; dx++) {
			for (byte dy = 0; dy <= blobRadius; dy++) {
				for (byte dz = 0; dz <= blobRadius; dz++) {
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
					if (dist > hollowRadius && dist <= blobRadius) {
						// do eight at a time for easiness!
						if (isLeaves) {
							placeLeafBlock(world, blockState, sx + dx, sy + dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx + dx, sy + dy, sz - dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy + dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy + dy, sz - dz, sbb);
							placeLeafBlock(world, blockState, sx + dx, sy - dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx + dx, sy - dy, sz - dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy - dy, sz + dz, sbb);
							placeLeafBlock(world, blockState, sx - dx, sy - dy, sz - dz, sbb);
						} else {
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
}
