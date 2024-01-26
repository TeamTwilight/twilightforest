package twilightforest.world.components.structures.trollcave;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.HugeMushroomUtil;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

public class TrollCaveConnectComponent extends TrollCaveMainComponent {
	protected final boolean[] openingTowards = {false, false, true, false};

	public TrollCaveConnectComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFTCCon.get(), ctx, nbt);
		this.openingTowards[0] = nbt.getBoolean("openingTowards0");
		this.openingTowards[1] = nbt.getBoolean("openingTowards1");
		this.openingTowards[2] = nbt.getBoolean("openingTowards2");
		this.openingTowards[3] = nbt.getBoolean("openingTowards3");
	}

	public TrollCaveConnectComponent(int index, int x, int y, int z, int caveSize, int caveHeight, Direction direction, Holder.Reference<StructureSpeleothemConfig> speleothemConfig) {
		super(TFStructurePieceTypes.TFTCCon.get(), index, x, y, z, speleothemConfig);
		this.size = caveSize;
		this.height = caveHeight;
		this.setOrientation(direction);
		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction, false);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		// too lazy to do this as a loop
		tagCompound.putBoolean("openingTowards0", this.openingTowards[0]);
		tagCompound.putBoolean("openingTowards1", this.openingTowards[1]);
		tagCompound.putBoolean("openingTowards2", this.openingTowards[2]);
		tagCompound.putBoolean("openingTowards3", this.openingTowards[3]);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		// make 4 caves
		if (this.getGenDepth() < 3) {
			for (final Rotation rotation : RotationUtil.ROTATIONS) {
				BlockPos dest = getValidOpening(rand, rotation);

				if (rand.nextBoolean() || !makeGardenCave(list, rand, this.getGenDepth() + 1, dest.getX(), dest.getY(), dest.getZ(), 30, 15, rotation)) {
					makeSmallerCave(list, rand, this.getGenDepth() + 1, dest.getX(), dest.getY(), dest.getZ(), 20, 15, rotation);
				}
			}
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
//		if (this.isBoundingBoxOutsideBiomes(world, sbb, highlands)) {
//			return false;
//		}

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);

		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// wall decorations
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			if (!this.openingTowards[rotation.ordinal()]) {
				decorateWall(world, sbb, decoRNG, rotation);
			}
		}

		this.placeSpeleothems(world, rand, sbb, decoRNG);

		// possible treasure
		decoRNG.setSeed(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));
		if (this.countExits() == 1 && decoRNG.nextInt(3) == 0) {
			// treasure!
			makeTreasureCrate(world, sbb);
		} else if (decoRNG.nextInt(3) == 0) {
			// or a monolith!
			makeMonolith(world, decoRNG, sbb);
		}
	}

	protected void makeMonolith(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		// monolith
		int mid = this.size / 2;
		int height = 7 + rand.nextInt(8);
		Rotation rotation = RotationUtil.ROTATIONS[rand.nextInt(4)];

		this.fillBlocksRotated(world, sbb, mid - 1, 0, mid - 1, mid - 1, height, mid - 1, Blocks.OBSIDIAN.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, mid, 0, mid - 1, mid, height - 2, mid - 1, Blocks.OBSIDIAN.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, mid - 1, 0, mid, mid - 1, height - 2, mid, Blocks.OBSIDIAN.defaultBlockState(), rotation);
		this.fillBlocksRotated(world, sbb, mid, 0, mid, mid, height - 4, mid, Blocks.OBSIDIAN.defaultBlockState(), rotation);
	}

	private int countExits() {
		int count = 0;
		for (boolean openingToward : this.openingTowards) {
			if (openingToward) {
				count++;
			}
		}
		return count;
	}

	private void decorateWall(WorldGenLevel world, BoundingBox sbb, RandomSource decoRNG, Rotation rotation) {
		if (decoRNG.nextBoolean()) {
			//FIXME: AtomicBlom: Don't do this, bring rotation all the way through.
			decorateBracketMushrooms(world, sbb, decoRNG, rotation);
		} else if (decoRNG.nextBoolean()) {
			decorateStoneFormation(world, sbb, decoRNG, rotation);
			decorateStoneFormation(world, sbb, decoRNG, rotation);
		} else {
			//decorateStoneProjection(world, sbb, decoRNG, rotation);
		}
	}

	private void decorateStoneFormation(WorldGenLevel world, BoundingBox sbb, RandomSource decoRNG, Rotation rotation) {
		int z = 5 + decoRNG.nextInt(7);
		int startY = 1 + decoRNG.nextInt(2);

		for (int y = startY; y < this.height; y += 2) {

			int width = 1;
			int depth = 1 + (decoRNG.nextInt(3) == 0 ? 1 : 0);
			makeSingleStoneFormation(world, sbb, decoRNG, rotation, z, y, width, depth);

			// wiggle a little
			z += decoRNG.nextInt(4) - decoRNG.nextInt(4);
			if (z < 5 || z > this.size - 5) {
				z = 5 + decoRNG.nextInt(7);
			}
		}
	}

	private void makeSingleStoneFormation(WorldGenLevel world, BoundingBox sbb, RandomSource decoRNG, Rotation rotation, int z, int y, int width, int depth) {
		if (decoRNG.nextInt(8) == 0) {
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, Blocks.OBSIDIAN.defaultBlockState(), rotation);
		} else if (decoRNG.nextInt(4) == 0) {
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, TFBlocks.TROLLSTEINN.get().defaultBlockState(), rotation);
		} else {
			// normal stone
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, Blocks.STONE.defaultBlockState(), rotation);
		}
		//this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.5F, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, TFBlocks.trollsteinn, 0, Blocks.STONE, 0, rotation);
	}

	private void decorateStoneProjection(WorldGenLevel world, BoundingBox sbb, RandomSource decoRNG, Rotation rotation) {
		int z = 7 + decoRNG.nextInt(3) - decoRNG.nextInt(3);
		int y = 7 + decoRNG.nextInt(3) - decoRNG.nextInt(3);

		this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, y, z, size - 2, y + 3, z + 3, TFBlocks.TROLLSTEINN.get().defaultBlockState(), Blocks.STONE.defaultBlockState(), rotation);
		if (decoRNG.nextBoolean()) {
			// down
			this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, 1, z, size - 6, y - 1, z + 3, TFBlocks.TROLLSTEINN.get().defaultBlockState(), Blocks.STONE.defaultBlockState(), rotation);
		} else {
			// up
			this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, y + 4, z, size - 6, height - 2, z + 3, TFBlocks.TROLLSTEINN.get().defaultBlockState(), Blocks.STONE.defaultBlockState(), rotation);
		}
	}

	/**
	 * Decorate with a patch of bracket fungi
	 */
	private void decorateBracketMushrooms(WorldGenLevel world, BoundingBox sbb, RandomSource decoRNG, Rotation rotation) {
		int z = 5 + decoRNG.nextInt(7);
		int startY = 1 + decoRNG.nextInt(4);

		for (int y = startY; y < this.height; y += 2) {

			int width = 1 + decoRNG.nextInt(2) + decoRNG.nextInt(2);
			int depth = 1 + decoRNG.nextInt(2) + decoRNG.nextInt(2);
			Block mushBlock = ((decoRNG.nextInt(3) == 0) ? TFBlocks.HUGE_MUSHGLOOM.get() : (decoRNG.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK));
			makeSingleBracketMushroom(world, sbb, rotation, z, y, width, depth, mushBlock.defaultBlockState());

			// wiggle a little
			z += decoRNG.nextInt(4) - decoRNG.nextInt(4);
			if (z < 5 || z > this.size - 5) {
				z = 5 + decoRNG.nextInt(7);
			}
		}
	}

	/**
	 * Make one mushroom with the specified parameters
	 */
	private void makeSingleBracketMushroom(WorldGenLevel world, BoundingBox sbb, Rotation rotation, int z, int y, int width, int depth, BlockState mushBlock) {

		this.fillBlocksRotated(world, sbb, size - depth, y, z - (width - 1), size - 2, y, z + (width - 1), HugeMushroomUtil.getState(HugeMushroomUtil.HugeMushroomType.CENTER, mushBlock), rotation);

		this.fillBlocksRotated(world, sbb, size - (depth + 1), y, z - (width - 1), size - (depth + 1), y, z + (width - 1), getMushroomState(mushBlock, HugeMushroomUtil.HugeMushroomType.EAST), rotation);

		final BlockState northMushroom = getMushroomState(mushBlock, HugeMushroomUtil.HugeMushroomType.SOUTH);
		for (int d = 0; d < (depth - 1); d++) {
			this.setBlockStateRotated(world, northMushroom, size - (2 + d), y, z - width, rotation, sbb);
		}
		final BlockState northWestMushroom = getMushroomState(mushBlock, HugeMushroomUtil.HugeMushroomType.SOUTH_EAST);
		this.setBlockStateRotated(world, northWestMushroom, size - (depth + 1), y, z - width, rotation, sbb);

		final BlockState southMushroom = getMushroomState(mushBlock, HugeMushroomUtil.HugeMushroomType.NORTH);
		for (int d = 0; d < (depth - 1); d++) {
			this.setBlockStateRotated(world, southMushroom, size - (2 + d), y, z + width, rotation, sbb);
		}
		final BlockState southWestMushroom = getMushroomState(mushBlock, HugeMushroomUtil.HugeMushroomType.NORTH_EAST);
		this.setBlockStateRotated(world, southWestMushroom, size - (depth + 1), y, z + width, rotation, sbb);
	}

	private BlockState getMushroomState(BlockState mushroomBlockState, HugeMushroomUtil.HugeMushroomType defaultRotation) {
		return HugeMushroomUtil.getState(defaultRotation, mushroomBlockState);
	}

	protected boolean makeGardenCave(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, caveSize, direction);

		TrollCaveMainComponent cave = new TrollCaveGardenComponent(index, dest.getX(), dest.getY(), dest.getZ(), caveSize, caveHeight, direction, this.speleothemConfigHolder);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(cave.getBoundingBox());
		StructurePiece otherGarden = findNearbyGarden(list, cave.getBoundingBox());
		if ((intersect == null || intersect == this) && otherGarden == null) {
			list.addPiece(cave);
			cave.addChildren(this, list, rand);
			//addOpening(x, y, z, rotation);

			this.openingTowards[rotation.ordinal()] = true;

			return true;
		}
		return false;
	}

	@Nullable
	private StructurePiece findNearbyGarden(StructurePieceAccessor list, BoundingBox boundingBox) {
		BoundingBox largeBox = new BoundingBox(boundingBox.minX() - 30, boundingBox.minY() - 30, boundingBox.minZ() - 30, boundingBox.maxX() - 30, boundingBox.maxY() - 30, boundingBox.maxZ() - 30);

		if (list instanceof StructurePiecesBuilder start) {
			for (StructurePiece component : start.pieces) {
				if (component instanceof TrollCaveGardenComponent && component.getBoundingBox().intersects(largeBox)) {
					return component;
				}
			}
		}

		return null;
	}

	@Override
	protected boolean makeSmallerCave(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		if (super.makeSmallerCave(list, rand, index, x, y, z, caveSize, caveHeight, rotation)) {
			this.openingTowards[rotation.ordinal()] = true;
			return true;
		}
		return false;
	}
}
