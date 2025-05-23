package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.type.*;

import java.util.function.Supplier;

public class TFStructureTypes {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<StructureType<?>, StructureType<ProgressionWrappedStructure>> PROGRESSION_WRAPPED = registerType("progression", () -> () -> ProgressionWrappedStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<FallenTrunkStructure>> FALLEN_TRUNK = registerType("fallen_trunk", () -> () -> FallenTrunkStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<HollowTreeStructure>> HOLLOW_TREE = registerType("hollow_tree", () -> () -> HollowTreeStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<HedgeMazeStructure>> HEDGE_MAZE = registerType("hedge_maze", () -> () -> HedgeMazeStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<HollowHillStructure>> HOLLOW_HILL = registerType("hollow_hill", () -> () -> HollowHillStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<QuestGroveStructure>> QUEST_GROVE = registerType("quest_grove", () -> () -> QuestGroveStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<MushroomTowerStructure>> MUSHROOM_TOWER = registerType("mushroom_tower", () -> () -> MushroomTowerStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<NagaCourtyardStructure>> NAGA_COURTYARD = registerType("naga_courtyard", () -> () -> NagaCourtyardStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<LichTowerStructure>> LICH_TOWER = registerType("lich_tower", () -> () -> LichTowerStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<LabyrinthStructure>> LABYRINTH = registerType("labyrinth", () -> () -> LabyrinthStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<HydraLairStructure>> HYDRA_LAIR = registerType("hydra_lair", () -> () -> HydraLairStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<KnightStrongholdStructure>> KNIGHT_STRONGHOLD = registerType("knight_stronghold", () -> () -> KnightStrongholdStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<DarkTowerStructure>> DARK_TOWER = registerType("dark_tower", () -> () -> DarkTowerStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<YetiCaveStructure>> YETI_CAVE = registerType("yeti_cave", () -> () -> YetiCaveStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<AuroraPalaceStructure>> AURORA_PALACE = registerType("aurora_palace", () -> () -> AuroraPalaceStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<TrollCaveStructure>> TROLL_CAVE = registerType("troll_cave", () -> () -> TrollCaveStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<GiantHouseStructure>> GIANT_HOUSE = registerType("giant_house", () -> () -> GiantHouseStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<FinalCastleStructure>> FINAL_CASTLE = registerType("final_castle", () -> () -> FinalCastleStructure.CODEC);

	private static <P extends Structure> DeferredHolder<StructureType<?>, StructureType<P>> registerType(String name, Supplier<StructureType<P>> factory) {
		return STRUCTURE_TYPES.register(name, factory);
	}
}
