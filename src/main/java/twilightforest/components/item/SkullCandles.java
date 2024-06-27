package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SkullCandles(int color, int count) {
	public static final Codec<SkullCandles> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.INT.fieldOf("color").forGetter(SkullCandles::color),
		Codec.intRange(1, 4).fieldOf("count").forGetter(SkullCandles::count)
	).apply(inst, SkullCandles::new));

	public static final StreamCodec<? super RegistryFriendlyByteBuf, SkullCandles> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, SkullCandles::color,
		ByteBufCodecs.INT, SkullCandles::count,
		SkullCandles::new
	);

	public static final SkullCandles DEFAULT = new SkullCandles(0, 1);
}
