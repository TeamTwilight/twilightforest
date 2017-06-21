package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.TFPacketHandler;
import twilightforest.network.PacketThrowPlayer;

public class EntityAITFThrowRider extends EntityAIBase {
	private final EntityCreature theEntityCreature;
	private int throwTimer;

	public EntityAITFThrowRider(EntityCreature par1EntityCreature, float par2) {
		this.theEntityCreature = par1EntityCreature;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.theEntityCreature.getPassengers().isEmpty() || this.theEntityCreature.getRNG().nextInt(5) > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void startExecuting() {
		Entity rider = this.theEntityCreature.getPassengers().get(0);
		rider.dismountRidingEntity();

		Vec3d throwVec = this.theEntityCreature.getLookVec().scale(2);
		throwVec = new Vec3d(throwVec.xCoord, 0.9, throwVec.zCoord);

		rider.addVelocity(throwVec.xCoord, throwVec.yCoord, throwVec.zCoord);

		if (rider instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) rider;

			IMessage message = new PacketThrowPlayer((float) throwVec.xCoord, (float) throwVec.yCoord, (float) throwVec.zCoord);
			TFPacketHandler.CHANNEL.sendTo(message, player);
		}

		this.throwTimer = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.theEntityCreature.getPassengers().isEmpty()) {
			this.throwTimer++;
		}

		return this.throwTimer <= 40;
	}

}
