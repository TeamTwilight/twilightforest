package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.iterators.VoxelBresenhamIterator;

public class HollowTreeRoot extends HollowTreeMedBranch {
	protected HollowTreeRoot(int i, BlockPos src, double length, double angle, double tilt, boolean leafy, BlockStateProvider root, BlockStateProvider wood) {
		this(i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy, root, wood);
	}

	protected HollowTreeRoot(int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy, BlockStateProvider root, BlockStateProvider wood) {
		super(TFStructurePieceTypes.TFHTRo.value(), i, src, dest, branchBoundingBox(src, dest, 0), length, angle, tilt, leafy, wood, root); // Might as well re-use the otherwise unused leaves field for configuring its root block
	}

	public HollowTreeRoot(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTRo.value(), context, tag);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource doNotUse, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		RandomSource decoRNG = this.getInterChunkDecoRNG(level);

		BlockPos rSrc = new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ());
		BlockPos rDest = new BlockPos(this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ());

		this.drawRootLine(level, writeableBounds, rSrc, rDest, decoRNG, this.wood, this.leaves);
		this.drawRootLine(level, writeableBounds, rSrc.below(), rDest.below(), decoRNG, this.wood, this.leaves);
	}

	/**
	 * Draws a line
	 */
	protected void drawRootLine(WorldGenLevel world, BoundingBox sbb, BlockPos rSrc, BlockPos rDest, RandomSource random, BlockStateProvider wood, BlockStateProvider root) {
		for (BlockPos coords : new VoxelBresenhamIterator(rSrc, rDest)) {
			BlockPos.MutableBlockPos worldPos = this.getWorldPos(coords.getX(), coords.getY(), coords.getZ());

			if (!sbb.isInside(worldPos)) continue;

			BlockState block = world.getBlockState(worldPos);

			// three choices here
			if (block.is(BlockTags.LOGS)) {
				// wood, do nothing
			} else {
				boolean flag = false;
				for (Direction direction : Direction.values()) {
					if (world.getBlockState(worldPos.relative(direction)).canBeReplaced()) {
						flag = true;
						break;
					}
				}
				if (flag) {
					// block is exposed, make it into wood
					this.placeProvidedBlock(world, wood, random, coords.getX(), coords.getY(), coords.getZ(), sbb, BlockPos.ZERO, false, false);
				} else {
					// block is covered up, make it into root
					this.placeProvidedBlock(world, root, random, coords.getX(), coords.getY(), coords.getZ(), sbb, BlockPos.ZERO, false, false);
				}
			}
		}
	}
}
