package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFStructurePieceTypes;


public class StrongholdAtriumComponent extends KnightStrongholdComponent {

	private boolean enterBottom;

	public StrongholdAtriumComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSAt.get(), nbt);
		this.enterBottom = nbt.getBoolean("enterBottom");
	}

	public StrongholdAtriumComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSAt.get(), i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putBoolean("enterBottom", this.enterBottom);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {

		if (y > -15) {
			this.enterBottom = false;
		} else if (y < -21) {
			this.enterBottom = true;
		} else {
			this.enterBottom = (z & 1) == 0;
		}

		if (enterBottom) {
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 18, 14, 18, facing);
		} else {
			// enter on the top
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -8, 0, 18, 14, 18, facing);
		}
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		if (this.enterBottom) {
			this.addDoor(4, 1, 0);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 13, 8, -1);
		} else {
			this.addDoor(13, 8, 0);
			addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 4, 1, -1);
		}

		addNewComponent(parent, list, random, Rotation.NONE, 13, 1, 18);
		addNewComponent(parent, list, random, Rotation.NONE, 4, 8, 18);
	}

	/**
	 * Generate the blocks that go here
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 13, 17, rand, deco.randomBlocks);


		// balcony
		this.generateBox(world, sbb, 1, 6, 1, 16, 7, 16, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 5, 8, 5, 12, 8, 12, deco.fenceState, AIR, false);
		this.generateAirBox(world, sbb, 6, 6, 6, 11, 8, 11);

		// balcony pillars
		placeBalconyPillar(world, sbb, Rotation.NONE);
		placeBalconyPillar(world, sbb, Rotation.CLOCKWISE_90);
		placeBalconyPillar(world, sbb, Rotation.CLOCKWISE_180);
		placeBalconyPillar(world, sbb, Rotation.COUNTERCLOCKWISE_90);

		// corner pillars
		this.generateBox(world, sbb, 1, 1, 1, 1, 12, 2, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 2, 1, 1, 2, 12, 1, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 16, 1, 1, 16, 12, 2, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 15, 1, 1, 15, 12, 1, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 1, 1, 15, 1, 12, 16, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 2, 1, 16, 2, 12, 16, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 16, 1, 15, 16, 12, 16, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 15, 1, 16, 15, 12, 16, false, rand, deco.randomBlocks);

		// grass
		BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
		this.generateMaybeBox(world, sbb, rand, 0.5F, 6, 0, 6, 11, 0, 11, grass, grass, false, false);
		this.generateBox(world, sbb, 7, 0, 7, 10, 0, 10, grass, AIR, false);

		// tree
		this.spawnATree(world, generator, rand.nextInt(5), 8, 1, 8, sbb);

		// statues
		placeCornerStatue(world, 2, 8, 2, 0, sbb);
		placeCornerStatue(world, 2, 1, 15, 1, sbb);
		placeCornerStatue(world, 15, 1, 2, 2, sbb);
		placeCornerStatue(world, 15, 8, 15, 3, sbb);

		// doors
		placeDoors(world, sbb);
	}

	private void spawnATree(WorldGenLevel world, ChunkGenerator generator, int treeNum, int x, int y, int z, BoundingBox sbb) {
		BlockPos pos = getBlockPosWithOffset(x, y, z);

		if (sbb.isInside(pos)) {
			ResourceKey<ConfiguredFeature<?, ?>> treeGen = switch (treeNum) {
				case 1 ->
					// jungle tree
					TreeFeatures.JUNGLE_TREE;
				case 2 ->
					// birch
					TreeFeatures.BIRCH;
				case 3 -> TFConfiguredFeatures.TWILIGHT_OAK_TREE;
				case 4 -> TFConfiguredFeatures.RAINBOW_OAK_TREE;
				default ->
					// oak tree
					TreeFeatures.OAK;
			};
			// grow a tree

			for (int i = 0; i < 100; i++) {
				if (world.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).getValueOrThrow(treeGen).place(world, generator, world.getRandom(), pos)) {
					break;
				}
			}
		}
	}

	private void placeBalconyPillar(WorldGenLevel world, BoundingBox sbb, Rotation rotation) {
		this.fillBlocksRotated(world, sbb, 5, 1, 5, 5, 12, 5, deco.pillarState, rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), false), 5, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), false), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 5, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 6, 5, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(Direction.WEST), true), 5, 12, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(Direction.WEST), true), 6, 12, 5, rotation, sbb);
	}
}
