package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import twilightforest.block.CritterBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.loot.TFLootTables;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.iterators.VoxelBresenhamIterator;

public abstract class HollowTreePiece extends StructurePiece {
	static final int PLACE_FLAG = 0b10011;

	public static final IntProvider DEFAULT_HEIGHT = UniformInt.of(32, 95);
	public static final IntProvider DEFAULT_RADIUS = UniformInt.of(1, 4);

	public static final BlockStateProvider DEFAULT_LOG = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.value());
	public static final BlockStateProvider DEFAULT_WOOD = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.value());
	public static final BlockStateProvider DEFAULT_ROOT = BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.value());
	public static final BlockStateProvider DEFAULT_LEAVES = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.value());
	public static final BlockStateProvider DEFAULT_VINE = BlockStateProvider.simple(Blocks.VINE.defaultBlockState());
	public static final BlockStateProvider DEFAULT_BUG = new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(TFBlocks.FIREFLY.value().defaultBlockState().setValue(CritterBlock.FACING, Direction.NORTH)).add(TFBlocks.CICADA.value().defaultBlockState().setValue(CritterBlock.FACING, Direction.NORTH)));
	public static final BlockStateProvider DEFAULT_DUNGEON_AIR = BlockStateProvider.simple(Blocks.AIR);
	public static final BlockStateProvider DEFAULT_DUNGEON_LOOT_BLOCK = BlockStateProvider.simple(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));

	public static final ResourceKey<LootTable> DEFAULT_DUNGEON_LOOT_TABLE = TFLootTables.TREE_CACHE;
	public static final Holder<EntityType<?>> DEFAULT_DUNGEON_MONSTER = TFEntities.SWARM_SPIDER;

	protected HollowTreePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
		super(type, genDepth, boundingBox);
	}

	public HollowTreePiece(StructurePieceType type, CompoundTag tag) {
		super(type, tag);
	}

	protected void placeProvidedBlock(WorldGenLevel world, BlockStateProvider possibleBlocks, RandomSource random, int sx, int sy, int sz, BoundingBox sbb, BlockPos origin, boolean forcedPlace, boolean leafHack) {
		BlockPos worldPos = this.getWorldPos(sx, sy, sz).immutable();

		if (!sbb.isInside(worldPos) || (!forcedPlace && !FeatureLogic.treesReplaceable(world.getBlockState(worldPos)))) return;

		BlockState state = possibleBlocks.getState(random, worldPos);

		if (state.hasProperty(LeavesBlock.DISTANCE)) {
			int distance = leafHack ? 1 : Mth.clamp(origin.distManhattan(worldPos), 1, 7);
			world.setBlock(worldPos, state.setValue(LeavesBlock.DISTANCE, distance), PLACE_FLAG);
		} else {
			world.setBlock(worldPos, state, PLACE_FLAG);
		}
	}

	// VanillaCopy of StructurePiece.fillColumnDown except with BlockStateProvider & RandomSource instead of an embedded blockstate
	protected void fillColumnDown(WorldGenLevel pLevel, BlockStateProvider possibleBlocks, RandomSource random, int sx, int sy, int sz, BoundingBox pBox) {
		BlockPos.MutableBlockPos worldPos = this.getWorldPos(sx, sy, sz);

		if (!pBox.isInside(worldPos)) return;

		while (this.isReplaceableByStructures(pLevel.getBlockState(worldPos)) && worldPos.getY() > pLevel.getMinY() + 1) {
			pLevel.setBlock(worldPos, possibleBlocks.getState(random, worldPos), PLACE_FLAG);
			worldPos.move(Direction.DOWN);
		}
	}

	// VanillaCopy of StructurePiece.fillColumnDown except with BlockStateProvider & RandomSource instead of an embedded blockstate
	protected void fillVineColumnDown(WorldGenLevel pLevel, BlockStateProvider possibleBlocks, RandomSource random, int sx, int sy, int sz, BoundingBox pBox, Direction direction) {
		BlockPos.MutableBlockPos worldPos = this.getWorldPos(sx, sy, sz);

		if (!pBox.isInside(worldPos)) return;

		while (this.nonFluidAndReplaceableByStructures(pLevel, worldPos) && worldPos.getY() > pLevel.getMinY() + 1) {
			pLevel.setBlock(worldPos, possibleBlocks.getState(random, worldPos).setValue(VineBlock.getPropertyForFace(direction), true), PLACE_FLAG);
			worldPos.move(Direction.DOWN);
		}
	}

	private boolean nonFluidAndReplaceableByStructures(WorldGenLevel pLevel, BlockPos.MutableBlockPos worldPos) {
		BlockState blockState = pLevel.getBlockState(worldPos);
		return blockState.getFluidState().isEmpty() && this.isReplaceableByStructures(blockState);
	}

	/**
	 * Draws a line
	 */
	protected void drawBresehnam(WorldGenLevel level, BoundingBox writeableBounds, BlockPos startPos, BlockPos endPos, BlockStateProvider stateProvider, RandomSource random) {
		for (BlockPos worldPos : new VoxelBresenhamIterator(startPos, endPos))
			if (writeableBounds.isInside(worldPos) && FeatureLogic.treesReplaceable(level.getBlockState(worldPos)))
				level.setBlock(worldPos, stateProvider.getState(random, worldPos), PLACE_FLAG);
	}

	/**
	 * Make a leaf blob
	 */
	protected void drawBlockBlob(WorldGenLevel world, BoundingBox sbb, int sx, int sy, int sz, int blobRadius, RandomSource random, BlockStateProvider stateProvider, boolean forcedPlace, boolean leafHack, boolean imperfect) {
		BlockPos origin = this.getWorldPos(sx, sy, sz).immutable();

		// then trace out a quadrant
		for (byte dx = 0; dx <= blobRadius; dx++) {
			for (byte dy = 0; dy <= blobRadius; dy++) {
				for (byte dz = 0; dz <= blobRadius; dz++) {
					// determine how far we are from the center.
					byte dist;

					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz) {
						dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}

					// if we're inside the blob, fill it
					if (dist <= blobRadius) {
						if (imperfect && dist == blobRadius) {
							// no cubes allowed!
							if (dx == dy && dy == dz) continue;
							// randomly don't generate some blocks on the very edges of the circles that comprise the leaf blob
							if (dx == dy && dz > dx && dx > 0 || dy == dz && dx > dy && dy > 0 || dz == dx && dy > dz && dz > 0) {
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy + dy, sz + dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy + dy, sz - dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy + dy, sz + dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy + dy, sz - dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy - dy, sz + dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy - dy, sz - dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy - dy, sz + dz, sbb, origin, forcedPlace, leafHack);
								if (random.nextInt(2) == 0) this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy - dy, sz - dz, sbb, origin, forcedPlace, leafHack);
								continue;
							}
						}

						// do eight at a time for easiness!
						this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy + dy, sz + dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy + dy, sz - dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy + dy, sz + dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy + dy, sz - dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy - dy, sz + dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy - dy, sz - dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy - dy, sz + dz, sbb, origin, forcedPlace, leafHack);
						this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy - dy, sz - dz, sbb, origin, forcedPlace, leafHack);
					}
				}
			}
		}
	}

	@NotNull
	protected XoroshiroRandomSource getInterChunkDecoRNG(WorldGenLevel level) {
		return new XoroshiroRandomSource(level.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));
	}
}
