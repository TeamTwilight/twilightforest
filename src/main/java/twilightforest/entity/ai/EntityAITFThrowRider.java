package twilightforest.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.PacketThrowPlayer;

public class EntityAITFThrowRider extends MeleeAttackGoal {

	private int throwTimer;
	private int timeout;

	public EntityAITFThrowRider(CreatureEntity creature, double speedIn, boolean useLongMemory) {
		super(creature, speedIn, useLongMemory);
	}

	@Override
	public boolean shouldExecute() {
		return this.attacker.getPassengers().isEmpty() && super.shouldExecute();
	}

	@Override
	public void startExecuting() {
		this.throwTimer = 10 + attacker.getRNG().nextInt(30); // Wait 0.5 to 2 seconds before we throw the target
		timeout = 80 + attacker.getRNG().nextInt(40); // Lets only try to chase for around 4-6 seconds
		super.startExecuting();
	}

	@Override
	public void tick() {
		timeout--;
		if (!attacker.getPassengers().isEmpty())
			throwTimer--;
		else
			super.tick();
	}

	// Vanilla Copy with edits
	@Override
	protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
		double d0 = this.getAttackReachSqr(p_190102_1_);

		if (p_190102_2_ <= d0 && this.getSwingCooldown() <= 0) {
			this.resetSwingCooldown();
			this.attacker.swingArm(Hand.MAIN_HAND);
			if (attacker.getPassengers().isEmpty() && p_190102_1_.getRidingEntity() == null) {
				p_190102_1_.startRiding(attacker);
			}
		}
	}

	@Override
	public void resetTask() {
		if (!attacker.getPassengers().isEmpty()) {
			Entity rider = attacker.getPassengers().get(0);
			rider.stopRiding();

			Vector3d throwVec = attacker.getLookVec().scale(2);
			throwVec = new Vector3d(throwVec.x, 0.9, throwVec.z);

			rider.addVelocity(throwVec.x, throwVec.y, throwVec.z);

			if (rider instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) rider;

				PacketThrowPlayer message = new PacketThrowPlayer((float) throwVec.x, (float) throwVec.y, (float) throwVec.z);
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
			}
		}
		super.resetTask();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (throwTimer > 0 && !attacker.getPassengers().isEmpty()) || (timeout > 0 && super.shouldContinueExecuting() && attacker.getPassengers().isEmpty());
	}

}
