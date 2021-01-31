package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import java.util.Random;

/**
 * A blob of leaves used to make trees
 *
 * @author Ben
 */
public class ComponentTFHollowTreeLeafDungeon extends StructureTFTreeComponent {
	int radius;

	public ComponentTFHollowTreeLeafDungeon(TemplateManager manager, CompoundNBT nbt) {
		super(TFHollowTreePieces.TFHTLD, nbt);
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
		super(TFHollowTreePieces.TFHTLD, feature, index);
		this.setCoordBaseMode(Direction.SOUTH);
		boundingBox = new MutableBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
		this.radius = radius;
	}

	/**
	 * Save to NBT
	 * TODO: See super
	 */
//	@Override
//	protected void writeStructureToNBT(CompoundNBT tagCompound) {
//		super.writeStructureToNBT(tagCompound);
//
//		tagCompound.putInt("leafRadius", this.radius);
//	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		this.radius = tagCompound.getInt("leafRadius");
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		return this.addComponentParts(world, generator, random, sbb, false);
	}

	@Override
	public boolean addComponentParts(ISeedReader world, ChunkGenerator generator, Random random, MutableBoundingBox sbb, boolean drawLeaves) {
		if (!drawLeaves) {
			// wood
			drawHollowBlob(world, sbb, radius, radius, radius, 3, 2, TFBlocks.oak_wood.get().getDefaultState(), false);
			// then treasure chest
			// which direction is this chest in?
			this.placeTreasureAtCurrentPosition(world, radius + 2, radius - 1, radius, TFTreasure.tree_cache, sbb);

			// then spawner
			setSpawner(world, radius, radius, radius, sbb, TFEntities.swarm_spider);
		} else {
			// hollow sphere of leaves on the outside
			drawHollowBlob(world, sbb, radius, radius, radius, 4, 2, TFBlocks.oak_leaves.get().getDefaultState(), true);
		}
		return true;
	}

	private void drawHollowBlob(ISeedReader world, MutableBoundingBox sbb, int sx, int sy, int sz, int blobRadius, int hollowRadius, BlockState blockState, boolean isLeaves) {
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
