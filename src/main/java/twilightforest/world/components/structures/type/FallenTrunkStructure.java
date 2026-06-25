package twilightforest.world.components.structures.type;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructureTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.fallentrunk.FallenTrunkPiece;
import twilightforest.world.components.structures.fallentrunk.TrunkUnderDensityFunction;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.*;
import java.util.stream.Collectors;

public class FallenTrunkStructure extends Structure implements CustomDensitySource, DecorationClearance {
	public static final MapCodec<FallenTrunkStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Structure.settingsCodec(instance),
		IntProviders.codec(16, 32).fieldOf("length").forGetter(s -> s.length),
		IntProviders.codec(20, 32).fieldOf("big_trunk_length").forGetter(s -> s.bigTrunkLength),
		BlockStateProvider.CODEC.fieldOf("log").forGetter(s -> s.log),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("chest_loot_table").forGetter(s -> s.chestLootTable)
	).apply(instance, FallenTrunkStructure::new));
	public static final List<Integer> radiuses = List.of(1, 2, 4);

	private final IntProvider length;
	private final IntProvider bigTrunkLength;
	private final BlockStateProvider log;
	private final ResourceKey<LootTable> chestLootTable;

	protected FallenTrunkStructure(StructureSettings settings, IntProvider length, IntProvider bigTrunkLength, BlockStateProvider log, ResourceKey<LootTable> chestLootTable) {
		super(settings);
		this.length = length;
		this.bigTrunkLength = bigTrunkLength;
		this.log = log;
		this.chestLootTable = chestLootTable;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		RandomSource random = RandomSource.create(context.seed() + chunkPos.x() * 14413411L + chunkPos.z() * 43387781L);

		int x = SectionPos.sectionToBlockCoord(chunkPos.x(), random.nextInt(16));
		int z = SectionPos.sectionToBlockCoord(chunkPos.z(), random.nextInt(16));
		int worldY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int radius = Util.getRandom(radiuses, random);
		int length = radius == radiuses.getLast() ? this.bigTrunkLength.sample(random) : this.length.sample(random);

		if (!isValidNoiseBiome(context, x, worldY, z))
			return Optional.empty();
		if (hasInvalidNearbyBiome(context, x, worldY, z, random))
			return Optional.empty();

		Direction orientation = Direction.Plane.HORIZONTAL.getRandomDirection(random);
		int xySize = radius > 1 ? radius * 2 + 1 : 4;
		int zSize = length - 1;

		BoundingBox baseBox = BoundingBox.orientBox(x, worldY, z,
			0, 0, 0,
			xySize, xySize, zSize,
			orientation);

		int targetY = computeTargetY(context, baseBox, worldY, radius);
		BoundingBox adjustedBox = BoundingBox.orientBox(x, targetY, z,
			0, 0, 0,
			xySize, xySize, zSize,
			orientation);

		long holeSeed = random.nextLong();
		return Optional.of(new GenerationStub(new BlockPos(x, adjustedBox.minY(), z), structurePiecesBuilder -> {
			StructurePiece piece = new FallenTrunkPiece(length, radius, log, chestLootTable, orientation, adjustedBox, holeSeed);
			structurePiecesBuilder.addPiece(piece);
			piece.addChildren(piece, structurePiecesBuilder, random);
		}));
	}

	private boolean isValidNoiseBiome(GenerationContext context, int x, int worldY, int z) {
		Holder<Biome> noiseBiome = context.chunkGenerator().getBiomeSource()
			.getNoiseBiome(x >> 2, worldY >> 2, z >> 2, context.randomState().sampler());
		return this.getModifiedStructureSettings().biomes().contains(noiseBiome);
	}

	private boolean hasInvalidNearbyBiome(GenerationContext context, int x, int worldY, int z, RandomSource random) {
		Pair<BlockPos, Holder<Biome>> invalidBiome = context.biomeSource().findBiomeHorizontal(
			x, worldY, z,
			this.length.maxInclusive(), 1,
			biomeHolder -> !context.validBiome().test(biomeHolder),
			random, false, context.randomState().sampler());
		return invalidBiome != null;
	}

	// Don't do anything for small trunks
	private int computeTargetY(GenerationContext context, BoundingBox box, int defaultY, int radius) {
		if (radius == radiuses.getFirst())
			return defaultY;

		return WorldUtil.adjustForTerrain(context, box.minX(), box.minZ(), box.maxX(), box.maxZ(), 4, Heightmap.Types.WORLD_SURFACE_WG);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.FALLEN_TRUNK.get();
	}

	public static FallenTrunkStructure buildStructureConfig(HolderSet<Biome> biomes) {
		return new FallenTrunkStructure(
			new Structure.StructureSettings(
				biomes,
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.NONE
			),
			UniformInt.of(17, 24), UniformInt.of(22, 28), BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()), TFLootTables.FALLEN_TRUNK_LOOT
		);
	}

	@Override
	public DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource) {
		FallenTrunkPiece piece = ((FallenTrunkPiece) structurePieceSource.getPieces().getFirst());
		ObjectList<Beardifier.Rigid> objectlist = ObjectArrayList.of(new Beardifier.Rigid(piece.getBoundingBox(), TerrainAdjustment.BEARD_THIN , 0));
		boolean isBigTree = piece.radius == radiuses.get(2);
		int minMounds = 1;
		int maxMounds = 2;
		return new TrunkUnderDensityFunction(objectlist, piece, isBigTree, minMounds, maxMounds);  // big trees are a special case
	}

	@Override
	public float chunkClearanceRadius() {
		return 0;
	}

	@Override
	public boolean isSurfaceDecorationsAllowed() {
		return false;
	}

	@Override
	public boolean isUndergroundDecoAllowed() {
		return true;
	}

	@Override
	public boolean isGrassDecoAllowed() {
		return true;
	}

	@Override
	public boolean shouldAdjustToTerrain() {
		return false;
	}
}
