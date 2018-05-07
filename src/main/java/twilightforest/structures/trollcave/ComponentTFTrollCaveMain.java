package twilightforest.structures.trollcave;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;
import twilightforest.world.TFGenCaveStalactite;
import twilightforest.world.TFGenMyceliumBlob;

import java.util.List;
import java.util.Random;

public class ComponentTFTrollCaveMain extends StructureTFComponentOld {

	protected int size;
	protected int height;

	public static final TFGenMyceliumBlob uberousGen = new TFGenMyceliumBlob(TFBlocks.uberous_soil, 4);

	public ComponentTFTrollCaveMain() {
	}

	public ComponentTFTrollCaveMain(TFFeature feature, int index) {
		super(feature, index);
	}


	public ComponentTFTrollCaveMain(TFFeature feature, World world, Random rand, int i, int x, int y, int z) {
		super(feature, i);
		this.setCoordBaseMode(EnumFacing.SOUTH);

		// adjust y
		y += 10;

		this.size = 30;
		this.height = 20;

		int radius = this.size / 2;
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -radius, -this.height, -radius, this.size, this.height, this.size, EnumFacing.SOUTH);

	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);

		tagCompound.setInteger("size", this.size);
		tagCompound.setInteger("height", this.height);
	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.size = tagCompound.getInteger("size");
		this.height = tagCompound.getInteger("height");
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		// make 4 caves
		for (final Rotation caveRotation : RotationUtil.ROTATIONS) {
			BlockPos dest = getValidOpening(rand, 5, caveRotation);

			makeSmallerCave(list, rand, this.getComponentType() + 1, dest.getX(), dest.getY(), dest.getZ(), 18, 15, caveRotation);
		}

		// add cloud castle
		ComponentTFCloudCastle castle = new ComponentTFCloudCastle(getFeatureType(), this.getComponentType() + 1, boundingBox.minX + ((boundingBox.maxX - boundingBox.minX) / 2), 168, boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2));
		list.add(castle);
		castle.buildComponent(this, list, rand);

		// add vault
		ComponentTFTrollVault vault = new ComponentTFTrollVault(getFeatureType(), this.getComponentType() + 1, boundingBox.minX + ((boundingBox.maxX - boundingBox.minX) / 2), boundingBox.minY, boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2));
		list.add(vault);
		vault.buildComponent(this, list, rand);
	}


	protected boolean makeSmallerCave(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		EnumFacing direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, caveSize, direction);

		ComponentTFTrollCaveConnect cave = new ComponentTFTrollCaveConnect(getFeatureType(), index, dest.getX(), dest.getY(), dest.getZ(), caveSize, caveHeight, direction);
		// check to see if it intersects something already there
		StructureComponent intersect = StructureComponent.findIntersecting(list, cave.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.add(cave);
			cave.buildComponent(list.get(0), list, rand);
			//addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);

		// stone stalactites!
		for (int i = 0; i < 128; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockStalactite(world, decoRNG, Blocks.STONE, 0.7F, true, dest.getX(), 3, dest.getZ(), sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockStalactite(world, decoRNG, Blocks.STONE, 0.5F, false, dest.getX(), 3, dest.getZ(), sbb);
		}


		// uberous!
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);

			generateAtSurface(world, uberousGen, decoRNG, dest.getX(), 60, dest.getZ(), sbb);
		}

		return true;
	}


	protected BlockPos getCoordsInCave(Random rand) {
		return new BlockPos(rand.nextInt(this.size - 1), rand.nextInt(this.height - 1), rand.nextInt(this.size - 1));
	}

	/**
	 * arguments: (World world, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
	 * maxZ)
	 *
	 * @param rand
	 */
	protected void hollowCaveMiddle(World par1World, StructureBoundingBox par2StructureBoundingBox, Random rand, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		int threshold = this.size / 5;

		for (int y = minY; y <= maxY; ++y) {
			for (int x = minX; x <= maxX; ++x) {
				for (int z = minZ; z <= maxZ; ++z) {
					int ex = Math.min(x - minX, maxX - x);
					int ey = Math.min((y - minY) * 2, maxY - y);
					int ez = Math.min(z - minZ, maxZ - z);

					double dist = Math.sqrt(ex * ey * ez);

					if (dist > threshold) {
						this.setBlockState(par1World, Blocks.AIR.getDefaultState(), x, y, z, par2StructureBoundingBox);
					} else if (dist == threshold && rand.nextInt(4) == 0 && this.getBlockStateFromPos(par1World, x, y, z, par2StructureBoundingBox).getBlock() == Blocks.STONE) {
						this.setBlockState(par1World, TFBlocks.trollsteinn.getDefaultState(), x, y, z, par2StructureBoundingBox);
					}
				}
			}
		}
	}


	/**
	 * Gets a random position in the specified direction that connects to stairs currently in the tower.
	 */
	public BlockPos getValidOpening(Random rand, int caveHeight, Rotation direction) {
		// variables!
		int offset = this.size / 4; // wall thickness
		int wLength = size - (offset * 2); // wall length

		// for directions 0 or 2, the wall lies along the z axis
		if (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) {
			int rx = direction == Rotation.NONE ? size - 1 : 0;
			int rz = offset + rand.nextInt(wLength);
			int ry = (rand.nextInt(offset) - rand.nextInt(offset));

			return new BlockPos(rx, ry, rz);
		}

		// for directions 1 or 3, the wall lies along the x axis
		if (direction == Rotation.CLOCKWISE_90 || direction == Rotation.COUNTERCLOCKWISE_90) {
			int rx = offset + rand.nextInt(wLength);
			int rz = direction == Rotation.CLOCKWISE_90 ? size - 1 : 0;
			int ry = (rand.nextInt(offset) - rand.nextInt(offset));

			return new BlockPos(rx, ry, rz);
		}


		return null;
	}


	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	@Override
	protected BlockPos offsetTowerCCoords(int x, int y, int z, int towerSize, EnumFacing direction) {

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		if (direction == EnumFacing.SOUTH) {
			return new BlockPos(dx - 1, dy - 1, dz - towerSize / 2);
		} else if (direction == EnumFacing.WEST) {
			return new BlockPos(dx + towerSize / 2, dy - 1, dz - 1);
		} else if (direction == EnumFacing.NORTH) {
			return new BlockPos(dx + 1, dy - 1, dz + towerSize / 2);
		} else if (direction == EnumFacing.EAST) {
			return new BlockPos(dx - towerSize / 2, dy - 1, dz + 1);
		}


		// ugh?
		return new BlockPos(x, y, z);
	}


	public boolean isBoundingBoxOutOfHighlands(World world, StructureBoundingBox sbb) {
		int minX = this.boundingBox.minX - 1;
		int minZ = this.boundingBox.minZ - 1;
		int maxX = this.boundingBox.maxX + 1;
		int maxZ = this.boundingBox.maxZ + 1;

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (world.getBiome(new BlockPos(x, 0, z)) != TFBiomes.highlands) {
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * Make a random stone stalactite
	 */
	protected void generateBlockStalactite(World world, Random rand, Block blockToGenerate, float length, boolean up, int x, int y, int z, StructureBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos)) {
			(new TFGenCaveStalactite(blockToGenerate, length, up)).generate(world, rand, pos);
		}
	}

	/**
	 * Use the generator at the surface above specified coords
	 */
	protected void generateAtSurface(World world, WorldGenerator generator, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = y;
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos)) {
			// find surface above the listed coords
			for (dy = y; dy < y + 32; dy++) {
				if (world.isAirBlock(pos)) {
					break;
				}
			}

			generator.generate(world, rand, pos);
		}
	}

	protected void makeTreasureCrate(World world, Random rand, StructureBoundingBox sbb) {
		// treasure!
		int mid = this.size / 2;
		this.fillWithBlocks(world, sbb, mid - 2, 0, mid - 2, mid + 1, 3, mid + 1, Blocks.OBSIDIAN.getDefaultState(), Blocks.OBSIDIAN.getDefaultState(), false);
		this.fillWithAir(world, sbb, mid - 1, 1, mid - 1, mid + 0, 2, mid + 0);
		this.placeTreasureAtCurrentPosition(world, rand, mid, 1, mid, TFTreasure.troll_garden, false, sbb);
	}

}
