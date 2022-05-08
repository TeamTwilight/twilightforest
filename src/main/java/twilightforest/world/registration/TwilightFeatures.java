package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;
import twilightforest.world.components.placements.OutOfStructureFilter;

import java.util.function.Supplier;

public final class TwilightFeatures {

    public static long seed;

    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, TwilightForestMod.ID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, TwilightForestMod.ID);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, TwilightForestMod.ID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registry.TRUNK_PLACER_TYPE_REGISTRY, TwilightForestMod.ID);

    public static final RegistryObject<TrunkPlacerType<BranchingTrunkPlacer>> TRUNK_BRANCHING = TRUNK_PLACERS.register("branching_trunk_placer", () -> new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<TrunkRiser>> TRUNK_RISER = TRUNK_PLACERS.register("trunk_mover_upper", () -> new TrunkPlacerType<>(TrunkRiser.CODEC));

    public static final RegistryObject<FoliagePlacerType<LeafSpheroidFoliagePlacer>> FOLIAGE_SPHEROID = FOLIAGE_PLACERS.register("spheroid_foliage_placer", () -> new FoliagePlacerType<>(LeafSpheroidFoliagePlacer.CODEC));

    public static final RegistryObject<TreeDecoratorType<TreeCorePlacer>> CORE_PLACER = TREE_DECORATORS.register("core_placer", () -> new TreeDecoratorType<>(TreeCorePlacer.CODEC));
    public static final RegistryObject<TreeDecoratorType<TrunkSideDecorator>> TRUNKSIDE_DECORATOR = TREE_DECORATORS.register("trunkside_decorator", () -> new TreeDecoratorType<>(TrunkSideDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<TreeRootsDecorator>> TREE_ROOTS = TREE_DECORATORS.register("tree_roots", () -> new TreeDecoratorType<>(TreeRootsDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<DangleFromTreeDecorator>> DANGLING_DECORATOR = TREE_DECORATORS.register("dangle_from_tree_decorator", () -> new TreeDecoratorType<>(DangleFromTreeDecorator.CODEC));

    public static final RegistryObject<PlacementModifierType<OutOfStructureFilter>> NO_STRUCTURE = registerPlacer("no_structure", () -> () -> OutOfStructureFilter.CODEC);
    public static final RegistryObject<PlacementModifierType<ChunkCenterModifier>> CHUNK_CENTERER = registerPlacer("chunk_centerer", () -> () -> ChunkCenterModifier.CODEC);
    public static final RegistryObject<PlacementModifierType<ChunkBlanketingModifier>> CHUNK_BLANKETING = registerPlacer("chunk_blanketing", () -> () -> ChunkBlanketingModifier.CODEC);

    //goofy but needed
    private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> registerPlacer(String name, Supplier<PlacementModifierType<P>> factory) {
        return PLACEMENT_MODIFIERS.register(name, factory);
    }
}
