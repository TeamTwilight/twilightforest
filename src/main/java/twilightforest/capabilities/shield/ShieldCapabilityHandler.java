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
	private int permanentShields;
	private EntityLivingBase host;
	private int timer;

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
		return temporaryShields + permanentShields;
	}

	@Override
	public int temporaryShieldsLeft() {
		return temporaryShields;
	}

	@Override
	public int permanentShieldsLeft() {
		return permanentShields;
	}

	@Override
	public void breakShield() {
		// Temp shields should break first before permanent ones. Reset time each time a temp shield is busted.
		if (temporaryShields > 0) {
			temporaryShields--;
			resetTimer();
		} else if (permanentShields > 0) {
			permanentShields--;
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
			resetTimer();
		} else {
			permanentShields = Math.max(amount, 0);
		}

		sendUpdatePacket();
	}

	@Override
	public void addShields(int amount, boolean temp) {
		if (temp) {
			if (temporaryShields <= 0)
				resetTimer(); // Since we add new shields to the stack instead of setting them, no timer reset is needed, unless they start from 0 shields.
			temporaryShields = Math.max(temporaryShields + amount, 0);
		} else {
			permanentShields = Math.max(permanentShields + amount, 0);
		}

		sendUpdatePacket();
	}

	void initShields(int temporary, int permanent) {
		temporaryShields = Math.max(temporary, 0);
		permanentShields = Math.max(permanent, 0);
		resetTimer();
	}

	private void resetTimer() {
		timer = 240;
	}

	private void sendUpdatePacket() {
		if (!host.world.isRemote) {
			IMessage message = new PacketUpdateShield(host, this);
			TFPacketHandler.CHANNEL.sendToAllTracking(message, host);
			// sendToAllTracking doesn't send to your own client so we need to send that as well.
			if (host instanceof EntityPlayerMP) {
				TFPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) host);
			}
		}
	}
}
