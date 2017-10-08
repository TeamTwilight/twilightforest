package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatEntry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityTFMistWolf extends EntityTFHostileWolf implements ISavedCombatEntriesOnDeath {

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
	public boolean attackEntityAsMob(Entity par1Entity) {
		if (super.attackEntityAsMob(par1Entity)) {
			float myBrightness = this.getBrightness();

			if (par1Entity instanceof EntityLivingBase && myBrightness < 0.10F) {
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
					((EntityLivingBase) par1Entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, effectDuration * 20, 0));
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

	private ArrayList<CombatEntry> combatList;

	@Override
	public void sendEndCombat() {
		super.sendEndCombat();
		combatList = new ArrayList<>(this.getCombatTracker().combatEntries);
	}

	@Override
	public List<CombatEntry> getCombatList() {
		return combatList;
	}
}
