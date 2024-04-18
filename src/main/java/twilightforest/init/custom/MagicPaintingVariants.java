package twilightforest.init.custom;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.data.AtlasGenerator;
import twilightforest.data.LangGenerator;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.MagicPaintingVariant.Layer;

import java.util.List;

import static twilightforest.entity.MagicPaintingVariant.Layer.OpacityModifier;
import static twilightforest.entity.MagicPaintingVariant.Layer.Parallax;

public class MagicPaintingVariants {
    public static final Codec<Holder<MagicPaintingVariant>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC, false);

    public static final ResourceKey<MagicPaintingVariant> DARKNESS = makeKey(TwilightForestMod.prefix("darkness"));
    public static final ResourceKey<MagicPaintingVariant> LUCID_LANDS = makeKey(TwilightForestMod.prefix("lucid_lands"));
    public static final ResourceKey<MagicPaintingVariant> UNTITLED = makeKey(TwilightForestMod.prefix("untitled"));
    public static final ResourceKey<MagicPaintingVariant> CASTAWAY_PARADISE = makeKey(TwilightForestMod.prefix("castaway_paradise"));

    private static ResourceKey<MagicPaintingVariant> makeKey(ResourceLocation name) {
        return ResourceKey.create(TFRegistries.Keys.MAGIC_PAINTINGS, name);
    }

    public static void bootstrap(BootstapContext<MagicPaintingVariant> context) {
        register(context, DARKNESS, "Darkness", "???", 4, 2, List.of(
                new Layer("background", null, null, true),
                new Layer("sky", new Parallax(Parallax.Type.VIEW_ANGLE, 0.01F, 128, 32), new OpacityModifier(OpacityModifier.Type.SINE_TIME, 0.03F, false, 0.0F, 1.0F), true),
                new Layer("terrain", null, null, false),
                new Layer("gems", null, null, true),
                new Layer("gems", null, new OpacityModifier(OpacityModifier.Type.DAY_TIME, 2.0F, true, 0.0F, 1.0F, 1, 23999), true),
                new Layer("lightning", null, new OpacityModifier(OpacityModifier.Type.LIGHTNING, 1.0F, false, 0.0F, 1.0F), true),
                new Layer("frame", null, null, false)
        ));
        register(context, LUCID_LANDS, "Lucid Lands", "Androsa", 2, 2, List.of(
                new Layer("background", null, null, true),
                new Layer("clouds", new Parallax(Parallax.Type.SINE_TIME, 0.01F, 48, 32), null, true),
                new Layer("volcanic_lands", null, null, true),
                new Layer("agate_jungle", new Parallax(Parallax.Type.VIEW_ANGLE, 0.005F, 44, 32), null, true),
                new Layer("crystal_plains", new Parallax(Parallax.Type.VIEW_ANGLE, 0.006F, 58, 32), null, true),
                new Layer("frame", null, null, false)
        ));
        register(context, UNTITLED, "Untitled", "Oz", 3, 2, List.of(
                new Layer("1_background", null, null, false),
                new Layer("2_temple_par", new Parallax(Parallax.Type.VIEW_ANGLE, 0.005F, 50, 34), null, false),
                new Layer("3_fog_1_opa", null, new OpacityModifier(OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 4.0F, 8.0F), false),
                new Layer("4_portal_par", new Parallax(Parallax.Type.VIEW_ANGLE, 0.005F, 56, 36), null, false),
                new Layer("5_fog_2_opa", null, new OpacityModifier(OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 6.0F, 10.0F), false),
                new Layer("6_tree_par", new Parallax(Parallax.Type.VIEW_ANGLE, 0.005F, 62, 38), null, false),
                new Layer("7_fog_3_opa", null, new OpacityModifier(OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 8.0F, 12.0F), false),
                new Layer("8_foreground_par", new Parallax(Parallax.Type.VIEW_ANGLE, 0.005F, 70, 40), null, false),
                new Layer("9_frame", null, null, false)
        ));
        register(context, CASTAWAY_PARADISE, "Castaway Paradise", "HexaBlu", 2, 4, List.of(
                new Layer("sunset", new Parallax(Parallax.Type.VIEW_ANGLE, 0.01F, 64, 64), null, true),
                new Layer("sun", null, null, true),
                new Layer("faraway_palm", null, null, false),
                new Layer("ocean", null, null, false),
                new Layer("deckchair", null, null, false),
                new Layer("palm_tree", null, null, false),
                new Layer("frame", null, null, false)
        ));
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(BootstapContext<MagicPaintingVariant> context, ResourceKey<MagicPaintingVariant> key, String title, String author, int width, int height, List<Layer> layers) {
        MagicPaintingVariant variant = new MagicPaintingVariant(width * 16, height * 16, layers);
        AtlasGenerator.MAGIC_PAINTING_HELPER.put(key.location(), variant);
        LangGenerator.MAGIC_PAINTING_HELPER.put(key.location(), Pair.of(title, author));
        context.register(key, variant);
    }
}
