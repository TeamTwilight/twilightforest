package twilightforest.client.particle.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import twilightforest.init.TFParticleType;

import javax.annotation.Nonnull;

public class LeafParticleData implements ParticleOptions {
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

	public static Codec<LeafParticleData> codecLeaf() {
		return RecordCodecBuilder.create((instance) -> instance.group(
				Codec.INT.fieldOf("r").forGetter((obj) -> obj.r),
				Codec.INT.fieldOf("g").forGetter((obj) -> obj.g),
				Codec.INT.fieldOf("b").forGetter((obj) -> obj.b))
				.apply(instance, LeafParticleData::new));
	}

	@Override
	public void writeToNetwork(@Nonnull FriendlyByteBuf buf) {
		buf.writeVarInt(r);
		buf.writeVarInt(g);
		buf.writeVarInt(b);
	}

	@Nonnull
	@Override
	public String writeToString() {
		return String.format("%d %d %d", r, g, b);
	}

	public static class Deserializer implements ParticleOptions.Deserializer<LeafParticleData> {
		@Nonnull
		@Override
		public LeafParticleData fromCommand(@Nonnull ParticleType<LeafParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
			reader.skipWhitespace();
			int r = reader.readInt();
			reader.skipWhitespace();
			int g = reader.readInt();
			reader.skipWhitespace();
			int b = reader.readInt();
			return new LeafParticleData(r, g, b);
		}

		@Nonnull
		@Override
		public LeafParticleData fromNetwork(@Nonnull ParticleType<LeafParticleData> type, FriendlyByteBuf buf) {
			return new LeafParticleData(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
		}
	}
}
