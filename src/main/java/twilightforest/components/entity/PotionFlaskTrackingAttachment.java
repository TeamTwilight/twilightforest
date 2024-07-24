package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFAdvancements;

import java.util.Objects;

public class PotionFlaskTrackingAttachment {

	@Nullable
	private Holder<Potion> lastUsedPotion;
	private int dosesDrank;
	private long lastTimeStarted;

	public static final Codec<PotionFlaskTrackingAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.POTION.holderByNameCodec().optionalFieldOf("last_used_potion", null).forGetter(o -> o.lastUsedPotion),
			Codec.INT.fieldOf("doses_drank").forGetter(o -> o.dosesDrank),
			Codec.LONG.fieldOf("last_game_time_started").forGetter(o -> o.lastTimeStarted))
		.apply(instance, PotionFlaskTrackingAttachment::new));

	public PotionFlaskTrackingAttachment() {
		this(null, 0, 0);
	}

	public PotionFlaskTrackingAttachment(@Nullable Holder<Potion> lastUsedPotion, int dosesDrank, long timeStarted) {
		this.lastUsedPotion = lastUsedPotion;
		this.dosesDrank = dosesDrank;
		this.lastTimeStarted = timeStarted;
	}

	public void trackDrink(Holder<Potion> potion, ServerPlayer player) {
		//reset advancement window if potion changed. Otherwise, continue incrementing the doses
		if (this.lastUsedPotion == null || !Objects.equals(this.lastUsedPotion, potion)) {
			this.dosesDrank = 1;
			this.lastTimeStarted = player.level().getGameTime();
		} else {
			this.dosesDrank++;
		}
		this.lastUsedPotion = potion;

		if (player.isAlive()) {
			TFAdvancements.DRINK_FROM_FLASK.get().trigger(player, this.dosesDrank, Mth.floor((float) (player.level().getGameTime() - this.lastTimeStarted) / 20L), this.lastUsedPotion);
		}
	}
}
