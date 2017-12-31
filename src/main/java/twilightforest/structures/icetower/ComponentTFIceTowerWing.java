package twilightforest.structures.icetower;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFIceTowerWing extends ComponentTFTowerWing {

	protected static final int SIZE = 11;
	private static final int RANGE = (int) (SIZE * 1.6F);

	boolean hasBase = false;
	protected int treasureFloor = -1;

	public ComponentTFIceTowerWing() {
		super();
	}

	protected ComponentTFIceTowerWing(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setBoolean("hasBase", this.hasBase);
		tagCompound.setInteger("treasureFloor", this.treasureFloor);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.hasBase = tagCompound.getBoolean("hasBase");
		this.treasureFloor = tagCompound.getInteger("treasureFloor");
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// should we build a base
		this.hasBase = this.shouldHaveBase(rand);

		// limit sprawl to a reasonable amount
		if (this.getComponentType() < 5) {

			Rotation dirOffset = RotationUtil.ROTATIONS[rand.nextInt(RotationUtil.ROTATIONS.length)];

			// make sub towers
			for (final Rotation rotation : RotationUtil.ROTATIONS) {

				Rotation dir = dirOffset.add(rotation);

//				if (rand.nextInt(6) < this.getComponentType())
//				{
//					continue;
//				}

				int[] dest = getValidOpening(rand, dir);

				if (this.getComponentType() == 4 && (parent instanceof ComponentTFIceTowerMain) && !((ComponentTFIceTowerMain) parent).hasBossWing) {
					boolean hasBoss = makeBossTowerWing(list, rand, this.getComponentType() + 1, dest[0], dest[1], dest[2], 15, 41, dir);
					((ComponentTFIceTowerMain) parent).hasBossWing = hasBoss;
				} else {
					int childHeight = (rand.nextInt(2) + rand.nextInt(2) + 2) * 10 + 1;
					makeTowerWing(list, rand, this.getComponentType() + 1, dest[0], dest[1], dest[2], SIZE, childHeight, dir);
				}
			}
		}

		// figure out where the treasure goes
		int floors = this.height / 10;

		// set treasure to a floor if we need to
		if (this.treasureFloor == -1 && floors > 1) {
			this.treasureFloor = rand.nextInt(floors - 1);
		}

		// add a roof?
		makeARoof(parent, list, rand);

		// beard?
		if (!this.hasBase) {
			makeABeard(parent, list, rand);
		}

	}

	protected boolean shouldHaveBase(Random rand) {
		return this.getComponentType() == 0 || rand.nextBoolean();
	}

	/**
	 * Have we strayed more than range blocks away from the center?
	 */
	private boolean isOutOfRange(StructureComponent parent, int nx, int ny, int nz, int range) {
		final StructureBoundingBox sbb = parent.getBoundingBox();
		final int centerX = sbb.minX + (sbb.maxX - sbb.minX + 1) / 2;
		final int centerZ = sbb.minZ + (sbb.maxZ - sbb.minZ + 1) / 2;

		return Math.abs(nx - centerX) > range
				|| Math.abs(nz - centerZ) > range;
	}

	/**
	 * Make a new wing
	 */
	@Override
	public boolean makeTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		// stop if out of range
		if (isOutOfRange((StructureComponent) list.get(0), dx[0], dx[1], dx[2], RANGE)) {
			return false;
		}

		ComponentTFIceTowerWing wing = new ComponentTFIceTowerWing(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.add(wing);
			wing.buildComponent(list.get(0), list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Make a new wing
	 */
	public boolean makeBossTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {

		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		ComponentTFIceTowerWing wing = new ComponentTFIceTowerBossWing(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.add(wing);
			wing.buildComponent(list.get(0), list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}

	}


	/**
	 * Gets a Y value where the stairs meet the specified X coordinate.
	 * Also works for Z coordinates.
	 */
	@Override
	protected int getYByStairs(int rx, Random rand, Rotation direction) {

		int floors = this.height / 10;

		return 11 + (rand.nextInt(floors - 1) * 10);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// make walls
		//fillWithMetadataBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, deco.blockID, deco.blockMeta, AIR, false);
		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, false, rand, deco.randomBlocks);

		// clear inside
		fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		if (this.hasBase) {
			// deco to ground
			for (int x = 0; x < this.size; x++) {
				for (int z = 0; z < this.size; z++) {
					this.replaceAirAndLiquidDownwards(world, deco.blockState, x, -1, z, sbb);
				}
			}
		}

		// nullify sky light
		this.nullifySkyLightForBoundingBox(world);

		// make floors
		makeFloorsForTower(world, decoRNG, sbb);

		// openings
		makeOpenings(world, sbb);


		return true;
	}

	/**
	 * Nullify all the sky light in this component bounding box
	 */
	@Override
	public void nullifySkyLightForBoundingBox(World world) {
		this.nullifySkyLight(world, boundingBox.minX + 1, boundingBox.minY + 1, boundingBox.minZ + 1, boundingBox.maxX - 1, boundingBox.maxY - 1, boundingBox.maxZ - 1);
	}

	protected void makeFloorsForTower(World world, Random decoRNG, StructureBoundingBox sbb) {
		int floors = this.height / 10;

		Rotation ladderDir = Rotation.COUNTERCLOCKWISE_90;
		Rotation downLadderDir = null;

		// divide the tower into floors
		int floorHeight = 10;
		for (int i = 0; i < floors - 1; i++) {
			// put down a ceiling
			placeFloor(world, decoRNG, sbb, floorHeight, i);

			downLadderDir = ladderDir.add(Rotation.CLOCKWISE_90);
			decorateFloor(world, decoRNG, i, (i * floorHeight), (i * floorHeight) + floorHeight, ladderDir, downLadderDir, sbb);

		}

		int topFloor = floors - 1;
		decorateTopFloor(world, decoRNG, topFloor, (topFloor * floorHeight), (topFloor * floorHeight) + floorHeight, ladderDir, downLadderDir, sbb);

	}

	/**
	 * Put down planks or whatevs for a floor
	 */
	protected void placeFloor(World world, Random rand, StructureBoundingBox sbb, int floorHeight, int floor) {
		for (int x = 1; x < size - 1; x++) {
			for (int z = 1; z < size - 1; z++) {
				setBlockState(world, deco.floorState, x, (floor * floorHeight) + floorHeight, z, sbb);
			}
		}
	}

	/**
	 * Make an opening in this tower for a door.  This now only makes one opening, so you need two
	 */
	@Override
	protected void makeDoorOpening(World world, int dx, int dy, int dz, StructureBoundingBox sbb) {
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
	protected void decorateFloor(World world, Random rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		boolean hasTreasure = (this.treasureFloor == floor);

		switch (rand.nextInt(8)) {
			case 0:
				//this.fillBlocksRotated(world, sbb, 9, bottom + 5, 1, 10, top + 1, 7, Blocks.WOOL, ladderUpDir, ladderUpDir);
				if (isNoDoorAreaRotated(9, bottom + 5, 1, 10, top + 1, 7, ladderUpDir)) {
					decorateWraparoundWallSteps(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
					break;
				} // fall through otherwise
			case 1:
				//this.fillBlocksRotated(world, sbb, 7, bottom, 0, 10, top + 1, 10, Blocks.WOOL, ladderUpDir, ladderUpDir);
				if (isNoDoorAreaRotated(7, bottom, 0, 10, top + 1, 10, ladderUpDir)) {
					decorateFarWallSteps(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
					break;
				} // fall through otherwise
			case 2:
				if (isNoDoorAreaRotated(9, bottom + 5, 1, 10, top + 1, 7, ladderUpDir)) {
					decorateWraparoundWallStepsPillars(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
					break;
				} // fall through otherwise
			case 3:
				decoratePlatform(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case 4:
				decoratePillarParkour(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case 5:
				decoratePillarPlatforms(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case 6:
				decoratePillarPlatformsOutside(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
			case 7:
			default:
				decorateQuadPillarStairs(world, rand, bottom, top, ladderUpDir, ladderDownDir, hasTreasure, sbb);
				break;
		}

	}

	private boolean isNoDoorAreaRotated(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Rotation rotation) {
		boolean isClear = true;
		// make a bounding box of the area
		StructureBoundingBox exclusionBox;
		switch (rotation) {
			case NONE:
			default:
				exclusionBox = new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
				break;
			case CLOCKWISE_90:
				exclusionBox = new StructureBoundingBox(this.size - 1 - maxZ, minY, minX, this.size - 1 - minZ, maxY, maxX);
				break;
			case CLOCKWISE_180:
				exclusionBox = new StructureBoundingBox(this.size - 1 - maxX, minY, this.size - 1 - maxZ, this.size - 1 - minX, maxY, this.size - 1 - minZ);
				break;
			case COUNTERCLOCKWISE_90:
				exclusionBox = new StructureBoundingBox(minZ, minY, this.size - 1 - maxX, maxZ, maxY, this.size - 1 - minX);
				break;
		}

		for (BlockPos door : this.openings) {
			if (exclusionBox.isVecInside(door)) {
				isClear = false;
			}
		}

		return isClear;
	}

	protected void decorateTopFloor(World world, Random rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, StructureBoundingBox sbb) {
		if (rand.nextBoolean()) {
			decoratePillarsCorners(world, rand, bottom, top, ladderDownDir, sbb);
		} else {
			decoratePillarsGrid(world, rand, bottom, top, ladderDownDir, sbb);
		}

		// generate treasure last so it doesn't get deleted
		if (this.isDeadEnd()) {
			decorateTopFloorTreasure(world, rand, bottom, top, ladderDownDir, sbb);
		}

	}

	private void decorateTopFloorTreasure(World world, Random rand, int bottom, int top, Rotation rotation, StructureBoundingBox sbb) {
		this.fillBlocksRotated(world, sbb, 5, bottom + 1, 5, 5, bottom + 4, 5, deco.pillarState, rotation);

		this.placeTreasureAtCurrentPosition(world, null, 5, bottom + 5, 5, TFTreasure.aurora_room, sbb);
	}

	private void decoratePillars(World world, Random rand, int bottom, int top, Rotation rotation, StructureBoundingBox sbb) {
		this.fillBlocksRotated(world, sbb, 3, bottom + 1, 3, 3, top - 1, 3, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 1, 3, 7, top - 1, 3, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 1, 7, 3, top - 1, 7, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 1, 7, 7, top - 1, 7, deco.pillarState, rotation);
	}

	private void decoratePillarsGrid(World world, Random rand, int bottom, int top, Rotation rotation, StructureBoundingBox sbb) {
		//int beamMetaNS = ((this.coordBaseMode + rotation) % 2 == 0) ? 4 : 8;
		//int beamMetaEW = (beamMetaNS == 4) ? 8 : 4;
		final IBlockState pillarEW = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
		final IBlockState pillarNS = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);

		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 9, bottom + 5, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 7, 9, bottom + 5, 7, pillarNS, rotation);

		this.decoratePillars(world, rand, bottom, top, rotation, sbb);
	}

	private void decoratePillarsCorners(World world, Random rand, int bottom, int top, Rotation rotation, StructureBoundingBox sbb) {
		//int beamMetaNS = ((this.coordBaseMode + rotation) % 2 == 0) ? 4 : 8;
		//int beamMetaEW = (beamMetaNS == 4) ? 8 : 4;
		final IBlockState pillarEW = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
		final IBlockState pillarNS = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);

		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 9, bottom + 5, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 7, 9, bottom + 5, 7, pillarNS, rotation);

		this.fillAirRotated(world, sbb, 3, bottom + 5, 3, 7, bottom + 5, 7, rotation);

		this.decoratePillars(world, rand, bottom, top, rotation, sbb);
	}

	private void decorateFarWallSteps(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {

		// far set of stairs
		for (int z = 1; z < 10; z++) {
			int y = bottom + 10 - (z / 2);
			this.setBlockStateRotated(world, (z % 2 == 0) ? deco.pillarState : deco.platformState, 9, y, z, ladderUpDir, sbb);
			for (int by = bottom + 1; by < y; by++) {
				this.setBlockStateRotated(world, deco.pillarState, 9, by, z, ladderUpDir, sbb);
			}
		}

		// near set of stairs
		for (int z = 1; z < 10; z++) {
			int y = bottom + 1 + (z / 2);
			this.setBlockStateRotated(world, (z % 2 == 0) ? deco.platformState : deco.pillarState, 8, y, z, ladderUpDir, sbb);
			for (int by = bottom + 1; by < y; by++) {
				this.setBlockStateRotated(world, deco.pillarState, 8, by, z, ladderUpDir, sbb);
			}
		}

		// entry stair
		this.setBlockStateRotated(world, deco.platformState, 7, bottom + 1, 1, ladderUpDir, sbb);


		// clear floor above
		for (int z = 2; z < 7; z++) {
			this.setBlockStateRotated(world, AIR, 9, top, z, ladderUpDir, sbb);
		}

		final IBlockState pillarNS = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 1, bottom + 8, 5, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
			//int beamMetaNS = ((this.coordBaseMode + ladderUpDir) % 2 == 0) ? 4 : 8;
			this.setBlockStateRotated(world, pillarNS, 1, bottom + 7, 5, ladderUpDir, sbb);
		}
	}

	private void decorateWraparoundWallSteps(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		IBlockState topPlatform = deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
		IBlockState bottomPlatform = deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);

		// far set of stairs
		for (int z = 1; z < 10; z++) {
			int y = bottom + 10 - (z / 2);
			this.setBlockStateRotated(world, ((z % 2 == 0) ? topPlatform : bottomPlatform), 9, y, z, ladderUpDir, sbb);
		}

		// right set of stairs
		for (int x = 1; x < 9; x++) {
			int y = bottom + 2 + ((x - 1) / 2);
			this.setBlockStateRotated(world, ((x % 2 == 0) ? topPlatform : bottomPlatform), x, y, 9, ladderUpDir, sbb);
		}

		// entry stairs
		this.setBlockStateRotated(world, topPlatform, 1, bottom + 1, 8, ladderUpDir, sbb);
		this.setBlockStateRotated(world, deco.platformState, 1, bottom + 1, 7, ladderUpDir, sbb);


		// clear floor above
		for (int z = 2; z < 7; z++) {
			this.setBlockStateRotated(world, AIR, 9, top, z, ladderUpDir, sbb);
		}

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 1, bottom + 5, 5, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
			//int beamMetaNS = ((this.coordBaseMode + ladderUpDir) % 2 == 0) ? 4 : 8;
			final IBlockState pillarNS = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
			this.setBlockStateRotated(world, pillarNS, 1, bottom + 4, 5, ladderUpDir, sbb);
		}
	}

	private void decorateWraparoundWallStepsPillars(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		Rotation rotation = ladderDownDir;
		//int beamMetaNS = ((this.coordBaseMode + rotation) % 2 == 0) ? 4 : 8;
		//int beamMetaEW = (beamMetaNS == 4) ? 8 : 4;
		final IBlockState pillarEW = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
		final IBlockState pillarNS = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);

		this.decorateWraparoundWallSteps(world, rand, bottom, top, ladderUpDir, ladderDownDir, false, sbb);
		this.decoratePillars(world, rand, bottom, top, rotation, sbb);

		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 5, 3, 9, bottom + 5, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 5, 7, 9, bottom + 5, 7, pillarNS, rotation);

		this.fillBlocksRotated(world, sbb, 1, bottom + 2, 3, 2, bottom + 2, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 6, 3, 2, bottom + 6, 3, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 4, 7, 2, bottom + 4, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 8, 7, 2, bottom + 8, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 6, 8, 3, bottom + 6, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 8, 8, 7, bottom + 8, 9, pillarEW, rotation);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 7, bottom + 6, 1, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
		}
	}

	private void decoratePlatform(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		IBlockState topPlatform = deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
		IBlockState bottomPlatform = deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);

		this.decoratePillars(world, rand, bottom, top, ladderDownDir, sbb);
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 3, 7, bottom + 5, 7, deco.floorState, ladderDownDir);

		// one flight
		for (int z = 6; z < 10; z++) {
			int y = bottom - 2 + (z / 2);
			this.setBlockStateRotated(world, ((z % 2 == 1) ? topPlatform : bottomPlatform), 1, y, z, ladderDownDir, sbb);
		}
		// two flight
		for (int x = 2; x < 6; x++) {
			int y = bottom + 2 + (x / 2);
			this.setBlockStateRotated(world, ((x % 2 == 1) ? topPlatform : bottomPlatform), x, y, 9, ladderDownDir, sbb);
		}
		// connector
		this.setBlockStateRotated(world, deco.platformState, 5, bottom + 5, 8, ladderDownDir, sbb);

		// connector
		this.setBlockStateRotated(world, deco.platformState, 5, bottom + 6, 2, ladderUpDir, sbb);
		// two flight
		for (int x = 5; x < 10; x++) {
			int y = bottom + 4 + (x / 2);
			this.setBlockStateRotated(world, ((x % 2 == 1) ? topPlatform : bottomPlatform), x, y, 1, ladderUpDir, sbb);
			if (x > 6) {
				this.setBlockStateRotated(world, AIR, x, top, 1, ladderUpDir, sbb);
			}
		}
		// one flight
		for (int z = 2; z < 5; z++) {
			int y = bottom + 8 + (z / 2);
			this.setBlockStateRotated(world, AIR, 9, top, z, ladderUpDir, sbb);
			this.setBlockStateRotated(world, ((z % 2 == 1) ? topPlatform : bottomPlatform), 9, y, z, ladderUpDir, sbb);
		}

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 3, bottom + 6, 5, ladderDownDir, TFTreasure.aurora_cache, false, sbb);
		}
	}

	private void decorateQuadPillarStairs(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		this.decoratePillars(world, rand, bottom, top, ladderDownDir, sbb);

		IBlockState topPlatform = deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
		IBlockState bottomPlatform = deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);

		// one flight
		for (int z = 6; z < 9; z++) {
			int y = bottom - 2 + (z / 2);
			this.setBlockStateRotated(world, z % 2 == 1 ? topPlatform : bottomPlatform, 2, y, z, ladderDownDir, sbb);
		}
		// two flight
		for (int x = 3; x < 9; x++) {
			int y = bottom + 1 + (x / 2);
			this.setBlockStateRotated(world, x % 2 == 1 ? topPlatform : bottomPlatform, x, y, 8, ladderDownDir, sbb);
		}
		// three flight
		for (int z = 7; z > 1; z--) {
			int y = top - 2 - ((z - 1) / 2);
			if (z < 4) {
				this.setBlockStateRotated(world, AIR, 8, top, z, ladderDownDir, sbb);
			}
			this.setBlockStateRotated(world, ((z % 2 == 1) ? topPlatform : bottomPlatform), 8, y, z, ladderDownDir, sbb);
		}
		// last flight
		for (int x = 7; x > 3; x--) {
			int y = top + 1 - ((x - 1) / 2);
			this.setBlockStateRotated(world, AIR, x, top, 2, ladderDownDir, sbb);
			this.setBlockStateRotated(world, ((x % 2 == 1) ? topPlatform : bottomPlatform), x, y, 2, ladderDownDir, sbb);
		}

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 3, bottom + 7, 7, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
		}
	}

	private void decoratePillarPlatforms(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		// platforms
		Rotation r = ladderUpDir;
		for (int i = 1; i < 10; i++) {
			r = r.add(Rotation.CLOCKWISE_90);
			this.fillBlocksRotated(world, sbb, 2, bottom + i, 2, 4, bottom + i, 4, deco.floorState, rotation);
		}

		// clear
		this.fillAirRotated(world, sbb, 2, top, 2, 8, top, 4, ladderUpDir);
		this.fillAirRotated(world, sbb, 2, top, 2, 4, top, 6, ladderUpDir);

		// extra pillar tops
		this.setBlockStateRotated(world, deco.platformState, 7, top, 3, ladderUpDir, sbb);
		this.setBlockStateRotated(world, deco.platformState, 3, top, 3, ladderUpDir, sbb);

		this.decoratePillars(world, rand, bottom, top, ladderUpDir, sbb);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 3, bottom + 5, 2, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
		}
	}

	private void decoratePillarPlatformsOutside(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		// platforms
		for (int i = 0; i < 2; i++) {
			for (Rotation r : RotationUtil.ROTATIONS) {
				if (i == 0 && r == Rotation.NONE) continue;
				Rotation rotation = ladderUpDir.add(r);
				this.fillBlocksRotated(world, sbb, 1, bottom + i, 1, 3, bottom + i, 3, deco.platformState, rotation);
				this.fillBlocksRotated(world, sbb, 4, bottom + i, 1, 6, bottom + i, 3, deco.floorState, rotation);
			}
		}

		// stairs
		Rotation rotation = ladderUpDir.add(Rotation.CLOCKWISE_180);

		this.fillAirRotated(world, sbb, 5, top, 8, 9, top, 9, rotation);
		this.fillAirRotated(world, sbb, 8, top, 6, 9, top, 9, rotation);

		this.fillBlocksRotated(world, sbb, 8, top - 2, 7, 9, top - 2, 7, deco.platformState, rotation);
		this.fillBlocksRotated(world, sbb, 8, top - 2, 8, 9, top - 2, 9, deco.floorState, rotation);
		this.fillBlocksRotated(world, sbb, 7, top - 1, 8, 7, top - 1, 9, deco.platformState, rotation);
		this.fillBlocksRotated(world, sbb, 6, top - 1, 8, 6, top - 1, 9, deco.platformState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), rotation);
		this.fillBlocksRotated(world, sbb, 5, top - 0, 8, 5, top - 0, 9, deco.platformState, rotation);

		this.decoratePillars(world, rand, bottom, top, ladderUpDir, sbb);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 3, bottom + 5, 2, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
		}
	}


	private void decoratePillarParkour(World world, Random rand, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, boolean hasTreasure, StructureBoundingBox sbb) {
		Rotation rotation = ladderDownDir;
		//int beamMetaNS = ((this.coordBaseMode + rotation) % 2 == 0) ? 4 : 8;
		//int beamMetaEW = (beamMetaNS == 4) ? 8 : 4;

		final IBlockState pillarEW = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
		final IBlockState pillarNS = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);

		// 4 pillars
		this.decoratePillars(world, rand, bottom, top, rotation, sbb);

		// center pillar
		this.setBlockStateRotated(world, deco.pillarState, 5, bottom + 1, 5, rotation, sbb);

		// pillar 2
		this.fillBlocksRotated(world, sbb, 5, bottom + 2, 7, 5, bottom + 2, 9, pillarEW, rotation);

		// gap 3
		this.fillBlocksRotated(world, sbb, 1, bottom + 3, 7, 2, bottom + 3, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 3, 8, 3, bottom + 3, 9, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 7, 7, 2, bottom + 7, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 7, 8, 3, bottom + 7, 9, pillarEW, rotation);
		this.fillAirRotated(world, sbb, 3, bottom + 4, 7, 3, bottom + 6, 7, rotation);

		// pillar 4
		this.fillBlocksRotated(world, sbb, 1, bottom + 4, 5, 2, bottom + 4, 5, pillarNS, rotation);

		// gap 5
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 2, bottom + 5, 3, pillarNS, rotation);
		this.fillAirRotated(world, sbb, 3, bottom + 6, 3, 3, bottom + 8, 3, rotation);

		// pillar 6
		this.fillBlocksRotated(world, sbb, 5, bottom + 6, 1, 5, bottom + 6, 2, pillarEW, rotation);

		// gap 7
		this.fillAirRotated(world, sbb, 7, bottom + 8, 3, 7, bottom + 10, 3, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 7, 1, 7, bottom + 7, 2, pillarEW, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 7, 3, 9, bottom + 7, 3, pillarNS, rotation);

		// pillar 8
		this.fillBlocksRotated(world, sbb, 8, bottom + 8, 5, 9, bottom + 8, 5, pillarNS, rotation);

		// gap 9 (no gap?)
		//this.fillAirRotated(world, sbb, 7, bottom + 10, 7, 7, bottom + 10, 7, rotation);
		this.fillBlocksRotated(world, sbb, 8, bottom + 9, 7, 9, bottom + 9, 7, pillarNS, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 9, 8, 7, bottom + 9, 9, pillarEW, rotation);

		// holes in ceiling
		this.fillAirRotated(world, sbb, 2, top, 2, 8, top, 4, ladderUpDir);
		this.fillAirRotated(world, sbb, 2, top, 2, 4, top, 6, ladderUpDir);

		// treasure!
		if (hasTreasure) {
			this.placeTreasureRotated(world, 8, bottom + 8, 7, ladderUpDir, TFTreasure.aurora_cache, false, sbb);
		}
	}

	/**
	 * Attach a roof to this tower.
	 * <p>
	 * This function keeps trying roofs starting with the largest and fanciest, and then keeps trying smaller and plainer ones
	 */
	@Override
	public void makeARoof(StructureComponent parent, List<StructureComponent> list, Random rand) {
		int index = this.getComponentType();
		tryToFitRoof(list, rand, new ComponentTFIceTowerRoof(getFeatureType(), index + 1, this));
	}

	/**
	 * Add a beard to this structure.  There is only one type of beard.
	 */
	@Override
	public void makeABeard(StructureComponent parent, List<StructureComponent> list, Random rand) {
		int index = this.getComponentType();
		ComponentTFIceTowerBeard beard;
		beard = new ComponentTFIceTowerBeard(getFeatureType(), index + 1, this);
		list.add(beard);
		beard.buildComponent(this, list, rand);
	}

}
