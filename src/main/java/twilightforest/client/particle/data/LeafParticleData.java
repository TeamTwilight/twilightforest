package twilightforest.client.particle.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import twilightforest.init.TFParticleType;

import javax.annotation.Nonnull;

public class LeafParticleData implements ParticleOptions {
	public static MapCodec<LeafParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.fieldOf("r").forGetter((obj) -> obj.r),
		Codec.INT.fieldOf("g").forGetter((obj) -> obj.g),
		Codec.INT.fieldOf("b").forGetter((obj) -> obj.b)
	).apply(instance, LeafParticleData::new));

	public static StreamCodec<? super RegistryFriendlyByteBuf, LeafParticleData> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, p -> p.r,
		ByteBufCodecs.VAR_INT, p -> p.g,
		ByteBufCodecs.VAR_INT, p -> p.b,
		LeafParticleData::new
	);

	public final int r;
	public final int g;
	public final int b;

	public LeafParticleData(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Nonnull
	@Override
	public ParticleType<?> getType() {
		return TFParticleType.FALLEN_LEAF.get();
	}
}
