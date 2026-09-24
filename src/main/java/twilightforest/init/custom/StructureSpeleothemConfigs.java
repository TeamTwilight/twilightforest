package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFRegistries;
import twilightforest.init.TFStructures;
import twilightforest.world.components.structures.StructureSpeleothemConfig;

public class StructureSpeleothemConfigs {
	public static final RegistryFileCodec<StructureSpeleothemConfig> CODEC = RegistryFileCodec.create(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC, false);

	public static final ResourceKey<StructureSpeleothemConfig> SMALL_HILL = makeKey(TFStructures.HOLLOW_HILL_SMALL.identifier());
	public static final ResourceKey<StructureSpeleothemConfig> MEDIUM_HILL = makeKey(TFStructures.HOLLOW_HILL_MEDIUM.identifier());
	public static final ResourceKey<StructureSpeleothemConfig> LARGE_HILL = makeKey(TFStructures.HOLLOW_HILL_LARGE.identifier());
	public static final ResourceKey<StructureSpeleothemConfig> HYDRA_LAIR = makeKey(TFStructures.HYDRA_LAIR.identifier());
	public static final ResourceKey<StructureSpeleothemConfig> YETI_CAVE = makeKey(TFStructures.YETI_CAVE.identifier());
	public static final ResourceKey<StructureSpeleothemConfig> TROLL_CAVE = makeKey(TFStructures.TROLL_CAVE.identifier());

	private static ResourceKey<StructureSpeleothemConfig> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, name);
	}

	public static Holder.Reference<StructureSpeleothemConfig> getConfigHolder(HolderLookup.Provider registryAccess, String strRL) {
		return getConfigHolder(registryAccess, makeKey(Identifier.parse(strRL)));
	}

	public static Holder.Reference<StructureSpeleothemConfig> getConfigHolder(HolderLookup.Provider registryAccess, ResourceKey<StructureSpeleothemConfig> resourceKey) {
		return registryAccess.lookupOrThrow(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).get(resourceKey).get();
	}
}
