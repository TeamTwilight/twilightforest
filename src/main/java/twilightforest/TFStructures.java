package twilightforest;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
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
import twilightforest.structures.start.StructureCourtyard;
import twilightforest.world.TFDimensions;

import java.util.HashMap;
import java.util.Map;

public class TFStructures {

	private static final Map<Structure<?>, StructureSeparationSettings> SEPARATION_SETTINGS = new HashMap<>();

	public static final Structure<NoFeatureConfig> NAGA_COURTYARD = new StructureCourtyard(NoFeatureConfig.field_236558_a_);
	public static final StructureFeature<?, ?> CONFIGURED_NAGA_COURTYARD = NAGA_COURTYARD.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

	public static void register(RegistryEvent.Register<Structure<?>> event) {
		SEPARATION_SETTINGS.clear();
		new NagaCourtyardPieces();
		register(event, NAGA_COURTYARD, CONFIGURED_NAGA_COURTYARD, StructureCourtyard.NAME, 1, 2);
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
