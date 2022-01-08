package twilightforest.world.registration;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;
import twilightforest.world.components.placements.OutOfStructureFilter;

public final class TwilightFeatures {
    public static final TrunkPlacerType<BranchingTrunkPlacer> TRUNK_BRANCHING = registerTrunk(TwilightForestMod.prefix("branching_trunk_placer"), BranchingTrunkPlacer.CODEC);
    public static final TrunkPlacerType<TrunkRiser> TRUNK_RISER = registerTrunk(TwilightForestMod.prefix("trunk_mover_upper"), TrunkRiser.CODEC);

    public static final FoliagePlacerType<LeafSpheroidFoliagePlacer> FOLIAGE_SPHEROID = registerFoliage(TwilightForestMod.prefix("spheroid_foliage_placer"), LeafSpheroidFoliagePlacer.CODEC);

    public static final TreeDecoratorType<TreeCorePlacer> CORE_PLACER = registerTreeFeature(TwilightForestMod.prefix("core_placer"), TreeCorePlacer.CODEC);
    public static final TreeDecoratorType<TrunkSideDecorator> TRUNKSIDE_DECORATOR = registerTreeFeature(TwilightForestMod.prefix("trunkside_decorator"), TrunkSideDecorator.CODEC);
    public static final TreeDecoratorType<TreeRootsDecorator> TREE_ROOTS = registerTreeFeature(TwilightForestMod.prefix("tree_roots"), TreeRootsDecorator.CODEC);
    public static final TreeDecoratorType<DangleFromTreeDecorator> DANGLING_DECORATOR = registerTreeFeature(TwilightForestMod.prefix("dangle_from_tree_decorator"), DangleFromTreeDecorator.CODEC);

    public static final PlacementModifierType<OutOfStructureFilter> NO_STRUCTURE = registerPlacer("no_structure", OutOfStructureFilter.CODEC);
    public static final PlacementModifierType<ChunkCenterModifier> CHUNK_CENTERER = registerPlacer("chunk_centerer", ChunkCenterModifier.CODEC);
    public static final PlacementModifierType<ChunkBlanketingModifier> CHUNK_BLANKETING = registerPlacer("chunk_blanketing", ChunkBlanketingModifier.CODEC);

    private static <P extends PlacementModifier> PlacementModifierType<P> registerPlacer(String name, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIERS, TwilightForestMod.prefix(name), () -> codec);
    }

    private static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(ResourceLocation name, Codec<P> codec) {
        //TRUNK REPLACER register forge side is current not good
        FoliagePlacerType<P> type = new FoliagePlacerType<>(codec);
        return Registry.register(Registry.FOLIAGE_PLACER_TYPES, name, type);
    }

    private static <P extends TrunkPlacer> TrunkPlacerType<P> registerTrunk(ResourceLocation name, Codec<P> codec) {
        // TRUNK_REPLACER is wrong, it only places, not replacing
        return Registry.register(Registry.TRUNK_PLACER_TYPES, name, new TrunkPlacerType<>(codec));
    }

    private static <P extends TreeDecorator> TreeDecoratorType<P> registerTreeFeature(ResourceLocation name, Codec<P> codec) {
        //TREE_DECORATOR_TYPES register forge side is current not good
        TreeDecoratorType<P> type = new TreeDecoratorType<>(codec);
        return Registry.register(Registry.TREE_DECORATOR_TYPES, name, type);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> ConfiguredFeature<FC, F> registerWorldFeature(ResourceLocation rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, rl, feature);
    }

    public static PlacedFeature registerWorldFeature(ResourceLocation rl, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, rl, feature);
    }
}
