package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.item.effects.StackableEffectConsumeEffect;

public class TFConsumeEffects {

	public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECTS = DeferredRegister.create(Registries.CONSUME_EFFECT_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<ConsumeEffect.Type<?>, ConsumeEffect.Type<StackableEffectConsumeEffect>> STACKABLE_EFFECTS = CONSUME_EFFECTS.register("stackable_effects", () -> new ConsumeEffect.Type<>(StackableEffectConsumeEffect.CODEC, StackableEffectConsumeEffect.STREAM_CODEC));

}
