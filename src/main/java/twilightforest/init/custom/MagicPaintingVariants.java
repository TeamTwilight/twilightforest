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
import twilightforest.client.MagicPaintingTextureManager;
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

    private static ResourceKey<MagicPaintingVariant> makeKey(ResourceLocation name) {
        return ResourceKey.create(TFRegistries.Keys.MAGIC_PAINTINGS, name);
    }

    public static void bootstrap(BootstapContext<MagicPaintingVariant> context) {
        register(context, DARKNESS, "Darkness", "???", 64, 32, List.of(
                new Layer("background", null, null, true),
                new Layer("sky", new Parallax(Parallax.Type.VIEW_ANGLE, 0.01F, 128, 32), new OpacityModifier(OpacityModifier.Type.SINE_TIME, 0.03F, false, null), true),
                new Layer("terrain", null, null, false),
                new Layer("gems", null, null, true),
                new Layer("gems", null, new OpacityModifier(OpacityModifier.Type.DAY_TIME, 2.0F, true, null), true),
                new Layer("lightning", null, new OpacityModifier(OpacityModifier.Type.LIGHTNING, 1.0F, false, null), true)
        ), MagicPaintingTextureManager.FRAME_SPRITE_LOCATION.toString());
        register(context, LUCID_LANDS, "Lucid Lands", "Androsa", 32, 32, List.of(
                new Layer("background", null, null, true),
                new Layer("clouds", new Parallax(Parallax.Type.SINE_TIME, 0.01F, 48, 32), null, true),
                new Layer("volcanic_lands", null, null, true),
                new Layer("agate_jungle", new Parallax(Parallax.Type.VIEW_ANGLE, 0.005F, 44, 32), null, true),
                new Layer("crystal_plains", new Parallax(Parallax.Type.VIEW_ANGLE, 0.006F, 58, 32), null, true)
        ), MagicPaintingTextureManager.FRAME_SPRITE_LOCATION.toString());
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(BootstapContext<MagicPaintingVariant> context, ResourceKey<MagicPaintingVariant> key, String title, String author, int width, int height, List<Layer> layers, String frame) {
        MagicPaintingVariant variant = new MagicPaintingVariant(width, height, layers, frame);
        AtlasGenerator.MAGIC_PAINTING_HELPER.put(key.location(), variant);
        LangGenerator.MAGIC_PAINTING_HELPER.put(key.location(), Pair.of(title, author));
        context.register(key, variant);
    }
}
