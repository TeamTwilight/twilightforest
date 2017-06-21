package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;

import java.util.List;


public class EntityTFLichMinion extends EntityZombie {
	EntityTFLich master;

	public EntityTFLichMinion(World par1World) {
		super(par1World);
		this.master = null;
	}

	public EntityTFLichMinion(World par1World, EntityTFLich entityTFLich) {
		super(par1World);
		this.master = entityTFLich;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		EntityLivingBase prevTarget = getAttackTarget();

		if (super.attackEntityFrom(par1DamageSource, par2)) {
			if (par1DamageSource.getEntity() instanceof EntityTFLich) {
				// return to previous target but speed up
				setRevengeTarget(prevTarget);
				addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 4));
				addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 1));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onLivingUpdate() {
		if (master == null) {
			findNewMaster();
		}
		// if we still don't have a master, or ours is dead, die.
		if (master == null || master.isDead) {
			this.setHealth(0);
		}
		super.onLivingUpdate();
	}

	private void findNewMaster() {
		List<EntityTFLich> nearbyLiches = world.getEntitiesWithinAABB(EntityTFLich.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).expand(32.0D, 16.0D, 32.0D));

		for (EntityTFLich nearbyLich : nearbyLiches) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewMinion(this)) {
				this.master = nearbyLich;

				// animate our new linkage!
				master.makeBlackMagicTrail(posX, posY + this.getEyeHeight(), posZ, master.posX, master.posY + master.getEyeHeight(), master.posZ);

				// become angry at our masters target
				setAttackTarget(master.getAttackTarget());

				// quit looking
				break;
			}
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
			((EntityPlayer) par1DamageSource.getSourceOfDamage()).addStat(TFAchievementPage.twilightHunter);
		}
	}
}
