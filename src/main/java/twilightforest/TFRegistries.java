package twilightforest;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;
import twilightforest.util.Enforcement;
import twilightforest.util.Restriction;
import twilightforest.util.WoodPalette;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

import java.util.Locale;

public class TFRegistries {

	public static final Registry<BiomeLayerType> BIOME_LAYER_TYPE = new RegistryBuilder<>(Keys.BIOME_LAYER_TYPE).create();
	public static final Registry<DwarfRabbitVariant> DWARF_RABBIT_VARIANT = new RegistryBuilder<>(Keys.DWARF_RABBIT_VARIANT).sync(true).create();
	public static final Registry<Enforcement> ENFORCEMENT = new RegistryBuilder<>(Keys.ENFORCEMENT).sync(true).create();
	public static final Registry<TinyBirdVariant> TINY_BIRD_VARIANT = new RegistryBuilder<>(Keys.TINY_BIRD_VARIANT).sync(true).create();

	public static final class Keys {
		public static final String REGISTRY_NAMESPACE = "twilight";
		
		//Normal Registries
		public static final ResourceKey<Registry<BiomeLayerType>> BIOME_LAYER_TYPE = ResourceKey.createRegistryKey(namedRegistry("biome_layer_type"));
		public static final ResourceKey<Registry<DwarfRabbitVariant>> DWARF_RABBIT_VARIANT = ResourceKey.createRegistryKey(TwilightForestMod.prefix("dwarf_rabbit_variant"));
		public static final ResourceKey<Registry<Enforcement>> ENFORCEMENT = ResourceKey.createRegistryKey(TwilightForestMod.prefix("enforcement"));
		public static final ResourceKey<Registry<TinyBirdVariant>> TINY_BIRD_VARIANT = ResourceKey.createRegistryKey(TwilightForestMod.prefix("tiny_bird_variant"));

		//Datapack Registries
		public static final ResourceKey<Registry<BiomeLayerFactory>> BIOME_STACK = ResourceKey.createRegistryKey(namedRegistry("biome_layer_stack"));
		public static final ResourceKey<Registry<MagicPaintingVariant>> MAGIC_PAINTINGS = ResourceKey.createRegistryKey(namedRegistry("magic_paintings"));
		public static final ResourceKey<Registry<Restriction>> RESTRICTIONS = ResourceKey.createRegistryKey(namedRegistry("restrictions"));
		public static final ResourceKey<Registry<StructureSpeleothemConfig>> STRUCTURE_SPELEOTHEM_SETTINGS = ResourceKey.createRegistryKey(namedRegistry("structure_speleothem_settings"));
		public static final ResourceKey<Registry<WoodPalette>> WOOD_PALETTES = ResourceKey.createRegistryKey(namedRegistry("wood_palettes"));

		public static ResourceLocation namedRegistry(String name) {
			return new ResourceLocation(REGISTRY_NAMESPACE, name.toLowerCase(Locale.ROOT));
		}
	}
}
