package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.PacketThrowPlayer;

public class EntityAITFThrowRider extends EntityAIAttackMelee {

	private int throwTimer;
	private int timeout;

	public EntityAITFThrowRider(EntityCreature par1EntityCreature, double speedIn, boolean useLongMemory) {
		super(par1EntityCreature, speedIn, useLongMemory);
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
	public void updateTask() {
		timeout--;
		if (!attacker.getPassengers().isEmpty())
			throwTimer--;
		else
			super.updateTask();
	}

	// Vanilla Copy with edits
	@Override
	protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_) {
		double d0 = this.getAttackReachSqr(p_190102_1_);

		if (p_190102_2_ <= d0 && this.attackTick <= 0) {
			this.attackTick = 20;
			this.attacker.swingArm(EnumHand.MAIN_HAND);
			if (attacker.getPassengers().isEmpty() && p_190102_1_.getRidingEntity() == null) {
				p_190102_1_.startRiding(attacker);
			}
		}
	}

	@Override
	public void resetTask() {
		if (!attacker.getPassengers().isEmpty()) {
			Entity rider = attacker.getPassengers().get(0);
			rider.dismountRidingEntity();

			Vec3d throwVec = attacker.getLookVec().scale(2);
			throwVec = new Vec3d(throwVec.x, 0.9, throwVec.z);

			rider.addVelocity(throwVec.x, throwVec.y, throwVec.z);

			if (rider instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) rider;

				IMessage message = new PacketThrowPlayer((float) throwVec.x, (float) throwVec.y, (float) throwVec.z);
				TFPacketHandler.CHANNEL.sendTo(message, player);
			}
		}
		super.resetTask();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (throwTimer > 0 && !attacker.getPassengers().isEmpty()) || (timeout > 0 && super.shouldContinueExecuting() && attacker.getPassengers().isEmpty());
	}

}
