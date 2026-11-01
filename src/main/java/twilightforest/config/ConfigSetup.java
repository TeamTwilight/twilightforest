package twilightforest.config;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigSetup {

	private static final ModConfigSpec CLIENT_SPEC;
	private static final ModConfigSpec SERVER_SPEC;
	private static final ModConfigSpec COMMON_SPEC;

	static final TFClientConfig CLIENT_CONFIG;
	static final TFServerConfig SERVER_CONFIG;
	static final TFCommonConfig COMMON_CONFIG;

	static {
		{
			final Pair<TFCommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFCommonConfig::new);
			ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC = specPair.getRight());
			COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFServerConfig::new);
			ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC = specPair.getRight());
			SERVER_CONFIG = specPair.getLeft();
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
		} else if (event.getConfig().getSpec() == SERVER_SPEC) {
			TFConfig.rebakeServerOptions(SERVER_CONFIG);
		} else if (event.getConfig().getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}

	public static void reloadConfigs(ModConfigEvent.Reloading event) {
		if (event.getConfig().getSpec() == CLIENT_SPEC) {
			TFConfig.rebakeClientOptions(CLIENT_CONFIG);
		} else if (event.getConfig().getSpec() == SERVER_SPEC) {
			TFConfig.rebakeServerOptions(SERVER_CONFIG);
		} else if (event.getConfig().getSpec() == COMMON_SPEC) {
			TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		}
	}
}
