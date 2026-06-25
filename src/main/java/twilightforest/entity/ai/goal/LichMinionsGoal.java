package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.monster.LichMinion;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class LichMinionsGoal extends Goal {

	public static final float ATTACK_RANGE = 20.0F;
	private final Lich lich;

	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime;

	public LichMinionsGoal(Lich boss) {
		this.lich = boss;
		this.strafingTime = -1;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.lich.getPhase() == 2 && !this.lich.isShadowClone();
	}

	@Override
	public void start() {
		this.lich.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.ZOMBIE_SCEPTER.get()));
	}

	@Override
	public void stop() {
		super.stop();
		this.seeTime = 0;
	}

	@Override
	public void tick() {
		if (this.lich.getTeleportInvisibility() > 0) return;
		LivingEntity targetedEntity = this.lich.getTarget();
		if (targetedEntity == null) return;
		float dist = this.lich.distanceTo(targetedEntity);
		// spawn minions every so often
		if (this.lich.getAttackCooldown() % 15 == 0) this.checkAndSpawnMinions();

		boolean hasLineOfSight = this.lich.getSensing().hasLineOfSight(targetedEntity);
		if (hasLineOfSight != this.seeTime > 0) this.seeTime = 0;

		if (hasLineOfSight) ++this.seeTime;
        else --this.seeTime;

		if (dist < ATTACK_RANGE && this.seeTime >= 20) {
			this.lich.getNavigation().stop();
			++this.strafingTime;
		} else this.strafingTime = -1;

		if (this.strafingTime >= 20) {
			if ((double)this.lich.getRandom().nextFloat() < 0.3) this.strafingClockwise = !this.strafingClockwise;
			if ((double)this.lich.getRandom().nextFloat() < 0.3) this.strafingBackwards = !this.strafingBackwards;
			this.strafingTime = 0;
		}

		if (this.strafingTime > -1) {
			if (dist > (double)(ATTACK_RANGE * 0.75F)) this.strafingBackwards = false;
            else if (dist < (double)(ATTACK_RANGE * 0.25F)) this.strafingBackwards = true;

			this.lich.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
			this.lich.lookAt(targetedEntity, 30.0F, 30.0F);
		} else {
			this.lich.getLookControl().setLookAt(targetedEntity, 30.0F, 30.0F);
		}

		if (this.lich.getAttackCooldown() == 0) {
			if (dist < 2.0F) {
				// melee attack
				this.lich.doHurtTarget((ServerLevel) this.lich.level(), targetedEntity);
				this.lich.swing(InteractionHand.MAIN_HAND);
				this.lich.setAttackCooldown(20);
			} else if (dist < ATTACK_RANGE && this.lich.getSensing().hasLineOfSight(targetedEntity)) {
				if (this.lich.getNextAttackType() == 0) this.lich.launchProjectileAt(new LichBolt(this.lich.level(), this.lich));
				else this.lich.launchProjectileAt(new LichBomb(this.lich.level(), this.lich));

				this.lich.swing(InteractionHand.MAIN_HAND);
				this.lich.setNextAttackType(this.lich.getRandom().nextBoolean() ? 0 : 1);
				this.lich.setAttackCooldown(60);
			} else {
				// if not, teleport around
				this.lich.teleportToNewTarget(targetedEntity, ATTACK_RANGE, null);
				this.lich.setAttackCooldown(20);
			}
		}
	}

	private void checkAndSpawnMinions() {
		if (!this.lich.level().isClientSide() && this.lich.getMinionsToSummon() > 0) {
			int minions = this.lich.countMyMinions();

			// if not, spawn one!
			if (minions < Lich.MAX_ACTIVE_MINIONS) {
				this.spawnMinionAt();
				this.lich.setMinionsToSummon(this.lich.getMinionsToSummon() - 1);
			}
		}
	}

	private void spawnMinionAt() {
		// find a good spot
		LivingEntity targetedEntity = this.lich.getTarget();
		Vec3 minionSpot = this.lich.findVecInLOSOf(targetedEntity);

		if (minionSpot != null && this.lich.level() instanceof ServerLevelAccessor accessor) {
			// put a clone there
			LichMinion minion = new LichMinion(this.lich.level(), this.lich);
			minion.setPos(minionSpot.x(), minionSpot.y(), minionSpot.z());
			EventHooks.finalizeMobSpawn(minion, accessor, accessor.getCurrentDifficultyAt(BlockPos.containing(minionSpot)), EntitySpawnReason.MOB_SUMMONED, null);
			this.lich.level().addFreshEntity(minion);

			minion.setTarget(targetedEntity);

			minion.spawnAnim();
			minion.playSound(TFSounds.MINION_SUMMON.get(), 1.0F, ((this.lich.getRandom().nextFloat() - this.lich.getRandom().nextFloat()) * 0.7F + 1.0F) * 0.75F);

			this.lich.swing(InteractionHand.MAIN_HAND);
			// make sparkles leading to it
			this.lich.makeMagicTrail(this.lich.getEyePosition(), minion.getEyePosition(), this.lich.getRandom().nextFloat() * 0.0625F + 0.125F, this.lich.getRandom().nextFloat() * 0.0625F + 0.125F, this.lich.getRandom().nextFloat() * 0.0625F + 0.125F);
		}
	}

}
