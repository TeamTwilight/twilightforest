package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;

public record MoonDialComponent(ResourceKey<Level> dimension, MoonPhase phase, DisplayMode mode) {
	public static final Codec<MoonDialComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceKey.codec(Registries.DIMENSION)
			.fieldOf("dimension")
			.forGetter(MoonDialComponent::dimension),
		MoonPhase.CODEC
			.fieldOf("phase")
			.forGetter(MoonDialComponent::phase),
		DisplayMode.CODEC
			.fieldOf("mode")
			.forGetter(MoonDialComponent::mode)
	).apply(instance, MoonDialComponent::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, MoonDialComponent> STREAM_CODEC = StreamCodec.composite(
		ResourceKey.streamCodec(Registries.DIMENSION),
		MoonDialComponent::dimension,
		ByteBufCodecs.VAR_INT.map(index -> MoonPhase.values()[index], MoonPhase::index),
		MoonDialComponent::phase,
		ByteBufCodecs.VAR_INT.map(index -> DisplayMode.values()[index], DisplayMode::ordinal),
		MoonDialComponent::mode,
		MoonDialComponent::new
	);

	public enum DisplayMode implements StringRepresentable {
		CURRENT_DIMENSION("current_dimension"),
		OVERWORLD("overworld");

		public static final Codec<DisplayMode> CODEC = StringRepresentable.fromEnum(DisplayMode::values);
		private final String name;

		DisplayMode(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}