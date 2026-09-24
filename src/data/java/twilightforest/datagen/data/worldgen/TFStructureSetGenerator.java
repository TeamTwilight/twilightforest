package twilightforest.datagen.data.worldgen;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import twilightforest.init.TFStructureSets;
import twilightforest.init.TFStructures;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TFStructureSetGenerator {

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        HolderGetter<StructureSet> structureSets = context.lookup(Registries.STRUCTURE_SET);

        List<StructureSet.StructureSelectionEntry> hollowTrees = List.of(
            new StructureSet.StructureSelectionEntry(structures.getOrThrow(TFStructures.HOLLOW_TREE), 1),
            new StructureSet.StructureSelectionEntry(structures.getOrThrow(TFStructures.SWAMP_HOLLOW_TREE), 1)
        );
        Holder.Reference<StructureSet> fallenTrunk = context.register(TFStructureSets.FALLEN_TRUNK, new StructureSet(structures.getOrThrow(TFStructures.FALLEN_TRUNK), new AvoidLandmarkGridPlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 0.8F, 1275623845, Optional.of(new StructurePlacement.ExclusionZone(structureSets.getOrThrow(TFStructureSets.HOLLOW_TREE), 1)), 7, 5, RandomSpreadType.TRIANGULAR, Optional.empty())));
        Holder.Reference<StructureSet> hollowTree = context.register(TFStructureSets.HOLLOW_TREE, new StructureSet(hollowTrees, new AvoidLandmarkGridPlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 0.5F, 34481210, Optional.empty(), 7, 5, RandomSpreadType.TRIANGULAR, Optional.empty())));

        Optional<AvoidLandmarkGridPlacement.AvoidAdditionalStructures> avoidTrees = Optional.of(new AvoidLandmarkGridPlacement.AvoidAdditionalStructures(new Object2IntArrayMap<>(Map.of(
            fallenTrunk, 2,
            hollowTree, 1
        ))));
        context.register(TFStructureSets.CAMP, new StructureSet(structures.getOrThrow(TFStructures.CAMP), new AvoidLandmarkGridPlacement(18, 14, RandomSpreadType.TRIANGULAR, 701432212, avoidTrees)));

        context.register(TFStructureSets.HEDGE_MAZE, new StructureSet(structures.getOrThrow(TFStructures.HEDGE_MAZE), new LandmarkGridPlacement(Optional.of(TFStructures.HEDGE_MAZE))));
        context.register(TFStructureSets.HOLLOW_HILL_SMALL, new StructureSet(structures.getOrThrow(TFStructures.HOLLOW_HILL_SMALL), new LandmarkGridPlacement(Optional.of(TFStructures.HOLLOW_HILL_SMALL))));
        context.register(TFStructureSets.HOLLOW_HILL_MEDIUM, new StructureSet(structures.getOrThrow(TFStructures.HOLLOW_HILL_MEDIUM), new LandmarkGridPlacement(Optional.of(TFStructures.HOLLOW_HILL_MEDIUM))));
        context.register(TFStructureSets.HOLLOW_HILL_LARGE, new StructureSet(structures.getOrThrow(TFStructures.HOLLOW_HILL_LARGE), new LandmarkGridPlacement(Optional.of(TFStructures.HOLLOW_HILL_LARGE))));
        context.register(TFStructureSets.NAGA_COURTYARD, new StructureSet(structures.getOrThrow(TFStructures.NAGA_COURTYARD), new LandmarkGridPlacement(Optional.of(TFStructures.NAGA_COURTYARD))));
        context.register(TFStructureSets.LICH_TOWER, new StructureSet(structures.getOrThrow(TFStructures.LICH_TOWER), new LandmarkGridPlacement(Optional.of(TFStructures.LICH_TOWER))));

        context.register(TFStructureSets.QUEST_GROVE, new StructureSet(structures.getOrThrow(TFStructures.QUEST_GROVE), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.LABYRINTH, new StructureSet(structures.getOrThrow(TFStructures.LABYRINTH), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.HYDRA_LAIR, new StructureSet(structures.getOrThrow(TFStructures.HYDRA_LAIR), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.KNIGHT_STRONGHOLD, new StructureSet(structures.getOrThrow(TFStructures.KNIGHT_STRONGHOLD), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.DARK_TOWER, new StructureSet(structures.getOrThrow(TFStructures.DARK_TOWER), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.YETI_CAVE, new StructureSet(structures.getOrThrow(TFStructures.YETI_CAVE), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.AURORA_PALACE, new StructureSet(structures.getOrThrow(TFStructures.AURORA_PALACE), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.TROLL_CAVE, new StructureSet(structures.getOrThrow(TFStructures.TROLL_CAVE), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.GIANT_HOUSE, new StructureSet(structures.getOrThrow(TFStructures.GIANT_HOUSE), LandmarkGridPlacement.forceStructureForCenters()));
        context.register(TFStructureSets.FINAL_CASTLE, new StructureSet(structures.getOrThrow(TFStructures.FINAL_CASTLE), LandmarkGridPlacement.forceStructureForCenters()));

        // uncomment to include in mod-internal datapack, for worldgen
        //context.register(MUSHROOM_TOWER, new StructureSet(structures.getOrThrow(TFStructures.MUSHROOM_TOWER), LandmarkGridPlacement.forceStructureForCenters()));
        //context.register(QUEST_ISLAND, new StructureSet(structures.getOrThrow(TFStructures.QUEST_ISLAND), LandmarkGridPlacement.forceStructureForCenters()));
        //context.register(DRUID_GROVE, new StructureSet(structures.getOrThrow(TFStructures.DRUID_GROVE), LandmarkGridPlacement.forceStructureForCenters()));
        //context.register(FLOATING_RUINS, new StructureSet(structures.getOrThrow(TFStructures.FLOATING_RUINS), LandmarkGridPlacement.forceStructureForCenters()));
        //context.register(WORLD_TREE, new StructureSet(structures.getOrThrow(TFStructures.WORLD_TREE), LandmarkGridPlacement.forceStructureForCenters()));
    }

}
