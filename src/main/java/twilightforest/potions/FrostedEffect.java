package twilightforest.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class FrostedEffect extends MobEffect {
	public static final UUID MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9");
	public static final double FROST_MULTIPLIER = -0.15D;

	public FrostedEffect() {
		super(MobEffectCategory.HARMFUL, 0x56CBFD);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, FrostedEffect.MOVEMENT_SPEED_MODIFIER_UUID.toString(), FROST_MULTIPLIER, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity living, int amplifier) {
		living.setIsInPowderSnow(true);
		if (amplifier > 0 && living.canFreeze()) {
			living.setTicksFrozen(Math.min(living.getTicksRequiredToFreeze(), living.getTicksFrozen() + amplifier));
		}
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
}
