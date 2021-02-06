package twilightforest;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import twilightforest.structures.courtyard.NagaCourtyardPieces;
import twilightforest.structures.darktower.TFDarkTowerPieces;
import twilightforest.structures.finalcastle.TFFinalCastlePieces;
import twilightforest.structures.icetower.TFIceTowerPieces;
import twilightforest.structures.lichtower.TFLichTowerPieces;
import twilightforest.structures.minotaurmaze.TFMinotaurMazePieces;
import twilightforest.structures.mushroomtower.TFMushroomTowerPieces;
import twilightforest.structures.start.TFStructure;
import twilightforest.structures.stronghold.TFStrongholdPieces;
import twilightforest.structures.trollcave.TFTrollCavePieces;
import twilightforest.world.ChunkGeneratorTwilightBase;

import java.util.HashMap;
import java.util.Map;

public class TFStructures {

	private static final Map<Structure<?>, StructureSeparationSettings> SEPARATION_SETTINGS = new HashMap<>();

	public static final Structure<NoFeatureConfig> HEDGE_MAZE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.HEDGE_MAZE);
	public static final StructureFeature<?, ?> CONFIGURED_HEDGE_MAZE = HEDGE_MAZE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> QUEST_GROVE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.QUEST_GROVE);
	public static final StructureFeature<?, ?> CONFIGURED_QUEST_GROVE = QUEST_GROVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> MUSHROOM_TOWER = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.MUSHROOM_TOWER);
	public static final StructureFeature<?, ?> CONFIGURED_MUSHROOM_TOWER = MUSHROOM_TOWER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> HOLLOW_HILL_SMALL = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.SMALL_HILL);
	public static final StructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_SMALL = HOLLOW_HILL_SMALL.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> HOLLOW_HILL_MEDIUM = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.MEDIUM_HILL);
	public static final StructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_MEDIUM = HOLLOW_HILL_MEDIUM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> HOLLOW_HILL_LARGE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.LARGE_HILL);
	public static final StructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_LARGE = HOLLOW_HILL_LARGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> NAGA_COURTYARD = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.NAGA_COURTYARD, true);
	public static final StructureFeature<?, ?> CONFIGURED_NAGA_COURTYARD = NAGA_COURTYARD.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> LICH_TOWER = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.LICH_TOWER);
	public static final StructureFeature<?, ?> CONFIGURED_LICH_TOWER = LICH_TOWER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> LABYRINTH = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.LABYRINTH);
	public static final StructureFeature<?, ?> CONFIGURED_LABYRINTH = LABYRINTH.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> HYDRA_LAIR = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.HYDRA_LAIR);
	public static final StructureFeature<?, ?> CONFIGURED_HYDRA_LAIR = HYDRA_LAIR.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> KNIGHT_STRONGHOLD = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.KNIGHT_STRONGHOLD);
	public static final StructureFeature<?, ?> CONFIGURED_KNIGHT_STRONGHOLD = KNIGHT_STRONGHOLD.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> DARK_TOWER = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.DARK_TOWER);
	public static final StructureFeature<?, ?> CONFIGURED_DARK_TOWER = DARK_TOWER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> YETI_CAVE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.YETI_CAVE);
	public static final StructureFeature<?, ?> CONFIGURED_YETI_CAVE = YETI_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> AURORA_PALACE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.ICE_TOWER);
	public static final StructureFeature<?, ?> CONFIGURED_AURORA_PALACE = AURORA_PALACE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> TROLL_CAVE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.TROLL_CAVE);
	public static final StructureFeature<?, ?> CONFIGURED_TROLL_CAVE = TROLL_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static final Structure<NoFeatureConfig> FINAL_CASTLE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.FINAL_CASTLE);
	public static final StructureFeature<?, ?> CONFIGURED_FINAL_CASTLE = FINAL_CASTLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static void register(RegistryEvent.Register<Structure<?>> event) {
		SEPARATION_SETTINGS.clear();
		TFFeature.init();
		new TFMushroomTowerPieces();
		new NagaCourtyardPieces();
		new TFLichTowerPieces();
		new TFMinotaurMazePieces();
		new TFStrongholdPieces();
		new TFDarkTowerPieces();
		new TFIceTowerPieces();
		new TFTrollCavePieces();
		new TFFinalCastlePieces();
		// FIXME Next version change, change the names to include underscores
		register(event, HEDGE_MAZE, CONFIGURED_HEDGE_MAZE, TwilightForestMod.prefix("hedgemaze"), 1, 2);
		register(event, QUEST_GROVE, CONFIGURED_QUEST_GROVE, TwilightForestMod.prefix("questgrove"), 1, 2);
		register(event, MUSHROOM_TOWER, CONFIGURED_MUSHROOM_TOWER, TwilightForestMod.prefix("mushroomtower"), 1, 2);
		register(event, HOLLOW_HILL_SMALL, CONFIGURED_HOLLOW_HILL_SMALL, TwilightForestMod.prefix("hollowhillsmall"), 1, 2);
		register(event, HOLLOW_HILL_MEDIUM, CONFIGURED_HOLLOW_HILL_MEDIUM, TwilightForestMod.prefix("hollowhillmedium"), 1, 2);
		register(event, HOLLOW_HILL_LARGE, CONFIGURED_HOLLOW_HILL_LARGE, TwilightForestMod.prefix("hollowhilllarge"), 1, 2);
		register(event, NAGA_COURTYARD, CONFIGURED_NAGA_COURTYARD, TwilightForestMod.prefix("courtyard"), 1, 2);
		register(event, LICH_TOWER, CONFIGURED_LICH_TOWER, TwilightForestMod.prefix("lichtower"), 1, 2);
		register(event, LABYRINTH, CONFIGURED_LABYRINTH, TwilightForestMod.prefix("labyrinth"), 1, 2);
		register(event, HYDRA_LAIR, CONFIGURED_HYDRA_LAIR, TwilightForestMod.prefix("hydralair"), 1, 2);
		register(event, KNIGHT_STRONGHOLD, CONFIGURED_KNIGHT_STRONGHOLD, TwilightForestMod.prefix("knightstronghold"), 1, 2);
		register(event, DARK_TOWER, CONFIGURED_DARK_TOWER, TwilightForestMod.prefix("darktower"), 1, 2);
		register(event, YETI_CAVE, CONFIGURED_YETI_CAVE, TwilightForestMod.prefix("yeticave"), 1, 2);
		register(event, AURORA_PALACE, CONFIGURED_AURORA_PALACE, TwilightForestMod.prefix("aurorapalace"), 1, 2);
		register(event, TROLL_CAVE, CONFIGURED_TROLL_CAVE, TwilightForestMod.prefix("trollcave"), 1, 2);
		register(event, FINAL_CASTLE, CONFIGURED_FINAL_CASTLE, TwilightForestMod.prefix("finalcastle"), 1, 2);
	}

	private static void register(RegistryEvent.Register<Structure<?>> event, Structure<?> structure, StructureFeature<?, ?> config, ResourceLocation name, int min, int max) {
		event.getRegistry().register(structure.setRegistryName(name));
		Structure.NAME_STRUCTURE_BIMAP.put(name.toString(), structure);
		StructureSeparationSettings seperation = new StructureSeparationSettings(max, min, 472681346);
		DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.field_236191_b_).
				put(structure, seperation).build();
		SEPARATION_SETTINGS.put(structure, seperation);
		Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(name.getNamespace(), "configured_".concat(name.getPath())), config);
		FlatGenerationSettings.STRUCTURES.put(structure, config);
	}

	public static void load(WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld && ((ServerWorld) event.getWorld()).getChunkProvider().generator instanceof ChunkGeneratorTwilightBase){
			ServerWorld serverWorld = (ServerWorld)event.getWorld();
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
			tempMap.putAll(SEPARATION_SETTINGS);
			serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
		}
	}
}
