package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.network.UpdateShieldPacket;

public class FortificationShieldAttachment {

	public static final Codec<FortificationShieldAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("temporary_shields").forGetter(o -> o.temporaryShields),
			Codec.INT.fieldOf("permanent_shields").forGetter(o -> o.permanentShields))
		.apply(instance, FortificationShieldAttachment::new));

	private int temporaryShields;
	private int permanentShields;
	private int timer;

	public FortificationShieldAttachment() {
		this(0, 0);
	}

	public FortificationShieldAttachment(int temporaryShields, int permanentShields) {
		this.temporaryShields = Math.max(temporaryShields, 0);
		this.permanentShields = Math.max(permanentShields, 0);
		this.resetTimer();
	}

	public void tick(LivingEntity entity) {
		if (this.temporaryShieldsLeft() > 0 && !(entity instanceof Player player && player.getAbilities().invulnerable)) {
			if (this.timer <= 0) {
				this.breakShield(entity, true);
			} else if (this.checkLichCrownBonus(entity)) {
				// Timer decay skipped after every 2 ticks so that shields last 50% longer with Lich Crown worn
				this.timer--;
			}
		}
	}

	private boolean checkLichCrownBonus(LivingEntity entity) {
		//return entity.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.LICH_CROWN) ? (entity.tickCount % 3) != 0 : true;
		// Simplified but same logic as the line above
		return !entity.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN) || (entity.tickCount % 3) != 0;
	}

	public int shieldsLeft() {
		return this.temporaryShields + this.permanentShields;
	}

	public int temporaryShieldsLeft() {
		return this.temporaryShields;
	}

	public int permanentShieldsLeft() {
		return this.permanentShields;
	}

	public void breakShield(LivingEntity entity, boolean expired) {
		// Temp shields should break first before permanent ones. Reset time each time a temp shield is busted.
		if (this.temporaryShields > 0) {
			this.temporaryShields--;
			this.resetTimer();
		} else if (this.permanentShields > 0) {
			this.permanentShields--;
		}

		if (entity instanceof ServerPlayer player && !expired) {
			player.awardStat(TFStats.TF_SHIELDS_BROKEN.get());
		}

		this.sendUpdatePacket(entity);
		entity.level().playSound(null, entity.blockPosition(), expired ? TFSounds.SHIELD_EXPIRE.get() : TFSounds.SHIELD_BREAK.get(), SoundSource.PLAYERS, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.3F + 1.0F);
	}

	public void setShields(LivingEntity entity, int amount, boolean temp) {
		if (temp) {
			this.temporaryShields = Math.clamp(amount, 0, 115);
			this.resetTimer();
		} else {
			this.permanentShields = Math.clamp(amount, 0, 115);
		}

		this.sendUpdatePacket(entity);
	}

	public void addShields(LivingEntity entity, int amount, boolean temp) {
		if (temp) {
			if (this.temporaryShields <= 0) {
				this.resetTimer(); // Since we add new shields to the stack instead of setting them, no timer reset is needed, unless they start from 0 shields.
			}

			this.temporaryShields = Math.clamp(this.temporaryShields + amount, 0, 115);
		} else {
			this.permanentShields = Math.clamp(this.permanentShields + amount, 0, 115);
		}

		sendUpdatePacket(entity);
	}

	private void resetTimer() {
		this.timer = 240;
	}

	private void sendUpdatePacket(LivingEntity entity) {
		if (entity instanceof ServerPlayer)
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new UpdateShieldPacket(entity.getId(), this.temporaryShields, this.permanentShields));
	}
}
