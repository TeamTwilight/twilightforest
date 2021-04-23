package twilightforest.structures.trollcave;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.MushroomUtil;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFTrollCaveConnect extends ComponentTFTrollCaveMain {

	protected boolean[] openingTowards = {false, false, true, false};

	public ComponentTFTrollCaveConnect(TemplateManager manager, CompoundNBT nbt) {
		super(TFTrollCavePieces.TFTCCon, nbt);
		this.openingTowards[0] = nbt.getBoolean("openingTowards0");
		this.openingTowards[1] = nbt.getBoolean("openingTowards1");
		this.openingTowards[2] = nbt.getBoolean("openingTowards2");
		this.openingTowards[3] = nbt.getBoolean("openingTowards3");
	}

	public ComponentTFTrollCaveConnect(TFFeature feature, int index, int x, int y, int z, int caveSize, int caveHeight, Direction direction) {
		super(TFTrollCavePieces.TFTCCon, feature, index);
		this.size = caveSize;
		this.height = caveHeight;
		this.setCoordBaseMode(direction);
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		// too lazy to do this as a loop
		tagCompound.putBoolean("openingTowards0", this.openingTowards[0]);
		tagCompound.putBoolean("openingTowards1", this.openingTowards[1]);
		tagCompound.putBoolean("openingTowards2", this.openingTowards[2]);
		tagCompound.putBoolean("openingTowards3", this.openingTowards[3]);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		// make 4 caves
		if (this.getComponentType() < 3) {
			for (final Rotation rotation : RotationUtil.ROTATIONS) {
				BlockPos dest = getValidOpening(rand, rotation);

				if (rand.nextBoolean() || !makeGardenCave(list, rand, this.getComponentType() + 1, dest.getX(), dest.getY(), dest.getZ(), 30, 15, rotation)) {
					makeSmallerCave(list, rand, this.getComponentType() + 1, dest.getX(), dest.getY(), dest.getZ(), 20, 15, rotation);
				}
			}
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
//		if (this.isBoundingBoxOutsideBiomes(world, sbb, highlands)) {
//			return false;
//		}

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
			generateBlockStalactite(world, generator, manager, decoRNG, Blocks.STONE, 0.5F, true, dest.getX(), 3, dest.getZ(), sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < 8; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockStalactite(world, generator, manager, decoRNG, Blocks.STONE, 0.5F, false, dest.getX(), 3, dest.getZ(), sbb);
		}

		// possible treasure
		decoRNG.setSeed(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));
		if (this.countExits() == 1 && decoRNG.nextInt(3) == 0) {
			// treasure!
			makeTreasureCrate(world, sbb);
		} else if (decoRNG.nextInt(3) == 0) {
			// or a monolith!
			makeMonolith(world, decoRNG, sbb);
		}

		return true;
	}

	protected void makeMonolith(ISeedReader world, Random rand, MutableBoundingBox sbb) {
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
			if (this.openingTowards[i]) {
				count++;
			}
		}
		return count;
	}

	private void decorateWall(ISeedReader world, MutableBoundingBox sbb, Random decoRNG, Rotation rotation) {
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

	private void decorateStoneFormation(ISeedReader world, MutableBoundingBox sbb, Random decoRNG, Rotation rotation) {
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

	private void makeSingleStoneFormation(ISeedReader world, MutableBoundingBox sbb, Random decoRNG, Rotation rotation, int z, int y, int width, int depth) {
		if (decoRNG.nextInt(8) == 0) {
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, Blocks.OBSIDIAN.getDefaultState(), rotation);
		} else if (decoRNG.nextInt(4) == 0) {
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, TFBlocks.trollsteinn.get().getDefaultState(), rotation);
		} else {
			// normal stone
			this.fillBlocksRotated(world, sbb, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, Blocks.STONE.getDefaultState(), rotation);
		}
		//this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.5F, size - (depth + 1), y - width, z - width, size - 1, y + width, z + width, TFBlocks.trollsteinn, 0, Blocks.STONE, 0, rotation);
	}

	private void decorateStoneProjection(ISeedReader world, MutableBoundingBox sbb, Random decoRNG, Rotation rotation) {
		int z = 7 + decoRNG.nextInt(3) - decoRNG.nextInt(3);
		int y = 7 + decoRNG.nextInt(3) - decoRNG.nextInt(3);

		this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, y, z, size - 2, y + 3, z + 3, TFBlocks.trollsteinn.get().getDefaultState(), Blocks.STONE.getDefaultState(), rotation);
		if (decoRNG.nextBoolean()) {
			// down
			this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, 1, z, size - 6, y - 1, z + 3, TFBlocks.trollsteinn.get().getDefaultState(), Blocks.STONE.getDefaultState(), rotation);
		} else {
			// up
			this.randomlyFillBlocksRotated(world, sbb, decoRNG, 0.25F, size - 9, y + 4, z, size - 6, height - 2, z + 3, TFBlocks.trollsteinn.get().getDefaultState(), Blocks.STONE.getDefaultState(), rotation);
		}
	}

	/**
	 * Decorate with a patch of bracket fungi
	 */
	private void decorateBracketMushrooms(ISeedReader world, MutableBoundingBox sbb, Random decoRNG, Rotation rotation) {
		int z = 5 + decoRNG.nextInt(7);
		int startY = 1 + decoRNG.nextInt(4);

		for (int y = startY; y < this.height; y += 2) {

			int width = 1 + decoRNG.nextInt(2) + decoRNG.nextInt(2);
			int depth = 1 + decoRNG.nextInt(2) + decoRNG.nextInt(2);
			Block mushBlock = ((decoRNG.nextInt(3) == 0) ? TFBlocks.huge_mushgloom.get() : (decoRNG.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK));
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
	private void makeSingleBracketMushroom(ISeedReader world, MutableBoundingBox sbb, Rotation rotation, int z, int y, int width, int depth, BlockState mushBlock) {

		this.fillBlocksRotated(world, sbb, size - depth, y, z - (width - 1), size - 2, y, z + (width - 1), MushroomUtil.getState(MushroomUtil.Type.CENTER, mushBlock), rotation);

		this.fillBlocksRotated(world, sbb, size - (depth + 1), y, z - (width - 1), size - (depth + 1), y, z + (width - 1), getMushroomState(mushBlock, MushroomUtil.Type.EAST), rotation);

		final BlockState northMushroom = getMushroomState(mushBlock, MushroomUtil.Type.SOUTH);
		for (int d = 0; d < (depth - 1); d++) {
			this.setBlockStateRotated(world, northMushroom, size - (2 + d), y, z - width, rotation, sbb);
		}
		final BlockState northWestMushroom = getMushroomState(mushBlock, MushroomUtil.Type.SOUTH_EAST);
		this.setBlockStateRotated(world, northWestMushroom, size - (depth + 1), y, z - width, rotation, sbb);

		final BlockState southMushroom = getMushroomState(mushBlock, MushroomUtil.Type.NORTH);
		for (int d = 0; d < (depth - 1); d++) {
			this.setBlockStateRotated(world, southMushroom, size - (2 + d), y, z + width, rotation, sbb);
		}
		final BlockState southWestMushroom = getMushroomState(mushBlock, MushroomUtil.Type.NORTH_EAST);
		this.setBlockStateRotated(world, southWestMushroom, size - (depth + 1), y, z + width, rotation, sbb);
	}

	private BlockState getMushroomState(BlockState mushroomBlockState, MushroomUtil.Type defaultRotation) {
		return MushroomUtil.getState(defaultRotation, mushroomBlockState);
	}

	protected boolean makeGardenCave(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, caveSize, direction);

		ComponentTFTrollCaveMain cave = new ComponentTFTrollCaveGarden(getFeatureType(), index, dest.getX(), dest.getY(), dest.getZ(), caveSize, caveHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = StructurePiece.findIntersecting(list, cave.getBoundingBox());
		StructurePiece otherGarden = findNearbyGarden(list, cave.getBoundingBox());
		if ((intersect == null || intersect == this) && otherGarden == null) {
			list.add(cave);
			cave.buildComponent(list.get(0), list, rand);
			//addOpening(x, y, z, rotation);

			this.openingTowards[rotation.ordinal()] = true;

			return true;
		}
		return false;
	}

	private StructurePiece findNearbyGarden(List<StructurePiece> list, MutableBoundingBox boundingBox) {

		MutableBoundingBox largeBox = new MutableBoundingBox(boundingBox);
		largeBox.minX -= 30;
		largeBox.minY -= 30;
		largeBox.minZ -= 30;
		largeBox.maxX += 30;
		largeBox.maxY += 30;
		largeBox.maxZ += 30;

		for (StructurePiece component : list) {
			if (component instanceof ComponentTFTrollCaveGarden && component.getBoundingBox().intersectsWith(largeBox)) {
				return component;
			}
		}

		return null;
	}

	@Override
	protected boolean makeSmallerCave(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		if (super.makeSmallerCave(list, rand, index, x, y, z, caveSize, caveHeight, rotation)) {
			this.openingTowards[rotation.ordinal()] = true;
			return true;
		}
		return false;
	}
}
