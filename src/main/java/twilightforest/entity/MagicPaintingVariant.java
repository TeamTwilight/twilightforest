package twilightforest.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.init.custom.MagicPaintingVariants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public record MagicPaintingVariant(int width, int height, List<Layer> layers) {
	public static final Codec<MagicPaintingVariant> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
		ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(MagicPaintingVariant::width),
		ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(MagicPaintingVariant::height),
		ExtraCodecs.nonEmptyList(Layer.CODEC.listOf()).fieldOf("layers").forGetter(MagicPaintingVariant::layers)
	).apply(recordCodecBuilder, MagicPaintingVariant::new));

	public static Optional<MagicPaintingVariant> getVariant(@Nullable HolderLookup.Provider regAccess, String id) {
		return getVariant(regAccess, ResourceLocation.withDefaultNamespace(id));
	}

	public static Optional<MagicPaintingVariant> getVariant(@Nullable HolderLookup.Provider regAccess, ResourceLocation id) {
		return getVariant(regAccess, ResourceKey.create(TFRegistries.Keys.MAGIC_PAINTINGS, id));
	}

	public static Optional<MagicPaintingVariant> getVariant(@Nullable HolderLookup.Provider regAccess, ResourceKey<MagicPaintingVariant> id) {
		return regAccess == null ? Optional.empty() : regAccess.asGetterLookup().lookup(TFRegistries.Keys.MAGIC_PAINTINGS).flatMap(reg -> reg.get(id)).map(Holder.Reference::value);
	}

	public static String getVariantId(RegistryAccess regAccess, MagicPaintingVariant variant) {
		return getVariantResourceLocation(regAccess, variant).toString();
	}

	public static ResourceLocation getVariantResourceLocation(RegistryAccess regAccess, MagicPaintingVariant variant) {
		return regAccess.registry(TFRegistries.Keys.MAGIC_PAINTINGS).map(reg -> reg.getKey(variant)).orElse(MagicPaintingVariants.DEFAULT.location());
	}

	public record Layer(String path, @Nullable Parallax parallax, @Nullable OpacityModifier opacityModifier, boolean fullbright) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			ExtraCodecs.NON_EMPTY_STRING.fieldOf("path").forGetter(Layer::path),
			Parallax.CODEC.optionalFieldOf("parallax").forGetter((layer) -> Optional.ofNullable(layer.parallax())),
			OpacityModifier.CODEC.optionalFieldOf("opacity_modifier").forGetter((layer) -> Optional.ofNullable(layer.opacityModifier())),
			Codec.BOOL.fieldOf("fullbright").forGetter(Layer::fullbright)
		).apply(recordCodecBuilder, Layer::create));

		@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
		private static Layer create(String path, Optional<Parallax> parallax, Optional<OpacityModifier> opacityModifier, boolean fullbright) {
			return new Layer(path, parallax.orElse(null), opacityModifier.orElse(null), fullbright);
		}

		public record Parallax(Type type, float multiplier, int width, int height) {
			public static final Codec<Parallax> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Type.CODEC.fieldOf("type").forGetter(Parallax::type),
				Codec.FLOAT.fieldOf("multiplier").forGetter(Parallax::multiplier),
				ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(Parallax::width),
				ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Parallax::height)
			).apply(recordCodecBuilder, Parallax::new));

			public enum Type implements StringRepresentable {
				VIEW_ANGLE("view_angle"),
				LINEAR_TIME("linear_time"),
				SINE_TIME("sine_time");

				static final Codec<Parallax.Type> CODEC = StringRepresentable.fromEnum(Parallax.Type::values);
				private final String name;

				Type(String pName) {
					this.name = pName;
				}

				@Override
				public String getSerializedName() {
					return this.name;
				}
			}
		}

		public record OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, float from, float to, ItemStack item) {
			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max) {
				this(type, multiplier, invert, min, max, Float.NaN, Float.NaN, ItemStack.EMPTY);
			}

			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, float from, float to) {
				this(type, multiplier, invert, min, max, from, to, ItemStack.EMPTY);
			}

			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, ItemStack item) {
				this(type, multiplier, invert, min, max, Float.NaN, Float.NaN, item);
			}

			public static final Codec<OpacityModifier> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				OpacityModifier.Type.CODEC.fieldOf("type").forGetter(OpacityModifier::type),
				ExtraCodecs.POSITIVE_FLOAT.fieldOf("multiplier").forGetter(OpacityModifier::multiplier),
				Codec.BOOL.fieldOf("invert").forGetter(OpacityModifier::invert),
				Codec.FLOAT.fieldOf("min").forGetter(OpacityModifier::min),
				ExtraCodecs.POSITIVE_FLOAT.fieldOf("max").forGetter(OpacityModifier::max),
				Codec.FLOAT.optionalFieldOf("from").forGetter((modifier) -> Float.isNaN(modifier.from()) ? Optional.empty() : Optional.of(modifier.from())),
				Codec.FLOAT.optionalFieldOf("to").forGetter((modifier) -> Float.isNaN(modifier.to()) ? Optional.empty() : Optional.of(modifier.to())),
				ItemStack.CODEC.optionalFieldOf("item_stack").forGetter((modifier) -> modifier.item().isEmpty() ? Optional.empty() : Optional.of(modifier.item()))
			).apply(recordCodecBuilder, OpacityModifier::create));

			@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
			private static OpacityModifier create(Type type, float multiplier, boolean invert, float min, float max, Optional<Float> from, Optional<Float> to, Optional<ItemStack> item) {
				if (type.usesRange() && (from.isEmpty() || to.isEmpty())) throw new NoSuchElementException("Range for opacity modifier is not defined!");
				return new OpacityModifier(type, multiplier, invert, min, max, from.orElse(Float.NaN), to.orElse(Float.NaN), item.orElse(ItemStack.EMPTY));
			}

			public enum Type implements StringRepresentable {
				DISTANCE("distance", true),
				WEATHER("weather", false),
				STORM("storm", false),
				LIGHTNING("lightning", false),
				DAY_TIME("day_time", true),
				SINE_TIME("sine_time", false),
				HEALTH("health", true),
				HUNGER("hunger", true),
				HOLDING_ITEM("holding_item", false);

				static final Codec<OpacityModifier.Type> CODEC = StringRepresentable.fromEnum(OpacityModifier.Type::values);
				private final String name;
				private final boolean usesRange;

				Type(String pName, boolean usesRange) {
					this.name = pName;
					this.usesRange = usesRange;
				}

				@Override
				public String getSerializedName() {
					return this.name;
				}

				public boolean usesRange() {
					return this.usesRange;
				}
			}
		}
	}
}
