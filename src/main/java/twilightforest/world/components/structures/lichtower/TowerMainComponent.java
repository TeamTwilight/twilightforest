package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.phys.AABB;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;

public class TowerMainComponent extends TowerWingComponent {

	public TowerMainComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTMai.get(), nbt);
	}

	public TowerMainComponent(RandomSource rand, int index, int x, int y, int z) {
		// some of these are subject to change if the ground level is > 30.
		super(TFStructurePieceTypes.TFLTMai.get(), index, x, y + 1, z, 15, 55 + rand.nextInt(32), Direction.SOUTH);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		// add a roof?
		makeARoof(parent, list, rand);

		// sub towers
		for (final Rotation rotation : RotationUtil.ROTATIONS) {
			int[] dest = getValidOpening(rand, rotation);

			// adjust height if we're too low at this point
			if (dest[1] < height / 2) {
				dest[1] += 20;
			}

			int childHeight = Math.min(21 + rand.nextInt(10), this.height - dest[1] - 3);

			if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 9, childHeight, rotation)) {
				makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 7, childHeight, rotation);
			}
		}

		// try one more time for large towers
		for (final Rotation rotation : RotationUtil.ROTATIONS) {
			int[] dest = getValidOpening(rand, rotation);

			// adjust height if we're too low at this point
			if (dest[1] < height / 2) {
				dest[1] += 10;
			}

			int childHeight = Math.min(21 + rand.nextInt(10), this.height - dest[1] - 3);

			if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 9, childHeight, rotation)) {
				makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 7, childHeight, rotation);
			}
		}

		// another set, if possible
		for (final Rotation rotation : RotationUtil.ROTATIONS) {
			int[] dest = getValidOpening(rand, rotation);

			int childHeight = Math.min(7 + rand.nextInt(6), this.height - dest[1] - 3);

			if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 5, childHeight, rotation)) {
				makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 3, childHeight, rotation);
			}
		}

		// outbuildings
		for (final Rotation rotation : RotationUtil.ROTATIONS) {
			int[] dest = getOutbuildingOpening(rand, rotation);

			int childHeight = 11 + rand.nextInt(10);
			int childSize = 7 + (rand.nextInt(2) * 2);

			makeTowerOutbuilding(list, rand, 1, dest[0], dest[1], dest[2], childSize, childHeight, rotation);
		}

		// TINY TOWERS!
		for (int i = 0; i < 4; i++) {
			for (final Rotation towerRotation : RotationUtil.ROTATIONS) {
				int[] dest = getValidOpening(rand, towerRotation);
				int childHeight = 6 + rand.nextInt(5);
				if (rand.nextInt(3) == 0 || !makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 5, childHeight, towerRotation)) {
					makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], 3, childHeight, towerRotation);
				}
			}
		}
	}

	/**
	 * Gets a random position in the specified direction that we can make an outbuilding at
	 */
	public int[] getOutbuildingOpening(RandomSource rand, Rotation rotation) {

		int rx = 0;
		int ry = 1;
		int rz = 0;

		switch (rotation) {
			case NONE -> {
				// for directions 0 or 2, the wall lies along the z axis
				rx = size - 1;
				rz = 6 + rand.nextInt(8);
			}
			case CLOCKWISE_90 -> {
				// for directions 1 or 3, the wall lies along the x axis
				rx = 1 + rand.nextInt(11);
				rz = size - 1;
			}
			case CLOCKWISE_180 -> {
				rx = 0;
				rz = 1 + rand.nextInt(8);
			}
			case COUNTERCLOCKWISE_90 -> {
				rx = 3 + rand.nextInt(11);
				rz = 0;
			}
		}

		return new int[]{rx, ry, rz};
	}

	public boolean makeTowerOutbuilding(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);
		TowerOutbuildingComponent outbuilding = new TowerOutbuildingComponent(index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(outbuilding.getBoundingBox());
		if (intersect == null) {
			list.addPiece(outbuilding);
			outbuilding.addChildren(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		generateBox(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, false, rand, TFStructureComponentOld.getStrongholdStones());

		// clear inside
		generateAirBox(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);
		final BlockState defaultState = Blocks.COBBLESTONE.defaultBlockState();
		// stone to ground
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				this.fillColumnDown(world, defaultState, x, -1, z, sbb);
			}
		}

		// nullify sky light
//		nullifySkyLightForBoundingBox(world);

		// fix highestOpening parameter so we don't get a ginormous lich room
		if ((height - highestOpening) > 15) {
			highestOpening = height - 15;
		}

		// stairs!
		makeStairs(world, rand, sbb);

		// throw a bunch of opening markers in there
		//makeOpeningMarkers(world, rand, 100, sbb);

		// openings
		makeOpenings(world, sbb);

		// decorate?
		decorateStairFloor(world, generator, rand, sbb);

		// stairway crossings
		makeStairwayCrossings(world, rand, sbb);

		// LICH TIME
		makeLichRoom(world, rand, sbb);

		// extra paintings in main tower
		makeTowerPaintings(world, rand, sbb);
	}

	/**
	 * Make 1-2 platforms joining the stairways
	 */
	protected void makeStairwayCrossings(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		int flights = (this.highestOpening / 5) - 2;

		for (int i = 2 + rand.nextInt(2); i < flights; i += 1 + rand.nextInt(5)) {
			makeStairCrossing(world, rand, i, sbb);
		}
	}

	protected void makeStairCrossing(WorldGenLevel world, RandomSource rand, int flight, BoundingBox sbb) {
		Direction temp = this.getOrientation();
		if (flight % 2 == 0) {
			this.setOrientation(getStructureRelativeRotation(Rotation.CLOCKWISE_90));
		}

		// place platform
		int floorLevel = flight * 5;
		BlockState crossingfloor = rand.nextBoolean() ? Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE) : Blocks.BIRCH_PLANKS.defaultBlockState();
		for (int dx = 6; dx <= 8; dx++) {
			for (int dz = 4; dz <= 10; dz++) {
				placeBlock(world, crossingfloor, dx, floorLevel, dz, sbb);
			}
		}

		// put fences
		floorLevel++;
		int dx = 6;
		for (int dz = 3; dz <= 11; dz++) {
			placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), dx, floorLevel, dz, sbb);
		}
		dx++;
		for (int dz = 3; dz <= 11; dz++) {
			placeBlock(world, AIR, dx, floorLevel, dz, sbb);
		}
		dx++;
		for (int dz = 3; dz <= 11; dz++) {
			placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), dx, floorLevel, dz, sbb);
		}

		// we need 2 extra blocks and 2 extra fences to look good
		placeBlock(world, crossingfloor, 6, floorLevel - 1, 11, sbb);
		placeBlock(world, crossingfloor, 8, floorLevel - 1, 3, sbb);

		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), 5, floorLevel, 11, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), 9, floorLevel, 3, sbb);

		// place spawner in the middle
		EntityType<?> mobID = switch (rand.nextInt(4)) {
			case 2 -> EntityType.ZOMBIE;
			case 3 -> TFEntities.SWARM_SPIDER.get();
			default -> EntityType.SKELETON;
		};
		setSpawner(world, 7, floorLevel + 2, 7, sbb, mobID);

		// make a fence arch support for the spawner
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), 6, floorLevel + 1, 7, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), 8, floorLevel + 1, 7, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), 6, floorLevel + 2, 7, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), 8, floorLevel + 2, 7, sbb);

		this.setOrientation(temp);
	}

	/**
	 * Make a neat little room for the lich to fight in
	 */
	protected void makeLichRoom(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		// figure out where the stairs end
		int floorLevel = 2 + (this.highestOpening / 5) * 5;
		// we need a floor
		final Rotation i = (this.highestOpening / 5) % 2 == 0 ? Rotation.NONE : Rotation.CLOCKWISE_90;
		makeLichFloor(world, floorLevel, i, sbb);

		// now a chandelier
		decorateLichChandelier(world, floorLevel, sbb);

		// make paintings
		decoratePaintings(world, rand, floorLevel, sbb);

		// and wall torches
		decorateTorches(world, rand, floorLevel, sbb);

		// seems like we should have a spawner
		placeBlock(world, TFBlocks.LICH_BOSS_SPAWNER.get().defaultBlockState(), size / 2, floorLevel + 2, size / 2, sbb);
	}

	protected void makeTowerPaintings(WorldGenLevel world, RandomSource rand, BoundingBox sbb) {
		int howMany = 10;

		// do wall 0.
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.WEST, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.WEST, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.WEST, 0, sbb);

		// do wall 1.
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.EAST, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.EAST, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.EAST, 0, sbb);
		// do wall 2.
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.NORTH, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.NORTH, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.NORTH, 0, sbb);
		// do wall 3.
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.SOUTH, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.SOUTH, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, Direction.SOUTH, 0, sbb);
	}

	/**
	 * Make the floor for the liches room
	 */
	protected void makeLichFloor(WorldGenLevel world, int floorLevel, Rotation rotation, BoundingBox sbb) {
		Direction temp = this.getOrientation();
		this.setOrientation(getStructureRelativeRotation(rotation));

		BlockState birchSlab = Blocks.BIRCH_SLAB.defaultBlockState()
			.setValue(SlabBlock.TYPE, SlabType.TOP);
		BlockState birchPlank = Blocks.BIRCH_PLANKS.defaultBlockState();

		// place a platform there
		for (int fx = 1; fx < 14; fx++) {
			for (int fz = 1; fz < 14; fz++) {
				if ((fx == 1 || fx == 2) && (fz >= 6 && fz <= 12)) {
					// blank, leave room for stairs
					if (fz == 6) {
						// upside down plank slabs
						placeBlock(world, birchSlab, fx, floorLevel, fz, sbb);
					}
				} else if ((fx == 12 || fx == 13) && (fz >= 3 && fz <= 8)) {
					// blank, leave room for stairs
					if (fz == 8) {
						// upside down plank slabs
						placeBlock(world, birchSlab, fx, floorLevel, fz, sbb);
					}
				} else if ((fx >= 4 && fx <= 10) && (fz >= 4 && fz <= 10)) {
					// glass floor in center, aside from 2 corners
					if ((fx == 4 && fz == 4) || (fx == 10 && fz == 10)) {
						placeBlock(world, birchPlank, fx, floorLevel, fz, sbb);
					} else {
						placeBlock(world, Blocks.GLASS.defaultBlockState(), fx, floorLevel, fz, sbb);
					}
				} else if ((fx == 2 || fx == 3) && (fz == 2 || fz == 3)) {
					// glass blocks in the corners
					placeBlock(world, Blocks.GLASS.defaultBlockState(), fx, floorLevel, fz, sbb);
				} else if ((fx == 11 || fx == 12) && (fz == 11 || fz == 12)) {
					// glass blocks in the corners
					placeBlock(world, Blocks.GLASS.defaultBlockState(), fx, floorLevel, fz, sbb);
				} else {
					placeBlock(world, birchPlank, fx, floorLevel, fz, sbb);
				}
			}
		}

		// eliminate the railings
		placeBlock(world, AIR, 3, floorLevel + 1, 11, sbb);
		placeBlock(world, AIR, 3, floorLevel + 1, 10, sbb);
		placeBlock(world, AIR, 3, floorLevel + 2, 11, sbb);

		placeBlock(world, AIR, 11, floorLevel + 1, 3, sbb);
		placeBlock(world, AIR, 11, floorLevel + 1, 4, sbb);
		placeBlock(world, AIR, 11, floorLevel + 2, 3, sbb);

		this.setOrientation(temp);
	}

	/**
	 * Make a fancy chandelier for the lich's room
	 */
	protected void decorateLichChandelier(WorldGenLevel world, int floorLevel, BoundingBox sbb) {
		int cx = size / 2;
		int cy = floorLevel + 4;
		int cz = size / 2;

		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx + 1, cy, cz, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx + 2, cy, cz, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx + 1, cy, cz + 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, cy, cz + 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, cy, cz + 2, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx - 1, cy, cz + 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx - 1, cy, cz, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx - 2, cy, cz, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx - 1, cy, cz - 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, cy, cz - 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, cy, cz - 2, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx + 1, cy, cz - 1, sbb);

		cy++;
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx + 1, cy, cz, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx + 2, cy, cz, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx + 1, cy, cz + 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, cy, cz + 1, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx, cy, cz + 2, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx - 1, cy, cz + 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx - 1, cy, cz, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx - 2, cy, cz, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx - 1, cy, cz - 1, sbb);
		placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, cy, cz - 1, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx, cy, cz - 2, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx + 1, cy, cz - 1, sbb);

		cy++;
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx + 1, cy, cz, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx, cy, cz + 1, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx - 1, cy, cz, sbb);
		placeBlock(world, Blocks.TORCH.defaultBlockState(), cx, cy, cz - 1, sbb);

		for (int y = floorLevel + 5; y < height - 1; y++) {
			placeBlock(world, Blocks.OAK_FENCE.defaultBlockState(), cx, y, cz, sbb);
		}
	}

	/**
	 * Cover the walls in the lich's room with paintings.  How is this going to work, chunk by chunk?
	 */
	protected void decoratePaintings(WorldGenLevel world, RandomSource rand, int floorLevel, BoundingBox sbb) {
		// I reduced this number since it turns out paintings already generate in the boss room before this is called
		int howMany = 25;

		for (final Direction horizontal : Direction.Plane.HORIZONTAL) {
			// do wall 0.
			generatePaintingsOnWall(world, rand, howMany, floorLevel, horizontal, 48, sbb);
			generatePaintingsOnWall(world, rand, howMany, floorLevel, horizontal, 32, sbb);
			generatePaintingsOnWall(world, rand, howMany, floorLevel, horizontal, 0, sbb);
		}
	}

	/**
	 * Put torches on each wall
	 */
	protected void decorateTorches(WorldGenLevel world, RandomSource rand, int floorLevel, BoundingBox sbb) {
		generateTorchesOnWall(world, rand, floorLevel, Direction.SOUTH, sbb);
		generateTorchesOnWall(world, rand, floorLevel, Direction.EAST, sbb);
		generateTorchesOnWall(world, rand, floorLevel, Direction.NORTH, sbb);
		generateTorchesOnWall(world, rand, floorLevel, Direction.WEST, sbb);
	}

	/**
	 * Place up to 5 torches (with fence holders) on the wall, checking that they don't overlap any paintings or other torches
	 */
	protected void generateTorchesOnWall(WorldGenLevel world, RandomSource rand,
										 int floorLevel, Direction direction, BoundingBox sbb) {
		for (int i = 0; i < 5; i++) {
			// get some random coordinates on the wall in the chunk
			BlockPos wCoords = getRandomWallSpot(rand, floorLevel, direction, sbb);

			if (wCoords == null)
				continue;

			// offset to see where the fence should be
			BlockPos.MutableBlockPos tCoords = new BlockPos.MutableBlockPos(wCoords.getX(), wCoords.getY(), wCoords.getZ());

			// is there a painting or another torch there?
			BlockState blockState = world.getBlockState(tCoords);
			BlockState aboveBlockState = world.getBlockState(tCoords.above());
			if (blockState.isAir() && aboveBlockState.isAir() && EntityUtil.getEntitiesInAABB(world, new AABB(tCoords)).size() == 0) {
				// if not, place a torch
				world.setBlock(tCoords, Blocks.OAK_FENCE.defaultBlockState().setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()), true), Block.UPDATE_CLIENTS);
				world.setBlock(tCoords.above(), Blocks.TORCH.defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.BEARD_BOX;
	}
}
