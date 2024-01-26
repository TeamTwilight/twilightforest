package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
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

	private final Lich lich;

	public LichMinionsGoal(Lich boss) {
		this.lich = boss;
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
	public void tick() {
		LivingEntity targetedEntity = this.lich.getTarget();
		if (targetedEntity == null)
			return;
		float dist = this.lich.distanceTo(targetedEntity);
		// spawn minions every so often
		if (this.lich.getAttackCooldown() % 15 == 0) {
			this.checkAndSpawnMinions();
		}

		if (this.lich.getAttackCooldown() == 0) {
			if (dist < 2.0F) {
				// melee attack
				this.lich.doHurtTarget(targetedEntity);
				this.lich.setAttackCooldown(20);
			} else if (dist < 20F && this.lich.getSensing().hasLineOfSight(targetedEntity)) {
				if (this.lich.getNextAttackType() == 0) {
					this.lich.launchProjectileAt(new LichBolt(this.lich.level(), this.lich));
				} else {
					this.lich.launchProjectileAt(new LichBomb(this.lich.level(), this.lich));
				}

				this.lich.swing(InteractionHand.MAIN_HAND);
				this.lich.setNextAttackType(this.lich.getRandom().nextBoolean() ? 0 : 1);
				this.lich.setAttackCooldown(60);
			} else {
				// if not, teleport around
				this.lich.teleportToSightOfEntity(targetedEntity);
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
			EventHooks.onFinalizeSpawn(minion, accessor, this.lich.level().getCurrentDifficultyAt(BlockPos.containing(minionSpot)), MobSpawnType.MOB_SUMMONED, null, null);
			this.lich.level().addFreshEntity(minion);

			minion.setTarget(targetedEntity);

			minion.spawnAnim();
			minion.playSound(TFSounds.MINION_SUMMON.get(), 1.0F, ((this.lich.getRandom().nextFloat() - this.lich.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

			this.lich.swing(InteractionHand.MAIN_HAND);
			// make sparkles leading to it
			this.lich.makeMagicTrail(this.lich.getEyePosition(), minion.getEyePosition(), 0.0F, 0.0F, 0.0F);
		}
	}

}
