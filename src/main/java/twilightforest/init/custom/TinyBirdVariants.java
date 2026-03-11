package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.TinyBirdVariant;

public class TinyBirdVariants {

	public static final ResourceKey<TinyBirdVariant> BLUE = makeKey(TwilightForestMod.prefix("blue"));
	public static final ResourceKey<TinyBirdVariant> BROWN = makeKey(TwilightForestMod.prefix("brown"));
	public static final ResourceKey<TinyBirdVariant> GOLD = makeKey(TwilightForestMod.prefix("gold"));
	public static final ResourceKey<TinyBirdVariant> RED = makeKey(TwilightForestMod.prefix("red"));

	private static ResourceKey<TinyBirdVariant> makeKey(ResourceLocation name) {
		return ResourceKey.create(TFRegistries.Keys.TINY_BIRD_VARIANT, name);
	}

	public static void bootstrap(BootstrapContext<TinyBirdVariant> context) {
		context.register(BLUE, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdblue.png")));
		context.register(BROWN, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdbrown.png")));
		context.register(GOLD, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdgold.png")));
		context.register(RED, new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdred.png")));
	}
}
