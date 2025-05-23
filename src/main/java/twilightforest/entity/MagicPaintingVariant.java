package twilightforest.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.init.custom.MagicPaintingVariants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public record MagicPaintingVariant(int width, int height, List<Layer> layers, Component author, ResourceLocation backTexture) {
	public static final Codec<MagicPaintingVariant> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
		ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(MagicPaintingVariant::width),
		ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(MagicPaintingVariant::height),
		ExtraCodecs.nonEmptyList(Layer.CODEC.listOf()).fieldOf("layers").forGetter(MagicPaintingVariant::layers),
		ComponentSerialization.CODEC.fieldOf("author").forGetter(MagicPaintingVariant::author),
		ResourceLocation.CODEC.fieldOf("back_texture").forGetter(MagicPaintingVariant::backTexture)
	).apply(recordCodecBuilder, MagicPaintingVariant::new));

	public static Optional<MagicPaintingVariant> getVariant(@Nullable HolderLookup.Provider regAccess, String id) {
		return getVariant(regAccess, ResourceLocation.withDefaultNamespace(id));
	}

	public static Optional<MagicPaintingVariant> getVariant(@Nullable HolderLookup.Provider regAccess, ResourceLocation id) {
		return getVariant(regAccess, ResourceKey.create(TFRegistries.Keys.MAGIC_PAINTINGS, id));
	}

	public static Optional<MagicPaintingVariant> getVariant(@Nullable HolderLookup.Provider regAccess, ResourceKey<MagicPaintingVariant> id) {
		return regAccess == null ? Optional.empty() : regAccess.lookup(TFRegistries.Keys.MAGIC_PAINTINGS).flatMap(reg -> reg.get(id)).map(Holder.Reference::value);
	}

	public static String getVariantId(RegistryAccess regAccess, MagicPaintingVariant variant) {
		return getVariantResourceLocation(regAccess, variant).toString();
	}

	public static ResourceLocation getVariantResourceLocation(RegistryAccess regAccess, MagicPaintingVariant variant) {
		return regAccess.lookup(TFRegistries.Keys.MAGIC_PAINTINGS).map(reg -> reg.getKey(variant)).orElse(MagicPaintingVariants.DEFAULT.location());
	}

	public record Layer(String path, @Nullable Parallax parallax, @Nullable OpacityModifier opacityModifier, boolean fullbright, boolean localLighting) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			ExtraCodecs.NON_EMPTY_STRING.fieldOf("path").forGetter(Layer::path),
			Parallax.CODEC.optionalFieldOf("parallax").forGetter((layer) -> Optional.ofNullable(layer.parallax())),
			OpacityModifier.CODEC.optionalFieldOf("opacity_modifier").forGetter((layer) -> Optional.ofNullable(layer.opacityModifier())),
			Codec.BOOL.fieldOf("fullbright").orElse(false).forGetter(Layer::fullbright),
			Codec.BOOL.fieldOf("local_lighting").orElse(false).forGetter(Layer::localLighting)
		).apply(recordCodecBuilder, Layer::create));

		@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
		private static Layer create(String path, Optional<Parallax> parallax, Optional<OpacityModifier> opacityModifier, boolean fullbright, boolean localLighting) {
			return new Layer(path, parallax.orElse(null), opacityModifier.orElse(null), fullbright, localLighting);
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

		public record OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, float from, float to, ItemStack item, Optional<MobEffectCategory> effectCategory) {
			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max) {
				this(type, multiplier, invert, min, max, Float.NaN, Float.NaN, ItemStack.EMPTY, Optional.empty());
			}

			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, float from, float to) {
				this(type, multiplier, invert, min, max, from, to, ItemStack.EMPTY, Optional.empty());
			}

			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, ItemStack item) {
				this(type, multiplier, invert, min, max, Float.NaN, Float.NaN, item, Optional.empty());
			}

			public OpacityModifier(Type type, float multiplier, boolean invert, float min, float max, MobEffectCategory effectCategory) {
				this(type, multiplier, invert, min, max, Float.NaN, Float.NaN, ItemStack.EMPTY, Optional.of(effectCategory));
			}

			//Just so we can access MobEffectCategory in json
			public static final Codec<MobEffectCategory> MOB_EFFECT_CATEGORY_CODEC = Codec.stringResolver(MobEffectCategory::toString, MobEffectCategory::valueOf);

			public static final Codec<OpacityModifier> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				OpacityModifier.Type.CODEC.fieldOf("type").forGetter(OpacityModifier::type),
				ExtraCodecs.POSITIVE_FLOAT.fieldOf("multiplier").forGetter(OpacityModifier::multiplier),
				Codec.BOOL.fieldOf("invert").forGetter(OpacityModifier::invert),
				Codec.FLOAT.fieldOf("min").forGetter(OpacityModifier::min),
				ExtraCodecs.POSITIVE_FLOAT.fieldOf("max").forGetter(OpacityModifier::max),
				Codec.FLOAT.optionalFieldOf("from").forGetter((modifier) -> Float.isNaN(modifier.from()) ? Optional.empty() : Optional.of(modifier.from())),
				Codec.FLOAT.optionalFieldOf("to").forGetter((modifier) -> Float.isNaN(modifier.to()) ? Optional.empty() : Optional.of(modifier.to())),
				ItemStack.CODEC.optionalFieldOf("item_stack").forGetter((modifier) -> modifier.item().isEmpty() ? Optional.empty() : Optional.of(modifier.item())),
				MOB_EFFECT_CATEGORY_CODEC.optionalFieldOf("effect_category").forGetter((modifier) -> modifier.effectCategory().isEmpty() ? Optional.empty() : modifier.effectCategory())
			).apply(recordCodecBuilder, OpacityModifier::create));

			@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
			private static OpacityModifier create(Type type, float multiplier, boolean invert, float min, float max, Optional<Float> from, Optional<Float> to, Optional<ItemStack> item, Optional<MobEffectCategory> effectCategory) {
				if (type.usesRange() && (from.isEmpty() || to.isEmpty())) throw new NoSuchElementException("Range for opacity modifier is not defined!");
				return new OpacityModifier(type, multiplier, invert, min, max, from.orElse(Float.NaN), to.orElse(Float.NaN), item.orElse(ItemStack.EMPTY), effectCategory);
			}

			public enum Type implements StringRepresentable {
				DISTANCE("distance", true, true),
				WEATHER("weather", false, true),
				STORM("storm", false, true),
				LIGHTNING("lightning", false, false),
				DAY_TIME("day_time", true, true),
				SINE_TIME("sine_time", false, false),
				HEALTH("health", true, true),
				HUNGER("hunger", true, true),
				HOLDING_ITEM("holding_item", false, true),
				MOB_EFFECT_CATEGORY("mob_effect_category", false, true);

				static final Codec<OpacityModifier.Type> CODEC = StringRepresentable.fromEnum(OpacityModifier.Type::values);
				private final String name;
				private final boolean usesRange; // Is this modifier forced to have a defined range
				private final boolean toThePowerOfItsMultiplier; // Is this modifier's alpha calculated to the power of its multiplier value

				Type(String pName, boolean usesRange, boolean toThePowerOfItsMultiplier) {
					this.name = pName;
					this.usesRange = usesRange;
					this.toThePowerOfItsMultiplier = toThePowerOfItsMultiplier;
				}

				@Override
				public String getSerializedName() {
					return this.name;
				}

				public boolean usesRange() {
					return this.usesRange;
				}

				public boolean powerOfMultiplier() {
					return this.toThePowerOfItsMultiplier;
				}
			}
		}
	}
}
