package twilightforest.structures.trollcave;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
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
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;


public class ComponentTFTrollCaveConnect extends ComponentTFTrollCaveMain {

	protected boolean[] openingTowards = new boolean[]{false, false, true, false};

	public ComponentTFTrollCaveConnect() {
	}

	public ComponentTFTrollCaveConnect(TFFeature feature, int index, int x, int y, int z, int caveSize, int caveHeight, EnumFacing direction) {
		super(feature, index);
		this.size = caveSize;
		this.height = caveHeight;
		this.setCoordBaseMode(direction);
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setBoolean("openingTowards0", this.openingTowards[0]);
		tagCompound.setBoolean("openingTowards1", this.openingTowards[1]);
		tagCompound.setBoolean("openingTowards2", this.openingTowards[2]);
		tagCompound.setBoolean("openingTowards3", this.openingTowards[3]);

	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);

		// too lazy to do this as a loop
		this.openingTowards[0] = tagCompound.getBoolean("openingTowards0");
		this.openingTowards[1] = tagCompound.getBoolean("openingTowards1");
		this.openingTowards[2] = tagCompound.getBoolean("openingTowards2");
		this.openingTowards[3] = tagCompound.getBoolean("openingTowards3");
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		// make 4 caves
		if (this.getComponentType() < 3) {

			for (final Rotation rotation : RotationUtil.ROTATIONS) {
				BlockPos dest = getValidOpening(rand, 2, rotation);

				if (rand.nextBoolean() || !makeGardenCave(list, rand, this.getComponentType() + 1, dest.getX(), dest.getY(), dest.getZ(), 30, 15, rotation)) {
					makeSmallerCave(list, rand, this.getComponentType() + 1, dest.getX(), dest.getY(), dest.getZ(), 20, 15, rotation);
				}

			}
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		if (this.isBoundingBoxOutOfHighlands(world, sbb)) {
			return false;
		} else {
			// clear inside
			hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);

			Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

			// wall decorations
			for (Rotation rotation : RotationUtil.ROTATIONS) {
				if (!this.openingTowards[rotation.ordinal()]) {
					decorateWall(world, sbb, decoRNG, rotation);
				}
			}

			decoRNG.setSeed(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));
			// stone stalactites!
			for (int i = 0; i < 32; i++) {
				BlockPos dest = getCoordsInCave(decoRNG);
				generateBlockStalactite(world, decoRNG, Blocks.STONE, 0.5F, true, dest.getX(), 3, dest.getZ(), sbb);
			}
			// stone stalagmites!
			for (int i = 0; i < 8; i++) {
				BlockPos dest = getCoordsInCave(decoRNG);
				generateBlockStalactite(world, decoRNG, Blocks.STONE, 0.5F, false, dest.getX(), 3, dest.getZ(), sbb);
			}

			// possible treasure
			decoRNG.setSeed(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));
			if (this.countExits() == 1 && decoRNG.nextInt(3) == 0) {
				// treasure!
				makeTreasureCrate(world, decoRNG, sbb);
			} else if (decoRNG.nextInt(3) == 0) {
				// or a monolith!
				makeMonolith(world, decoRNG, sbb);
			}

			return true;
		}
	}

	protected void makeMonolith(World world, Random rand, StructureBoundingBox sbb) {
		// monolith
		int mid = this.size / 2;
		int height = 7 + rand.nextInt(8);
		Rotation rotation = RotationUtil.ROTATIONS[rand.nextInt(4)];

		this.fillBlocksRotated(world, sbb, mid - 1, 0, mid - 1, mid - 1, height, mid - 1, Blocks.OBSIDIAN.getDefaultState(), rotation);
		this.fillBlocksRotated(world, sbb, mid + 0, 0, mid - 1, mid + 0, height - 2, mid - 1, Blocks.OBSIDIAN.getDefaultState(), rotation);
		this.fillBlocksRotated(world, sbb, mid - 1, 0, mid + 0, mid - 1, height - 2, mid + 0, Blocks.OBSIDIAN.getDefaultState(), rotation);
		this.fillBlocksRotated(world, sbb, mid + 0, 0, mid + 0, mid + 0, height - 4, mid + 0, Blocks.OBSIDIAN.getDefaultState(), rotation);
	}

	private int countExits() {
		int count = 0;
		for (int i = 0; i < this.openingTowards.length; i++) {
			if (this.openingTowards[i] == true) {
				count++;
			}
		}
		return count;
	}

	private void decorateWall(World world, StructureBoundingBox sbb, Random decoRNG, Rotation rotation) {

		if (decoRNG.nextBoolean()) {
			//FIXME: AtomicBlom: Don't do this, bring rotation all the way through.
			decorateBracketMushrooms(world, sbb, decoRNG, rotation);
		} else if (decoRNG.nextBoolean()) {
			decorateStoneFormation(world, sbb, decoRNG, rotation);
			decorateStoneFormation(world, sbb, decoRNG, rotation);
		} else {
			decorateStoneProjection(world, sbb, decoRNG, rotation);
		}

	}

	private void decorateStoneFormation(World world, StructureBoundingBox sbb, Random decoRNG, Rotation rotation) {
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

	private void makeSingleStoneFormation(World world, StructureBoundingBox sbb, Random decoRNG, Rotation rotation, int z, int y, int width, int depth) {
		if (decoRNG.nextInt(8) == 0) {
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, Blocks.OBSIDIAN.getDefaultState(), rotation);
		} else if (decoRNG.nextInt(4) == 0) {
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, TFBlocks.trollsteinn.getDefaultState(), rotation);
		} else {
			// normal stone
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, Blocks.STONE.getDefaultState(), rotation);
		}
		//this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.5F, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, TFBlocks.trollsteinn, 0, Blocks.STONE, 0, rotation);
	}

	private void decorateStoneProjection(World world, StructureBoundingBox sbb, Random decoRNG, Rotation rotation) {
		int z = 7 + decoRNG.nextInt(3) - decoRNG.nextInt(3);
		int y = 7 + decoRNG.nextInt(3) - decoRNG.nextInt(3);

		this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, y, z, size - 2, y + 3, z + 3, TFBlocks.trollsteinn.getDefaultState(), Blocks.STONE.getDefaultState(), rotation);
		if (decoRNG.nextBoolean()) {
			// down
			this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, 1, z, size - 6, y - 1, z + 3, TFBlocks.trollsteinn.getDefaultState(), Blocks.STONE.getDefaultState(), rotation);
		} else {
			// up
			this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, y + 4, z, size - 6, height - 2, z + 3, TFBlocks.trollsteinn.getDefaultState(), Blocks.STONE.getDefaultState(), rotation);
		}
	}

	/**
	 * Decorate with a patch of bracket fungi
	 */
	private void decorateBracketMushrooms(World world, StructureBoundingBox sbb, Random decoRNG, Rotation rotation) {
		int z = 5 + decoRNG.nextInt(7);
		int startY = 1 + decoRNG.nextInt(4);

		for (int y = startY; y < this.height; y += 2) {

			int width = 1 + decoRNG.nextInt(2) + decoRNG.nextInt(2);
			int depth = 1 + decoRNG.nextInt(2) + decoRNG.nextInt(2);
			Block mushBlock = ((decoRNG.nextInt(3) == 0) ? TFBlocks.huge_mushgloom : (decoRNG.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK));
			makeSingleBracketMushroom(world, sbb, rotation, z, y, width, depth, mushBlock.getDefaultState());

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
	private void makeSingleBracketMushroom(World world, StructureBoundingBox sbb, Rotation rotation, int z, int y, int width, int depth, IBlockState mushBlock) {


		this.fillBlocksRotated(world, sbb, size - depth, y, z - (width - 1), size - 2, y, z + (width - 1), mushBlock.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER), rotation);

		this.fillBlocksRotated(world, sbb, size - (depth + 1), y, z - (width - 1), size - (depth + 1), y, z + (width - 1), getMushroomState(mushBlock, BlockHugeMushroom.EnumType.EAST), rotation);

		final IBlockState northMushroom = getMushroomState(mushBlock, BlockHugeMushroom.EnumType.SOUTH);
		for (int d = 0; d < (depth - 1); d++) {
			this.setBlockStateRotated(world, northMushroom, size - (2 + d), y, z - width, rotation, sbb);
		}
		final IBlockState northWestMushroom = getMushroomState(mushBlock, BlockHugeMushroom.EnumType.SOUTH_EAST);
		this.setBlockStateRotated(world, northWestMushroom, size - (depth + 1), y, z - width, rotation, sbb);

		final IBlockState southMushroom = getMushroomState(mushBlock, BlockHugeMushroom.EnumType.NORTH);
		for (int d = 0; d < (depth - 1); d++) {
			this.setBlockStateRotated(world, southMushroom, size - (2 + d), y, z + width, rotation, sbb);
		}
		final IBlockState southWestMushroom = getMushroomState(mushBlock, BlockHugeMushroom.EnumType.NORTH_EAST);
		this.setBlockStateRotated(world, southWestMushroom, size - (depth + 1), y, z + width, rotation, sbb);


	}

	private IBlockState getMushroomState(IBlockState mushroomBlockState, BlockHugeMushroom.EnumType defaultRotation) {
		if (mushroomBlockState.getPropertyKeys().contains(BlockHugeMushroom.VARIANT)) {
			return mushroomBlockState.withProperty(BlockHugeMushroom.VARIANT, defaultRotation);
		}
		return mushroomBlockState;
	}

	protected boolean makeGardenCave(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		EnumFacing direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, caveSize, direction);

		ComponentTFTrollCaveMain cave = new ComponentTFTrollCaveGarden(getFeatureType(), index, dest.getX(), dest.getY(), dest.getZ(), caveSize, caveHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, cave.getBoundingBox());
		StructureComponent otherGarden = findNearbyGarden(list, cave.getBoundingBox());
		if ((intersect == null || intersect == this) && otherGarden == null) {
			list.add(cave);
			cave.buildComponent(list.get(0), list, rand);
			//addOpening(x, y, z, rotation);

			this.openingTowards[rotation.ordinal()] = true;

			return true;
		} else {
			return false;
		}
	}

	private StructureComponent findNearbyGarden(List<StructureComponent> list, StructureBoundingBox boundingBox) {

		StructureBoundingBox largeBox = new StructureBoundingBox(boundingBox);
		largeBox.minX -= 30;
		largeBox.minY -= 30;
		largeBox.minZ -= 30;
		largeBox.maxX += 30;
		largeBox.maxY += 30;
		largeBox.maxZ += 30;

		for (StructureComponent component : list) {
			if (component instanceof ComponentTFTrollCaveGarden && component.getBoundingBox().intersectsWith(largeBox)) {
				return component;
			}
		}

		return null;
	}

	@Override
	protected boolean makeSmallerCave(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		if (super.makeSmallerCave(list, rand, index, x, y, z, caveSize, caveHeight, rotation)) {
			this.openingTowards[rotation.ordinal()] = true;
			return true;
		} else {
			return false;
		}
	}
}
