package twilightforest.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class PotionFlaskTrackingAttachment {

	private Optional<Potion> lastUsedPotion;
	private int doses;
	private long lastTimeStarted;

	public static final Codec<PotionFlaskTrackingAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					BuiltInRegistries.POTION.byNameCodec().optionalFieldOf("last_used_potion").forGetter(o -> o.lastUsedPotion),
					Codec.INT.fieldOf("doses_drank").forGetter(o -> o.doses),
					Codec.LONG.fieldOf("last_game_time_started").forGetter(o -> o.lastTimeStarted))
			.apply(instance, PotionFlaskTrackingAttachment::new));

	public PotionFlaskTrackingAttachment() {
		this(Optional.empty(), 0, 0);
	}

	public PotionFlaskTrackingAttachment(Optional<Potion> lastUsedPotion, int doses, long timeStarted) {
		this.lastUsedPotion = lastUsedPotion;
		this.doses = doses;
		this.lastTimeStarted = timeStarted;
	}

	public void incrementDoses(@Nullable Potion potion, ServerPlayer player) {
		//reset advancement window if potion changed. Otherwise, continue incrementing the doses
		if (this.lastUsedPotion.isEmpty() || this.lastUsedPotion.get() != potion) {
			this.doses = 1;
			this.lastTimeStarted = player.level().getGameTime();
		} else {
			this.doses++;
		}
		this.lastUsedPotion = Optional.ofNullable(potion);

		if (player.isAlive()) {
			TFAdvancements.DRINK_FROM_FLASK.get().trigger(player, this.doses, Mth.floor((float) (player.level().getGameTime() - this.lastTimeStarted) / 20L), this.lastUsedPotion.orElse(Potions.EMPTY));
		}
	}
}
