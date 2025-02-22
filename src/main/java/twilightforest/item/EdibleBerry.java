package twilightforest.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EdibleBerry extends Item {
	protected final BerryEffect[] effectsToApply;

	public EdibleBerry() {
		this(new BerryEffect[0]);
	}

	public EdibleBerry(BerryEffect... effects) {
		super(new Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.4f).fast().build()));
		effectsToApply = effects;
	}

	// We need custom effect application, so we can't use FoodProperties. Use the same thing as chorus fruit
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		if (!level.isClientSide) {
			applyBerryEffects(level, livingEntity);
		}

		return super.finishUsingItem(stack, level, livingEntity);
	}

	protected void applyBerryEffects(Level level, LivingEntity livingEntity) {
		for (BerryEffect effect : effectsToApply) {
			if (effect.chanceToApply >= level.random.nextFloat())
				applyBerryEffect(effect, livingEntity);
		}
	}

	protected void applyBerryEffect(BerryEffect berryEffect, LivingEntity livingEntity) {
		int currentDuration = 0;
		MobEffectInstance activeEffect = livingEntity.getEffect(berryEffect.effect);
		if (activeEffect != null) {
			currentDuration = activeEffect.getDuration();
		}
		livingEntity.addEffect(new MobEffectInstance(berryEffect.effect, currentDuration + berryEffect.extraDurationSeconds * 20, berryEffect.amplifier));
	}

	public record BerryEffect(Holder<MobEffect> effect, int extraDurationSeconds, int amplifier, float chanceToApply) {
		public BerryEffect(Holder<MobEffect> effect, int extraDurationSeconds, float chanceToApply) {
			this(effect, extraDurationSeconds, 0, chanceToApply);
		}

		public BerryEffect(Holder<MobEffect> effect, int extraDurationSeconds) {
			this(effect, extraDurationSeconds, 0, 1.0f);
		}
	}
}
