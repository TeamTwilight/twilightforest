package twilightforest.init.custom;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.DwarfRabbitVariant;

public class DwarfRabbitVariants {
	public static final ResourceKey<DwarfRabbitVariant> BROWN = makeKey(TwilightForestMod.prefix("brown"));
	public static final ResourceKey<DwarfRabbitVariant> DUTCH = makeKey(TwilightForestMod.prefix("dutch"));
	public static final ResourceKey<DwarfRabbitVariant> WHITE = makeKey(TwilightForestMod.prefix("white"));

	private static ResourceKey<DwarfRabbitVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, name);
	}

}
