package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import twilightforest.TwilightForestMod;

//TODO
// I would like to look at migrating the models to using EntityModelJson (https://www.curseforge.com/minecraft/mc-mods/entity-model-json) in the future.
// we can make the pack depend on it to load the new models instead of having them hardcoded here.
// could also shade the mod since I dont trust people to actually download the mod. I can already see the bug reports flooding in, yikes
public class JappaPackReloadListener implements ResourceManagerReloadListener {

	private static boolean jappaPackLoaded = false;
	public static final JappaPackReloadListener INSTANCE = new JappaPackReloadListener();

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		jappaPackLoaded = Minecraft.getInstance().getResourceManager().listPacks().anyMatch(pack -> pack.getResource(PackType.CLIENT_RESOURCES, TwilightForestMod.prefix("jappa_models.marker")) != null);
	}

	public boolean isJappaPackLoaded() {
		return jappaPackLoaded;
	}
}
