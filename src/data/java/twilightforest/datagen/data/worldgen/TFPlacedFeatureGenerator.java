package twilightforest.datagen.data.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Util;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.util.ConcatenatedListView;
import twilightforest.init.*;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;

import java.util.List;

public class TFPlacedFeatureGenerator {
    private static List<PlacementModifier> tfTreeCheckArea(BlockState sapling) {
        return tfTreeCheckArea(sapling, HolderSet.empty());
    }

    private static List<PlacementModifier> tfTreeCheckArea(BlockState sapling, HolderSet<Structure> structuresAllowed) {
        return List.of(InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(structuresAllowed), PlacementUtils.filteredByBlockSurvival(sapling.getBlock()), BiomeFilter.biome());
    }

    private static List<PlacementModifier> tfTreeCheckArea(PlacementModifier count, BlockState sapling) {
        return tfTreeCheckArea(count, sapling, HolderSet.empty());
    }

    private static List<PlacementModifier> tfTreeCheckArea(PlacementModifier count, BlockState sapling, HolderSet<Structure> structuresAllowed) {
        return ImmutableList.of(count, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(structuresAllowed), PlacementUtils.filteredByBlockSurvival(sapling.getBlock()), BiomeFilter.biome());
    }

    private static List<PlacementModifier> darkForestTreeCheck(PlacementModifier count) {
        return ImmutableList.of(count, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, new AvoidLandmarkModifier(true, false, 10, HolderSet.empty()), BiomeFilter.biome());
    }

    private static PlacementModifier darkForestFloorScan() {
        return EnvironmentScanPlacement.scanningFor(
            Direction.DOWN,
            BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD)),
            BlockPredicate.anyOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(BlockTags.LEAVES), BlockPredicate.matchesBlocks(TFBlocks.HARDENED_DARK_LEAVES.get())),
            32
        );
    }

    private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(AvoidLandmarkModifier filter, int rarity) {
        return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter, BiomeFilter.biome());
    }

    private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(AvoidLandmarkModifier filter, int rarity, PlacementModifier... extra) {
        return ImmutableList.<PlacementModifier>builder().add(extra).add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, filter, BiomeFilter.biome());
    }

    private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(AvoidLandmarkModifier filter, int rarity, VerticalAnchor minAnchor, VerticalAnchor maxAnchor, PlacementModifier... extra) {
        return ImmutableList.<PlacementModifier>builder().add(extra).add(filter, RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), HeightRangePlacement.uniform(minAnchor, maxAnchor), BiomeFilter.biome());
    }

    private static ImmutableList.Builder<PlacementModifier> hollowLog(AvoidLandmarkModifier filter) {
        return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter, BiomeFilter.biome());
    }

    private static ImmutableList<PlacementModifier> oreberry(int rarity) {
        return tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), rarity, VerticalAnchor.absolute(-10), VerticalAnchor.absolute(-8)).build();
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        AvoidLandmarkModifier avoidLichTower = AvoidLandmarkModifier.checkVegetation();
        AvoidLandmarkModifier avoidStrongholdPieces = new AvoidLandmarkModifier(false, false, true, 1, HolderSet.empty());
        Holder.Reference<Structure> lichTowerHolder = context.lookup(Registries.STRUCTURE).getOrThrow(TFStructures.LICH_TOWER);
        HolderSet.Direct<Structure> allowLichTower = HolderSet.direct(lichTowerHolder);

        context.register(TFPlacedFeatures.PLACED_LAKE_LAVA, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_LAVA), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 10).build()));
        context.register(TFPlacedFeatures.PLACED_LAKE_WATER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_WATER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 32).build()));
        context.register(TFPlacedFeatures.PLACED_LAKE_FROZEN, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_FROZEN), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 4).build()));
        context.register(TFPlacedFeatures.PLACED_DRUID_HUT, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DRUID_HUT), tfFeatureCheckArea(new AvoidLandmarkModifier(true, true, 16, HolderSet.empty()), 105).build()));
        context.register(TFPlacedFeatures.PLACED_DENSE_FERNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DENSE_FERNS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.countExtra(3, 0.5F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), CountPlacement.of(64), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidLichTower).build()));
        context.register(TFPlacedFeatures.PLACED_DENSE_LARGE_FERNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DENSE_LARGE_FERNS), ImmutableList.<PlacementModifier>builder().add(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), CountPlacement.of(64), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidLichTower).build()));
        context.register(TFPlacedFeatures.PLACED_DENSE_LAKE_WATER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_WATER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 4).build()));
        context.register(TFPlacedFeatures.PLACED_GRAVEYARD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.GRAVEYARD), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 70).build()));
        context.register(TFPlacedFeatures.PLACED_BIG_MUSHGLOOM, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.BIG_MUSHGLOOM), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 1).build()));
        context.register(TFPlacedFeatures.PLACED_FALLEN_LEAVES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FALLEN_LEAVES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 1).build()));
        context.register(TFPlacedFeatures.PLACED_FIDDLEHEAD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FIDDLEHEAD), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), CountPlacement.of(96), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)).build()));
        context.register(TFPlacedFeatures.PLACED_FIRE_JET, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FIRE_JET), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_FOUNDATION, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FOUNDATION), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 90).build()));
        context.register(TFPlacedFeatures.PLACED_GROVE_RUINS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.GROVE_RUINS), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 110).build()));
        context.register(TFPlacedFeatures.PLACED_HOLLOW_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HOLLOW_LOG), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 85).build()));
        context.register(TFPlacedFeatures.PLACED_HOLLOW_STUMP, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HOLLOW_STUMP), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 80).build()));
        context.register(TFPlacedFeatures.PLACED_HUGE_LILY_PAD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HUGE_LILY_PAD), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), CountPlacement.of(10), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_HUGE_WATER_LILY, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HUGE_WATER_LILY), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_MAYAPPLE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MAYAPPLE), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), CountPlacement.of(96), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower).build()));
        context.register(TFPlacedFeatures.PLACED_MONOLITH, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MONOLITH), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 90).build()));
        context.register(TFPlacedFeatures.PLACED_MUSHGLOOM_CLUSTER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MUSHGLOOM_CLUSTER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.filteredByBlockSurvival(TFBlocks.MUSHGLOOM.get()), BiomeFilter.biome(), CountPlacement.of(96), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower).build()));
        context.register(TFPlacedFeatures.PLACED_SPARSE_MUSHGLOOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MUSHGLOOM_CLUSTER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(15), InSquarePlacement.spread(), PlacementUtils.filteredByBlockSurvival(TFBlocks.MUSHGLOOM.get()), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_MYCELIUM_BLOB, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MYCELIUM_BLOB), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 3).build()));
        context.register(TFPlacedFeatures.PLACED_OUTSIDE_STALAGMITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.OUTSIDE_STALAGMITE), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 77).build()));
        context.register(TFPlacedFeatures.PLACED_PLANT_ROOTS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.PLANT_ROOTS), tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), 1, CountPlacement.of(4), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(10))).build()));

        // Rarity was determined through empirical experiments, so the number of oreberries per chunk is 0.2
        context.register(TFPlacedFeatures.PLACED_IRON_OREBERRIES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.IRON_OREBERRIES), oreberry(3)));
        context.register(TFPlacedFeatures.PLACED_GOLD_OREBERRIES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.GOLD_OREBERRIES), oreberry(3)));
        context.register(TFPlacedFeatures.PLACED_COPPER_OREBERRIES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.COPPER_OREBERRIES), oreberry(3)));
        context.register(TFPlacedFeatures.PLACED_IRON_OREBERRIES_ENCHANTED_FOREST, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.IRON_OREBERRIES), oreberry(5)));
        context.register(TFPlacedFeatures.PLACED_GOLD_OREBERRIES_ENCHANTED_FOREST, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.GOLD_OREBERRIES), oreberry(5)));
        context.register(TFPlacedFeatures.PLACED_COPPER_OREBERRIES_ENCHANTED_FOREST, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.COPPER_OREBERRIES), oreberry(5)));
        context.register(TFPlacedFeatures.PLACED_ESSENCE_OREBERRIES_ENCHANTED_FOREST, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.ESSENCE_OREBERRIES), oreberry(5)));

        context.register(TFPlacedFeatures.PLACED_RASPBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.RASPBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 50, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_SWAMP_RASPBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.RASPBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 200, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_BLUEBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.BLUEBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 50, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_SNOWY_BLUEBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.BLUEBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 100, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_BLACKBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.BLACKBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 200, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_SWAMP_BLACKBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.BLACKBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 25, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_MALOBERRY_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MALOBERRY_BUSHES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 25, PlacementUtils.HEIGHTMAP_TOP_SOLID).build()));
        context.register(TFPlacedFeatures.PLACED_PUMPKIN_LAMPPOST, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.PUMPKIN_LAMPPOST), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 10).build()));
        context.register(TFPlacedFeatures.PLACED_SMOKER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMOKER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_STONE_CIRCLE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.STONE_CIRCLE), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 105).build()));
        context.register(TFPlacedFeatures.PLACED_THORNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.THORNS), ImmutableList.<PlacementModifier>builder().add(ChunkBlanketingModifier.addThorns(HolderSet.direct(biomes.getOrThrow(TFBiomes.THORNLANDS))), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_TORCH_BERRIES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TORCH_BERRIES), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)), CountPlacement.of(8), InSquarePlacement.spread(), AvoidLandmarkModifier.checkUnderground(), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_TROLL_ROOTS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TROLL_ROOTS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountOnEveryLayerPlacement.of(12), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_TROLL_MUSHGLOOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TROLL_MUSHGLOOMS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), CountPlacement.of(3), AvoidLandmarkModifier.checkUnderground(), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_VANILLA_ROOTS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.VANILLA_ROOTS), tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), 1, CountPlacement.of(16), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0)), PlacementUtils.filteredByBlockSurvival(TFBlocks.TORCHBERRY_PLANT.get())).build()));
        context.register(TFPlacedFeatures.PLACED_WEBS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.WEBS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(60), InSquarePlacement.spread(), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_WOOD_ROOTS_SPREAD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.WOOD_ROOTS_SPREAD), tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), 40, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0))).build()));

        context.register(TFPlacedFeatures.PLACED_SNOW_UNDER_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SNOW_UNDER_TREES), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_TF_OAK_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TF_OAK_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
        context.register(TFPlacedFeatures.PLACED_CANOPY_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
        context.register(TFPlacedFeatures.PLACED_MANGROVE_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MANGROVE_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
        context.register(TFPlacedFeatures.PLACED_SPRUCE_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SPRUCE_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
        context.register(TFPlacedFeatures.PLACED_SMALL_GRANITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMALL_GRANITE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)), RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_SMALL_DIORITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMALL_DIORITE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)), RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_SMALL_ANDESITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMALL_ANDESITE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)), RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));

        context.register(TFPlacedFeatures.PLACED_DARK_PUMPKINS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_PUMPKINS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(50), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));
        context.register(TFPlacedFeatures.PLACED_DARK_GRASS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_GRASS), ImmutableList.<PlacementModifier>builder().add(CountPlacement.of(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(64), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));
        context.register(TFPlacedFeatures.PLACED_DARK_FERNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_FERNS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(64), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));
        context.register(TFPlacedFeatures.PLACED_DARK_MUSHGLOOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_MUSHGLOOMS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(50), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));
        context.register(TFPlacedFeatures.PLACED_DARK_BROWN_MUSHROOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_BROWN_MUSHROOMS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(50), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));
        context.register(TFPlacedFeatures.PLACED_DARK_RED_MUSHROOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_RED_MUSHROOMS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(50), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));
        context.register(TFPlacedFeatures.PLACED_DARK_DEAD_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_DEAD_BUSHES), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, darkForestFloorScan(), BiomeFilter.biome(), CountPlacement.of(50), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.SUBSTRATE_OVERWORLD))), avoidStrongholdPieces).build()));

        context.register(TFPlacedFeatures.PLACED_ENCHANTED_FOREST_VINES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.ENCHANTED_FOREST_VINES), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP, BiomeFilter.biome()).build()));

        context.register(TFPlacedFeatures.PLACED_LEGACY_COAL_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_COAL_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(127)), InSquarePlacement.spread(), CountPlacement.of(20), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_LEGACY_IRON_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_IRON_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(63)), InSquarePlacement.spread(), CountPlacement.of(20), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_LEGACY_GOLD_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_GOLD_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(31)), InSquarePlacement.spread(), CountPlacement.of(2), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_LEGACY_REDSTONE_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_REDSTONE_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), InSquarePlacement.spread(), CountPlacement.of(8), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_LEGACY_DIAMOND_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_DIAMOND_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), InSquarePlacement.spread(), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_LEGACY_LAPIS_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_LAPIS_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(30)), InSquarePlacement.spread(), CountPlacement.of(2), BiomeFilter.biome()).build()));
        context.register(TFPlacedFeatures.PLACED_LEGACY_COPPER_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_COPPER_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(96)), InSquarePlacement.spread(), CountPlacement.of(6), BiomeFilter.biome()).build()));

        context.register(TFPlacedFeatures.PLACED_WELL_PLACER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.WELL_PLACER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 80).build()));
        context.register(TFPlacedFeatures.PLACED_LAMPPOST_PLACER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAMPPOST_PLACER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 2).build()));
        context.register(TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DEFAULT_FALLEN_LOGS), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 40).build()));

        context.register(TFPlacedFeatures.PLACED_FLOWER_PLACER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FLOWER_PLACER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(3), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(2), BiomeFilter.biome(), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 7), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower).build()));
        context.register(TFPlacedFeatures.PLACED_FLOWER_PLACER_ALT, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FLOWER_PLACER_ALT), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(3), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(2), BiomeFilter.biome(), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 7), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower).build()));

        context.register(TFPlacedFeatures.PLACED_DEAD_CANOPY_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DEAD_CANOPY_TREE), tfTreeCheckArea(PlacementUtils.countExtra(2, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_MEGA_CANOPY_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MEGA_CANOPY_TREE), ImmutableList.of(ChunkCenterModifier.center(), RarityFilter.onAverageOnceEvery(20), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), PlacementUtils.filteredByBlockSurvival(TFBlocks.CANOPY_SAPLING.get().defaultBlockState().getBlock()), BiomeFilter.biome())));

        context.register(TFPlacedFeatures.PLACED_MANGROVE_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MANGROVE_TREE), List.of(PlacementUtils.countExtra(3, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(6), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), PlacementUtils.filteredByBlockSurvival(TFBlocks.MANGROVE_SAPLING.get()), BiomeFilter.biome(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE))); // Check Ocean floor first for the sapling check then shift to water level as Surface_WG
        context.register(TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(1, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(1, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_FOREST_MEGA_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FOREST_MEGA_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(7, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
        context.register(TFPlacedFeatures.PLACED_SAVANNAH_MEGA_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SAVANNAH_MEGA_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(0, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
        context.register(TFPlacedFeatures.PLACED_SAVANNAH_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(1, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_SWAMPY_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SWAMPY_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(4, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
        context.register(TFPlacedFeatures.PLACED_OAK_BUSH, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.OAK_BUSH), tfTreeCheckArea(PlacementUtils.countExtra(1, 1 / 3F, 1), Blocks.OAK_SAPLING.defaultBlockState())));  // 0.33F raise exception
        context.register(TFPlacedFeatures.PLACED_OAK_BUSH_DENSE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.OAK_BUSH), tfTreeCheckArea(PlacementUtils.countExtra(2, 1 / 3F, 2), Blocks.OAK_SAPLING.defaultBlockState())));  // 0.33F raise exception
        context.register(TFPlacedFeatures.PLACED_DARKWOOD_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARKWOOD_TREE), List.of(PlacementUtils.countExtra(5, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, new AvoidLandmarkModifier(true, false, 16, HolderSet.empty()), PlacementUtils.filteredByBlockSurvival(TFBlocks.DARKWOOD_SAPLING.get()), BiomeFilter.biome())));

        context.register(TFPlacedFeatures.PLACED_CANOPY_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_TREES), tfTreeCheckArea(PlacementUtils.countExtra(7, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_DENSE_CANOPY_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DENSE_CANOPY_TREES), tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_FIREFLY_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FIREFLY_FOREST_TREES), tfTreeCheckArea(PlacementUtils.countExtra(3, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_DARK_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARKWOOD_TREE), darkForestTreeCheck(PlacementUtils.countExtra(8, 0.1F, 1))));
        context.register(TFPlacedFeatures.PLACED_DARK_FOREST_TREE_MIX, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_FOREST_TREES), darkForestTreeCheck(PlacementUtils.countExtra(3, 0.1F, 1))));
        context.register(TFPlacedFeatures.PLACED_HIGHLANDS_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HIGHLANDS_TREES), tfTreeCheckArea(PlacementUtils.countExtra(3, 0.1F, 1), Blocks.SPRUCE_SAPLING.defaultBlockState())));
        context.register(TFPlacedFeatures.PLACED_ENCHANTED_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.ENCHANTED_FOREST_TREES), tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.RAINBOW_OAK_SAPLING.get().defaultBlockState())));
        context.register(TFPlacedFeatures.PLACED_SNOWY_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SNOWY_FOREST_TREES), List.of(PlacementUtils.countExtra(10, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.POWDER_SNOW)), 8), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW)), BiomeFilter.biome())));
        context.register(TFPlacedFeatures.PLACED_VANILLA_TF_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.VANILLA_TF_TREES), tfTreeCheckArea(TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState(), allowLichTower)));
        context.register(TFPlacedFeatures.PLACED_VANILLA_TF_BIG_MUSH, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.VANILLA_TF_BIG_MUSH), tfTreeCheckArea(TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));

        context.register(TFPlacedFeatures.PLACED_CANOPY_MUSHROOMS_SPARSE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_MUSHROOMS_SPARSE), tfTreeCheckArea(PlacementUtils.countExtra(3, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
        context.register(TFPlacedFeatures.PLACED_CANOPY_MUSHROOMS_DENSE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_MUSHROOMS_DENSE), tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));

        List<PlacementModifier> avoidLichTowerList = List.of(avoidLichTower);

        Holder<ConfiguredFeature<?, ?>> grassConfig = features.getOrThrow(VegetationFeatures.GRASS);
        // PlacementUtils.register(context, PATCH_GRASS_PLAIN, grassConfig, NoiseThresholdCountPlacement.of(-0.8, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), avoidLichTower);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_GRASS_FOREST, grassConfig, ConcatenatedListView.of(Util.copyAndAdd(VegetationPlacements.worldSurfaceSquaredWithCount(2), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)), avoidLichTowerList));
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_GRASS_BADLANDS, grassConfig, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_GRASS_SAVANNA, grassConfig, ConcatenatedListView.of(Util.copyAndAdd(VegetationPlacements.worldSurfaceSquaredWithCount(20), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)), avoidLichTowerList));
        // PlacementUtils.register(context, PATCH_GRASS_NORMAL, grassConfig, ConcatenatedListView.of(VegetationPlacements.worldSurfaceSquaredWithCount(5), avoidLichTowerList));

        Holder<ConfiguredFeature<?, ?>> taigaGrassConfig = features.getOrThrow(VegetationFeatures.TAIGA_GRASS);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_GRASS_TAIGA_2, taigaGrassConfig, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_GRASS_TAIGA, taigaGrassConfig, ConcatenatedListView.of(Util.copyAndAdd(VegetationPlacements.worldSurfaceSquaredWithCount(7), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)), avoidLichTowerList));

        Holder<ConfiguredFeature<?, ?>> jungleGrassConfig = features.getOrThrow(VegetationFeatures.GRASS_JUNGLE);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_GRASS_JUNGLE, jungleGrassConfig, ConcatenatedListView.of(Util.copyAndAdd(VegetationPlacements.worldSurfaceSquaredWithCount(25), CountPlacement.of(32), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)), avoidLichTowerList));

        Holder<ConfiguredFeature<?, ?>> tallGrassConfig = features.getOrThrow(VegetationFeatures.TALL_GRASS);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_TALL_GRASS, tallGrassConfig, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), CountPlacement.of(96), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower);

        Holder<ConfiguredFeature<?, ?>> fernConfig = features.getOrThrow(VegetationFeatures.LARGE_FERN);
        PlacementUtils.register(context, TFPlacedFeatures.PATCH_LARGE_FERN, fernConfig, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), CountPlacement.of(96), RandomOffsetPlacement.ofTriangle(7, 3), BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE), avoidLichTower);

        Holder<ConfiguredFeature<?, ?>> forestFlowersConfig = features.getOrThrow(VegetationFeatures.FOREST_FLOWERS);
        PlacementUtils.register(context,
            TFPlacedFeatures.FLOWER_FOREST_FLOWERS,
            forestFlowersConfig,
            RarityFilter.onAverageOnceEvery(7),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 3), 0, 3)),
            BiomeFilter.biome(),
            avoidLichTower);
    }

}
