package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.*;
import twilightforest.init.custom.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
			.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigs::bootstrap)
			.add(Registries.STRUCTURE, TFStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
			.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap)
			.add(Registries.NOISE_SETTINGS, TFDimensionSettings::bootstrapNoise)
			.add(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack::bootstrap)
			.add(Registries.DIMENSION_TYPE, TFDimensionSettings::bootstrapType)
			.add(Registries.LEVEL_STEM, TFDimensionSettings::bootstrapStem)
			.add(Registries.BIOME, TFBiomes::bootstrap)
			.add(TFRegistries.Keys.WOOD_PALETTES, WoodPalettes::bootstrap)
			.add(Registries.DAMAGE_TYPE, TFDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, TFTrimMaterials::bootstrap)
			.add(TFRegistries.Keys.RESTRICTIONS, Restrictions::bootstrap)
			.add(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariants::bootstrap);

	public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of("minecraft", TwilightForestMod.ID));
	}
}
