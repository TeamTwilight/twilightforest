package twilightforest.structures.lichtower;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.StructureTFHelper;
import twilightforest.util.RotationUtil;
import twilightforest.util.TFEntityNames;
import twilightforest.util.VanillaEntityNames;

import javax.annotation.Nullable;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.block.BlockStoneSlab.EnumType.SMOOTHBRICK;
import static twilightforest.block.BlockTFCastleMagic.COLOR;


public class ComponentTFTowerWing extends StructureTFComponentOld {

	public ComponentTFTowerWing() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int size;
	protected int height;
	protected Class<? extends ComponentTFTowerRoof> roofType;

	protected ArrayList<BlockPos> openings = new ArrayList<BlockPos>();
	protected int highestOpening;
	protected boolean[] openingTowards = new boolean[]{false, false, true, false};

	protected ComponentTFTowerWing(TFFeature feature, int i) {
		super(feature, i);
		this.highestOpening = 0;
	}

	protected ComponentTFTowerWing(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i);

		this.size = pSize;
		this.height = pHeight;
		this.setCoordBaseMode(direction);

		this.highestOpening = 0;

		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction);
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("towerSize", this.size);
		tagCompound.setInteger("towerHeight", this.height);

		tagCompound.setIntArray("doorInts", this.getDoorsAsIntArray());

		tagCompound.setInteger("highestOpening", this.highestOpening);
		tagCompound.setBoolean("openingTowards0", this.openingTowards[0]);
		tagCompound.setBoolean("openingTowards1", this.openingTowards[1]);
		tagCompound.setBoolean("openingTowards2", this.openingTowards[2]);
		tagCompound.setBoolean("openingTowards3", this.openingTowards[3]);

	}

	/**
	 * Turn the openings array into an array of ints.
	 */
	private int[] getDoorsAsIntArray() {
		IntBuffer ibuffer = IntBuffer.allocate(this.openings.size() * 3);

		for (BlockPos door : openings) {
			ibuffer.put(door.getX());
			ibuffer.put(door.getY());
			ibuffer.put(door.getZ());
		}

		return ibuffer.array();
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.size = tagCompound.getInteger("towerSize");
		this.height = tagCompound.getInteger("towerHeight");

		this.readOpeningsFromArray(tagCompound.getIntArray("doorInts"));

		this.highestOpening = tagCompound.getInteger("highestOpening");
		// too lazy to do this as a loop
		this.openingTowards[0] = tagCompound.getBoolean("openingTowards0");
		this.openingTowards[1] = tagCompound.getBoolean("openingTowards1");
		this.openingTowards[2] = tagCompound.getBoolean("openingTowards2");
		this.openingTowards[3] = tagCompound.getBoolean("openingTowards3");
	}

	/**
	 * Read in openings from int array
	 */
	private void readOpeningsFromArray(int[] intArray) {
		for (int i = 0; i < intArray.length; i += 3) {
			BlockPos door = new BlockPos(intArray[i], intArray[i + 1], intArray[i + 2]);

			this.openings.add(door);
		}
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// add a roof?
		makeARoof(parent, list, rand);

		// add a beard
		makeABeard(parent, list, rand);


		if (size > 4) {
			// sub towers
			for (final Rotation towerRotation : RotationUtil.ROTATIONS) {
				if (towerRotation == Rotation.CLOCKWISE_180) continue;
				int[] dest = getValidOpening(rand, towerRotation);
				if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], size - 2, height - 4, towerRotation) && this.size > 8) {
					if (!makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], size - 4, height - 6, towerRotation)) {
						makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], size - 6, height - 12, towerRotation);
					}
				}
			}
		}
	}

	public boolean makeTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// kill too-small towers
		if (wingHeight < 6) {
			return false;
		}

		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		if (rand.nextInt(6) == 0) {
			return makeBridge(list, rand, index, x, y, z, wingSize, wingHeight, rotation);
			// or I don't know if we just want to continue instead if the bridge fails. 
			// I think there are very few circumstances where we can make a wing and not a bridge
		}

		ComponentTFTowerWing wing = new ComponentTFTowerWing(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, wing.boundingBox);
		if (intersect == null || intersect == this) {
			list.add(wing);
			wing.buildComponent(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			if (rand.nextInt(3) > 0) {
				return makeBridge(list, rand, index, x, y, z, wingSize, wingHeight, rotation);
			} else {
				// I guess we're done for this branch
				return false;
			}
		}
	}


	protected boolean makeBridge(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// bridges are size 3 always
		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, 3, direction);
		// adjust height for those stupid little things
		if (wingSize == 3 && wingHeight > 10) {
			wingHeight = 6 + rand.nextInt(5);
		}
		ComponentTFTowerBridge bridge = new ComponentTFTowerBridge(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, bridge.boundingBox);
		if (intersect == null || intersect == this) {
			intersect = StructureComponent.findIntersecting(list, bridge.getWingBB());
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

	/**
	 * Add an opening to the outside (or another tower) in the specified direction.
	 */
	public void addOpening(int dx, int dy, int dz, Rotation direction) {
		openingTowards[direction.ordinal()] = true;
		if (dy > highestOpening) {
			highestOpening = dy;
		}
		openings.add(new BlockPos(dx, dy, dz));
	}

	/**
	 * Add an opening to the outside (or another tower) in the specified facing.
	 */
	public void addOpening(int dx, int dy, int dz, EnumFacing facing) {
		this.addOpening(dx, dy, dz, RotationUtil.getRelativeRotation(this.getCoordBaseMode(), facing));
	}

	/**
	 * Add a beard to this structure.  There is only one type of beard.
	 */
	public void makeABeard(StructureComponent parent, List<StructureComponent> list, Random rand) {

		boolean attached = parent.getBoundingBox().minY < this.boundingBox.minY;

		int index = this.getComponentType();
		ComponentTFTowerBeard beard;
		if (attached) {
			beard = new ComponentTFTowerBeardAttached(getFeatureType(), index + 1, this);
		} else {
			beard = new ComponentTFTowerBeard(getFeatureType(), index + 1, this);
		}
		list.add(beard);
		beard.buildComponent(this, list, rand);
	}


	/**
	 * Attach a roof to this tower.
	 * <p>
	 * This function keeps trying roofs starting with the largest and fanciest, and then keeps trying smaller and plainer ones
	 */
	public void makeARoof(StructureComponent parent, List<StructureComponent> list, Random rand) {

		// we are attached if our parent is taller than we are
		boolean attached = parent.getBoundingBox().maxY > this.boundingBox.maxY;

		if (attached) {
			makeAttachedRoof(list, rand);
		} else {
			makeFreestandingRoof(list, rand);
		}

	}


	protected void makeAttachedRoof(List<StructureComponent> list, Random rand) {
		int index = this.getComponentType();
		ComponentTFTowerRoof roof;

		// this is our preferred roof type:
		if (roofType == null && rand.nextInt(32) != 0) {
			tryToFitRoof(list, rand, new ComponentTFTowerRoofGableForwards(getFeatureType(), index + 1, this));
		}

		// this is for roofs that don't fit.
		if (roofType == null && rand.nextInt(8) != 0) {
			tryToFitRoof(list, rand, new ComponentTFTowerRoofSlabForwards(getFeatureType(), index + 1, this));
		}

		// finally, if we're cramped for space, try this
		if (roofType == null && rand.nextInt(32) != 0) {
			// fall through to this next roof
			roof = new ComponentTFTowerRoofAttachedSlab(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}

		// last resort
		if (roofType == null) {
			// fall through to this next roof
			roof = new ComponentTFTowerRoofFence(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}
	}


	/**
	 * Check to see if this roof fits.  If it does:
	 * Add the specified roof to this tower and set the roofType variable.
	 */
	protected void tryToFitRoof(List<StructureComponent> list, Random rand, ComponentTFTowerRoof roof) {
		if (roof.fits(this, list, rand)) {
			list.add(roof);
			roof.buildComponent(this, list, rand);
			roofType = roof.getClass();
		}
	}

	protected void makeFreestandingRoof(List<StructureComponent> list, Random rand) {
		int index = this.getComponentType();
		ComponentTFTowerRoof roof;

		// most roofs that fit fancy roofs will be this
		if (roofType == null && rand.nextInt(8) != 0) {
			roof = new ComponentTFTowerRoofPointyOverhang(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}

		// don't pass by this one if it fits
		if (roofType == null) {
			roof = new ComponentTFTowerRoofStairsOverhang(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}

		// don't pass by this one if it fits
		if (roofType == null) {
			roof = new ComponentTFTowerRoofStairs(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}

		if (roofType == null && rand.nextInt(53) != 0) {
			// fall through to this next roof
			roof = new ComponentTFTowerRoofSlab(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}

		if (roofType == null) {
			// fall through to this next roof
			roof = new ComponentTFTowerRoofFence(getFeatureType(), index + 1, this);
			tryToFitRoof(list, rand, roof);
		}
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		// make walls
		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, false, rand, StructureTFComponentOld.getStrongholdStones());

		// clear inside
		fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		// sky light
		nullifySkyLightForBoundingBox(world);

		// marker blocks
//        setBlockState(world, Blocks.WOOL, this.coordBaseMode, size / 2, 2, size / 2, sbb);
//        setBlockState(world, Blocks.GOLD_BLOCK, 0, 0, 0, 0, sbb);

		// stairs!
		if (highestOpening > 1) {
			makeStairs(world, rand, sbb);
		}

		// decorate?
		decorateThisTower(world, rand, sbb);


		// windows
		makeWindows(world, rand, sbb, size < 4);

		// throw a bunch of opening markers in there
//        makeOpeningMarkers(world, rand, 100, sbb);

		// openings
		makeOpenings(world, sbb);

//        // relight
//     	for (int lx = -1; lx < 3; lx++) {
//         	for (int lz = -1; lz < 3; lz++) {
//         		world.updateLightByType(EnumSkyBlock.Sky, sbb.minX + (lx * 16), sbb.maxY, sbb.minZ + (lx * 16));
//         	}
//     	}


		return true;
	}


	/**
	 * Puts some colorful markers by possible openings in this tower.  Debug only.
	 *
	 * @param numMarkers How many markers to make
	 */
	protected void makeOpeningMarkers(World world, Random rand, int numMarkers, StructureBoundingBox sbb) {
		if (size > 4) {
			final IBlockState woolWhite = Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
			final IBlockState woolOrange = Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
			final IBlockState woolMagenta = Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.MAGENTA);
			final IBlockState woolLightBlue = Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE);

			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.NONE);
				setBlockState(world, woolWhite, spot[0], spot[1], spot[2], sbb);
			}
			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.CLOCKWISE_90);
				setBlockState(world, woolOrange, spot[0], spot[1], spot[2], sbb);
			}
			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.CLOCKWISE_180);
				setBlockState(world, woolMagenta, spot[0], spot[1], spot[2], sbb);
			}
			for (int i = 0; i < numMarkers; i++) {
				int[] spot = getValidOpening(rand, Rotation.COUNTERCLOCKWISE_90);
				setBlockState(world, woolLightBlue, spot[0], spot[1], spot[2], sbb);
			}
		}
	}


	/**
	 * Add some appropriate decorations to this tower
	 */
	protected void decorateThisTower(World world, Random rand, StructureBoundingBox sbb) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) * (this.boundingBox.minZ * 756839));

		if (size > 3) {
			// only decorate towers with more than one available square inside.
			if (isDeadEnd()) {
				decorateDeadEnd(world, decoRNG, sbb);
			} else {
				// for now we'll just assume that any tower with more than one exit is a stair tower
				decorateStairTower(world, decoRNG, sbb);
			}
		}
	}


	/**
	 * Decorates a dead end tower.  These towers have no stairs, and will be the focus of our interior design.
	 */
	protected void decorateDeadEnd(World world, Random rand, StructureBoundingBox sbb) {
		final IBlockState birchPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		int floors = (this.height - 1) / 5;

		// divide the tower into floors
		int floorHeight = this.height / floors;
		for (int i = 1; i < floors; i++) {
			// put down a floor
			for (int x = 1; x < size - 1; x++) {
				for (int z = 1; z < size - 1; z++) {
					setBlockState(world, birchPlanks, x, (i * floorHeight), z, sbb);
				}
			}
		}


		if (floors > 1) {
			Rotation ladderDir = Rotation.COUNTERCLOCKWISE_90;
			Rotation downLadderDir;

			// decorate bottom floor
			decorateFloor(world, rand, 0, 1, floorHeight, ladderDir, null, sbb);

			// decorate middle floors
			for (int i = 1; i < floors - 1; i++) {
				int bottom = 1 + floorHeight * i;
				int top = floorHeight * (i + 1);

				downLadderDir = ladderDir;
				ladderDir = ladderDir.add(Rotation.CLOCKWISE_90);

				decorateFloor(world, rand, i, bottom, top, ladderDir, downLadderDir, sbb);
			}

			// decorate top floor
			decorateFloor(world, rand, floors, 1 + floorHeight * (floors - 1), height - 1, null, ladderDir, sbb);
		} else {
			// just one floor, decorate that, no ladders
			decorateFloor(world, rand, 0, 1, height - 1, null, null, sbb);
		}
	}

	/**
	 * Called to decorate each floor.  This is responsible for adding a ladder up, the stub of the ladder going down, then picking a theme for each floor and executing it.
	 */
	protected void decorateFloor(World world, Random rand, int floor, int bottom, int top, @Nullable Rotation ladderUpDir, @Nullable Rotation ladderDownDir, StructureBoundingBox sbb) {

		final IBlockState ladder = Blocks.LADDER.getDefaultState();
		if (ladderUpDir != null) {
			// add ladder going up
			final IBlockState ladderUp = ladder.withProperty(BlockLadder.FACING, ladderUpDir.rotate(EnumFacing.EAST));

			int dx = getLadderX(ladderUpDir);
			int dz = getLadderZ(ladderUpDir);
			for (int dy = bottom; dy < top; dy++) {
				setBlockState(world, ladderUp, dx, dy, dz, sbb);
			}
		}

		if (ladderDownDir != null) {
			// add ladder going down
			final IBlockState ladderDown = ladder.withProperty(BlockLadder.FACING, ladderDownDir.rotate(EnumFacing.EAST));
			int dx = getLadderX(ladderDownDir);
			int dz = getLadderZ(ladderDownDir);
			for (int dy = bottom - 1; dy < bottom + 2; dy++) {
				setBlockState(world, ladderDown, dx, dy, dz, sbb);
			}
		}

		// some of these go only on the bottom floor (ladderDownDir == null) and some go only on upper floors
		if (rand.nextInt(7) == 0 && ladderDownDir == null) {
			decorateWell(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(7) == 0 && ladderDownDir == null) {
			decorateSkeletonRoom(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(6) == 0 && ladderDownDir == null) {
			decorateZombieRoom(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(5) == 0 && ladderDownDir == null) {
			decorateCactusRoom(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(4) == 0 && ladderDownDir != null) {
			decorateTreasureChest(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(5) == 0) {
			decorateSpiderWebs(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(12) == 0 && ladderDownDir != null) {
			// these are annoying
			decorateSolidRock(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else if (rand.nextInt(3) == 0) {
			decorateFullLibrary(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		} else {
			decorateLibrary(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
//			if (ladderDownDir == -1) {
//				// trap it!
//				decorateTrap(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
//			}
		}
	}


	/**
	 * Decorate this floor with a scenic well.
	 */
	protected void decorateWell(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		int cx = size / 2;
		int cz = cx;
		int cy = bottom;

		IBlockState waterOrLava = rand.nextInt(4) == 0 ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState();

		if (size > 5) {
			// actual well structure
			final IBlockState stoneBricks = Blocks.STONEBRICK.getDefaultState();
			final IBlockState stoneSlabs = StructureTFHelper.stoneSlab
					.withProperty(BlockStoneSlab.VARIANT, SMOOTHBRICK);

			setBlockState(world, stoneBricks, cx - 1, cy + 0, cz - 1, sbb);
			setBlockState(world, stoneSlabs, cx - 1, cy + 1, cz - 1, sbb);
			setBlockState(world, stoneBricks, cx + 0, cy + 0, cz - 1, sbb);
			setBlockState(world, stoneBricks, cx + 1, cy + 0, cz - 1, sbb);
			setBlockState(world, stoneSlabs, cx + 1, cy + 1, cz - 1, sbb);
			setBlockState(world, stoneBricks, cx - 1, cy + 0, cz + 0, sbb);
			setBlockState(world, waterOrLava, cx + 0, cy + 0, cz + 0, sbb);
			setBlockState(world, stoneBricks, cx + 1, cy + 0, cz + 0, sbb);
			setBlockState(world, stoneBricks, cx - 1, cy + 0, cz + 1, sbb);
			setBlockState(world, stoneSlabs, cx - 1, cy + 1, cz + 1, sbb);
			setBlockState(world, stoneBricks, cx + 0, cy + 0, cz + 1, sbb);
			setBlockState(world, stoneBricks, cx + 1, cy + 0, cz + 1, sbb);
			setBlockState(world, stoneSlabs, cx + 1, cy + 1, cz + 1, sbb);
		}

		setBlockState(world, waterOrLava, cx + 0, cy - 1, cz + 0, sbb);

	}

	/**
	 * Add a skeleton spawner on this floor and decorate it in an appropriately scary manner.
	 */
	protected void decorateSkeletonRoom(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		// skeleton spawner
		setSpawner(world, size / 2, bottom + 2, size / 2, sbb, VanillaEntityNames.SKELETON);

		// floor-to-ceiling chains
		ArrayList<BlockPos> chainList = new ArrayList<BlockPos>();
		chainList.add(new BlockPos(size / 2, bottom + 2, size / 2)); // don't block the spawner
		for (int i = 0; i < size + 2; i++) {
			BlockPos chain = new BlockPos(2 + rand.nextInt(size - (4)), height - 2, 2 + rand.nextInt(size - (4)));
			if (!chainCollides(chain, chainList)) {
				// if it doesn't collide, manufacture it and add it to the list
				for (int dy = bottom; dy < top; dy++) {
					setBlockState(world, Blocks.IRON_BARS.getDefaultState(), chain.getX(), dy, chain.getZ(), sbb);
				}
				chainList.add(chain);
			}
		}


		// spider webs in the corner
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				if (dx == 1 || dx == size - 2 || dz == 1 || dz == size - 2) {
					// side of the room
					if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
						// not an occupied position
						setBlockState(world, Blocks.WEB.getDefaultState(), dx, top - 1, dz, sbb);
					}
				}
			}
		}

	}


	/**
	 * Add a zombie spawner on this floor and decorate it in an appropriately scary manner.
	 */
	protected void decorateZombieRoom(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		// zombie spawner
		setSpawner(world, size / 2, bottom + 2, size / 2, sbb, VanillaEntityNames.ZOMBIE);
		final IBlockState ironBars = Blocks.IRON_BARS.getDefaultState();
		final IBlockState soulSand = Blocks.SOUL_SAND.getDefaultState();
		final IBlockState brownMushroom = Blocks.BROWN_MUSHROOM.getDefaultState();

		// random brown mushrooms
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
					// not an occupied position
					if (rand.nextInt(5) == 0) {
						setBlockState(world, brownMushroom, dx, bottom, dz, sbb);
					}
				}
			}
		}

		// slab tables
		ArrayList<BlockPos> slabList = new ArrayList<BlockPos>();
		slabList.add(new BlockPos(size / 2, bottom + 2, size / 2)); // don't block the spawner
		for (int i = 0; i < size - 1; i++) {
			BlockPos slab = new BlockPos(2 + rand.nextInt(size - (4)), height - 2, 2 + rand.nextInt(size - (4)));
			if (!chainCollides(slab, slabList)) {
				// if it doesn't collide, manufacture it and add it to the list

				setBlockState(world, ironBars, slab.getX(), bottom + 0, slab.getZ(), sbb);
				setBlockState(world, StructureTFHelper.birchSlab, slab.getX(), bottom + 1, slab.getZ(), sbb);
				setBlockState(world, soulSand, slab.getX(), bottom + 2, slab.getZ(), sbb);
				slabList.add(slab);
			}
		}
	}

	/**
	 * Fill this room with sand and floor-to-ceiling cacti
	 */
	protected void decorateCactusRoom(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		// sand & random dead bush
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				// sand
				setBlockState(world, Blocks.SAND.getDefaultState(), dx, bottom - 1, dz, sbb);
				if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
					// not an occupied position
					if (rand.nextInt(4) == 0) {
						setBlockState(world, Blocks.DEADBUSH.getDefaultState(), dx, bottom, dz, sbb);
					}
				}
			}
		}

		// cacti
		ArrayList<BlockPos> cactusList = new ArrayList<BlockPos>();
		cactusList.add(new BlockPos(size / 2, bottom + 2, size / 2)); // don't block the spawner
		for (int i = 0; i < size + 12; i++) {
			BlockPos cactus = new BlockPos(2 + rand.nextInt(size - (4)), height - 2, 2 + rand.nextInt(size - (4)));
			if (!chainCollides(cactus, cactusList)) {
				// if it doesn't collide, manufacture it and add it to the list
				for (int dy = bottom; dy < top; dy++) {
					setBlockState(world, Blocks.CACTUS.getDefaultState(), cactus.getX(), dy, cactus.getZ(), sbb);
				}
				cactusList.add(cactus);
			}
		}
	}


	/**
	 * Decorate this floor with an enticing treasure chest.
	 */
	protected void decorateTreasureChest(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		int cx = size / 2;
		int cz = cx;
		final IBlockState stoneBrick = Blocks.STONEBRICK.getDefaultState();

		// bottom decoration
		final IBlockState stoneBrickStairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();
		final IBlockState topStoneBrickStairs = stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);

		setBlockState(world, stoneBrick, cx, bottom, cz, sbb);
		setBlockState(world, stoneBrick, cx, top-1, cz, sbb);
		if(size < 6) {
			surroundBlockCardinalRotated(world, stoneBrickStairs, cx, bottom, cz, sbb);

			surroundBlockCardinalRotated(world, topStoneBrickStairs, cx, top-1, cz, sbb);
		} else {
			surroundBlockCardinalRotated(world, stoneBrickStairs, cx, bottom, cz, sbb);
			surroundBlockCorners(world, stoneBrick, cx, bottom, cz, sbb);

			// pillars
			for (int cy = bottom + 1; cy < top - 1; cy++) {
				surroundBlockCorners(world, stoneBrick, cx, cy, cz, sbb);
			}

			surroundBlockCardinalRotated(world, topStoneBrickStairs, cx, top-1, cz, sbb);
			surroundBlockCorners(world, stoneBrick, cx, top-1, cz, sbb);
		}

		placeTreasureAtCurrentPosition(world, rand, cx, bottom + 1, cz, TFTreasure.tower_room, sbb);
	}


	/**
	 * Decorate this floor with a mass of messy spider webs.
	 */
	protected void decorateSpiderWebs(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		for (int dy = bottom; dy < top; dy++) {
			int chance = (top - dy + 2);
			for (int dx = 1; dx <= size - 2; dx++) {
				for (int dz = 1; dz <= size - 2; dz++) {
					if (!isLadderPos(dx, dz, ladderUpDir, ladderDownDir) && rand.nextInt(chance) == 0) {
						setBlockState(world, Blocks.WEB.getDefaultState(), dx, dy, dz, sbb);
					}
				}
			}
		}

		// 20% chance of a spider spawner!
		if (rand.nextInt(5) == 0) {
			ResourceLocation spiderName;
			switch (rand.nextInt(4)) {
				case 3:
					spiderName = VanillaEntityNames.CAVE_SPIDER;
					break;
				case 2:
					spiderName = TFEntityNames.SWARM_SPIDER;
					break;
				case 1:
					spiderName = TFEntityNames.HEDGE_SPIDER;
					break;
				case 0:
				default:
					spiderName = VanillaEntityNames.SPIDER;
					break;
			}

			setSpawner(world, size / 2, bottom + 2, size / 2, sbb, spiderName);


		} else {
			decorateFurniture(world, rand, bottom, size - 2, sbb);
		}
	}


	/**
	 * Place some furniture around the room.  This should probably only be called on larger towers.
	 */
	protected void decorateFurniture(World world, Random rand, int bottom, int freeSpace, StructureBoundingBox sbb) {
		// 66% chance of a table
		if (rand.nextInt(3) > 0) {
			setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), size / 2, bottom, size / 2, sbb);
			setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), size / 2, bottom + 1, size / 2, sbb);
		}

		// chairs!
		final IBlockState spruceStairs = Blocks.SPRUCE_STAIRS.getDefaultState();
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			setBlockState(world, spruceStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST), size / 2 + 1, bottom, size / 2, sbb);
		}
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			setBlockState(world, spruceStairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH), size / 2, bottom, size / 2 + 1, sbb);
		}
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			setBlockState(world, spruceStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST), size / 2 - 1, bottom, size / 2, sbb);
		}
		if (rand.nextInt(3) == 0 && freeSpace > 1) {
			setBlockState(world, spruceStairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), size / 2, bottom, size / 2 - 1, sbb);
		}
	}


	/**
	 * Decorate this floor with solid rock
	 */
	protected void decorateSolidRock(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		for (int dy = bottom; dy < top; dy++) {
			for (int dx = 1; dx <= size - 2; dx++) {
				for (int dz = 1; dz <= size - 2; dz++) {
					if (!isLadderPos(dx, dz, ladderUpDir, ladderDownDir) && rand.nextInt(9) != 0) {
						setBlockState(world, Blocks.STONE.getDefaultState(), dx, dy, dz, sbb);
					}
				}
			}
		}

		//TODO: maybe seed a few ores in there.
	}


	/**
	 * Decorate this floor with an orderly library
	 */
	protected void decorateLibrary(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		// put some bookshelves around the room
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				for (int dy = bottom; dy < top - 1; dy++) {
					if (dx == 1 || dx == size - 2 || dz == 1 || dz == size - 2) {
						// side of the room
						if (!isWindowPos(dx, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
							// not an occupied position
							setBlockState(world, Blocks.BOOKSHELF.getDefaultState(), dx, dy, dz, sbb);
						}
					}
				}
			}
		}
		// treasure?!?!
		if (rand.nextInt(2) == 0 && this.size > 5) {
			decorateLibraryTreasure(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		}

		if (rand.nextInt(2) == 0 && this.size > 5) {
			decorateFurniture(world, rand, bottom, size - 2, sbb);
		}

	}


	/**
	 * Place a library treasure chest somewhere in the library
	 */
	protected void decorateLibraryTreasure(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		switch (rand.nextInt(4)) {
			case 0:
			default:
				if (!isLadderPos(2, 1, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, rand, 2, top - 2, 1, TFTreasure.tower_library, sbb);
					break;
				}
			case 1:
				if (!isLadderPos(size - 2, 2, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, rand, size - 2, top - 2, 2, TFTreasure.tower_library, sbb);
					break;
				}
			case 2:
				if (!isLadderPos(size - 3, size - 2, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, rand, size - 3, top - 2, size - 2, TFTreasure.tower_library, sbb);
					break;
				}
			case 3:
				if (!isLadderPos(1, size - 3, ladderUpDir, ladderDownDir)) {
					placeTreasureAtCurrentPosition(world, rand, 1, top - 2, size - 3, TFTreasure.tower_library, sbb);
					break;
				}
		}
	}

	/**
	 * Decorate this floor with an overflowing library
	 */
	protected void decorateFullLibrary(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		// put some bookshelves around the room
		for (int dx = 1; dx <= size - 2; dx++) {
			for (int dz = 1; dz <= size - 2; dz++) {
				for (int dy = bottom; dy < top; dy++) {
					if (dx % 2 != 0 && ((dz >= dx && dz <= size - dx - 1) || (dz >= size - dx - 1 && dz <= dx))
							|| dz % 2 != 0 && ((dx >= dz && dx <= size - dz - 1) || (dx >= size - dz - 1 && dx <= dz))) {
						// concentric rings
						if (!isWindowPos(dx, dy, dz) && !isOpeningPos(dx, dy, dz) && !isLadderPos(dx, dz, ladderUpDir, ladderDownDir)) {
							// not an occupied position
							setBlockState(world, Blocks.BOOKSHELF.getDefaultState(), dx, dy, dz, sbb);
						}
					}
				}
			}
		}
		// treasure?!?!
		if (rand.nextInt(2) == 0 && this.size > 5) {
			decorateLibraryTreasure(world, rand, bottom, top, ladderUpDir, ladderDownDir, sbb);
		}
	}

	/**
	 * "Decorate" with a lot of TNT.
	 * <p>
	 * This is not called at the moment, since I added monsters and the monsters set off the trap.  Perhaps I need a better way of activating it.
	 */
	protected void decorateTrap(World world, Random rand, int bottom, int top, int ladderUpDir, int ladderDownDir, StructureBoundingBox sbb) {
		for (int dx = 2; dx <= size - 3; dx++) {
			for (int dz = 2; dz <= size - 3; dz++) {
				if (dx == 2 || dx == size - 3 || dz == 2 || dz == size - 3) {
					setBlockState(world, Blocks.TNT.getDefaultState(), dx, -1, dz, sbb);
				}
			}
		}
		for (int dy = bottom - 2; dy < top - 2; dy++) {
			setBlockState(world, Blocks.TNT.getDefaultState(), 1, dy, 1, sbb);
			setBlockState(world, Blocks.TNT.getDefaultState(), 1, dy, size - 2, sbb);
			setBlockState(world, Blocks.TNT.getDefaultState(), size - 2, dy, 1, sbb);
			setBlockState(world, Blocks.TNT.getDefaultState(), size - 2, dy, size - 2, sbb);
		}
	}


	/**
	 * Checks if there is a window at the specified x and z.  Does not check the Y.
	 */
	protected boolean isWindowPos(int x, int z) {
		if (x == 1 && z == size / 2) {
			return true;
		}
		if (x == size - 2 && z == size / 2) {
			return true;
		}
		if (x == size / 2 && z == 1) {
			return true;
		}
		if (x == size / 2 && z == size - 2) {
			return true;
		}

		// okay, looks good
		return false;
	}

	/**
	 * Checks if there is a window at the specified x, y, and z.
	 */
	protected boolean isWindowPos(int x, int y, int z) {
		int checkYDir = -1;

		if (x == 1 && z == size / 2) {
			checkYDir = 2;
		} else if (x == size - 2 && z == size / 2) {
			checkYDir = 0;
		} else if (x == size / 2 && z == 1) {
			checkYDir = 3;
		} else if (x == size / 2 && z == size - 2) {
			checkYDir = 1;
		}

		if (checkYDir > -1) {
			// check if we are at one of the Y positions with a window.
			return !openingTowards[checkYDir] && (y == 2 || y == 3 || (height > 8 && (y == height - 3 || y == height - 4)));
		} else {
			// okay, looks good
			return false;
		}
	}

	/**
	 * Checks if putting a block at the specified x, y, and z would block an opening.
	 * TODO: this could be much smarter.  Although since there's usually only one opening, I guess it's not bad.
	 */
	protected boolean isOpeningPos(int x, int y, int z) {
		for (BlockPos door : openings) {
			// determine which wall we're at
			BlockPos.MutableBlockPos inside = new BlockPos.MutableBlockPos(door);
			if (inside.getX() == 0) {
				inside.move(EnumFacing.EAST);
			} else if (inside.getX() == size - 1) {
				inside.move(EnumFacing.WEST);
			} else if (inside.getZ() == 0) {
				inside.move(EnumFacing.SOUTH);
			} else if (inside.getZ() == size - 1) {
				inside.move(EnumFacing.NORTH);
			}
			// check the block
			if (inside.getX() == x && inside.getZ() == z && (inside.getY() == y || inside.getY() + 1 == y)) {
				return true;
			}
		}
		// guess not!
		return false;
	}


	protected boolean isLadderPos(int x, int z, Rotation ladderUpDir, Rotation ladderDownDir) {
		if (ladderUpDir != null && x == getLadderX(ladderUpDir) && z == getLadderZ(ladderUpDir)) {
			return true;
		}
		if (ladderDownDir != null && x == getLadderX(ladderDownDir) && z == getLadderZ(ladderDownDir)) {
			return true;
		}

		// okay, looks good
		return false;
	}

	/**
	 * Gets the X coordinate of the ladder on the specified wall.
	 */
	protected int getLadderX(Rotation ladderDir) {
		switch (ladderDir) {
			case NONE:
				return size - 2;
			case CLOCKWISE_90:
				return size / 2 + 1;
			case CLOCKWISE_180:
				return 1;
			case COUNTERCLOCKWISE_90:
				return size / 2 - 1;
			default:
				return size / 2;
		}
	}

	/**
	 * Gets the Z coordinate of the ladder on the specified wall.
	 */
	protected int getLadderZ(Rotation ladderDir) {

		switch (ladderDir) {
			case NONE:
				return size / 2 - 1;
			case CLOCKWISE_90:
				return size - 2;
			case CLOCKWISE_180:
				return size / 2 + 1;
			case COUNTERCLOCKWISE_90:
				return 1;
			default:
				return size / 2;
		}
	}

	/**
	 * Decorate a tower with stairs.
	 * <p>
	 * We have two schemes here.  We can either decorate the whole tower with a
	 * decoration that rises the entire height of the tower (such as a pillar)
	 * or we can divide the tower into the "stair" section on the bottom and the
	 * "attic" section at the top and decorate those seperately.
	 */
	protected void decorateStairTower(World world, Random rand, StructureBoundingBox sbb) {

		// if it's tall enough, consider adding extra floors onto the top.
		if (height - highestOpening > 8) {
			int base = highestOpening + 3;
			int floors = (this.height - base) / 5;

			// divide the tower into floors
			int floorHeight = (this.height - base) / floors;
			for (int i = 0; i < floors; i++) {
				// put down a floor
				for (int x = 1; x < size - 1; x++) {
					for (int z = 1; z < size - 1; z++) {
						setBlockState(world, StructureTFHelper.birchPlanks, x, (i * floorHeight + base), z, sbb);
					}
				}
			}

			Rotation ladderDir = Rotation.NONE;
			Rotation downLadderDir;

			// place a ladder going up
			//TODO: make this ladder connect better to the stairs
			int dx = getLadderX(ladderDir);
			int dz = getLadderZ(ladderDir);
			final IBlockState defaultState = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, ladderDir.rotate(EnumFacing.EAST));
			for (int dy = 1; dy < 3; dy++) {
				setBlockState(world, defaultState, dx, base - dy, dz, sbb);
			}

			// decorate middle floors
			for (int i = 0; i < floors - 1; i++) {
				int bottom = base + 1 + floorHeight * i;
				int top = base + floorHeight * (i + 1);

				downLadderDir = ladderDir;
				ladderDir = ladderDir.add(Rotation.CLOCKWISE_90);

				decorateFloor(world, rand, i, bottom, top, ladderDir, downLadderDir, sbb);
			}

			// decorate top floor
			decorateFloor(world, rand, floors, base + 1 + floorHeight * (floors - 1), height - 1, null, ladderDir, sbb);

			// decorate below the bottom floor, into the stairs
			if (base > 8) {
				switch (rand.nextInt(4)) {
					case 0:
						decorateChandelier(world, rand, base + 1, sbb);
						break;
					case 1:
						decorateHangingChains(world, rand, base + 1, sbb);
						break;
					case 2:
						decorateFloatingBooks(world, rand, base + 1, sbb);
						break;
					case 3:
						decorateFloatingVines(world, rand, base + 1, sbb);
						break;
				}
			}
		} else {
			// decorate the top normally
			if (size > 5) {
				switch (rand.nextInt(4)) {
					case 0:
						decorateChandelier(world, rand, height, sbb);
						break;
					case 1:
						decorateHangingChains(world, rand, height, sbb);
						break;
					case 2:
						decorateFloatingBooks(world, rand, height, sbb);
						break;
					case 3:
						decorateFloatingVines(world, rand, height, sbb);
						break;
				}
			} else if (size > 3) {
				switch (rand.nextInt(3)) {
					case 0:
						decorateHangingChains(world, rand, height, sbb);
						break;
					case 1:
						decorateFloatingBooks(world, rand, height, sbb);
						break;
					case 2:
						decorateFloatingVines(world, rand, height, sbb);
						break;
				}
			}
		}

		decorateStairFloor(world, rand, sbb);
	}


	/**
	 * Decorate the bottom floor of this tower.
	 * <p>
	 * This is for towers with stairs at the bottom, not towers divided into floors
	 */
	protected void decorateStairFloor(World world, Random rand, StructureBoundingBox sbb) {
		// decorate the bottom
		if (size > 5) {
			if (rand.nextInt(3) == 0) {
				decorateStairWell(world, rand, sbb);
			} else if (rand.nextInt(3) > 0 || this.size >= 15) {
				// a few empty bottoms
				decoratePlanter(world, rand, sbb);
			}
		}
	}


	/**
	 * Make a chandelier.  The chandelier hangs down a random amount between the top of the tower and the highest opening.
	 */
	protected void decorateChandelier(World world, Random rand, int decoTop, StructureBoundingBox sbb) {
		if (decoTop < 8 || size < 8) {
			return;
		}

		int cx = size / 2;
		int cy = decoTop - rand.nextInt(decoTop - 7) - 2;
		int cz = size / 2;

		final IBlockState oakFence = Blocks.OAK_FENCE.getDefaultState();

		//setDebugEntity(world, cx, cy, cz, sbb, "TowerWing.decorateChandelier");
		surroundBlockCardinal(world, oakFence, cx, cy, cz, sbb);
		surroundBlockCardinal(world, oakFence, cx, cy + 1, cz, sbb);

		for (int y = cy; y < decoTop - 1; y++) {
			setBlockState(world, oakFence, cx, y, cz, sbb);
		}
	}


	/**
	 * Decorates a tower with chains hanging down.
	 * <p>
	 * The chains go from the ceiling to just above the highest doorway.
	 */
	protected void decorateHangingChains(World world, Random rand, int decoTop, StructureBoundingBox sbb) {
		// a list of existing chains
		ArrayList<BlockPos> chainList = new ArrayList<BlockPos>();
		// try size + 2 times to find a chain that does not collide
		for (int i = 0; i < size + 2; i++) {
			int filled = size < 15 ? 2 : 4;
			BlockPos chain = new BlockPos(filled + rand.nextInt(size - (filled * 2)), decoTop - 2, filled + rand.nextInt(size - (filled * 2)));
			if (!chainCollides(chain, chainList)) {
				// if it doesn't collide, manufacture it and add it to the list
				int length = 1 + rand.nextInt(decoTop - 7);
				decorateOneChain(world, rand, chain.getX(), decoTop, length, chain.getZ(), sbb);
				chainList.add(chain);
			}
		}


	}

	/**
	 * Return true if the specified coords are orthogonally adjacent to any other coords on the list.
	 */
	protected boolean chainCollides(BlockPos coords, List<BlockPos> list) {
		for (BlockPos existing : list) {
			// if x is within 1 and z is equal, we collide
			if (coords.getZ() == existing.getZ() && Math.abs(coords.getX() - existing.getX()) <= 1) {
				return true;
			}
			// similarly, if z is within 1 and x is equal, we collide
			if (coords.getX() == existing.getX() && Math.abs(coords.getZ() - existing.getZ()) <= 1) {
				return true;
			}
		}
		// we're good
		return false;
	}

	protected void decorateOneChain(World world, Random rand, int dx, int decoTop, int length, int dz, StructureBoundingBox sbb) {
		for (int y = 1; y <= length; y++) {
			setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx, decoTop - y - 1, dz, sbb);
		}
		// make the "ball" at the end.
		IBlockState ballBlock;
		switch (rand.nextInt(10)) {
			case 0:
				ballBlock = Blocks.IRON_BLOCK.getDefaultState();
				break;
			case 1:
				ballBlock = Blocks.BOOKSHELF.getDefaultState();
				break;
			case 2:
				ballBlock = Blocks.NETHERRACK.getDefaultState();
				break;
			case 3:
				ballBlock = Blocks.SOUL_SAND.getDefaultState();
				break;
			case 4:
				ballBlock = Blocks.GLASS.getDefaultState();
				break;
			case 5:
				ballBlock = Blocks.LAPIS_BLOCK.getDefaultState();
				break;
			case 6:
				ballBlock = Blocks.MONSTER_EGG.getDefaultState()
						.withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONEBRICK);
				break;
			case 7:
			default:
				ballBlock = Blocks.GLOWSTONE.getDefaultState();
				break;
		}
		setBlockState(world, ballBlock, dx, decoTop - length - 2, dz, sbb);
	}

	/**
	 * Decorates a tower with an array of floating bookshelves.
	 */
	protected void decorateFloatingBooks(World world, Random rand, int decoTop, StructureBoundingBox sbb) {
		// a list of existing bookshelves
		ArrayList<BlockPos> shelfList = new ArrayList<BlockPos>();
		// try size + 2 times to find a shelf that does not collide
		for (int i = 0; i < size + 2; i++) {
			int filled = size < 15 ? 2 : 4;
			BlockPos shelf = new BlockPos(filled + rand.nextInt(size - (filled * 2)), decoTop - 2, filled + rand.nextInt(size - (filled * 2)));
			if (!chainCollides(shelf, shelfList)) {
				// if it doesn't collide, manufacture it and add it to the list
				int bottom = 2 + rand.nextInt(decoTop - 7);
				int top = rand.nextInt(bottom - 1) + 2;
				for (int y = top; y <= bottom; y++) {
					setBlockState(world, Blocks.BOOKSHELF.getDefaultState(), shelf.getX(), decoTop - y, shelf.getZ(), sbb);
				}
				shelfList.add(shelf);
			}
		}
	}


	/**
	 * Decorates a tower with an array of floating vines, attached to mossy cobblestone.
	 */
	protected void decorateFloatingVines(World world, Random rand, int decoTop, StructureBoundingBox sbb) {
		final IBlockState mossyCobbleStone = Blocks.MOSSY_COBBLESTONE.getDefaultState();
		final IBlockState vine = Blocks.VINE.getDefaultState();
		final IBlockState vineNorth = vine.withProperty(BlockVine.NORTH, true);
		final IBlockState vineSouth = vine.withProperty(BlockVine.SOUTH, true);
		final IBlockState vineEast = vine.withProperty(BlockVine.EAST, true);
		final IBlockState vineWest = vine.withProperty(BlockVine.WEST, true);

		// a list of existing blocks
		ArrayList<BlockPos> mossList = new ArrayList<BlockPos>();
		// try size + 2 times to find a rock pillar that does not collide
		for (int i = 0; i < size + 2; i++) {
			int filled = size < 15 ? 2 : 4;
			BlockPos moss = new BlockPos(filled + rand.nextInt(size - (filled * 2)), decoTop - 2, filled + rand.nextInt(size - (filled * 2)));
			if (!chainCollides(moss, mossList)) {
				// if it doesn't collide, manufacture it and add it to the list
				int bottom = 2 + rand.nextInt(decoTop - 7);
				int top = rand.nextInt(bottom - 1) + 2;
				for (int y = top; y <= bottom; y++) {
					setBlockState(world, mossyCobbleStone, moss.getX(), decoTop - y, moss.getZ(), sbb);
					// surround it with vines
					setBlockState(world, vineEast, moss.getX() + 1, decoTop - y, moss.getZ() + 0, sbb);
					setBlockState(world, vineWest, moss.getX() - 1, decoTop - y, moss.getZ() + 0, sbb);
					setBlockState(world, vineSouth, moss.getX() + 0, decoTop - y, moss.getZ() + 1, sbb);
					setBlockState(world, vineNorth, moss.getX() + 0, decoTop - y, moss.getZ() - 1, sbb);
				}
				mossList.add(moss);
			}
		}

		// put vines on the sides of the tower.
		for (int y = highestOpening + 3; y < decoTop - 1; y++) {
			for (int x = 1; x < size - 1; x++) {
				if (rand.nextInt(3) == 0) {
					setBlockState(world, vineSouth, x, y, 1, sbb);
				}
				if (rand.nextInt(3) == 0) {
					setBlockState(world, vineNorth, x, y, size - 2, sbb);
				}
			}
			for (int z = 1; z < size - 1; z++) {
				if (rand.nextInt(3) == 0) {
					setBlockState(world, vineEast, 1, y, z, sbb);
				}
				if (rand.nextInt(3) == 0) {
					setBlockState(world, vineWest, size - 2, y, z, sbb);
				}
			}
		}
	}

	/**
	 * Makes a planter.  Depending on the situation, it can be filled with trees, flowers, or crops
	 */
	protected void decoratePlanter(World world, Random rand, StructureBoundingBox sbb) {
		int cx = size / 2;
		int cz = cx;

		surroundBlockCardinal(world, StructureTFHelper.stoneSlab, cx, 1, cz, sbb);

		if (size > 7)
			surroundBlockCorners(world, StructureTFHelper.stoneSlabDouble, cx, 1, cz, sbb);


		// place a cute planted thing
		setBlockState(world, Blocks.GRASS.getDefaultState(), cx, 1, cz, sbb);

		int i = rand.nextInt(6);
		boolean isTree = i > 4;
		final IBlockState plant = isTree ? StructureTFHelper.randomSapling(i) : StructureTFHelper.randomMushroom(i);


		setBlockState(world, plant, cx, 2, cz, sbb);
		final BlockPos pos = getBlockPosWithOffset(cx, 2, cz);

		if(isTree) //grow tree
			((BlockSapling) Blocks.SAPLING).grow(world, pos, plant, world.rand);
		else //grow sapling
			plant.getBlock().updateTick(world, pos, plant, world.rand);


		// otherwise, place the block into a flowerpot
		IBlockState whatHappened = this.getBlockStateFromPos(world, cx, 2, cz, sbb);
		if (whatHappened.getBlock() == plant.getBlock() || whatHappened.getBlock() == Blocks.AIR)
			setBlockState(world, Blocks.FLOWER_POT.getDefaultState(), cx, 2, cz, sbb);
	}

	/**
	 * Decorate the floor of this stair tower with a scenic well.
	 */
	protected void decorateStairWell(World world, Random rand, StructureBoundingBox sbb) {
		int cx = size / 2;
		int cz = cx;
		int cy = 1;

		final IBlockState waterOrLava = rand.nextInt(4) == 0 ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState();

		final IBlockState stoneSlab = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, SMOOTHBRICK);
		final IBlockState stoneBrick = Blocks.STONEBRICK.getDefaultState();

		if (size > 7) {
			// actual well structure
			setBlockState(world, stoneBrick, cx - 1, cy + 0, cz - 1, sbb);
			setBlockState(world, stoneSlab, cx - 1, cy + 1, cz - 1, sbb);
			setBlockState(world, stoneBrick, cx + 0, cy + 0, cz - 1, sbb);
			setBlockState(world, stoneBrick, cx + 1, cy + 0, cz - 1, sbb);
			setBlockState(world, stoneSlab, cx + 1, cy + 1, cz - 1, sbb);
			setBlockState(world, stoneBrick, cx - 1, cy + 0, cz + 0, sbb);
			setBlockState(world, waterOrLava, cx + 0, cy + 0, cz + 0, sbb);
			setBlockState(world, stoneBrick, cx + 1, cy + 0, cz + 0, sbb);
			setBlockState(world, stoneBrick, cx - 1, cy + 0, cz + 1, sbb);
			setBlockState(world, stoneSlab, cx - 1, cy + 1, cz + 1, sbb);
			setBlockState(world, stoneBrick, cx + 0, cy + 0, cz + 1, sbb);
			setBlockState(world, stoneBrick, cx + 1, cy + 0, cz + 1, sbb);
			setBlockState(world, stoneSlab, cx + 1, cy + 1, cz + 1, sbb);
		}

		setBlockState(world, waterOrLava, cx + 0, cy - 1, cz + 0, sbb);

	}


	/**
	 * Returns true if this tower has only one exit.
	 */
	public boolean isDeadEnd() {
		return openings.size() == 1;
	}

	/**
	 * Returns true if this tower has four exits.
	 */
	public boolean hasExitsOnAllWalls() {
		int exits = 0;
		for (int i = 0; i < 4; i++) {
			exits += this.openingTowards[i] ? 1 : 0;
		}

		return exits == 4;
	}

	/**
	 * Returns true if this tower has stairs
	 */
	public boolean hasStairs() {
		return highestOpening > 1;
	}

	/**
	 * Iterate through the openings on our list and add them to the tower
	 */
	protected void makeOpenings(World world, StructureBoundingBox sbb) {
		for (BlockPos door : openings) {
			makeDoorOpening(world, door.getX(), door.getY(), door.getZ(), sbb);
		}
	}


	/**
	 * Make an opening in this tower for a door.  This now only makes one opening, so you need two
	 */
	protected void makeDoorOpening(World world, int dx, int dy, int dz, StructureBoundingBox sbb) {
		// try to add blocks outside this door
//		if (dx == 0) {
//			setBlockState(world, Blocks.STONE.getDefaultState(), dx - 1, dy + 0, dz, sbb);
//	        setBlockState(world, Blocks.STONE.getDefaultState(), dx - 1, dy + 1, dz, sbb);
//		}
//		if (dx == size - 1) {
//			setBlockState(world, Blocks.STONE.getDefaultState(), dx + 1, dy + 0, dz, sbb);
//	        setBlockState(world, Blocks.STONE.getDefaultState(), dx + 1, dy + 1, dz, sbb);
//		}
//		if (dz == 0) {
//			setBlockState(world, Blocks.STONE.getDefaultState(), dx, dy + 0, dz - 1, sbb);
//	        setBlockState(world, Blocks.STONE.getDefaultState(), dx, dy + 1, dz - 1, sbb);
//		}
//		if (dz == size - 1) {
//			setBlockState(world, Blocks.STONE.getDefaultState(), dx, dy + 0, dz + 1, sbb);
//	        setBlockState(world, Blocks.STONE.getDefaultState(), dx, dy + 1, dz + 1, sbb);
//		}
//		

		setBlockState(world, AIR, dx, dy + 0, dz, sbb);
		setBlockState(world, AIR, dx, dy + 1, dz, sbb);
//        updateLight(world, dx, dy + 0, dz);
//        updateLight(world, dx, dy + 1, dz);

		if (getBlockStateFromPos(world, dx, dy + 2, dz, sbb).getBlock() != Blocks.AIR) {
			IBlockState state = StructureTFHelper.stoneSlabDouble;
			setBlockState(world, state, dx, dy + 2, dz, sbb);
		}

		// clear the door
		if (dx == 0) {
//			setBlockState(world, AIR, dx - 1, dy + 0, dz, sbb);
//	        setBlockState(world, AIR, dx - 1, dy + 1, dz, sbb);
			updateLight(world, dx - 1, dy + 0, dz);
			updateLight(world, dx - 1, dy + 1, dz);
		}
		if (dx == size - 1) {
//			setBlockState(world, AIR, dx + 1, dy + 0, dz, sbb);
//	        setBlockState(world, AIR, dx + 1, dy + 1, dz, sbb);
			updateLight(world, dx + 1, dy + 0, dz);
			updateLight(world, dx + 1, dy + 1, dz);
		}
		if (dz == 0) {
//			setBlockState(world, AIR, dx, dy + 0, dz - 1, sbb);
//	        setBlockState(world, AIR, dx, dy + 1, dz - 1, sbb);
			updateLight(world, dx, dy + 0, dz - 1);
			updateLight(world, dx, dy + 1, dz - 1);
		}
		if (dz == size - 1) {
//			setBlockState(world, AIR, dx, dy + 0, dz + 1, sbb);
//	        setBlockState(world, AIR, dx, dy + 1, dz + 1, sbb);
			updateLight(world, dx, dy + 0, dz + 1);
			updateLight(world, dx, dy + 1, dz + 1);
		}
	}

	protected void updateLight(World world, int dx, int dy, int dz) {
		//world.updateAllLightTypes(getXWithOffset(dx, dz), getYWithOffset(dy), getZWithOffset(dx, dz));
	}

	/**
	 * Gets a random position in the specified direction that connects to stairs currently in the tower.
	 */
	public int[] getValidOpening(Random rand, Rotation direction) {
		// variables!
		int wLength = size - 2; // wall length
		int offset = 1; // wall thickness

		// size 15 towers have funny landings, so don't generate on the very edge
		if (this.size == 15) {
			wLength = 11;
			offset = 2;
		}

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
	protected int getYByStairs(int rx, Random rand, Rotation direction) {
		// initialize some variables
		int rise = 1;
		int base = 0;

		if (size == 15) {
			rise = 10;
			// we lie a little here to get the towers off the ground
			base = (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) ? 23 : 28;
		}
		if (size == 9) {
			rise = 6;
			base = (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) ? 2 : 5;
		}
		if (size == 7) {
			rise = 4;
			base = (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) ? 2 : 4;
		}
		if (size == 5) {
			rise = 4;
			// bleh, a switch.
			switch (direction) {
				case NONE:
					base = 3;
					break;
				case CLOCKWISE_90:
					base = 2;
					break;
				case CLOCKWISE_180:
					base = 5;
					break;
				case COUNTERCLOCKWISE_90:
					base = 4;
					break;
			}
		}

		int flights = ((height - 6 - base) / rise) + 1;

		if (base > 0 && flights > 0) {
			// pick a flight of stairs to be on
			int flightChosen = rand.nextInt(flights);
			// calculate where we would be if rx = 0
			int dy = (flightChosen * rise) + base;
			// the staircase (a/de)scends across the room.
			if (size == 15) {
				// blech, another dumb kludge here
				dy -= (direction == Rotation.NONE || direction == Rotation.COUNTERCLOCKWISE_90) ? (rx - 2) / 2 : (size - rx - 3) / 2;
			} else {
				// the rest are fairly normal
				dy -= (direction == Rotation.NONE || direction == Rotation.COUNTERCLOCKWISE_90) ? (rx - 1) / 2 : (size - rx - 2) / 2;
			}
//			// even xs can be one higher if they want.
//			if (rx % 2 == 0 && size != 15) {
//				dy += rand.nextInt(2);
//			}
			// don't go through the floor
			if (dy < 1) {
				dy = 1;
			}

			return dy;
		}

		return 0;
	}


	/**
	 * Makes 3 windows outside this tower.
	 * <p>
	 * The function currently looks "outside" to see if the window will be blocked, but it can't see into the future, so a tower built after this one may block it.
	 * <p>
	 * Maybe this could eventually have access to the list of bounding boxes for better accuracy?
	 */
	protected void makeWindows(World world, Random rand, StructureBoundingBox sbb, boolean real) {


		//for (int i = 0; i < 4; i++) {
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			boolean realWindows = real && !openingTowards[rotation.ordinal()];
			makeWindowBlock(world, size - 1, 2, size / 2, rotation, sbb, realWindows);
			makeWindowBlock(world, size - 1, 3, size / 2, rotation, sbb, realWindows);
			makeWindowBase(world, size - 1, 1, size / 2, rotation, sbb);
			if (height > 8) {
				makeWindowBlock(world, size - 1, height - 3, size / 2, rotation, sbb, realWindows);
				makeWindowBlock(world, size - 1, height - 4, size / 2, rotation, sbb, realWindows);
				makeWindowBase(world, size - 1, height - 5, size / 2, rotation, sbb);
			}
		}
	}


	/**
	 * Makes a window block.  Specify a point in a wall, and this function checks to
	 * see if it is blocked on the inside or outside, and if not, adds a pane of glass.
	 */
	protected void makeWindowBlock(World world, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb, boolean realWindows) {
		EnumFacing temp = this.getCoordBaseMode();
		this.setCoordBaseMode(rotation.rotate(temp));

		// look outside
		Block outside = getBlockStateFromPos(world, x + 1, y, z, sbb).getBlock();

		// look inside
		Block inside = getBlockStateFromPos(world, x - 1, y, z, sbb).getBlock();

		// make a window!
		if (realWindows && inside == Blocks.AIR && outside == Blocks.AIR) {
			setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), x, y, z, sbb);
		} else {
			// cobblestone where the window might have been
			setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), x, y, z, sbb);
		}

		this.setCoordBaseMode(temp);

	}

	/**
	 * Makes a window base
	 */
	protected void makeWindowBase(World world, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb) {
		EnumFacing temp = this.getCoordBaseMode();
		this.setCoordBaseMode(rotation.rotate(temp));
		IBlockState state = StructureTFHelper.stoneSlabDouble;
		setBlockState(world, state, x, y, z, sbb);
		this.setCoordBaseMode(temp);

	}

	/**
	 * Add stairs to this tower.
	 */
	protected boolean makeStairs(World world, Random rand, StructureBoundingBox sbb) {
		if (this.size == 15) {
			return makeStairs15(world, rand, sbb);
		}
		if (this.size == 9) {
			return makeStairs9(world, rand, sbb);
		}
		if (this.size == 7) {
			return makeStairs7(world, rand, sbb);
		}
		if (this.size == 5) {
			return makeStairs5(world, rand, sbb);
		}

		return false;
	}

	/**
	 * Stair maker for a size 5 tower
	 */
	protected boolean makeStairs5(World world, Random rand, StructureBoundingBox sbb) {
		// staircases rotating around the tower
		int rise = 1;
//		int numFlights = ((this.height - 3) / rise) - 1;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs5flight(world, rand, sbb, i * rise, getRotation(Rotation.NONE, i * 3), true);
		}

		return true;
	}

	/**
	 * Function called by makeStairs5 to place stair blocks
	 */
	protected void makeStairs5flight(World world, Random rand, StructureBoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		EnumFacing temp = this.getCoordBaseMode();

		this.setCoordBaseMode(rotation.rotate(temp));

		final IBlockState bottomSlab = useBirchWood ?
				StructureTFHelper.birchSlab :
				StructureTFHelper.stoneSlab;
		final IBlockState topSlab = useBirchWood ?
				StructureTFHelper.birchSlabTop :
				StructureTFHelper.stoneSlabTop;

		setBlockState(world, bottomSlab, 2, 1 + height, 3, sbb);
		setBlockState(world, topSlab, 3, 1 + height, 3, sbb);

		this.setCoordBaseMode(temp);
	}

	/**
	 * Stair maker for a size 7 tower
	 */
	protected boolean makeStairs7(World world, Random rand, StructureBoundingBox sbb) {
		// foot of stairs
		setBlockState(world, StructureTFHelper.birchSlab, 1, 1, 4, sbb);
		setBlockState(world, StructureTFHelper.birchSlabTop, 1, 1, 5, sbb);

		setBlockState(world, StructureTFHelper.stoneSlab, 5, 1, 2, sbb);
		setBlockState(world, StructureTFHelper.stoneSlabTop, 5, 1, 1, sbb);

		// staircases rotating around the tower
		int rise = 2;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs7flight(world, rand, sbb, 1 + i * rise, getRotation(Rotation.NONE, i * 3), true);
			makeStairs7flight(world, rand, sbb, 1 + i * rise, getRotation(Rotation.CLOCKWISE_180, i * 3), false);
		}

		return true;
	}

	/**
	 * Function called by makeStairs7 to place stair blocks
	 */
	protected void makeStairs7flight(World world, Random rand, StructureBoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		final EnumFacing temp = this.getCoordBaseMode();

		this.setCoordBaseMode(rotation.rotate(temp));
		final IBlockState slabBottom = useBirchWood ?
				StructureTFHelper.birchSlab :
				StructureTFHelper.stoneSlab;
		final IBlockState slabTop = useBirchWood ?
				StructureTFHelper.birchSlabTop :
				StructureTFHelper.stoneSlabTop;

		setBlockState(world, slabBottom, 2, 1 + height, 5, sbb);
		setBlockState(world, slabTop, 3, 1 + height, 5, sbb);
		setBlockState(world, slabBottom, 4, 2 + height, 5, sbb);
		setBlockState(world, slabTop, 5, 2 + height, 5, sbb);

		this.setCoordBaseMode(temp);
	}

	/**
	 * Stair maker for a size 9 tower
	 */
	protected boolean makeStairs9(World world, Random rand, StructureBoundingBox sbb) {

		// foot of stairs
		setBlockState(world, StructureTFHelper.birchSlab, 1, 1, 6, sbb);
		setBlockState(world, StructureTFHelper.birchSlabTop, 1, 1, 7, sbb);

		setBlockState(world, StructureTFHelper.stoneSlab, 7, 1, 2, sbb);
		setBlockState(world, StructureTFHelper.stoneSlabTop, 7, 1, 1, sbb);

		// staircases rotating around the tower
		int rise = 3;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs9flight(world, rand, sbb, 1 + i * rise, getRotation(Rotation.NONE, i * 3), true);
			makeStairs9flight(world, rand, sbb, 1 + i * rise, getRotation(Rotation.CLOCKWISE_180, i * 3), false);
		}

		return true;
	}

	/**
	 * Function called by makeStairs7 to place stair blocks
	 *
	 */
	protected void makeStairs9flight(World world, Random rand, StructureBoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		//TODO: Can we just... not do this?
		EnumFacing temp = this.getCoordBaseMode();
		this.setCoordBaseMode(rotation.rotate(temp));

		final IBlockState slabBot = useBirchWood ?
				StructureTFHelper.birchSlab :
				StructureTFHelper.stoneSlab;
		final IBlockState slabTop = useBirchWood ?
				StructureTFHelper.birchSlabTop :
				StructureTFHelper.stoneSlabTop;

		setBlockState(world, slabBot, 2, 1 + height, 7, sbb);
		setBlockState(world, slabTop, 3, 1 + height, 7, sbb);
		setBlockState(world, slabBot, 4, 2 + height, 7, sbb);
		setBlockState(world, slabTop, 5, 2 + height, 7, sbb);
		setBlockState(world, slabBot, 6, 3 + height, 7, sbb);
		setBlockState(world, slabTop, 7, 3 + height, 7, sbb);

		this.setCoordBaseMode(temp);
	}

	/**
	 * Stair maker for a size 15 tower
	 */
	protected boolean makeStairs15(World world, Random rand, StructureBoundingBox sbb) {
		final IBlockState planks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		final IBlockState oakFence = Blocks.OAK_FENCE.getDefaultState();
		final IBlockState birchSlab = StructureTFHelper.birchSlab;
		final IBlockState stoneSlab = StructureTFHelper.stoneSlab;
		final IBlockState doubleStoneSlab = StructureTFHelper.stoneSlabDouble;

		// foot of stairs
		setBlockState(world, birchSlab, 1, 1, 9, sbb);
		setBlockState(world, birchSlab, 2, 1, 9, sbb);

		setBlockState(world, planks, 1, 1, 10, sbb);
		setBlockState(world, planks, 2, 1, 10, sbb);
		setBlockState(world, birchSlab, 1, 2, 11, sbb);
		setBlockState(world, birchSlab, 2, 2, 11, sbb);
		setBlockState(world, planks, 1, 2, 12, sbb);
		setBlockState(world, planks, 2, 2, 12, sbb);
		setBlockState(world, planks, 1, 2, 13, sbb);
		setBlockState(world, planks, 2, 2, 13, sbb);

		setBlockState(world, planks, 3, 2, 11, sbb);
		setBlockState(world, oakFence, 3, 3, 11, sbb);
		setBlockState(world, oakFence, 3, 4, 11, sbb);
		setBlockState(world, planks, 3, 1, 10, sbb);
		setBlockState(world, oakFence, 3, 2, 10, sbb);
		setBlockState(world, oakFence, 3, 3, 10, sbb);
		setBlockState(world, planks, 3, 1, 9, sbb);
		setBlockState(world, oakFence, 3, 2, 9, sbb);

		setBlockState(world, stoneSlab, 13, 1, 5, sbb);
		setBlockState(world, stoneSlab, 12, 1, 5, sbb);
		setBlockState(world, doubleStoneSlab, 13, 1, 4, sbb);
		setBlockState(world, doubleStoneSlab, 12, 1, 4, sbb);
		setBlockState(world, stoneSlab, 13, 2, 3, sbb);
		setBlockState(world, stoneSlab, 12, 2, 3, sbb);
		setBlockState(world, doubleStoneSlab, 13, 2, 2, sbb);
		setBlockState(world, doubleStoneSlab, 12, 2, 2, sbb);
		setBlockState(world, doubleStoneSlab, 13, 2, 1, sbb);
		setBlockState(world, doubleStoneSlab, 12, 2, 1, sbb);
		setBlockState(world, doubleStoneSlab, 11, 2, 3, sbb);
		setBlockState(world, oakFence, 11, 3, 3, sbb);
		setBlockState(world, oakFence, 11, 4, 3, sbb);
		setBlockState(world, doubleStoneSlab, 11, 1, 4, sbb);
		setBlockState(world, oakFence, 11, 2, 4, sbb);
		setBlockState(world, oakFence, 11, 3, 4, sbb);
		setBlockState(world, doubleStoneSlab, 11, 1, 5, sbb);
		setBlockState(world, oakFence, 11, 2, 5, sbb);


		// staircases rotating around the tower
		int rise = 5;
		int numFlights = (highestOpening / rise);
		for (int i = 0; i < numFlights; i++) {
			makeStairs15flight(world, rand, sbb, 2 + i * rise, getRotation(Rotation.NONE, i * 3), true);
			makeStairs15flight(world, rand, sbb, 2 + i * rise, getRotation(Rotation.CLOCKWISE_180, i * 3), false);
		}

		return true;
	}

	private Rotation getRotation(Rotation startRotation, int rotations) {
		final int totalIncrements = startRotation.ordinal() + rotations;
		return RotationUtil.ROTATIONS[totalIncrements & 3];
	}

	/**
	 * Function called by makeStairs7 to place stair blocks
	 * pretty sure this is the one that makes the main staircase
	 */
	protected void makeStairs15flight(World world, Random rand, StructureBoundingBox sbb, int height, Rotation rotation, boolean useBirchWood) {
		EnumFacing temp = this.getCoordBaseMode();

		this.setCoordBaseMode(rotation.rotate(temp));

		final IBlockState oakFence = Blocks.OAK_FENCE.getDefaultState();

		final IBlockState slabBot = useBirchWood ?
				StructureTFHelper.birchSlab :
				StructureTFHelper.stoneSlab;
		final IBlockState slabTop = useBirchWood ?
				StructureTFHelper.birchSlabTop :
				StructureTFHelper.stoneSlabTop;
		final IBlockState slabDoub = useBirchWood ?
				StructureTFHelper.birchPlanks :
				StructureTFHelper.stoneSlabDouble;

		setBlockState(world, slabBot, 3, 1 + height, 13, sbb);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 4, 1 + height, 13, slabTop);
		setBlockState(world, slabBot, 5, 2 + height, 13, sbb);
		setBlockState(world, slabTop, 6, 2 + height, 13, sbb);
		setBlockState(world, slabBot, 7, 3 + height, 13, sbb);
		setBlockState(world, slabTop, 8, 3 + height, 13, sbb);
		setBlockState(world, slabBot, 9, 4 + height, 13, sbb);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 10, 4 + height, 13, slabTop);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 11, 5 + height, 13, slabBot);
		setBlockState(world, slabTop, 12, 5 + height, 13, sbb);
		setBlockState(world, slabTop, 13, 5 + height, 13, sbb);

		randomlyPlaceBlock(world, sbb, rand, 0.9F, 3, 1 + height, 12, slabBot);
		setBlockState(world, slabTop, 4, 1 + height, 12, sbb);
		setBlockState(world, slabBot, 5, 2 + height, 12, sbb);
		setBlockState(world, slabTop, 6, 2 + height, 12, sbb);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 7, 3 + height, 12, slabBot);
		setBlockState(world, slabTop, 8, 3 + height, 12, sbb);
		setBlockState(world, slabBot, 9, 4 + height, 12, sbb);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 10, 4 + height, 12, slabTop);
		setBlockState(world, slabBot, 11, 5 + height, 12, sbb);
		setBlockState(world, slabTop, 12, 5 + height, 12, sbb);
		setBlockState(world, slabTop, 13, 5 + height, 12, sbb);

		setBlockState(world, slabDoub, 4, 1 + height, 11, sbb);
		setBlockState(world, slabDoub, 5, 2 + height, 11, sbb);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 6, 2 + height, 11, slabTop);
		setBlockState(world, slabDoub, 7, 3 + height, 11, sbb);
		randomlyPlaceBlock(world, sbb, rand, 0.9F, 8, 3 + height, 11, slabTop);
		setBlockState(world, slabDoub, 9, 4 + height, 11, sbb);
		setBlockState(world, slabTop, 10, 4 + height, 11, sbb);
		setBlockState(world, slabDoub, 11, 5 + height, 11, sbb);

		setBlockState(world, oakFence, 4, 2 + height, 11, sbb);
		setBlockState(world, oakFence, 5, 3 + height, 11, sbb);
		setBlockState(world, oakFence, 6, 3 + height, 11, sbb);
		setBlockState(world, oakFence, 7, 4 + height, 11, sbb);
		setBlockState(world, oakFence, 8, 4 + height, 11, sbb);
		setBlockState(world, oakFence, 9, 5 + height, 11, sbb);
		setBlockState(world, oakFence, 10, 5 + height, 11, sbb);
		setBlockState(world, oakFence, 11, 6 + height, 11, sbb);

		setBlockState(world, oakFence, 4, 3 + height, 11, sbb);
		setBlockState(world, oakFence, 6, 4 + height, 11, sbb);
		setBlockState(world, oakFence, 8, 5 + height, 11, sbb);
		setBlockState(world, oakFence, 10, 6 + height, 11, sbb);
		setBlockState(world, oakFence, 11, 7 + height, 11, sbb);

		this.setCoordBaseMode(temp);
	}


	/**
	 * Makes paintings of the minimum size or larger on the specified wall
	 */
	protected void generatePaintingsOnWall(World world, Random rand, int howMany, int floorLevel, EnumFacing direction, int minSize, StructureBoundingBox sbb) {
		for (int i = 0; i < howMany; i++) {
			// get some random coordinates on the wall in the chunk
			BlockPos pCoords = getRandomWallSpot(rand, floorLevel, direction, sbb);

			// initialize a painting object
			EnumArt art = getPaintingOfSize(rand, minSize);
			EntityPainting painting = new EntityPainting(world, pCoords, direction);
			painting.art = art;
			painting.setPosition(pCoords.getX(), pCoords.getY(), pCoords.getZ()); // this is done to refresh the bounding box after changing the art

			// check if we can fit a painting there
			if (checkPainting(world, painting, sbb)) {
				// place the painting
				world.spawnEntity(painting);
			}
		}
	}

	/**
	 * At least one of the painting's parameters must be the specified size or greater
	 */
	protected EnumArt getPaintingOfSize(Random rand, int minSize) {
		ArrayList<EnumArt> valid = new ArrayList<EnumArt>();

		for (EnumArt art : EnumArt.values()) {
			if (art.sizeX >= minSize || art.sizeY >= minSize) {
				valid.add(art);
			}
		}

		if (valid.size() > 0) {
			return valid.get(rand.nextInt(valid.size()));
		} else {
			return null;
		}
	}

	/**
	 * This is similar to EntityPainting.isOnValidSurface, except that it does not check for a solid wall behind the painting.
	 */
	protected boolean checkPainting(World world, EntityPainting painting, StructureBoundingBox sbb) {

		if (painting == null) {
			return false;
		}

		final AxisAlignedBB largerBox = painting.getEntityBoundingBox();

		if (!world.getCollisionBoxes(painting, largerBox).isEmpty()) {
			return false;
		} else {
			List<Entity> collidingEntities = world.getEntitiesWithinAABBExcludingEntity(painting, largerBox);

			for (Entity entityOnList : collidingEntities) {
				if (entityOnList instanceof EntityHanging) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * This returns the real-world coordinates of a possible painting or torch spot on the specified wall of this tower.
	 */
	protected BlockPos getRandomWallSpot(Random rand, int floorLevel, EnumFacing direction, StructureBoundingBox sbb) {
		int minX = this.boundingBox.minX + 2;
		int maxX = this.boundingBox.maxX - 2;

		int minY = this.boundingBox.minY + floorLevel + 2;
		int maxY = this.boundingBox.maxY - 2;

		int minZ = this.boundingBox.minZ + 2;
		int maxZ = this.boundingBox.maxZ - 2;

		// constrain the paintings to one wall
		// these directions correspond to painting facing directions, not necessarily to the structure orienting directions
		if (direction == EnumFacing.SOUTH) {
			minZ = this.boundingBox.minZ;
			maxZ = this.boundingBox.minZ;
		}
		else if (direction == EnumFacing.WEST) {
			maxX = this.boundingBox.maxX;
			minX = this.boundingBox.maxX;
		}
		else if (direction == EnumFacing.NORTH) {
			maxZ = this.boundingBox.maxZ;
			minZ = this.boundingBox.maxZ;
		}
		else if (direction == EnumFacing.EAST) {
			minX = this.boundingBox.minX;
			maxX = this.boundingBox.minX;
		}

		// try 30 times to get a proper result
		for (int i = 0; i < 30; i++) {
			int cx = minX + (maxX > minX ? rand.nextInt(maxX - minX) : 0);
			int cy = minY + (maxY > minY ? rand.nextInt(maxY - minY) : 0);
			int cz = minZ + (maxZ > minZ ? rand.nextInt(maxZ - minZ) : 0);

			final BlockPos blockPos = new BlockPos(cx, cy, cz).offset(direction);
			if (sbb.isVecInside(blockPos)) {
				return blockPos;
			}
		}

		// I guess we didn't get one
		//TwilightForestMod.LOGGER.info("getRandomWallSpot - We didn't find a valid random spot on the wall.");
		return null;
	}

	/**
	 * This method is for final castle towers actually.
	 * We need to break up this class into a more abstract tower class and a concrete lich tower class one day
	 */
	protected void makeGlyphBranches(World world, Random rand, EnumDyeColor colour, StructureBoundingBox sbb) {
		// pick a random side of the tower
		Rotation rotation = RotationUtil.ROTATIONS[rand.nextInt(4)];

		// start somewhere in the lower part
		int startHeight = rand.nextInt((int) (this.height * 0.66F));

		// near the middle
		int startZ = 3 + rand.nextInt(this.size - 6);

		final IBlockState magicBlock = TFBlocks.castle_rune_brick.getDefaultState().withProperty(COLOR, colour);

		// make a line all the way down to the foundation
		int dx = this.getXWithOffsetRotated(0, startZ, rotation);
		int dz = this.getZWithOffsetRotated(0, startZ, rotation);
		if (sbb.isVecInside(new BlockPos(dx, this.boundingBox.minY + 1, dz))) {
			for (int dy = this.getYWithOffset(startHeight); dy > 0; dy--) {
				final BlockPos pos = new BlockPos(dx, dy, dz);
				if (world.getBlockState(pos).getBlock() == TFBlocks.castle_brick) {
					world.setBlockState(pos, magicBlock, 2);
				} else {
					break;
				}
			}
		}

		// go left a little
		int leftOffset = startZ - (1 + rand.nextInt(3));
		int leftHeight = rand.nextInt(this.height - startHeight);
		if (leftOffset >= 0) {
			for (int z = startZ; z > leftOffset; z--) {
				this.setBlockStateRotated(world, magicBlock, 0, startHeight, z, rotation, sbb);
			}
			for (int y = startHeight; y < (startHeight + leftHeight); y++) {
				this.setBlockStateRotated(world, magicBlock, 0, y, leftOffset, rotation, sbb);
			}

		}

		// go right a little
		int rightOffset = startZ + (1 + rand.nextInt(3));
		int rightHeight = rand.nextInt(this.height - startHeight);
		if (rightOffset < this.size - 1) {
			for (int z = startZ; z < rightOffset; z++) {
				this.setBlockStateRotated(world, magicBlock, 0, startHeight, z, rotation, sbb);
			}
			for (int y = startHeight; y < (startHeight + rightHeight); y++) {
				this.setBlockStateRotated(world, magicBlock, 0, y, rightOffset, rotation, sbb);
			}

		}
	}

}
