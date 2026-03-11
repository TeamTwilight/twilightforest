package twilightforest.potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import twilightforest.TwilightForestMod;

import java.util.UUID;

public class FrostedEffect extends MobEffect {
	public static final ResourceLocation MOVEMENT_SPEED_MODIFIER = TwilightForestMod.prefix("frosted_slowdown");
	public static final double FROST_MULTIPLIER = -0.15D;

	@SuppressWarnings("this-escape")
	public FrostedEffect() {
		super(MobEffectCategory.HARMFUL, 0x56CBFD);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, FrostedEffect.MOVEMENT_SPEED_MODIFIER, FROST_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public boolean applyEffectTick(LivingEntity living, int amplifier) {
		living.setIsInPowderSnow(true);
		if (amplifier > 0 && living.canFreeze()) {
			living.setTicksFrozen(Math.min(living.getTicksRequiredToFreeze(), living.getTicksFrozen() + amplifier));
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
}
