package twilightforest.capabilities.shield;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.network.PacketUpdateShield;
import twilightforest.network.TFPacketHandler;

public class ShieldCapabilityHandler implements IShieldCapability {

	private int temporaryShields;
	private int permamentShields;
	private EntityLivingBase host;
	private int timer = 0;

	@Override
	public void setEntity(EntityLivingBase entity) {
		host = entity;
	}

	@Override
	public void update() {
		if (!host.world.isRemote && shieldsLeft() > 0 && timer-- <= 0 && (!(host instanceof EntityPlayer) || !((EntityPlayer) host).isCreative()))
			breakShield();
	}

	@Override
	public int shieldsLeft() {
		return temporaryShields + permamentShields;
	}

	@Override
	public int temporaryShieldsLeft() {
		return temporaryShields;
	}

	@Override
	public int permamentShieldsLeft() {
		return permamentShields;
	}

	@Override
	public void breakShield() {
		// Temp shields should break first before permanent ones. Reset time each time a temp shield is busted.
		if (temporaryShields > 0) {
			temporaryShields--;
			timer = 240;
		} else if (permamentShields > 0) {
			permamentShields--;
		}

		host.world.playSound(null, host.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, ((host.getRNG().nextFloat() - host.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
		sendUpdatePacket();
	}

	@Override
	public void replenishShields() {
		setShields(5, true);
		host.world.playSound(null, host.getPosition(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1.0F, (host.getRNG().nextFloat() - host.getRNG().nextFloat()) * 0.2F + 1.0F);
	}

	@Override
	public void setShields(int amount, boolean temp) {
		if (temp) {
			temporaryShields = Math.max(amount, 0);
			timer = 240;
		} else {
			permamentShields = Math.max(amount, 0);
		}

		sendUpdatePacket();
	}

	@Override
	public void addShields(int amount, boolean temp) {
		if (temp) {
			if (temporaryShields <= 0)
				timer = 240; // Since we add new shields to the stack instead of setting them, no timer reset is needed, unless they start from 0 shields.
			temporaryShields += amount;
		} else {
			permamentShields += amount;
		}

		sendUpdatePacket();
	}

	private void sendUpdatePacket() {
		if (!host.world.isRemote) {
			IMessage message = new PacketUpdateShield(host, this);
			TFPacketHandler.CHANNEL.sendToAllTracking(message, host);
			// sendToAllTracking doesn't send to your own client so we need to send that as well.
			if (host instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) host;
				if (player.connection != null) {
					TFPacketHandler.CHANNEL.sendTo(message, player);
				}
			}
		}
	}
}
