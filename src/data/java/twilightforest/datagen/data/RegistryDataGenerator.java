package twilightforest.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.datagen.data.custom.*;
import twilightforest.datagen.data.worldgen.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
		.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatureGenerator::bootstrap)
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
		.add(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerGenerator::bootstrap);

	public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of("minecraft", TwilightForestMod.ID));
	}
}
