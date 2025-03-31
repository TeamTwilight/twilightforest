package twilightforest.network;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Component;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Optional;

@Component
public class ModUpdateURLInterceptor {

	private final Logger logger = LogManager.getLogger();

	@Nullable
	private MethodHandle ModInfo_updateJSONURL = null;

	public ModUpdateURLInterceptor() {
		ModList.get().getModFileById(TwilightForestMod.ID).getMods().forEach(info -> {
			if (info instanceof ModInfo mod) {
				if (ModInfo_updateJSONURL == null) {
					try {
						Field field = ModInfo.class.getDeclaredField("updateJSONURL");
						field.trySetAccessible();
						ModInfo_updateJSONURL = MethodHandles.lookup().unreflectSetter(field);
					} catch (Exception ex) {
						logger.error("Error", ex);
					}
				}
				if (ModInfo_updateJSONURL != null) {
					try {
						ModInfo_updateJSONURL.invokeExact(mod, Optional.of(URI.create("https://gh.tamaized.com/TeamTwilight/twilightforest/update.json?m=%s&l=%s&v=%s".formatted(
							ModList.get().getModFileById("minecraft").getFile().getModFileInfo().versionString(),
							"NeoForge",
							ModList.get().getModFileById("neoforge").getFile().getModFileInfo().versionString()
						)).toURL()));
					} catch (Throwable ex) {
						logger.error("Error", ex);
					}
				}
			}
		});
	}

}
