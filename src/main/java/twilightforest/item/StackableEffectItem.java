package twilightforest.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StackableEffectItem extends Item {
	protected final StackableEffectInstance[] effectsToApply;

	public StackableEffectItem() {
		this(new StackableEffectInstance[0]);
	}

	public StackableEffectItem(StackableEffectInstance... effects) {
		super(new Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.4F).fast().build()));
		this.effectsToApply = effects;
	}

	// We need custom effect application, so we can't use FoodProperties. Use the same thing as chorus fruit
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		if (!level.isClientSide()) {
			this.applyEffects(level, livingEntity);
		}

		return super.finishUsingItem(stack, level, livingEntity);
	}

	protected void applyEffects(Level level, LivingEntity livingEntity) {
		for (StackableEffectInstance effect : this.effectsToApply) {
			if (effect.chanceToApply() >= level.getRandom().nextFloat()) {
				this.applyOrStackEffect(effect, livingEntity);
			}
		}
	}

	protected void applyOrStackEffect(StackableEffectInstance effect, LivingEntity livingEntity) {
		int currentDuration = 0;
		MobEffectInstance activeEffect = livingEntity.getEffect(effect.effect());
		if (activeEffect != null) {
			currentDuration = activeEffect.getDuration();
		}
		livingEntity.addEffect(new MobEffectInstance(effect.effect(), currentDuration + effect.extraDurationTicks(), effect.amplifier()));
	}

	public record StackableEffectInstance(Holder<MobEffect> effect, int extraDurationTicks, int amplifier, float chanceToApply) {
		public StackableEffectInstance(Holder<MobEffect> effect, int extraDurationSeconds, float chanceToApply) {
			this(effect, extraDurationSeconds * 20, 0, chanceToApply);
		}

		public StackableEffectInstance(Holder<MobEffect> effect, int extraDurationSeconds) {
			this(effect, extraDurationSeconds * 20, 0, 1.0F);
		}
	}
}
