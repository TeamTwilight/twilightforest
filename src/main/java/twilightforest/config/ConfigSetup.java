package twilightforest.config;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigSetup {

	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec SYNCED_SPEC;
	private static final ModConfigSpec LOCAL_SPEC;

	static final TFClientConfig CLIENT_CONFIG;
	static final TFSyncedConfig SYNCED_CONFIG;
	static final TFLocalConfig LOCAL_CONFIG;

	static {
		{
			final Pair<TFLocalConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFLocalConfig::new);
			ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.LOCAL, LOCAL_SPEC = specPair.getRight());
			LOCAL_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFSyncedConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFSyncedConfig::new);
			ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SYNCED, SYNCED_SPEC = specPair.getRight());
			SYNCED_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFClientConfig::new);
			ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC = specPair.getRight());
			CLIENT_CONFIG = specPair.getLeft();
		}
	}

	public static void loadConfigs(ModConfigEvent.Loading event) {
		if (event.getConfig().getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (event.getConfig().getSpec() == SYNCED_SPEC) {
			TFConfig.rebakeSyncedOptions(SYNCED_CONFIG);
		} else if (event.getConfig().getSpec() == LOCAL_SPEC) {
			TFConfig.rebakeLocalOptions(LOCAL_CONFIG);
		}
	}

	public static void reloadConfigs(ModConfigEvent.Reloading event) {
		if (event.getConfig().getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (event.getConfig().getSpec() == SYNCED_SPEC) {
			TFConfig.rebakeSyncedOptions(SYNCED_CONFIG);
		} else if (event.getConfig().getSpec() == LOCAL_SPEC) {
			TFConfig.rebakeLocalOptions(LOCAL_CONFIG);
		}
	}
}
