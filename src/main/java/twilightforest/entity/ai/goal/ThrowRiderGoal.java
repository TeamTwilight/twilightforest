package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.components.entity.YetiThrowAttachment;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataAttachments;
import twilightforest.network.MovePlayerPacket;
import twilightforest.tags.TFEntityTypeTags;

public class ThrowRiderGoal extends MeleeAttackGoal {

	private int throwTimer;

	public ThrowRiderGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	public boolean canUse() {
		return this.mob.getPassengers().isEmpty() &&
			this.mob.getTarget() != null &&
			!this.mob.getTarget().is(Tags.EntityTypes.BOSSES) &&
			super.canUse();
	}

	@Override
	public void start() {
		this.throwTimer = 10 + this.mob.getRandom().nextInt(30); // Wait 0.5 to 2 seconds before we throw the target
		super.start();
	}

	@Override
	public void tick() {
		if (!this.mob.getPassengers().isEmpty() && this.mob.getPassengers().getFirst() instanceof LivingEntity victim) {
			damageRider(victim);
			this.throwTimer--;
		} else
			super.tick();
	}

	private void damageRider(LivingEntity victim) {
		if (throwTimer > 0) {
			this.mob.doHurtTarget(getServerLevel(this.mob), victim);
		}
	}

	// Vanilla Copy with edits
	@Override
	protected void checkAndPerformAttack(LivingEntity victim) {
		if (this.canPerformAttack(victim) && isTimeToAttack() && this.mob.getPassengers().isEmpty()) {
			this.resetAttackCooldown();
			this.mob.swing(InteractionHand.MAIN_HAND);
			if (this.mob.getPassengers().isEmpty()) {
				var v = victim.getVehicle();

				if (v == null || !v.is(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING)) {
					// Pluck them from the boat, minecart, donkey, or whatever
					victim.stopRiding();

					victim.startRiding(this.mob, true, false);
				}
			}
		}
	}

	@Override
	public void stop() {
		if (!this.mob.getPassengers().isEmpty()) {
			Entity rider = this.mob.getPassengers().getFirst();
			HostileMountEvents.hostileDismount(rider);

			double throwVecModifier = 4.0D + this.mob.getRandom().nextGaussian() * (2.0 / 3.0);
			Vec3 throwVec = new Vec3(this.mob.getLookAngle().x() * throwVecModifier, 0.9, this.mob.getLookAngle().z() * throwVecModifier);

			rider.push(throwVec.x(), throwVec.y(), throwVec.z());

			if (rider instanceof Player player) {
				YetiThrowAttachment attachment = rider.getData(TFDataAttachments.YETI_THROWING);
				attachment.setThrown(player, true, this.mob);
			}

			if (rider instanceof ServerPlayer server) {
				PacketDistributor.sendToPlayer(server, new MovePlayerPacket(throwVec.x(), throwVec.y(), throwVec.z()));
			}
		}
		super.stop();
	}

	@Override
	public boolean canContinueToUse() {
		return (this.throwTimer > 0 && !this.mob.getPassengers().isEmpty()) || (super.canContinueToUse() && this.mob.getPassengers().isEmpty());
	}
}
