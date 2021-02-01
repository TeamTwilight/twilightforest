package twilightforest.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;

public class EntityTFMistWolf extends EntityTFHostileWolf {

	public EntityTFMistWolf(EntityType<? extends EntityTFMistWolf> type, World world) {
		super(type, world);
		setCollarColor(DyeColor.GRAY);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return EntityTFHostileWolf.registerAttributes()
				.createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			float myBrightness = this.getBrightness();

			if (entity instanceof LivingEntity && myBrightness < 0.10F) {
				int effectDuration;
				switch (world.getDifficulty()) {
					case EASY:
						effectDuration = 0;
						break;
					default:
					case NORMAL:
						effectDuration = 7;
						break;
					case HARD:
						effectDuration = 15;
						break;
				}

				if (effectDuration > 0) {
					((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.BLINDNESS, effectDuration * 20, 0));
				}
			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void setAttackTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != getAttackTarget())
			playSound(TFSounds.MISTWOLF_TARGET, 4F, getSoundPitch());
		super.setAttackTarget(entity);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MISTWOLF_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.MISTWOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.MISTWOLF_DEATH;
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.6F;
	}
}
