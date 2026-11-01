package twilightforest.forge;

import net.minecraftforge.common.ToolAction;

/**
 * Les gestes d'outil que Forge ne connait pas.
 *
 * <p>Sa liste s'arrete a `FISHING_ROD_CAST` ; NeoForge en a d'autres, dont l'allumage, que le mod
 * utilise pour ses bougies. Un geste n'est qu'un nom : le declarer suffit, et le meme nom reste
 * partageable avec d'autres mods.</p>
 */
public final class TFToolActions {
	private TFToolActions() {}

	public static final ToolAction FIRESTARTER_LIGHT = ToolAction.get("firestarter_light");
}
