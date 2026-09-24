package twilightforest.init.custom;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.TinyBirdVariant;

public class TinyBirdVariants {

	public static final ResourceKey<TinyBirdVariant> BLUE = makeKey(TwilightForestMod.prefix("blue"));
	public static final ResourceKey<TinyBirdVariant> BROWN = makeKey(TwilightForestMod.prefix("brown"));
	public static final ResourceKey<TinyBirdVariant> GOLD = makeKey(TwilightForestMod.prefix("gold"));
	public static final ResourceKey<TinyBirdVariant> RED = makeKey(TwilightForestMod.prefix("red"));

	private static ResourceKey<TinyBirdVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.TINY_BIRD_VARIANT, name);
	}

}
