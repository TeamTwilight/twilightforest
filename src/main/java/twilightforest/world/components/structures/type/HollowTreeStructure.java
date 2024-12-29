package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.init.TFStructureTypes;
import twilightforest.util.features.FeatureLogic;
import twilightforest.world.components.structures.TreeGrowerStartable;
import twilightforest.world.components.structures.hollowtree.HollowTreePiece;
import twilightforest.world.components.structures.hollowtree.HollowTreeTrunk;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class HollowTreeStructure extends Structure implements DecorationClearance, TreeGrowerStartable {
	public static final MapCodec<HollowTreeStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Structure.settingsCodec(instance),
		DecorationConfig.FLAT_CODEC.forGetter(s -> s.decorationConfig),
		IntProvider.codec(16, 128).fieldOf("height").forGetter(s -> s.height),
		IntProvider.codec(1, 8).fieldOf("radius").forGetter(s -> s.radius),
		BlockStateProvider.CODEC.fieldOf("log").forGetter(s -> s.log),
		BlockStateProvider.CODEC.fieldOf("wood").forGetter(s -> s.wood),
		BlockStateProvider.CODEC.fieldOf("root").forGetter(s -> s.root),
		BlockStateProvider.CODEC.fieldOf("leaves").forGetter(s -> s.leaves),
		BlockStateProvider.CODEC.fieldOf("vine").forGetter(s -> s.vine),
		BlockStateProvider.CODEC.fieldOf("bug").forGetter(s -> s.bug),
		BlockStateProvider.CODEC.fieldOf("dungeon_wood").forGetter(s -> s.dungeonWood),
		BlockStateProvider.CODEC.fieldOf("dungeon_air").forGetter(s -> s.dungeonAir),
		BlockStateProvider.CODEC.fieldOf("dungeon_loot_block").forGetter(s -> s.dungeonLootBlock),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("dungeon_loot_table").forGetter(s -> s.dungeonLootTable),
		RegistryFixedCodec.create(Registries.ENTITY_TYPE).fieldOf("dungeon_monster").forGetter(s -> s.dungeonMonster),
		Codec.BOOL.optionalFieldOf("allow_in_water", false).forGetter(s -> s.allowInWater)
	).apply(instance, HollowTreeStructure::new));

	private final DecorationConfig decorationConfig;

	private final IntProvider height;
	private final IntProvider radius;

	private final BlockStateProvider log;
	private final BlockStateProvider wood;
	private final BlockStateProvider root;
	private final BlockStateProvider leaves;
	private final BlockStateProvider vine;
	private final BlockStateProvider bug;

	private final BlockStateProvider dungeonWood;
	private final BlockStateProvider dungeonAir;
	private final BlockStateProvider dungeonLootBlock;
	private final ResourceKey<LootTable> dungeonLootTable;
	private final Holder<EntityType<?>> dungeonMonster;

	private final boolean allowInWater;

	public HollowTreeStructure(
		StructureSettings settings,
		DecorationConfig decorationConfig,
		IntProvider height,
		IntProvider radius,
		BlockStateProvider log,
		BlockStateProvider wood,
		BlockStateProvider root,
		BlockStateProvider leaves,
		BlockStateProvider vine,
		BlockStateProvider bug,
		BlockStateProvider dungeonWood,
		BlockStateProvider dungeonAir,
		BlockStateProvider dungeonLootBlock,
		ResourceKey<LootTable> dungeonLootTable,
		Holder<EntityType<?>> dungeonMonster,
		boolean allowInWater
	) {
		super(settings);

		this.decorationConfig = decorationConfig;

		this.height = height;
		this.radius = radius;

		this.log = log;
		this.wood = wood;
		this.root = root;
		this.leaves = leaves;
		this.vine = vine;
		this.bug = bug;

		this.dungeonWood = dungeonWood;
		this.dungeonAir = dungeonAir;
		this.dungeonLootBlock = dungeonLootBlock;
		this.dungeonLootTable = dungeonLootTable;
		this.dungeonMonster = dungeonMonster;

		this.allowInWater = allowInWater;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();

		RandomSource random = RandomSource.create(context.seed() + chunkPos.x * 25117L + chunkPos.z * 151121L);

		int x = SectionPos.sectionToBlockCoord(chunkPos.x, random.nextInt(16));
		int z = SectionPos.sectionToBlockCoord(chunkPos.z, random.nextInt(16));
		int seaFloorY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
		int worldY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

		int height = Math.min(this.height.sample(random) + worldY, context.heightAccessor().getMaxBuildHeight()) - worldY;

		if (height < 16 || (!this.allowInWater && seaFloorY < worldY))
			return Optional.empty();
		if (!this.getModifiedStructureSettings().biomes().contains(context.chunkGenerator().getBiomeSource().getNoiseBiome(x >> 2, worldY >> 2, z >> 2, context.randomState().sampler())))
			return Optional.empty();

		int radius = this.radius.sample(random);

		BoundingBox boundingBox = new BoundingBox(x - (radius + 1), worldY, z - (radius + 1), x + radius + 1, worldY + height, z + radius + 1);

		return Optional.of(new GenerationStub(new BlockPos(x, worldY, z), structurePiecesBuilder -> {
			StructurePiece piece = new HollowTreeTrunk(height, radius, boundingBox, this.log, this.wood, this.root, this.leaves, this.vine, this.bug, this.dungeonWood, this.dungeonAir, this.dungeonLootBlock, this.dungeonLootTable, this.dungeonMonster);

			structurePiecesBuilder.addPiece(piece);
			piece.addChildren(piece, structurePiecesBuilder, context.random());
		}));
	}

	@Override
	public boolean checkSaplingClearance(ServerLevel level, BlockPos pos) {
		int rangeClearance = 3;
		Iterable<BlockPos> blockPos = BlockPos.betweenClosed(pos.getX() - rangeClearance, pos.getY() - rangeClearance, pos.getZ() - rangeClearance, pos.getX() + rangeClearance, pos.getY() + 128, pos.getZ() + rangeClearance);

		for (BlockPos posCheck : blockPos) {
			if (posCheck.equals(pos))
				continue;

			if (!FeatureLogic.treesReplaceable(level.getBlockState(posCheck)))
				return false;
		}

		return true;
	}

	@Override
	public StructureStart generateFromSapling(RegistryAccess registryAccess, ChunkGenerator generator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager templateManager, long seed, BlockPos blockPos, LevelHeightAccessor heightAccessor) {
		ChunkPos chunkPos = new ChunkPos(blockPos);
		RandomSource random = RandomSource.create(seed + chunkPos.x * 25117L + chunkPos.z * 151121L);

		int height = Math.min(this.height.sample(random) + blockPos.getY(), heightAccessor.getMaxBuildHeight()) - blockPos.getY();

		if (height < 16)
			return StructureStart.INVALID_START;

		int radius = this.radius.sample(random);

		BoundingBox boundingBox = new BoundingBox(blockPos.getX() - radius - 1, blockPos.getY(), blockPos.getZ() - radius - 1, blockPos.getX() + radius + 1, blockPos.getY() + height, blockPos.getZ() + radius + 1);

		GenerationStub stub = new GenerationStub(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), structurePiecesBuilder -> {
			StructurePiece piece = new HollowTreeTrunk(height, radius, boundingBox, this.log, this.wood, this.root, this.leaves, this.vine, this.bug, this.dungeonWood, this.dungeonAir, this.dungeonLootBlock, this.dungeonLootTable, this.dungeonMonster);

			structurePiecesBuilder.addPiece(piece);
			piece.addChildren(piece, structurePiecesBuilder, random);
		});

		StructurePiecesBuilder structurepiecesbuilder = stub.getPiecesBuilder();
		StructureStart structurestart = new StructureStart(this, chunkPos, 0, structurepiecesbuilder.build());
		return structurestart.isValid() ? structurestart : StructureStart.INVALID_START;
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.HOLLOW_TREE.get();
	}

	@Override
	public boolean isSurfaceDecorationsAllowed() {
		return this.decorationConfig.surfaceDecorations();
	}

	@Override
	public boolean isUndergroundDecoAllowed() {
		return this.decorationConfig.undergroundDecorations();
	}

	@Override
	public boolean isGrassDecoAllowed() {
		return this.decorationConfig.vegetation();
	}

	@Override
	public boolean shouldAdjustToTerrain() {
		return true;
	}

	@Override
	public float chunkClearanceRadius() {
		return this.decorationConfig.chunkClearanceRadius();
	}

	public static HollowTreeStructure buildStructureConfig(boolean allowInWater, HolderSet<Biome> biomes) {
		return new HollowTreeStructure(
			new Structure.StructureSettings(
				biomes,
				Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.NONE
			),
			new DecorationClearance.DecorationConfig(2, false, true, true),
			HollowTreePiece.DEFAULT_HEIGHT,
			HollowTreePiece.DEFAULT_RADIUS,
			HollowTreePiece.DEFAULT_LOG,
			HollowTreePiece.DEFAULT_WOOD,
			HollowTreePiece.DEFAULT_ROOT,
			HollowTreePiece.DEFAULT_LEAVES,
			HollowTreePiece.DEFAULT_VINE,
			HollowTreePiece.DEFAULT_BUG,
			HollowTreePiece.DEFAULT_WOOD,
			HollowTreePiece.DEFAULT_DUNGEON_AIR,
			HollowTreePiece.DEFAULT_DUNGEON_LOOT_BLOCK,
			HollowTreePiece.DEFAULT_DUNGEON_LOOT_TABLE,
			HollowTreePiece.DEFAULT_DUNGEON_MONSTER,
			allowInWater
		);
	}
}
