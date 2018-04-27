package twilightforest.potions;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twilightforest.TwilightForestMod;

@GameRegistry.ObjectHolder(TwilightForestMod.ID)
public class TFPotions {

	@GameRegistry.ObjectHolder("frosted")
	public static final Potion frosty;

	static {
		frosty = null;
	}

}
