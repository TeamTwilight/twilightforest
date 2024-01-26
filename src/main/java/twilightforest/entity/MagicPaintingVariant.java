package twilightforest.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public record MagicPaintingVariant(int width, int height, List<Layer> layers,  String framePath) {
    public static final Codec<MagicPaintingVariant> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(MagicPaintingVariant::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(MagicPaintingVariant::height),
            ExtraCodecs.nonEmptyList(Layer.CODEC.listOf()).fieldOf("layers").forGetter(MagicPaintingVariant::layers),
            ExtraCodecs.NON_EMPTY_STRING.fieldOf("frame").forGetter(MagicPaintingVariant::framePath)
    ).apply(recordCodecBuilder, MagicPaintingVariant::new));

    public static Optional<MagicPaintingVariant> getVariant(RegistryAccess regAccess, String id) {
        return getVariant(regAccess, new ResourceLocation(id));
    }

    public static Optional<MagicPaintingVariant> getVariant(RegistryAccess regAccess, ResourceLocation id) {
        return regAccess.registry(TFRegistries.Keys.MAGIC_PAINTINGS).map(reg -> reg.get(id));
    }

    public static String getVariantId(RegistryAccess regAccess, MagicPaintingVariant variant) {
        return getVariantResourceLocation(regAccess, variant).toString();
    }

    public static ResourceLocation getVariantResourceLocation(RegistryAccess regAccess, MagicPaintingVariant variant) {
        return regAccess.registry(TFRegistries.Keys.MAGIC_PAINTINGS).map(reg -> reg.getKey(variant)).orElse(new ResourceLocation(MagicPainting.EMPTY));
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

        public record OpacityModifier(Type type, float multiplier, boolean invert, @Nullable ItemStack item) {
            public static final Codec<OpacityModifier> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
                    OpacityModifier.Type.CODEC.fieldOf("type").forGetter(OpacityModifier::type),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("multiplier").forGetter(OpacityModifier::multiplier),
                    Codec.BOOL.fieldOf("invert").forGetter(OpacityModifier::invert),
                    ItemStack.CODEC.optionalFieldOf("item_stack").forGetter((modifier) -> Optional.ofNullable(modifier.item()))
            ).apply(recordCodecBuilder, OpacityModifier::create));

            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
            private static OpacityModifier create(Type type, float multiplier, boolean invert, Optional<ItemStack> item) {
                return new OpacityModifier(type, multiplier, invert, item.orElse(null));
            }

            public enum Type implements StringRepresentable {
                DISTANCE("distance"),
                WEATHER("weather"),
                LIGHTNING("lightning"),
                DAY_TIME("day_time"),
                DAY_TIME_SHARP("day_time_sharp"),
                SINE_TIME("sine_time"),
                HEALTH("health"),
                HUNGER("hunger"),
                HOLDING_ITEM("holding_item");

                static final Codec<OpacityModifier.Type> CODEC = StringRepresentable.fromEnum(OpacityModifier.Type::values);
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
    }
}
