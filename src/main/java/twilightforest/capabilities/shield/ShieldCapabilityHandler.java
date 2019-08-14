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
	private boolean dirty;

	@Override
	public void setEntity(EntityLivingBase entity) {
		host = entity;
	}

	@Override
	public void update() {
		if (!host.world.isRemote && shieldsLeft() > 0 && timer-- <= 0 && (!(host instanceof EntityPlayer) || !((EntityPlayer) host).isCreative()))
			breakShield();

		if (dirty) {
			sendUpdatePacket();
			dirty = false;
		}
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
		deferUpdatePacket();
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

		deferUpdatePacket();
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

		deferUpdatePacket();
	}

	private void deferUpdatePacket() {
		// FIX: https://github.com/TeamTwilight/twilightforest/issues/751
		// We may not be added to the world yet, in which case we need to defer the update packet until the
		// first tick of this entity (which can only occur if added the entity is a part of the world)
		if (this.host.isAddedToWorld()) {
			this.sendUpdatePacket();
		} else {
			this.dirty = true;
		}
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
