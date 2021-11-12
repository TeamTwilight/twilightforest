package twilightforest.util;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import twilightforest.TwilightForestMod;

public class TFStats {

	public static final ResourceLocation BUGS_SQUISHED = makeTFStat("bugs_squished");
	public static final ResourceLocation UNCRAFTING_TABLE_INTERACTIONS = makeTFStat("uncrafting_table_interactions");
	public static final ResourceLocation TROPHY_PEDESTALS_ACTIVATED = makeTFStat("trophy_pedestals_activated");
	public static final ResourceLocation E115_SLICES_EATEN = makeTFStat("e115_slices_eaten");
	public static final ResourceLocation TORCHBERRIES_HARVESTED = makeTFStat("torchberries_harvested");
	public static final ResourceLocation BLOCKS_CRUMBLED = makeTFStat("blocks_crumbled");
	public static final ResourceLocation LIFE_CHARMS_ACTIVATED = makeTFStat("life_charms_activated");
	public static final ResourceLocation KEEPING_CHARMS_ACTIVATED = makeTFStat("keeping_charms_activated");
	public static final ResourceLocation SKULL_CANDLES_MADE = makeTFStat("skull_candles_made");
	public static final ResourceLocation TF_SHIELDS_BROKEN = makeTFStat("tf_shields_broken");

	//this is just here so we can poke it in init
	public static void init() {}

	private static ResourceLocation makeTFStat(String pKey) {
		ResourceLocation resourcelocation = TwilightForestMod.prefix(pKey);
		Registry.register(Registry.CUSTOM_STAT, pKey, resourcelocation);
		Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT);
		return resourcelocation;
	}
}
