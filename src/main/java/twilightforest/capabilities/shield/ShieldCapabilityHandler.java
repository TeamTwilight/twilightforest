package twilightforest.capabilities.shield;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import twilightforest.TFSounds;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;
import twilightforest.util.TFStats;

public class ShieldCapabilityHandler implements IShieldCapability {
	private int temporaryShields;
	private int permanentShields;
	private LivingEntity host;
	private int timer;

	@Override
	public void setEntity(LivingEntity entity) {
		host = entity;
	}

	@Override
	public void update() {
		if (!host.level.isClientSide && shieldsLeft() > 0 && timer-- <= 0 && (!(host instanceof Player) || !((Player) host).isCreative()))
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

		if(host instanceof Player player && player instanceof ServerPlayer) player.awardStat(TFStats.TF_SHIELDS_BROKEN);
		host.level.playSound(null, host.blockPosition(), TFSounds.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, ((host.getRandom().nextFloat() - host.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
		sendUpdatePacket();
	}

	@Override
	public void replenishShields() {
		setShields(5, true);
		host.level.playSound(null, host.blockPosition(), TFSounds.SHIELD_ADD, SoundSource.PLAYERS, 1.0F, (host.getRandom().nextFloat() - host.getRandom().nextFloat()) * 0.2F + 1.0F);
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
		if (host instanceof ServerPlayer)
			TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> host), new UpdateShieldPacket(host, this));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("tempshields", this.temporaryShieldsLeft());
		tag.putInt("permshields", this.permanentShieldsLeft());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.initShields(tag.getInt("tempshields"), tag.getInt("permshields"));
	}
}
