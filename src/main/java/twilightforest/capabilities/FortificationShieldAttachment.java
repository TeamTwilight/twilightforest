package twilightforest.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
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
	private int breakTimer;

	public FortificationShieldAttachment() {
		this(0, 0);
	}

	public FortificationShieldAttachment(int temporaryShields, int permanentShields) {
		this.temporaryShields = Math.max(temporaryShields, 0);
		this.permanentShields = Math.max(permanentShields, 0);
		this.resetTimer();
	}

	public void tick(LivingEntity entity) {
		if (!entity.level().isClientSide() && this.temporaryShieldsLeft() > 0 && this.timer-- <= 0 && this.breakTimer <= 0 && (!(entity instanceof Player player) || !player.getAbilities().invulnerable))
			this.breakShield(entity);
		if (this.breakTimer > 0)
			this.breakTimer--;
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

	public void breakShield(LivingEntity entity) {
		if (this.breakTimer <= 0) {
			// Temp shields should break first before permanent ones. Reset time each time a temp shield is busted.
			if (this.temporaryShields > 0) {
				this.temporaryShields--;
				this.resetTimer();
			} else if (this.permanentShields > 0) {
				this.permanentShields--;
			}

			if (entity instanceof ServerPlayer player)
				player.awardStat(TFStats.TF_SHIELDS_BROKEN.get());
			this.sendUpdatePacket(entity);
			entity.level().playSound(null, entity.blockPosition(), TFSounds.SHIELD_BREAK.get(), SoundSource.PLAYERS, 1.0F, ((entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.breakTimer = 20;
		}
	}

	public void replenishShields(LivingEntity entity) {
		this.setShields(entity, 5, true);
		entity.playSound(TFSounds.SHIELD_ADD.get(), 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
	}

	public void setShields(LivingEntity entity, int amount, boolean temp) {
		if (temp) {
			this.temporaryShields = Math.max(amount, 0);
			this.resetTimer();
		} else {
			this.permanentShields = Math.max(amount, 0);
		}

		this.sendUpdatePacket(entity);
	}

	public void addShields(LivingEntity entity, int amount, boolean temp) {
		if (temp) {
			if (this.temporaryShields <= 0)
				this.resetTimer(); // Since we add new shields to the stack instead of setting them, no timer reset is needed, unless they start from 0 shields.
			this.temporaryShields = Math.max(this.temporaryShields + amount, 0);
		} else {
			this.permanentShields = Math.max(this.permanentShields + amount, 0);
		}

		sendUpdatePacket(entity);
	}

	private void resetTimer() {
		this.timer = 240;
	}

	private void sendUpdatePacket(LivingEntity entity) {
		if (entity instanceof ServerPlayer)
			PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity).send(new UpdateShieldPacket(entity.getId(), this.temporaryShields, this.permanentShields));
	}
}
