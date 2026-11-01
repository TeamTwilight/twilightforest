package twilightforest.network;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Optional;

/**
 * Redirigeait le verificateur de mises a jour vers le flux de l'equipe Twilight Forest, qui ne
 * decrit que les versions NeoForge (et lit la version du mod {@code neoforge}, absent ici).
 *
 * <p>Desactive dans le portage Forge : plus declare comme composant, donc jamais construit. Le mod
 * n'annonce pas de mise a jour plutot que d'en annoncer une qui ne s'installe pas sous Forge.</p>
 */
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
