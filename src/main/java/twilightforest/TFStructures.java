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
import twilightforest.structures.lichtower.TFLichTowerPieces;
import twilightforest.structures.start.TFStructure;
import twilightforest.world.TFDimensions;

import java.util.HashMap;
import java.util.Map;

public class TFStructures {

	private static final Map<Structure<?>, StructureSeparationSettings> SEPARATION_SETTINGS = new HashMap<>();

	public static final Structure<NoFeatureConfig> HEDGE_MAZE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.HEDGE_MAZE);
	public static final StructureFeature<?, ?> CONFIGURED_HEDGE_MAZE = HEDGE_MAZE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
	public static final Structure<NoFeatureConfig> HALLOW_HILL_SMALL = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.SMALL_HILL);
	public static final StructureFeature<?, ?> CONFIGURED_HALLOW_HILL_SMALL = HALLOW_HILL_SMALL.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
	public static final Structure<NoFeatureConfig> HALLOW_HILL_MEDIUM = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.MEDIUM_HILL);
	public static final StructureFeature<?, ?> CONFIGURED_HALLOW_HILL_MEDIUM = HALLOW_HILL_MEDIUM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
	public static final Structure<NoFeatureConfig> HALLOW_HILL_LARGE = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.LARGE_HILL);
	public static final StructureFeature<?, ?> CONFIGURED_HALLOW_HILL_LARGE = HALLOW_HILL_LARGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
	public static final Structure<NoFeatureConfig> NAGA_COURTYARD = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.NAGA_COURTYARD, true);
	public static final StructureFeature<?, ?> CONFIGURED_NAGA_COURTYARD = NAGA_COURTYARD.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
	public static final Structure<NoFeatureConfig> LICH_TOWER = new TFStructure<>(NoFeatureConfig.field_236558_a_, TFFeature.LICH_TOWER, true);
	public static final StructureFeature<?, ?> CONFIGURED_LICH_TOWER = LICH_TOWER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static void register(RegistryEvent.Register<Structure<?>> event) {
		SEPARATION_SETTINGS.clear();
		TFFeature.init();
		new NagaCourtyardPieces();
		new TFLichTowerPieces();
		register(event, HEDGE_MAZE, CONFIGURED_HEDGE_MAZE, TwilightForestMod.prefix("hedgemaze"), 1, 2);
		register(event, HALLOW_HILL_SMALL, CONFIGURED_HALLOW_HILL_SMALL, TwilightForestMod.prefix("hallowhillsmall"), 1, 2);
		register(event, HALLOW_HILL_MEDIUM, CONFIGURED_HALLOW_HILL_MEDIUM, TwilightForestMod.prefix("hallowhillmedium"), 1, 2);
		register(event, HALLOW_HILL_LARGE, CONFIGURED_HALLOW_HILL_LARGE, TwilightForestMod.prefix("hallowhilllarge"), 1, 2);
		register(event, NAGA_COURTYARD, CONFIGURED_NAGA_COURTYARD, TwilightForestMod.prefix("courtyard"), 1, 2);
		register(event, LICH_TOWER, CONFIGURED_LICH_TOWER, TwilightForestMod.prefix("lichtower"), 1, 2);
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
		if(event.getWorld() instanceof ServerWorld && ((ServerWorld) event.getWorld()).getDimensionKey().equals(TFDimensions.twilightForest)){
			ServerWorld serverWorld = (ServerWorld)event.getWorld();
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
			tempMap.putAll(SEPARATION_SETTINGS);
			serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
		}
	}
}
