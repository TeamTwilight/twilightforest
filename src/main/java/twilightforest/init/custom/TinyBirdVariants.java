package twilightforest.init.custom;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.TinyBirdVariant;

public class TinyBirdVariants {

	public static final DeferredRegister<TinyBirdVariant> TINY_BIRD_VARIANTS = DeferredRegister.create(TFRegistries.Keys.TINY_BIRD_VARIANT, TwilightForestMod.ID);

	public static final DeferredHolder<TinyBirdVariant, TinyBirdVariant> BLUE = TINY_BIRD_VARIANTS.register("blue", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdblue.png")));
	public static final DeferredHolder<TinyBirdVariant, TinyBirdVariant> BROWN = TINY_BIRD_VARIANTS.register("brown", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdbrown.png")));
	public static final DeferredHolder<TinyBirdVariant, TinyBirdVariant> GOLD = TINY_BIRD_VARIANTS.register("gold", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdgold.png")));
	public static final DeferredHolder<TinyBirdVariant, TinyBirdVariant> RED = TINY_BIRD_VARIANTS.register("red", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdred.png")));
}
