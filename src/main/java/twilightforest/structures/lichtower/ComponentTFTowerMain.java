package twilightforest.structures.lichtower;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;
import twilightforest.util.TFEntityNames;
import twilightforest.util.VanillaEntityNames;

import java.util.List;
import java.util.Random;


public class ComponentTFTowerMain extends ComponentTFTowerWing {

	public ComponentTFTowerMain() {
		super();
	}

	public ComponentTFTowerMain(TFFeature feature, World world, Random rand, int index, int x, int y, int z) {
		// some of these are subject to change if the ground level is > 30.
		super(feature, index, x, y, z, 15, 55 + rand.nextInt(32), EnumFacing.SOUTH);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
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
	public int[] getOutbuildingOpening(Random rand, Rotation rotation) {

		int rx = 0;
		int ry = 1;
		int rz = 0;

		switch (rotation) {
			case NONE:
				// for directions 0 or 2, the wall lies along the z axis
				rx = size - 1;
				rz = 6 + rand.nextInt(8);
				break;
			case CLOCKWISE_90:
				// for directions 1 or 3, the wall lies along the x axis
				rx = 1 + rand.nextInt(11);
				rz = size - 1;
				break;
			case CLOCKWISE_180:
				rx = 0;
				rz = 1 + rand.nextInt(8);
				break;
			case COUNTERCLOCKWISE_90:
				rx = 3 + rand.nextInt(11);
				rz = 0;
				break;
		}

		return new int[]{rx, ry, rz};
	}


	public boolean makeTowerOutbuilding(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		EnumFacing direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);
		ComponentTFTowerOutbuilding outbuilding = new ComponentTFTowerOutbuilding(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, outbuilding.getBoundingBox());
		if (intersect == null) {
			list.add(outbuilding);
			outbuilding.buildComponent(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// make walls
		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, false, rand, StructureTFComponentOld.getStrongholdStones());

		// clear inside
		fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);
		final IBlockState defaultState = Blocks.COBBLESTONE.getDefaultState();
		// stone to ground
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				this.replaceAirAndLiquidDownwards(world, defaultState, x, -1, z, sbb);
			}

		}

		// nullify sky light
		nullifySkyLightForBoundingBox(world);

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
		decorateStairFloor(world, rand, sbb);

		// stairway crossings
		makeStairwayCrossings(world, rand, sbb);

		// LICH TIME
		makeLichRoom(world, rand, sbb);

		// extra paintings in main tower
		makeTowerPaintings(world, rand, sbb);


		return true;
	}

	/**
	 * Make 1-2 platforms joining the stairways
	 */
	protected void makeStairwayCrossings(World world, Random rand, StructureBoundingBox sbb) {
		int flights = (this.highestOpening / 5) - 2;

		for (int i = 2 + rand.nextInt(2); i < flights; i += 1 + rand.nextInt(5)) {
			makeStairCrossing(world, rand, i, sbb);
		}
	}

	protected void makeStairCrossing(World world, Random rand, int flight, StructureBoundingBox sbb) {
		EnumFacing temp = this.getCoordBaseMode();
		if (flight % 2 == 0) {
			this.setCoordBaseMode(getStructureRelativeRotation(Rotation.CLOCKWISE_90));
		}

		// place platform
		int floorLevel = 0 + flight * 5;
		IBlockState crossingfloor = rand.nextBoolean() ? Blocks.DOUBLE_STONE_SLAB.getDefaultState() : Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH);
		for (int dx = 6; dx <= 8; dx++) {
			for (int dz = 4; dz <= 10; dz++) {
				setBlockState(world, crossingfloor, dx, floorLevel, dz, sbb);
			}
		}

		// put fences
		floorLevel++;
		int dx = 6;
		for (int dz = 3; dz <= 11; dz++) {
			setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), dx, floorLevel, dz, sbb);
		}
		dx++;
		for (int dz = 3; dz <= 11; dz++) {
			setBlockState(world, AIR, dx, floorLevel, dz, sbb);
		}
		dx++;
		for (int dz = 3; dz <= 11; dz++) {
			setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), dx, floorLevel, dz, sbb);
		}

		// we need 2 extra blocks and 2 extra fences to look good
		setBlockState(world, crossingfloor, 6, floorLevel - 1, 11, sbb);
		setBlockState(world, crossingfloor, 8, floorLevel - 1, 3, sbb);

		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 5, floorLevel, 11, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 9, floorLevel, 3, sbb);

		// place spawner in the middle
		ResourceLocation mobID = VanillaEntityNames.SKELETON;
		switch (rand.nextInt(4)) {
			case 0:
			case 1:
				mobID = VanillaEntityNames.SKELETON;
				break;
			case 2:
				mobID = VanillaEntityNames.ZOMBIE;
				break;
			case 3:
				mobID = TFEntityNames.SWARM_SPIDER;
				break;
		}
		setSpawner(world, 7, floorLevel + 2, 7, sbb, mobID);

		// make a fence arch support for the spawner
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, floorLevel + 1, 7, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 8, floorLevel + 1, 7, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, floorLevel + 2, 7, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 8, floorLevel + 2, 7, sbb);


		this.setCoordBaseMode(temp);
	}

	/**
	 * Make a neat little room for the lich to fight in
	 */
	protected void makeLichRoom(World world, Random rand, StructureBoundingBox sbb) {
		// figure out where the stairs end
		int floorLevel = 2 + (this.highestOpening / 5) * 5;
		// we need a floor
		final Rotation i = (this.highestOpening / 5) % 2 == 0 ? Rotation.NONE : Rotation.CLOCKWISE_90;
		makeLichFloor(world, floorLevel, i, sbb);

		// now a chandelier
		decorateLichChandelier(world, floorLevel, sbb);

		// and wall torches
		decorateTorches(world, rand, floorLevel, sbb);

		// make paintings
		decoratePaintings(world, rand, floorLevel, sbb);

		// seems like we should have a spawner
		setBlockState(world, TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.LICH), size / 2, floorLevel + 2, size / 2, sbb);
	}


	protected void makeTowerPaintings(World world, Random rand, StructureBoundingBox sbb) {
		int howMany = 10;

		// do wall 0.
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.WEST, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.WEST, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.WEST, 0, sbb);

		// do wall 1.
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.EAST, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.EAST, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.EAST, 0, sbb);
		// do wall 2.
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.NORTH, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.NORTH, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.NORTH, 0, sbb);
		// do wall 3.
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.SOUTH, 48, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.SOUTH, 32, sbb);
		generatePaintingsOnWall(world, rand, howMany, 0, EnumFacing.SOUTH, 0, sbb);
	}

	/**
	 * Make the floor for the liches room
	 */
	protected void makeLichFloor(World world, int floorLevel, Rotation rotation, StructureBoundingBox sbb) {
		EnumFacing temp = this.getCoordBaseMode();
		this.setCoordBaseMode(getStructureRelativeRotation(rotation));

		BlockPlanks.EnumType birch = BlockPlanks.EnumType.BIRCH;
		IBlockState birchSlab = Blocks.WOODEN_SLAB.getDefaultState()
				.withProperty(BlockPlanks.VARIANT, birch)
				.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
		IBlockState birchPlank = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, birch);

		// place a platform there
		for (int fx = 1; fx < 14; fx++) {
			for (int fz = 1; fz < 14; fz++) {
				if ((fx == 1 || fx == 2) && (fz >= 6 && fz <= 12)) {
					// blank, leave room for stairs
					if (fz == 6) {
						// upside down plank slabs
						setBlockState(world, birchSlab, fx, floorLevel, fz, sbb);
					}
				} else if ((fx == 12 || fx == 13) && (fz >= 3 && fz <= 8)) {
					// blank, leave room for stairs
					if (fz == 8) {
						// upside down plank slabs
						setBlockState(world, birchSlab, fx, floorLevel, fz, sbb);
					}
				} else if ((fx >= 4 && fx <= 10) && (fz >= 4 && fz <= 10)) {
					// glass floor in center, aside from 2 corners
					if ((fx == 4 && fz == 4) || (fx == 10 && fz == 10)) {
						setBlockState(world, birchPlank, fx, floorLevel, fz, sbb);
					} else {
						setBlockState(world, Blocks.GLASS.getDefaultState(), fx, floorLevel, fz, sbb);
					}
				} else if ((fx == 2 || fx == 3) && (fz == 2 || fz == 3)) {
					// glass blocks in the corners
					setBlockState(world, Blocks.GLASS.getDefaultState(), fx, floorLevel, fz, sbb);
				} else if ((fx == 11 || fx == 12) && (fz == 11 || fz == 12)) {
					// glass blocks in the corners
					setBlockState(world, Blocks.GLASS.getDefaultState(), fx, floorLevel, fz, sbb);
				} else {
					setBlockState(world, birchPlank, fx, floorLevel, fz, sbb);
				}
			}
		}

		// eliminate the railings
		setBlockState(world, AIR, 3, floorLevel + 1, 11, sbb);
		setBlockState(world, AIR, 3, floorLevel + 1, 10, sbb);
		setBlockState(world, AIR, 3, floorLevel + 2, 11, sbb);

		setBlockState(world, AIR, 11, floorLevel + 1, 3, sbb);
		setBlockState(world, AIR, 11, floorLevel + 1, 4, sbb);
		setBlockState(world, AIR, 11, floorLevel + 2, 3, sbb);

		this.setCoordBaseMode(temp);

	}

	/**
	 * Make a fancy chandelier for the lich's room
	 */
	protected void decorateLichChandelier(World world, int floorLevel, StructureBoundingBox sbb) {
		int cx = size / 2;
		int cy = floorLevel + 4;
		int cz = size / 2;

		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 1, cy, cz + 0, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 2, cy, cz + 0, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 1, cy, cz + 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, cy, cz + 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, cy, cz + 2, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx - 1, cy, cz + 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx - 1, cy, cz + 0, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx - 2, cy, cz + 0, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx - 1, cy, cz - 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, cy, cz - 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, cy, cz - 2, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 1, cy, cz - 1, sbb);

		cy++;
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 1, cy, cz + 0, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 2, cy, cz + 0, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 1, cy, cz + 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, cy, cz + 1, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 0, cy, cz + 2, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx - 1, cy, cz + 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx - 1, cy, cz + 0, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx - 2, cy, cz + 0, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx - 1, cy, cz - 1, sbb);
		setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, cy, cz - 1, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 0, cy, cz - 2, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 1, cy, cz - 1, sbb);

		cy++;
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 1, cy, cz + 0, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 0, cy, cz + 1, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx - 1, cy, cz + 0, sbb);
		setBlockState(world, Blocks.TORCH.getDefaultState(), cx + 0, cy, cz - 1, sbb);

		for (int y = floorLevel + 5; y < height - 1; y++) {
			setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), cx + 0, y, cz + 0, sbb);
		}
	}

	/**
	 * Cover the walls in the lich's room with paintings.  How is this going to work, chunk by chunk?
	 */
	protected void decoratePaintings(World world, Random rand, int floorLevel, StructureBoundingBox sbb) {
		int howMany = 100;

		for (final EnumFacing horizontal : EnumFacing.HORIZONTALS) {
			// do wall 0.
			generatePaintingsOnWall(world, rand, howMany, floorLevel, horizontal, 48, sbb);
			generatePaintingsOnWall(world, rand, howMany, floorLevel, horizontal, 32, sbb);
			generatePaintingsOnWall(world, rand, howMany, floorLevel, horizontal, 0, sbb);
		}
	}

	/**
	 * Put torches on each wall
	 */
	protected void decorateTorches(World world, Random rand, int floorLevel, StructureBoundingBox sbb) {
		generateTorchesOnWall(world, rand, floorLevel, EnumFacing.SOUTH, sbb);
		generateTorchesOnWall(world, rand, floorLevel, EnumFacing.EAST, sbb);
		generateTorchesOnWall(world, rand, floorLevel, EnumFacing.NORTH, sbb);
		generateTorchesOnWall(world, rand, floorLevel, EnumFacing.WEST, sbb);
	}

	/**
	 * Place up to 5 torches (with fence holders) on the wall, checking that they don't overlap any paintings or other torches
	 */
	protected void generateTorchesOnWall(World world, Random rand,
										 int floorLevel, EnumFacing direction, StructureBoundingBox sbb) {
		for (int i = 0; i < 5; i++) {
			// get some random coordinates on the wall in the chunk
			BlockPos wCoords = getRandomWallSpot(rand, floorLevel, direction, sbb);

			// offset to see where the fence should be
			BlockPos.MutableBlockPos tCoords = new BlockPos.MutableBlockPos(wCoords);

			// is there a painting or another torch there?
			AxisAlignedBB torchBox = new AxisAlignedBB(tCoords.getX(), tCoords.getY(), tCoords.getZ(), tCoords.getX() + 1.0, tCoords.getY() + 2.0, tCoords.getZ() + 1.0);
			IBlockState blockState = world.getBlockState(tCoords);
			IBlockState aboveBlockState = world.getBlockState(tCoords.up());
			if (blockState.getMaterial() == Material.AIR &&
					aboveBlockState.getMaterial() == Material.AIR &&
					world.getEntitiesWithinAABBExcludingEntity(null, torchBox).size() == 0) {
				// if not, place a torch
				world.setBlockState(tCoords, Blocks.OAK_FENCE.getDefaultState(), 2);
				world.setBlockState(tCoords.up(), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP), 2);
			}
		}
	}


}
