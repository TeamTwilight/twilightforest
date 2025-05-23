package twilightforest.entity.monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;

public class MistWolf extends HostileWolf {

	public MistWolf(EntityType<? extends MistWolf> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return HostileWolf.registerAttributes()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		if (super.doHurtTarget(level, entity)) {
			float myBrightness = this.level().getMaxLocalRawBrightness(this.blockPosition());

			if (entity instanceof LivingEntity && myBrightness < 0.10F) {
				int effectDuration = switch (this.level().getDifficulty()) {
					case EASY -> 0;
					case HARD -> 15;
					default -> 7;
				};

				if (effectDuration > 0 && !this.level().getBlockState(this.blockPosition()).isSolid()) {
					((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, effectDuration * 20, 0));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected SoundEvent getTargetSound() {
		return TFSounds.MIST_WOLF_TARGET.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MIST_WOLF_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.MIST_WOLF_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MIST_WOLF_DEATH.get();
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.6F;
	}
}
