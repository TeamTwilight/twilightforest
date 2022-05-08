package twilightforest.world.components.structures.trollcave;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;
import twilightforest.util.RotationUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.feature.BlockSpikeFeature;
import twilightforest.world.components.feature.config.SpikeConfig;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.features.TFConfiguredFeatures;

import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class TrollCaveMainComponent extends TFStructureComponentOld {
	protected static final SpikeConfig STONE_STALACTITE = new SpikeConfig(BlockStateProvider.simple(Blocks.STONE), UniformInt.of(6, 9), UniformInt.of(3, 4), true);
	protected static final SpikeConfig STONE_STALAGMITE = new SpikeConfig(BlockStateProvider.simple(Blocks.STONE), UniformInt.of(4, 7), UniformInt.of(3, 4), false);

	protected int size;
	protected int height;

	public TrollCaveMainComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TrollCavePieces.TFTCMai, nbt);
	}

	public TrollCaveMainComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.size = nbt.getInt("size");
		this.height = nbt.getInt("height");
	}

	public TrollCaveMainComponent(StructurePieceType type, TFFeature feature, int i, int x, int y, int z) {
		super(type, feature, i, x, y, z);
		this.setOrientation(Direction.SOUTH); // DEPTH_AVERAGE

		// adjust y
		y += 10;

		this.size = 30;
		this.height = 20;

		int radius = this.size / 2;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -radius, -this.height, -radius, this.size, this.height, this.size, Direction.SOUTH);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("size", this.size);
		tagCompound.putInt("height", this.height);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		// make 4 caves
		for (final Rotation caveRotation : RotationUtil.ROTATIONS) {
			BlockPos dest = getValidOpening(rand, caveRotation);
			makeSmallerCave(list, rand, this.getGenDepth() + 1, dest.getX(), dest.getY(), dest.getZ(), 18, 15, caveRotation);
		}

		// add cloud castle
		CloudCastleComponent castle = new CloudCastleComponent(getFeatureType(), this.getGenDepth() + 1, boundingBox.minX() + ((boundingBox.maxX() - boundingBox.minX()) / 2), 168, boundingBox.minZ() + ((boundingBox.maxZ() - boundingBox.minZ()) / 2));
		list.addPiece(castle);
		castle.addChildren(this, list, rand);

		// add vault
		TrollVaultComponent vault = new TrollVaultComponent(getFeatureType(), this.getGenDepth() + 1, boundingBox.minX() + ((boundingBox.maxX() - boundingBox.minX()) / 2), boundingBox.minY(), boundingBox.minZ() + ((boundingBox.maxZ() - boundingBox.minZ()) / 2));
		list.addPiece(vault);
		vault.addChildren(this, list, rand);
	}

	protected boolean makeSmallerCave(StructurePieceAccessor list, Random rand, int index, int x, int y, int z, int caveSize, int caveHeight, Rotation rotation) {
		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dest = offsetTowerCCoords(x, y, z, caveSize, direction);

		TrollCaveConnectComponent cave = new TrollCaveConnectComponent(getFeatureType(), index, dest.getX(), dest.getY(), dest.getZ(), caveSize, caveHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = list.findCollisionPiece(cave.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.addPiece(cave);
			cave.addChildren(this, list, rand);
			//addOpening(x, y, z, rotation);
			return true;
		}
		return false;
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);

		// stone stalactites!
		for (int i = 0; i < 128; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockSpike(world, STONE_STALACTITE, dest.atY(this.height), sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockSpike(world, STONE_STALAGMITE, dest.atY(0), sbb);
		}

		// uberous!
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateAtSurface(world, generator, TFConfiguredFeatures.UBEROUS_SOIL_PATCH_BIG, decoRNG, dest.getX(), dest.getZ(), sbb);
		}
	}

	protected BlockPos.MutableBlockPos getCoordsInCave(Random rand) {
		return new BlockPos.MutableBlockPos(rand.nextInt(this.size - 1), rand.nextInt(this.height - 1), rand.nextInt(this.size - 1));
	}

	protected void hollowCaveMiddle(WorldGenLevel world, BoundingBox boundingBox, Random rand, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		int threshold = this.size / 5;

		for (int y = minY; y <= maxY; ++y) {
			for (int x = minX; x <= maxX; ++x) {
				for (int z = minZ; z <= maxZ; ++z) {

					int ex = Math.min(x - minX, maxX - x);
					int ey = Math.min((y - minY) * 2, maxY - y);
					int ez = Math.min(z - minZ, maxZ - z);

					double dist = Math.sqrt(ex * ey * ez);

					if (dist > threshold) {
						this.placeBlock(world, Blocks.AIR.defaultBlockState(), x, y, z, boundingBox);
					} else if (dist == threshold && rand.nextInt(4) == 0 && this.getBlock(world, x, y, z, boundingBox).is(BlockTags.BASE_STONE_OVERWORLD)) {
						this.placeBlock(world, TFBlocks.TROLLSTEINN.get().defaultBlockState(), x, y, z, boundingBox);
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

		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);

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


	protected void generateBlockSpike(WorldGenLevel world, SpikeConfig config, Vec3i pos, BoundingBox sbb) {
		generateBlockSpike(world, config, pos.getX(), pos.getY(), pos.getZ(), sbb);
	}

	protected void generateBlockSpike(WorldGenLevel world, SpikeConfig config, int x, int y, int z, BoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos)) {
			// generate an RNG for this stalactite
			//TODO: MOAR RANDOM!
			Random stalRNG = new Random(world.getSeed() + (long) dx * dz);

			// make the actual stalactite
			BlockSpikeFeature.startSpike(world, pos, config, stalRNG);
		}
	}

	/**
	 * Use the generator at the surface above specified coords
	 */
	protected <FC extends FeatureConfiguration> void generateAtSurface(WorldGenLevel world, ChunkGenerator generator, Holder<ConfiguredFeature<FC, ?>> feature, Random rand, int x, int z, BoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getWorldX(x, z);
		int dz = getWorldZ(x, z);

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(dx, WorldUtil.getSeaLevel(Objects.requireNonNull(WorldUtil.getChunkGenerator(world))) + 25, dz);

		for(int i = 0; i < 15; i++) {
			pos.move(0, 1, 0);
			if (sbb.isInside(pos) && world.getBlockState(pos.above()).isAir()) {
				feature.value().place(world, generator, rand, pos);
				break;
			}
		}
	}

	protected void makeTreasureCrate(WorldGenLevel world, BoundingBox sbb) {
		// treasure!
		int mid = this.size / 2;
		this.generateBox(world, sbb, mid - 2, 0, mid - 2, mid + 1, 3, mid + 1, Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState(), false);
		this.generateAirBox(world, sbb, mid - 1, 1, mid - 1, mid, 2, mid);
		this.placeTreasureAtCurrentPosition(world, mid, 1, mid, TFTreasure.TROLL_GARDEN, false, sbb);
	}

	@Override
	public NoiseEffect getNoiseEffect() {
		return NoiseEffect.BURY;
	}
}
