package twilightforest.world.components.structures.camp;

import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Component;

@Component
public final class CampPieces {
	public final Identifier start = TwilightForestMod.prefix("camp/structure_start");
	public final Identifier tent = TwilightForestMod.prefix("camp/tent");
	public final Identifier rackPath = TwilightForestMod.prefix("camp/rack_path");
	public final Identifier mainPath = TwilightForestMod.prefix("camp/main_path");
	public final Identifier path = TwilightForestMod.prefix("camp/path");
	public final Identifier deco = TwilightForestMod.prefix("camp/deco");
	public final Identifier rack = TwilightForestMod.prefix("camp/rack");
}
