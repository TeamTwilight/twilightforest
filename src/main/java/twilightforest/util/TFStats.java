package twilightforest.util;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;

public class TFStats {

	public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Registry.CUSTOM_STAT_REGISTRY, TwilightForestMod.ID);

	public static final RegistryObject<ResourceLocation> BUGS_SQUISHED = makeTFStat("bugs_squished");
	public static final RegistryObject<ResourceLocation> UNCRAFTING_TABLE_INTERACTIONS = makeTFStat("uncrafting_table_interactions");
	public static final RegistryObject<ResourceLocation> TROPHY_PEDESTALS_ACTIVATED = makeTFStat("trophy_pedestals_activated");
	public static final RegistryObject<ResourceLocation> E115_SLICES_EATEN = makeTFStat("e115_slices_eaten");
	public static final RegistryObject<ResourceLocation> TORCHBERRIES_HARVESTED = makeTFStat("torchberries_harvested");
	public static final RegistryObject<ResourceLocation> BLOCKS_CRUMBLED = makeTFStat("blocks_crumbled");
	public static final RegistryObject<ResourceLocation> LIFE_CHARMS_ACTIVATED = makeTFStat("life_charms_activated");
	public static final RegistryObject<ResourceLocation> KEEPING_CHARMS_ACTIVATED = makeTFStat("keeping_charms_activated");
	public static final RegistryObject<ResourceLocation> SKULL_CANDLES_MADE = makeTFStat("skull_candles_made");
	public static final RegistryObject<ResourceLocation> TF_SHIELDS_BROKEN = makeTFStat("tf_shields_broken");

	private static RegistryObject<ResourceLocation> makeTFStat(String key) {
		ResourceLocation resourcelocation = TwilightForestMod.prefix(key);
		//Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT);
		return STATS.register(key, () -> resourcelocation);
	}
}
