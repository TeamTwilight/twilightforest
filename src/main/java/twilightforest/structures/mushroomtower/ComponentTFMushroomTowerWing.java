package twilightforest.structures.mushroomtower;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerRoof;
import twilightforest.structures.lichtower.ComponentTFTowerWing;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFMushroomTowerWing extends ComponentTFTowerWing {

	private static final int RANGE = 200;
	protected static final int FLOOR_HEIGHT = 4;
	protected static final int MAIN_SIZE = 15;

	boolean hasBase = false;
	public boolean isAscender = false;

	public ComponentTFMushroomTowerWing(TemplateManager manager, CompoundNBT nbt) {
		super(TFMushroomTowerPieces.TFMTWin, nbt);
	}

	public ComponentTFMushroomTowerWing(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	protected ComponentTFMushroomTowerWing(IStructurePieceType piece, TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(piece, feature, i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * Save to NBT
	 * TODO: See super
	 */
//	@Override
//	protected void writeStructureToNBT(CompoundNBT tagCompound) {
//		super.writeStructureToNBT(tagCompound);
//
//		tagCompound.putBoolean("hasBase", this.hasBase);
//		tagCompound.putBoolean("isAscender", this.isAscender);
//	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		this.hasBase = tagCompound.getBoolean("hasBase");
		this.isAscender = tagCompound.getBoolean("isAscender");
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// should we build a base?
		this.hasBase = size > 3;

		// if we are an ascender, build the destination tower
		if (this.isAscender) {

			int[] dest = getValidOpening(rand, Rotation.CLOCKWISE_180);
			dest[1] = this.height - 3;
			int childHeight = (rand.nextInt(3) + rand.nextInt(3) + 2) * FLOOR_HEIGHT + 1;
			boolean madeIt = makeMainBridge(list, rand, this.getComponentType() + 1, dest[0], dest[1], dest[2], size + 4, childHeight, Rotation.CLOCKWISE_180);

			if (!madeIt) {
				TwilightForestMod.LOGGER.info("Did not make bridge back to new main");
			} else {
				TwilightForestMod.LOGGER.debug("Made bridge back to new main");
			}
		}

		// limit sprawl to a reasonable amount
		if (this.getComponentType() < 5 && this.size > 6) {
			// make sub towers
			//for (int i = 0; i < 4; i++) {
			for (Rotation i : RotationUtil.ROTATIONS) {

				if (this.size < MAIN_SIZE && i == Rotation.CLOCKWISE_180) {
					continue;
				}

				int[] dest = getValidOpening(rand, i);

				int childHeight = (rand.nextInt(2) + rand.nextInt(2) + 2) * FLOOR_HEIGHT + 1;

				makeBridge(list, rand, this.getComponentType() + 1, dest[0], dest[1], dest[2], size - 4, childHeight, i);
			}
		}

		if (this.isHighest(this.boundingBox, this.size, list) || !this.hasBase) {
			// add a roof?
			makeARoof(parent, list, rand);
		}
	}

	/**
	 * Have we strayed more than range blocks away from the center?
	 */
	private boolean isOutOfRange(StructurePiece parent, int nx, int ny, int nz, int range) {
		final MutableBoundingBox sbb = parent.getBoundingBox();
		final int centerX = sbb.minX + (sbb.maxX - sbb.minX + 1) / 2;
		final int centerZ = sbb.minZ + (sbb.maxZ - sbb.minZ + 1) / 2;
		return Math.abs(nx - centerX) > range
				|| Math.abs(nz - centerZ) > range;
	}

	/**
	 * Make a new wing
	 */
	@Override
	public boolean makeTowerWing(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		// stop if out of range
		if (isOutOfRange((StructurePiece) list.get(0), dx[0], dx[1], dx[2], RANGE)) {
			return false;
		}

		// adjust coordinates to fit an existing tower
		if (wingSize > 3) {
			dx = adjustCoordinates(dx[0], dx[1], dx[2], wingSize, direction, list);
		}

		ComponentTFMushroomTowerWing wing = new ComponentTFMushroomTowerWing(TFMushroomTowerPieces.TFMTWin, getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = StructurePiece.findIntersecting(list, wing.getBoundingBox());
		if (intersect == null || intersect == this || intersect instanceof ComponentTFTowerRoofMushroom) {

			// if we are coming from an ascender bridge, mark the destination component
			if (this instanceof ComponentTFMushroomTowerBridge && this.isAscender) {
				wing.isAscender = true;
			}

			list.add(wing);
			wing.buildComponent((StructurePiece) list.get(0), list, rand);
			addOpening(x, y, z, rotation);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adjust the coordinates for this tower to link up with any others within 3
	 */
	protected int[] adjustCoordinates(int x, int y, int z, int wingSize, Direction direction, List<StructurePiece> list) {

		// go through list.  if there are any same size towers within wingSize, return their xyz instead

		for (Object obj : list) {
			if (obj instanceof ComponentTFTowerWing && !(obj instanceof ComponentTFMushroomTowerBridge)) {
				ComponentTFTowerWing otherWing = (ComponentTFTowerWing) obj;

				if (wingSize == otherWing.size && otherWing.getBoundingBox().intersectsWith(x - 3, z - 3, x + 3, z + 3)) {
					switch (direction) {
						case SOUTH:
							return new int[]{otherWing.getBoundingBox().minX, y, otherWing.getBoundingBox().minZ};
						case WEST:
							return new int[]{otherWing.getBoundingBox().maxX, y, otherWing.getBoundingBox().minZ};
						case NORTH:
							return new int[]{otherWing.getBoundingBox().maxX, y, otherWing.getBoundingBox().maxZ};
						case EAST:
							return new int[]{otherWing.getBoundingBox().minX, y, otherWing.getBoundingBox().maxZ};
						default:
							return new int[]{x, y, z};
					}
				}
			}
		}

		return new int[]{x, y, z};
	}

	/**
	 * Are there (not) any other towers below this bounding box?
	 */
	private boolean isHighest(MutableBoundingBox boundingBox, int size, List<StructurePiece> list) {
		// go through list.  if there are any same size towers within wingSize, return their xyz instead

		MutableBoundingBox boxAbove = new MutableBoundingBox(boundingBox);

		boxAbove.maxY = 256;

		for (Object obj : list) {
			if (this != obj && obj instanceof ComponentTFTowerWing && !(obj instanceof ComponentTFMushroomTowerBridge)) {
				ComponentTFTowerWing otherWing = (ComponentTFTowerWing) obj;

				if (size == otherWing.size && otherWing.getBoundingBox().intersectsWith(boxAbove)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Make a mushroom roof!
	 */
	@Override
	public void makeARoof(StructurePiece parent, List<StructurePiece> list, Random rand) {

		ComponentTFTowerRoof roof = new ComponentTFTowerRoofMushroom(getFeatureType(), this.getComponentType() + 1, this, 1.6F);
		if (StructurePiece.findIntersecting(list, roof.getBoundingBox()) instanceof ComponentTFTowerRoofMushroom) {
			list.add(roof);
			roof.buildComponent(this, list, rand);
		} else {
			roof = new ComponentTFTowerRoofMushroom(getFeatureType(), this.getComponentType() + 1, this, 1.0F);
			if (StructurePiece.findIntersecting(list, roof.getBoundingBox()) instanceof ComponentTFTowerRoofMushroom) {
				list.add(roof);
				roof.buildComponent(this, list, rand);
			} else {
				roof = new ComponentTFTowerRoofMushroom(getFeatureType(), this.getComponentType() + 1, this, 0.6F);
				list.add(roof);
				roof.buildComponent(this, list, rand);
			}
		}
	}

	@Override
	protected boolean makeBridge(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		return this.makeBridge(list, rand, index, x, y, z, wingSize, wingHeight, rotation, false);
	}

	protected boolean makeBridge(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation, boolean ascender) {
		// bridges are size  always
		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 3, direction);

		// adjust height for those stupid little things
		if (wingSize == 3) {
			wingHeight = 4;
		}

		ComponentTFMushroomTowerBridge bridge = new ComponentTFMushroomTowerBridge(TFMushroomTowerPieces.TFMTBri, getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		bridge.isAscender = ascender;
		// check to see if it intersects something already there
		StructurePiece intersect = StructurePiece.findIntersecting(list, bridge.getBoundingBox());
		if (intersect == null || intersect == this) {
			intersect = StructurePiece.findIntersecting(list, bridge.getWingBB());
		} else {
			return false;
		}
		// okay, I think we can actually make one, as long as we're not still intersecting something.
		if (intersect == null || intersect == this) {
			list.add(bridge);
			bridge.buildComponent(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	private boolean makeMainBridge(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 3, direction);

		ComponentTFMushroomTowerMainBridge bridge = new ComponentTFMushroomTowerMainBridge(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);

		list.add(bridge);
		bridge.buildComponent(this, list, rand);
		addOpening(x, y, z, rotation);
		return true;
	}

	/**
	 * Gets a random position in the specified direction that connects to stairs currently in the tower.
	 */
	@Override
	public int[] getValidOpening(Random rand, Rotation direction) {
		// variables!
		int wLength = Math.min(size / 3, 3); // wall length
		int offset = (size - wLength) / 2; // wall thickness

		// for directions 0 or 2, the wall lies along the z axis
		if (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) {
			int rx = direction == Rotation.NONE ? size - 1 : 0;
			int rz = offset + rand.nextInt(wLength);
			int ry = getYByStairs(rz, rand, direction);

			return new int[]{rx, ry, rz};
		}

		// for directions 1 or 3, the wall lies along the x axis
		if (direction == Rotation.CLOCKWISE_90 || direction == Rotation.COUNTERCLOCKWISE_90) {
			int rx = offset + rand.nextInt(wLength);
			int rz = direction == Rotation.CLOCKWISE_90 ? size - 1 : 0;
			int ry = getYByStairs(rx, rand, direction);

			return new int[]{rx, ry, rz};
		}

		return new int[]{0, 0, 0};
	}

	/**
	 * Gets a Y value where the stairs meet the specified X coordinate.
	 * Also works for Z coordinates.
	 */
	@Override
	protected int getYByStairs(int rx, Random rand, Rotation direction) {

		int floors = this.height / FLOOR_HEIGHT;

		return FLOOR_HEIGHT + 1 + (rand.nextInt(floors - 1) * FLOOR_HEIGHT);
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<?> generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn) {
		World worldIn = world.getWorld();
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

//		// clear inside
//		fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		makeTrunk(worldIn, sbb);

		// make floors
		makeFloorsForTower(worldIn, decoRNG, sbb);

		// nullify sky light
//		this.nullifySkyLightForBoundingBox(worldIn);

		// openings
		makeOpenings(worldIn, sbb);

		return true;
	}

	private void makeTrunk(World world, MutableBoundingBox sbb) {
		int diameter = this.size / 2;
		int hollow = (int) (diameter * 0.8);

		int cx = diameter;
		int cz = diameter;

		// build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx);
				int az = Math.abs(dz);
				int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.4));

				// make a trunk!
				if (dist <= diameter) {
					// make floor/ceiling
					setBlockState(world, deco.floorState, dx + cx, 0, dz + cz, sbb);
					setBlockState(world, deco.floorState, dx + cx, height, dz + cz, sbb);

					// make walls
					if (dist > hollow) {
						for (int dy = 0; dy <= height; dy++) {
							setBlockState(world, deco.blockState, dx + cx, dy, dz + cz, sbb);
						}
					} else {
						for (int dy = 1; dy <= height - 1; dy++) {
							setBlockState(world, AIR, dx + cx, dy, dz + cz, sbb);
						}
					}

					// make base
					if (this.hasBase) {
						this.replaceAirAndLiquidDownwards(world, deco.blockState, dx + cx, -1, dz + cz, sbb);
					}
				}
			}
		}
	}

	//TODO: Parameter "decoRNG" is unused. Remove?
	private void makeFloorsForTower(World world, Random decoRNG, MutableBoundingBox sbb) {
		int floors = this.height / FLOOR_HEIGHT;

		int ladderDir = 3;
		int downLadderDir = -1;

		// divide the tower into floors
		for (int i = 0; i < floors; i++) {
			placeFloor(world, i * FLOOR_HEIGHT, sbb);

			downLadderDir = ladderDir;
			ladderDir++;
			ladderDir %= 4;

//			decorateFloor(world, decoRNG, i, (i * floorHeight), (i * floorHeight) + floorHeight, ladderDir, downLadderDir, sbb);
		}
	}

	private void placeFloor(World world, int dy, MutableBoundingBox sbb) {
		int diameter = this.size / 2;
		int hollow = (int) (diameter * 0.8);

		int cx = diameter;
		int cz = diameter;

		// build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx);
				int az = Math.abs(dz);
				int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.4));

				// make a floor!
				if (dist <= hollow) { {
						setBlockState(world, this.isAscender ? Blocks.JUNGLE_PLANKS.getDefaultState() : deco.floorState, dx + cx, dy, dz + cz, sbb);
					}
				}
			}
		}
	}

	/**
	 * Make an opening in this tower for a door.  This now only makes one opening, so you need two
	 */
	@Override
	protected void makeDoorOpening(World world, int dx, int dy, int dz, MutableBoundingBox sbb) {
		super.makeDoorOpening(world, dx, dy, dz, sbb);

		if (getBlockStateFromPos(world, dx, dy + 2, dz, sbb).getBlock() != Blocks.AIR) {
			setBlockState(world, deco.accentState, dx, dy + 2, dz, sbb);
		}
	}

	/**
	 * Called to decorate each floor.  This is responsible for adding a ladder up, the stub of the ladder going down, then picking a theme for each floor and executing it.
	 *
	 * @param floor
	 * @param bottom
	 * @param top
	 * @param ladderUpDir
	 * @param ladderDownDir
	 */
	@Override
	protected void decorateFloor(World world, Random rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, MutableBoundingBox sbb) {
		//decorateWraparoundWallSteps(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
	}
}
