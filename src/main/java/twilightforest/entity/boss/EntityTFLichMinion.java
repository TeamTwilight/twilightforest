package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityTFLichMinion extends EntityZombie {

	private static final DataParameter<Boolean> STRONG = EntityDataManager.createKey(EntityTFLichMinion.class, DataSerializers.BOOLEAN);

	EntityTFLich master;

	public EntityTFLichMinion(World world) {
		super(world);
		this.master = null;
	}

	public EntityTFLichMinion(World world, EntityTFLich entityTFLich) {
		super(world);
		this.master = entityTFLich;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(STRONG, false);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		EntityLivingBase prevTarget = getAttackTarget();

		if (super.attackEntityFrom(source, amount)) {
			if (source.getTrueSource() instanceof EntityTFLich) {
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
		List<EntityTFLich> nearbyLiches = world.getEntitiesWithinAABB(EntityTFLich.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D));

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
	protected void onNewPotionEffect(PotionEffect effect) {
		super.onNewPotionEffect(effect);
		if (!world.isRemote && effect.getPotion() == MobEffects.STRENGTH) {
			dataManager.set(STRONG, true);
		}
	}

	@Override
	protected void onFinishedPotionEffect(PotionEffect effect) {
		super.onFinishedPotionEffect(effect);
		if (!world.isRemote && effect.getPotion() == MobEffects.STRENGTH) {
			dataManager.set(STRONG, false);
		}
	}

	public boolean isStrong() {
		return dataManager.get(STRONG);
	}
}
