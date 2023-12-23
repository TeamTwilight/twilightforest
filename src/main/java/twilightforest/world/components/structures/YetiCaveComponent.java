package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;

public class YetiCaveComponent extends HollowHillComponent {

	public YetiCaveComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFYeti.get(), nbt);
	}

	public YetiCaveComponent(int i, int x, int y, int z, Holder.Reference<StructureSpeleothemConfig> speleothemConfig) {
		super(TFStructurePieceTypes.TFYeti.get(), i, 2, x, y, z, speleothemConfig);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox writeableBounds, ChunkPos chunkPosIn, BlockPos blockPos) {
		// fill in features

		int maxRadius = 24;

		BlockPos center = this.getLocatorPosition();
		for (BlockPos.MutableBlockPos dest : this.speleothemConfig.latticeIterator(writeableBounds, 0)) {
			int xDist = Math.abs(dest.getX() - center.getX());
			int zDist = Math.abs(dest.getZ() - center.getZ());

			if (xDist <= maxRadius && zDist <= maxRadius) {
				BlockPos ceiling = dest.atY(15 - Math.min(xDist, zDist) / 6);

				if (this.speleothemConfig.shouldDoAStalactite(rand))
                	this.generateOreStalactite(world, ceiling, writeableBounds, true);

				if (this.speleothemConfig.shouldDoAStalagmite(rand)) // FIXME Fix floor position (our config currently allows zero of these)
					this.generateOreStalactite(world, ceiling, writeableBounds, false);
			}
		}

		// spawn alpha yeti
		final BlockState yetiSpawner = TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState();
		this.setBlockStateRotated(world, yetiSpawner, this.radius, 1, this.radius, Rotation.NONE, writeableBounds);
	}
}