package twilightforest.init;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

public class TFStats {

	public static final DeferredRegister<Identifier> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, TwilightForestMod.ID);
	private static final List<Runnable> STAT_SETUP = new ArrayList<>();

	public static final DeferredHolder<Identifier, Identifier> BUGS_SQUISHED = makeTFStat("bugs_squished");
	public static final DeferredHolder<Identifier, Identifier> UNCRAFTING_TABLE_INTERACTIONS = makeTFStat("uncrafting_table_interactions");
	public static final DeferredHolder<Identifier, Identifier> TROPHY_PEDESTALS_ACTIVATED = makeTFStat("trophy_pedestals_activated");
	public static final DeferredHolder<Identifier, Identifier> E115_SLICES_EATEN = makeTFStat("e115_slices_eaten");
	public static final DeferredHolder<Identifier, Identifier> TORCHBERRIES_HARVESTED = makeTFStat("torchberries_harvested");
	public static final DeferredHolder<Identifier, Identifier> BLOCKS_CRUMBLED = makeTFStat("blocks_crumbled");
	public static final DeferredHolder<Identifier, Identifier> LIFE_CHARMS_ACTIVATED = makeTFStat("life_charms_activated");
	public static final DeferredHolder<Identifier, Identifier> KEEPING_CHARMS_ACTIVATED = makeTFStat("keeping_charms_activated");
	public static final DeferredHolder<Identifier, Identifier> SKULL_CANDLES_MADE = makeTFStat("skull_candles_made");
	public static final DeferredHolder<Identifier, Identifier> TF_SHIELDS_BROKEN = makeTFStat("tf_shields_broken");

	private static DeferredHolder<Identifier, Identifier> makeTFStat(String key) {
		Identifier identifier = TwilightForestMod.prefix(key);
		STAT_SETUP.add(() -> Stats.CUSTOM.get(identifier, StatFormatter.DEFAULT));
		return STATS.register(key, () -> identifier);
	}

	public static void init() {
		STAT_SETUP.forEach(Runnable::run);
	}
}
