package twilightforest.capabilities.shield;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.network.PacketUpdateShield;
import twilightforest.network.TFPacketHandler;

public class ShieldCapabilityHandler implements IShieldCapability {

	private int shields;
	private EntityLivingBase host;

	@Override
	public void setEntity(EntityLivingBase entity) {
		host = entity;
	}

	@Override
	public void update() {
		if (!host.world.isRemote && shieldsLeft() > 0 && host.ticksExisted % 240 == 0)
			breakShield();
	}

	@Override
	public int shieldsLeft() {
		return shields;
	}

	@Override
	public void breakShield() {
		shields--;
		host.world.playSound(null, host.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, ((host.getRNG().nextFloat() - host.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
		sendUpdatePacket();
	}

	@Override
	public void replenishShields() {
		setShields(5);
		host.world.playSound(null, host.getPosition(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1.0F, (host.getRNG().nextFloat() - host.getRNG().nextFloat()) * 0.2F + 1.0F);
	}

	@Override
	public void setShields(int amount) {
		shields = amount;
		sendUpdatePacket();
	}

	private void sendUpdatePacket() {
		if (!host.world.isRemote) {
			IMessage message = new PacketUpdateShield(host, this);
			TFPacketHandler.CHANNEL.sendToAllAround(message, new NetworkRegistry.TargetPoint(host.dimension, host.posX, host.posY, host.posZ, 256));
		}
	}
}
