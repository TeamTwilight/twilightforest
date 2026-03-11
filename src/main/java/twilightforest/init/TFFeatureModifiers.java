package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;

import java.util.function.Supplier;

public final class TFFeatureModifiers {

	public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, TwilightForestMod.ID);
	public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<BranchingTrunkPlacer>> TRUNK_BRANCHING = TRUNK_PLACERS.register("branching_trunk_placer", () -> new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC));
	public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<TrunkRiser>> TRUNK_RISER = TRUNK_PLACERS.register("trunk_mover_upper", () -> new TrunkPlacerType<>(TrunkRiser.CODEC));

	public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<LeafSpheroidFoliagePlacer>> FOLIAGE_SPHEROID = FOLIAGE_PLACERS.register("spheroid_foliage_placer", () -> new FoliagePlacerType<>(LeafSpheroidFoliagePlacer.CODEC));

	public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<TreeCorePlacer>> CORE_PLACER = TREE_DECORATORS.register("core_placer", () -> new TreeDecoratorType<>(TreeCorePlacer.CODEC));
	public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<TrunkSideDecorator>> TRUNKSIDE_DECORATOR = TREE_DECORATORS.register("trunkside_decorator", () -> new TreeDecoratorType<>(TrunkSideDecorator.CODEC));
	public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<TreeRootsDecorator>> TREE_ROOTS = TREE_DECORATORS.register("tree_roots", () -> new TreeDecoratorType<>(TreeRootsDecorator.CODEC));
	public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<DangleFromTreeDecorator>> DANGLING_DECORATOR = TREE_DECORATORS.register("dangle_from_tree_decorator", () -> new TreeDecoratorType<>(DangleFromTreeDecorator.CODEC));

	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<AvoidLandmarkModifier>> NO_STRUCTURE = registerPlacer("no_structure", () -> () -> AvoidLandmarkModifier.CODEC);
	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<ChunkCenterModifier>> CHUNK_CENTERER = registerPlacer("chunk_centerer", () -> () -> ChunkCenterModifier.CODEC);
	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<ChunkBlanketingModifier>> CHUNK_BLANKETING = registerPlacer("chunk_blanketing", () -> () -> ChunkBlanketingModifier.CODEC);

	//goofy but needed
	private static <P extends PlacementModifier> DeferredHolder<PlacementModifierType<?>, PlacementModifierType<P>> registerPlacer(String name, Supplier<PlacementModifierType<P>> factory) {
		return PLACEMENT_MODIFIERS.register(name, factory);
	}
}
