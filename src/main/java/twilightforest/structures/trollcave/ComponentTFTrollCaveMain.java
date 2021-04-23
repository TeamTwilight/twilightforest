package twilightforest.structures.trollcave;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.config.CaveStalactiteConfig;
import twilightforest.worldgen.BlockConstants;
import twilightforest.worldgen.ConfiguredFeatures;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ComponentTFTrollCaveMain extends StructureTFComponentOld {

	protected int size;
	protected int height;

	public static final ConfiguredFeature<?,?> uberousGen = TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(BlockConstants.UBEROUS_SOIL, FeatureSpread.func_242253_a(10, 8), 1, ImmutableList.of(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.DIRT))).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(60, 10)));

	public ComponentTFTrollCaveMain(TemplateManager manager, CompoundNBT nbt) {
		this(TFTrollCavePieces.TFTCMai, nbt);
	}

	public ComponentTFTrollCaveMain(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
		this.size = nbt.getInt("size");
		this.height = nbt.getInt("height");
	}

	public ComponentTFTrollCaveMain(IStructurePieceType type, TFFeature feature, int index) {
		super(type, feature, index);
	}

	public ComponentTFTrollCaveMain(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z) {
		this(type, feature, i);
		this.setCoordBaseMode(Direction.SOUTH);

		// adjust y
		y += 10;

		this.size = 30;
		this.height = 20;

		int radius = this.size / 2;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -radius, -this.height, -radius, this.size, this.height, this.size, Direction.SOUTH);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putInt("size", this.size);
		tagCompound.putInt("height", this.height);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		// make 4 caves
		for (final Rotation caveRotation : RotationUtil.ROTATIONS) {
			BlockPos dest = getValidOpening(rand, caveRotation);
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

	protected boolean makeSmallerCave(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, caveSize, direction);

		ComponentTFTrollCaveConnect cave = new ComponentTFTrollCaveConnect(getFeatureType(), index, dest.getX(), dest.getY(), dest.getZ(), caveSize, caveHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = StructurePiece.findIntersecting(list, cave.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.add(cave);
			cave.buildComponent(list.get(0), list, rand);
			//addOpening(x, y, z, rotation);
			return true;
		}
		return false;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);

		// stone stalactites!
		for (int i = 0; i < 128; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockStalactite(world, generator, manager, decoRNG, Blocks.STONE, 0.7F, true, dest.getX(), 3, dest.getZ(), sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockStalactite(world, generator, manager, decoRNG, Blocks.STONE, 0.5F, false, dest.getX(), 3, dest.getZ(), sbb);
		}

		// uberous!
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateAtSurface(world, manager, generator, uberousGen, decoRNG, dest.getX(), 60, dest.getZ(), sbb);
		}

		return true;
	}

	protected BlockPos getCoordsInCave(Random rand) {
		return new BlockPos(rand.nextInt(this.size - 1), rand.nextInt(this.height - 1), rand.nextInt(this.size - 1));
	}

	protected BlockPos getCenterBiasedCaveCoords(Random rand) {
		return new BlockPos(this.size - rand.nextInt(this.size / 2), rand.nextInt(this.height - 1), this.size - rand.nextInt(this.size / 2));
	}

	protected void hollowCaveMiddle(ISeedReader world, MutableBoundingBox boundingBox, Random rand, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		int threshold = this.size / 5;

		for (int y = minY; y <= maxY; ++y) {
			for (int x = minX; x <= maxX; ++x) {
				for (int z = minZ; z <= maxZ; ++z) {

					int ex = Math.min(x - minX, maxX - x);
					int ey = Math.min((y - minY) * 2, maxY - y);
					int ez = Math.min(z - minZ, maxZ - z);

					double dist = Math.sqrt(ex * ey * ez);

					if (dist > threshold) {
						this.setBlockState(world, Blocks.AIR.getDefaultState(), x, y, z, boundingBox);
					} else if (dist == threshold && rand.nextInt(4) == 0 && this.getBlockStateFromPos(world, x, y, z, boundingBox).getBlock().isIn(BlockTags.BASE_STONE_OVERWORLD)) {
						this.setBlockState(world, TFBlocks.trollsteinn.get().getDefaultState(), x, y, z, boundingBox);
					}
				}
			}
		}
	}

	/**
	 * Gets a random position in the specified direction that connects to stairs currently in the tower.
	 */
	public BlockPos getValidOpening(Random rand, Rotation direction) {
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
	protected BlockPos offsetTowerCCoords(int x, int y, int z, int towerSize, Direction direction) {

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		if (direction == Direction.SOUTH) {
			return new BlockPos(dx - 1, dy - 1, dz - towerSize / 2);
		} else if (direction == Direction.WEST) {
			return new BlockPos(dx + towerSize / 2, dy - 1, dz - 1);
		} else if (direction == Direction.NORTH) {
			return new BlockPos(dx + 1, dy - 1, dz + towerSize / 2);
		} else if (direction == Direction.EAST) {
			return new BlockPos(dx - towerSize / 2, dy - 1, dz + 1);
		}

		// ugh?
		return new BlockPos(x, y, z);
	}

	protected static final Predicate<Biome> highlands = biome -> false; // FIXME biome == TFBiomes.highlands.get();

	/**
	 * Make a random stone stalactite
	 */
	protected void generateBlockStalactite(ISeedReader world, ChunkGenerator generator, StructureManager manager, Random rand, Block blockToGenerate, float length, boolean up, int x, int y, int z, MutableBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos)) {
			TFBiomeFeatures.CAVE_STALACTITE.get().withConfiguration(new CaveStalactiteConfig(blockToGenerate.getDefaultState(), length, -1, -1, up)).generate(world, generator, rand, pos);
		}
	}

	/**
	 * Use the generator at the surface above specified coords
	 */
	protected void generateAtSurface(ISeedReader world, StructureManager manager, ChunkGenerator generator, ConfiguredFeature<?,?> feature, Random rand, int x, int y, int z, MutableBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = y;
		int dz = getZWithOffset(x, z);

		BlockPos.Mutable pos = new BlockPos.Mutable(dx, dy, dz);

		if (sbb.isVecInside(pos)) {
			// find surface above the listed coords
			for (dy = y; dy < y + 32; dy++) {
				pos.setY(dy);
				if (world.isAirBlock(pos)) {
					break;
				}
			}

			feature.generate(world, generator, rand, pos.toImmutable());
		}
	}

	protected void makeTreasureCrate(ISeedReader world, MutableBoundingBox sbb) {
		// treasure!
		int mid = this.size / 2;
		this.fillWithBlocks(world, sbb, mid - 2, 0, mid - 2, mid + 1, 3, mid + 1, Blocks.OBSIDIAN.getDefaultState(), Blocks.OBSIDIAN.getDefaultState(), false);
		this.fillWithAir(world, sbb, mid - 1, 1, mid - 1, mid + 0, 2, mid + 0);
		this.placeTreasureAtCurrentPosition(world, mid, 1, mid, TFTreasure.troll_garden, false, sbb);
	}
}
