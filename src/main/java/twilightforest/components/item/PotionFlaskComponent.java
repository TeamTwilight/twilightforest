package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.alchemy.PotionContents;

public record PotionFlaskComponent(PotionContents potion, int doses, int breakage, boolean breakable) {

	public static final PotionFlaskComponent EMPTY = new PotionFlaskComponent(PotionContents.EMPTY, 0, 0, true);
	public static final PotionFlaskComponent EMPTY_UNBREAKABLE = new PotionFlaskComponent(PotionContents.EMPTY, 0, 0, false);

	public static final Codec<PotionFlaskComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		PotionContents.CODEC.optionalFieldOf("potion", PotionContents.EMPTY).forGetter(PotionFlaskComponent::potion),
		Codec.INT.optionalFieldOf("doses", 0).forGetter(PotionFlaskComponent::doses),
		Codec.INT.optionalFieldOf("breakage", 0).forGetter(PotionFlaskComponent::breakage),
		Codec.BOOL.optionalFieldOf("breakable", true).forGetter(PotionFlaskComponent::breakable)
	).apply(instance, PotionFlaskComponent::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, PotionFlaskComponent> STREAM_CODEC = StreamCodec.composite(
		PotionContents.STREAM_CODEC, PotionFlaskComponent::potion,
		ByteBufCodecs.INT, PotionFlaskComponent::doses,
		ByteBufCodecs.INT, PotionFlaskComponent::breakage,
		ByteBufCodecs.BOOL, PotionFlaskComponent::breakable,
		PotionFlaskComponent::new);

	public PotionFlaskComponent tryAddDose(PotionContents potion) {
		return new PotionFlaskComponent(potion, this.doses() + 1, this.breakage(), this.breakable());
	}

	public PotionFlaskComponent removeDose() {
		int newDose = this.doses() - 1;
		return new PotionFlaskComponent(newDose < 1 ? PotionContents.EMPTY : this.potion(), newDose, this.breakable() ? this.breakage() + 1 : this.breakage(), this.breakable());
	}
}
