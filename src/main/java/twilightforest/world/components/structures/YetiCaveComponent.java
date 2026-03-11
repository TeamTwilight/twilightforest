package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;

// FIXME Get rid of the HollowHillComponent inheritance
public class YetiCaveComponent extends HollowHillComponent {

	public YetiCaveComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFYeti.get(), nbt);
	}

	public YetiCaveComponent(int i, int x, int y, int z, Holder.Reference<StructureSpeleothemConfig> speleothemConfig) {
		super(TFStructurePieceTypes.TFYeti.get(), i, 2, x, y, z, speleothemConfig);

		// FIXME Get rid of HollowHillComponent so this ugly hack can be sanitized
		this.boundingBox = new BoundingBox(
			this.boundingBox.minX(),
			this.boundingBox.minY() - 1,
			this.boundingBox.minZ(),
			this.boundingBox.maxX(),
			this.boundingBox.maxY(),
			this.boundingBox.maxZ()
		);
	}

	@Override // FIXME Bandaid for adjusting structure box
	protected int getWorldY(int pY) {
		return super.getWorldY(pY + 1);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox writeableBounds, ChunkPos chunkPosIn, BlockPos blockPos) {
		int maxRadius = 24;
		int bottomYRelative = this.getWorldY(4);

		BlockPos center = this.getLocatorPosition();

		drainWater(writeableBounds, world, 6, Blocks.CAVE_AIR.defaultBlockState(), center.getX(), center.getZ(), maxRadius + 10, Blocks.PACKED_ICE.defaultBlockState(), bottomYRelative);

		// fill in features
		for (BlockPos.MutableBlockPos dest : this.speleothemConfig.latticeIterator(writeableBounds, bottomYRelative)) {
			int xDist = Math.abs(dest.getX() - center.getX());
			int zDist = Math.abs(dest.getZ() - center.getZ());
			int minDelta = Math.min(xDist, zDist);

			if (xDist <= maxRadius && zDist <= maxRadius) {
				if (this.speleothemConfig.shouldDoAStalactite(rand))
					this.generateSpeleothem(world, dest.above(15 - minDelta / 6), writeableBounds, true);

				if (this.speleothemConfig.shouldDoAStalagmite(rand))
					this.generateSpeleothem(world, dest.above(-4 + minDelta / 6), writeableBounds, false);
			}
		}

		// spawn alpha yeti
		final BlockState yetiSpawner = TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState();
		this.setBlockStateRotated(world, yetiSpawner, this.radius, 1, this.radius, Rotation.NONE, writeableBounds);
	}

	public static void drainWater(BoundingBox chunkBox, CommonLevelAccessor level, int maxDepth, BlockState airState, int xCenter, int zCenter, double radius, BlockState undergroundBlock, int yStart) {
		int minY = yStart - maxDepth;

		for (int z = chunkBox.minZ(); z <= chunkBox.maxZ(); z++) {
			int dZ = Mth.abs(zCenter - z);
			for (int x = chunkBox.minX(); x <= chunkBox.maxX(); x++) {
				int dX = Mth.abs(xCenter - x);

				if (Math.max(dX, dZ) >= radius) {
					continue;
				}

				int maxY = Math.min(yStart, level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1);

				boolean crossedFloor = false;

				for (int y = maxY; y >= minY; y--) {
					BlockPos posChecked = new BlockPos(x, y, z);

					BlockState stateAt = level.getBlockState(posChecked);

					if (stateAt.getFluidState().is(FluidTags.WATER)) {
						level.setBlock(posChecked, airState, Block.UPDATE_ALL);
					} else {
						crossedFloor = true;
					}

					if (crossedFloor) {
						if (stateAt.is(Blocks.DIRT) || stateAt.is(Blocks.SNOW_BLOCK)) {
							level.setBlock(posChecked, undergroundBlock, Block.UPDATE_ALL);
						}
					}
				}
			}
		}
	}
}