package twilightforest.world.registration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.courtyard.NagaCourtyardPieces;
import twilightforest.world.components.structures.darktower.DarkTowerPieces;
import twilightforest.world.components.structures.finalcastle.FinalCastlePieces;
import twilightforest.world.components.structures.icetower.IceTowerPieces;
import twilightforest.world.components.structures.lichtower.LichTowerPieces;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerRevampPieces;
import twilightforest.world.components.structures.minotaurmaze.MinotaurMazePieces;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerPieces;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.stronghold.StrongholdPieces;
import twilightforest.world.components.structures.trollcave.TrollCavePieces;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;

import java.util.HashMap;
import java.util.Map;

public class TFStructures {

	public static final Map<StructureFeature<?>, StructureFeatureConfiguration> SEPARATION_SETTINGS = new HashMap<>();

	public static final StructureFeature<NoneFeatureConfiguration> HEDGE_MAZE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.HEDGE_MAZE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HEDGE_MAZE = HEDGE_MAZE.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> QUEST_GROVE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.QUEST_GROVE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_QUEST_GROVE = QUEST_GROVE.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> MUSHROOM_TOWER = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.MUSHROOM_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_MUSHROOM_TOWER = MUSHROOM_TOWER.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_SMALL = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.SMALL_HILL);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_SMALL = HOLLOW_HILL_SMALL.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_MEDIUM = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.MEDIUM_HILL);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_MEDIUM = HOLLOW_HILL_MEDIUM.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_LARGE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.LARGE_HILL);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HOLLOW_HILL_LARGE = HOLLOW_HILL_LARGE.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> NAGA_COURTYARD = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.NAGA_COURTYARD, true);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_NAGA_COURTYARD = NAGA_COURTYARD.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> LICH_TOWER = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.LICH_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_LICH_TOWER = LICH_TOWER.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> LABYRINTH = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.LABYRINTH);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_LABYRINTH = LABYRINTH.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> HYDRA_LAIR = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.HYDRA_LAIR);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_HYDRA_LAIR = HYDRA_LAIR.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> KNIGHT_STRONGHOLD = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.KNIGHT_STRONGHOLD);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_KNIGHT_STRONGHOLD = KNIGHT_STRONGHOLD.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> DARK_TOWER = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.DARK_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_DARK_TOWER = DARK_TOWER.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> YETI_CAVE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.YETI_CAVE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_YETI_CAVE = YETI_CAVE.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> AURORA_PALACE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.ICE_TOWER);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_AURORA_PALACE = AURORA_PALACE.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> TROLL_CAVE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.TROLL_CAVE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_TROLL_CAVE = TROLL_CAVE.configured(FeatureConfiguration.NONE);

	public static final StructureFeature<NoneFeatureConfiguration> FINAL_CASTLE = new TFStructureStart<>(NoneFeatureConfiguration.CODEC, TFFeature.FINAL_CASTLE);
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_FINAL_CASTLE = FINAL_CASTLE.configured(FeatureConfiguration.NONE);

	public static void register(RegistryEvent.Register<StructureFeature<?>> event) {
		SEPARATION_SETTINGS.clear();
		TFFeature.init();
		new MushroomTowerPieces();
		new NagaCourtyardPieces();
		new LichTowerPieces();
		new LichTowerRevampPieces();
		new MinotaurMazePieces();
		new StrongholdPieces();
		new DarkTowerPieces();
		new IceTowerPieces();
		new TrollCavePieces();
		new FinalCastlePieces();

		register(event, HEDGE_MAZE, CONFIGURED_HEDGE_MAZE, TwilightForestMod.prefix("hedge_maze"), 1, 2);
		register(event, QUEST_GROVE, CONFIGURED_QUEST_GROVE, TwilightForestMod.prefix("quest_grove"), 1, 2);
		register(event, MUSHROOM_TOWER, CONFIGURED_MUSHROOM_TOWER, TwilightForestMod.prefix("mushroom_tower"), 1, 2);
		register(event, HOLLOW_HILL_SMALL, CONFIGURED_HOLLOW_HILL_SMALL, TwilightForestMod.prefix("hollow_hill_small"), 1, 2);
		register(event, HOLLOW_HILL_MEDIUM, CONFIGURED_HOLLOW_HILL_MEDIUM, TwilightForestMod.prefix("hollow_hill_medium"), 1, 2);
		register(event, HOLLOW_HILL_LARGE, CONFIGURED_HOLLOW_HILL_LARGE, TwilightForestMod.prefix("hollow_hill_large"), 1, 2);
		register(event, NAGA_COURTYARD, CONFIGURED_NAGA_COURTYARD, TwilightForestMod.prefix("courtyard"), 1, 2);
		register(event, LICH_TOWER, CONFIGURED_LICH_TOWER, TwilightForestMod.prefix("lich_tower"), 1, 2);
		register(event, LABYRINTH, CONFIGURED_LABYRINTH, TwilightForestMod.prefix("labyrinth"), 1, 2);
		register(event, HYDRA_LAIR, CONFIGURED_HYDRA_LAIR, TwilightForestMod.prefix("hydra_lair"), 1, 2);
		register(event, KNIGHT_STRONGHOLD, CONFIGURED_KNIGHT_STRONGHOLD, TwilightForestMod.prefix("knight_stronghold"), 1, 2);
		register(event, DARK_TOWER, CONFIGURED_DARK_TOWER, TwilightForestMod.prefix("dark_tower"), 1, 2);
		register(event, YETI_CAVE, CONFIGURED_YETI_CAVE, TwilightForestMod.prefix("yeti_cave"), 1, 2);
		register(event, AURORA_PALACE, CONFIGURED_AURORA_PALACE, TwilightForestMod.prefix("aurora_palace"), 1, 2);
		register(event, TROLL_CAVE, CONFIGURED_TROLL_CAVE, TwilightForestMod.prefix("troll_cave"), 1, 2);
		register(event, FINAL_CASTLE, CONFIGURED_FINAL_CASTLE, TwilightForestMod.prefix("final_castle"), 1, 2);

		// TODO Beardify more structures (Or bury)
		StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(HEDGE_MAZE, QUEST_GROVE, NAGA_COURTYARD, LICH_TOWER, LABYRINTH, KNIGHT_STRONGHOLD, DARK_TOWER, TROLL_CAVE, FINAL_CASTLE).build();
	}

	private static void register(RegistryEvent.Register<StructureFeature<?>> event, StructureFeature<?> structure, ConfiguredStructureFeature<?, ?> config, ResourceLocation name, int min, int max) {
		event.getRegistry().register(structure.setRegistryName(name));
		StructureFeature.STRUCTURES_REGISTRY.put(name.toString(), structure);
		StructureFeatureConfiguration seperation = new StructureFeatureConfiguration(max, min, 472681346);
		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).
				put(structure, seperation).build();
		SEPARATION_SETTINGS.put(structure, seperation);
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(name.getNamespace(), "configured_".concat(name.getPath())), config);
		FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(structure, config);
	}

	public static void load(WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerLevel && ((ServerLevel) event.getWorld()).getChunkSource().generator instanceof ChunkGeneratorTwilight) {
			ServerLevel serverWorld = (ServerLevel)event.getWorld();
			Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putAll(SEPARATION_SETTINGS);
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}
}
