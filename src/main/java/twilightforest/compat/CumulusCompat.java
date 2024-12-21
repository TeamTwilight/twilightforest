package twilightforest.compat;

import com.aetherteam.cumulus.api.CumulusEntrypoint;
import com.aetherteam.cumulus.api.Menu;
import com.aetherteam.cumulus.api.MenuInitializer;
import com.aetherteam.cumulus.api.MenuRegisterCallback;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestTitleScreen;

@CumulusEntrypoint
public class CumulusCompat implements MenuInitializer {
	private static final ResourceLocation TWILIGHT_FOREST_ICON = ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "textures/gui/menu_api/menu_icon_twilight_forest.png");
	private static final Component TWILIGHT_FOREST_NAME = Component.translatable("twilightforest.menu_title.twilight_forest");
	public static final Menu TWILIGHT_FOREST = new Menu(
		TWILIGHT_FOREST_ICON,
		TWILIGHT_FOREST_NAME,
		new TwilightForestTitleScreen(),
		new Menu.Properties()
			.music(TwilightForestTitleScreen.MENU)
			.panorama(new CubeMap(ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "textures/gui/title/panorama/panorama")))
	);

	@Override
	public void registerMenus(MenuRegisterCallback menuRegisterCallback) {
		menuRegisterCallback.registerMenu(ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "twilight_forest"), TWILIGHT_FOREST);
	}
}
