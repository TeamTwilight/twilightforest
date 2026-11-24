package twilightforest.datagen.generator;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.datagen.data.*;
import twilightforest.datagen.data.custom.*;
import twilightforest.datagen.data.custom.stalactites.StalactiteGenerator;
import twilightforest.datagen.data.custom.structuredefinitions.CampStructureDefinitionGenerator;
import twilightforest.datagen.data.custom.structuredefinitions.FinalCastleStructureDefinitionGenerator;
import twilightforest.datagen.data.custom.structuredefinitions.LichTowerStructureDefinitionGenerator;
import twilightforest.datagen.data.custom.structuredefinitions.NagaCourtyardStructureDefinitionGenerator;
import twilightforest.datagen.data.DataMapGenerator;
import twilightforest.datagen.data.RecipePriorityGenerator;
import twilightforest.datagen.data.reloadable.*;
import twilightforest.datagen.data.reloadable.loot.*;
import twilightforest.datagen.data.tags.*;
import twilightforest.datagen.data.world.*;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.Set;

@Component
public class DataGenerator {

	private static final RegistrySetBuilder WORLD_BUILDER = new RegistrySetBuilder()
		.add(Registries.FEATURE, TFFeatureGenerator::bootstrap)
		.add(Registries.PLACED_FEATURE, TFPlacedFeatureGenerator::bootstrap)
		.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigGenerator::bootstrap)
		.add(Registries.STRUCTURE, TFStructureGenerator::bootstrap)
		.add(Registries.STRUCTURE_SET, TFStructureSetGenerator::bootstrap)
		.add(Registries.CONFIGURED_CARVER, TFCaveCarverGenerator::bootstrap)
		.add(Registries.DENSITY_FUNCTION, TFDensityFunctionGenerator::bootstrap)
		.add(Registries.NOISE_SETTINGS, TFDimensionGenerator::bootstrapNoise)
		.add(TFRegistries.Keys.BIOME_STACK, TFBiomeLayerGenerator::bootstrap)
		.add(TFRegistries.Keys.BIOME_TERRAIN_DATA, TFBiomeLayerGenerator::bootstrapData)
		.add(Registries.DIMENSION_TYPE, TFDimensionGenerator::bootstrapType)
		.add(Registries.LEVEL_STEM, TFDimensionGenerator::bootstrapStem)
		.add(Registries.BIOME, TFBiomeGenerator::bootstrap)
		.add(TFRegistries.Keys.WOOD_PALETTES, WoodPaletteGenerator::bootstrap)
		.add(Registries.DAMAGE_TYPE, TFDamageTypeGenerator::bootstrap)
		.add(Registries.TRIM_MATERIAL, TFTrimMaterialGenerator::bootstrap)
		.add(TFRegistries.Keys.RESTRICTIONS, RestrictionGenerator::bootstrap)
		.add(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariantGenerator::bootstrap)
		.add(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessorGenerator::bootstrap)
		.add(Registries.BANNER_PATTERN, TFBannerPatternGenerator::bootstrap)
		.add(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariantGenerator::bootstrap)
		.add(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifierGenerator::bootstrap)
		.add(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariantGenerator::bootstrap)
		.add(Registries.JUKEBOX_SONG, TFJukeboxSongGenerator::bootstrap)
		.add(Registries.ENCHANTMENT, TFEnchantmentsGenerator::bootstrap)
		.add(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerGenerator::bootstrap)
		.add(Registries.TIMELINE, TFTimelineGenerator::bootstrap)
		.add(Registries.WORLD_CLOCK, TFWorldClockGenerator::bootstrap);

	private static final RegistrySetBuilder RELOADABLE_BUILDER = new RegistrySetBuilder()
		.add(
			Registries.LOOT_TABLE,
			new LootTableProvider(
				TFLootTables.allBuiltin(),
				List.of(
					new LootTableProvider.SubProviderEntry(TFBlockLoot::new, LootContextParamSets.BLOCK),
					new LootTableProvider.SubProviderEntry(TFBlockInteractLoot::new, LootContextParamSets.BLOCK_INTERACT),
					new LootTableProvider.SubProviderEntry(TFChestLoot::new, LootContextParamSets.CHEST),
					new LootTableProvider.SubProviderEntry(TFEntityLoot::new, LootContextParamSets.ENTITY),
					new LootTableProvider.SubProviderEntry(TFEntityInteractLoot::new, LootContextParamSets.ENTITY_INTERACT),
					new LootTableProvider.SubProviderEntry(TFSpecialLoot::new, LootContextParamSets.EMPTY)
				)
			)
		).add(
			Registries.ADVANCEMENT,
			new AdvancementProvider(
				List.of(TFAdvancementSubProvider::new)
			)
		).add(
			new MultiRegistryBootstrap() {
				@Override
				public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
					return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
				}

				@Override
				public void run(MultiRegistryBootstrap.BootstrapGetter registries) {
					new TFRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
				}
			}
		).add(
			Registries.CONTEXT_INT_PROVIDER,
			TFContextIntProviders::bootstrap
		);

	public void generate(GatherDataEvent.Client event) {
		net.minecraft.data.DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();

		// World registries
		DatapackBuiltinEntriesProvider worldProvider = generator.addProvider(
			true,
			DatapackBuiltinEntriesProvider.forWorldLayer(
				output,
				"TF World Registries",
				event.getWorldLookupProvider(),
				WORLD_BUILDER,
				Set.of(TwilightForestMod.ID)
			)
		);

		// Reloadable registries
		generator.addProvider(
			true,
			DatapackBuiltinEntriesProvider.forReloadableLayer(
				output,
				"TF Reloadable Registries",
				worldProvider.getRegistryProvider(),
				event.getReloadableLookupProvider(),
				RELOADABLE_BUILDER,
				Set.of(TwilightForestMod.ID)
			)
		);

		// Tags
		generator.addProvider(true, new BannerPatternTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new BiomeTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new BlockEntityTypeTagGenerator(output, worldProvider.getRegistryProvider()));
		BlockTagGenerator blockTags = new BlockTagGenerator(output, worldProvider.getRegistryProvider());
		generator.addProvider(true, blockTags);
		generator.addProvider(true, new DamageTypeTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new DimensionTypeTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new EntityTypeTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new ItemTagGenerator(output, worldProvider.getRegistryProvider(), blockTags.contentsGetter()));
		generator.addProvider(true, new PaintingVariantTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new StructureTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new TimelineTagGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new WoodPaletteTagGenerator(output, worldProvider.getRegistryProvider()));

		// Everything else
		generator.addProvider(true, new DataMapGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new LootModifierGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new QuestGenerator(output));
		generator.addProvider(true, new RecipePriorityGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new StalactiteGenerator(output));
		generator.addProvider(true, new CampStructureDefinitionGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new FinalCastleStructureDefinitionGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new LichTowerStructureDefinitionGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new NagaCourtyardStructureDefinitionGenerator(output, worldProvider.getRegistryProvider()));
		generator.addProvider(true, new TFStructureUpdater("structures", output, event.getResourceManager(PackType.SERVER_DATA)));
	}
}
