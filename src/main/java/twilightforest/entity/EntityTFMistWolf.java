package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFMistWolf extends EntityTFHostileWolf {
	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/mist_wolf");

	public EntityTFMistWolf(World world) {
		super(world);
		this.setSize(1.4F, 1.9F);
		setCollarColor(EnumDyeColor.GRAY);
	}

	@Override
	protected void setAttributes() {
		super.setAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			float myBrightness = this.getBrightness();

			if (entity instanceof EntityLivingBase && myBrightness < 0.10F) {
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
					((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, effectDuration * 20, 0));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.6F;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
