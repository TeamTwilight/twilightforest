package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class PotionFlaskTrackingAttachment {

	private Optional<Holder<Potion>> lastUsedPotion;
	private int doses;
	private long lastTimeStarted;

	public static final Codec<PotionFlaskTrackingAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.POTION.holderByNameCodec().optionalFieldOf("last_used_potion").forGetter(o -> o.lastUsedPotion),
			Codec.INT.fieldOf("doses_drank").forGetter(o -> o.doses),
			Codec.LONG.fieldOf("last_game_time_started").forGetter(o -> o.lastTimeStarted))
		.apply(instance, PotionFlaskTrackingAttachment::new));

	public PotionFlaskTrackingAttachment() {
		this(Optional.empty(), 0, 0);
	}

	public PotionFlaskTrackingAttachment(Optional<Holder<Potion>> lastUsedPotion, int doses, long timeStarted) {
		this.lastUsedPotion = lastUsedPotion;
		this.doses = doses;
		this.lastTimeStarted = timeStarted;
	}

	public void incrementDoses(Optional<Holder<Potion>> potion, ServerPlayer player) {
		if (potion.isEmpty()) return;
		//reset advancement window if potion changed. Otherwise, continue incrementing the doses
		if (this.lastUsedPotion.isEmpty() || this.lastUsedPotion != potion) {
			this.doses = 1;
			this.lastTimeStarted = player.level().getGameTime();
		} else {
			this.doses++;
		}
		this.lastUsedPotion = potion;

		if (player.isAlive()) {
			TFAdvancements.DRINK_FROM_FLASK.get().trigger(player, this.doses, Mth.floor((float) (player.level().getGameTime() - this.lastTimeStarted) / 20L), this.lastUsedPotion);
		}
	}
}
