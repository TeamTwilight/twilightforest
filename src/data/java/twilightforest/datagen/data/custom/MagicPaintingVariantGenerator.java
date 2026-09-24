package twilightforest.datagen.data.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import twilightforest.client.MagicPaintingAtlasInfo;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.List;

public class MagicPaintingVariantGenerator {

    public static void bootstrap(BootstrapContext<MagicPaintingVariant> context) {
        register(context, MagicPaintingVariants.DARKNESS, "Darkness", /* Name omitted by choice */ "???", 4, 2, MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, List.of(
            new MagicPaintingVariant.Layer("background", null, null, true, true),
            new MagicPaintingVariant.Layer("sky", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.01F, 128, 32), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.SINE_TIME, 0.03F, false, 0.0F, 1.0F), true, true),
            new MagicPaintingVariant.Layer("terrain", null, null, false, true),
            new MagicPaintingVariant.Layer("gems", null, null, true, true),
            new MagicPaintingVariant.Layer("gems", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DAY_TIME, 2.0F, true, 0.0F, 1.0F, 1, 23999), true, true),
            new MagicPaintingVariant.Layer("lightning", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.LIGHTNING, 1.0F, false, 0.0F, 1.0F), true, true),
            new MagicPaintingVariant.Layer("frame", null, null, false, false)
        ));
        HolderSet<Item> goldIngots = context.lookup(Registries.ITEM).getOrThrow(Tags.Items.INGOTS_GOLD);
        register(context, MagicPaintingVariants.LUCID_LANDS, "Lucid Lands", "Androsa", 3, 3, MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, List.of(
            new MagicPaintingVariant.Layer("background", null, null, true, true),
            new MagicPaintingVariant.Layer("clouds", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.LINEAR_TIME, 0.00075F, 122, 48), null, true, true),
            new MagicPaintingVariant.Layer("mookaite_mesa", null, null, true, true),
            new MagicPaintingVariant.Layer("agate_jungle", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.005F, 58, 48), null, true, true),
            new MagicPaintingVariant.Layer("crystal_plains", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.006F, 74, 48), null, true, true),
            new MagicPaintingVariant.Layer("background_gold", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.HOLDING_ITEM, 0.01F, false, 0.0F, 1.0F, goldIngots), true, true),
            new MagicPaintingVariant.Layer("clouds_gold", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.LINEAR_TIME, 0.00075F, 122, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.HOLDING_ITEM, 0.01F, false, 0.0F, 1.0F, goldIngots), true, true),
            new MagicPaintingVariant.Layer("golden_hills", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.HOLDING_ITEM, 0.01F, false, 0.0F, 1.0F, goldIngots), true, true),
            new MagicPaintingVariant.Layer("golden_forest", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.005F, 58, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.HOLDING_ITEM, 0.01F, false, 0.0F, 1.0F, goldIngots), true, true),
            new MagicPaintingVariant.Layer("golden_sands", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.006F, 74, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.HOLDING_ITEM, 0.01F, false, 0.0F, 1.0F, goldIngots), true, true),
            new MagicPaintingVariant.Layer("background_corrupt", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.MOB_EFFECT_CATEGORY, 0.01F, false, 0.0F, 1.0F, MobEffectCategory.HARMFUL), true, true),
            new MagicPaintingVariant.Layer("clouds_corrupt", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.LINEAR_TIME, 0.00075F, 122, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.MOB_EFFECT_CATEGORY, 0.01F, false, 0.0F, 1.0F, MobEffectCategory.HARMFUL), false, true),
            new MagicPaintingVariant.Layer("goldstone_peaks", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.MOB_EFFECT_CATEGORY, 0.01F, false, 0.0F, 1.0F, MobEffectCategory.HARMFUL), false, true),
            new MagicPaintingVariant.Layer("goldstone_peaks_overlay", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.MOB_EFFECT_CATEGORY, 0.01F, false, 0.0F, 1.0F, MobEffectCategory.HARMFUL), true, true),
            new MagicPaintingVariant.Layer("goldstone_forest", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.005F, 58, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.MOB_EFFECT_CATEGORY, 0.01F, false, 0.0F, 1.0F, MobEffectCategory.HARMFUL), false, true),
            new MagicPaintingVariant.Layer("goldstone_plains", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.006F, 74, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.MOB_EFFECT_CATEGORY, 0.01F, false, 0.0F, 1.0F, MobEffectCategory.HARMFUL), false, true),
            new MagicPaintingVariant.Layer("frame", null, null, false, false)
        ));
        register(context, MagicPaintingVariants.THE_HOSTILE_PARADISE, "The Hostile Paradise", "Oz", 3, 2, MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, List.of(
            new MagicPaintingVariant.Layer("1_background", null, null, true, true),
            new MagicPaintingVariant.Layer("2_distant_islands_par", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.02F, 86, 38), null, true, true),
            new MagicPaintingVariant.Layer("3_gold_dungeon_par", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.02F, 78, 36), null, true, true),
            new MagicPaintingVariant.Layer("4_fog_1_opa", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 4.0F, 12.0F), true, true),
            new MagicPaintingVariant.Layer("5_silver_dungeon_par", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.02F, 70, 36), null, true, true),
            new MagicPaintingVariant.Layer("6_fog_2_opa", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 5.0F, 15.0F), true, true),
            new MagicPaintingVariant.Layer("7_portal_par", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.02F, 62, 34), null, true, true),
            new MagicPaintingVariant.Layer("8_fog_3_opa", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 6.0F, 18.0F), true, true),
            new MagicPaintingVariant.Layer("9_field_par", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.02F, 54, 34), null, true, true),
            new MagicPaintingVariant.Layer("10_flowers_par", null, null, true, true),
            new MagicPaintingVariant.Layer("11_fog_4_opa", null, new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 12.0F, 24.0F), true, true),
            new MagicPaintingVariant.Layer("12_clouds_par", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.02F, 58, 32), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DISTANCE, 1.0F, false, 0.0F, 1.0F, 12.0F, 24.0F), true, true),
            new MagicPaintingVariant.Layer("13_frame", null, null, false, false)
        ));
        register(context, MagicPaintingVariants.CASTAWAY_PARADISE, "Castaway Paradise", "HexaBlu", 2, 4, MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, List.of(
            new MagicPaintingVariant.Layer("sunset", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.01F, 64, 64), null, true, true),
            new MagicPaintingVariant.Layer("sun", null, null, true, true),
            new MagicPaintingVariant.Layer("faraway_palm", null, null, false, true),
            new MagicPaintingVariant.Layer("ocean", null, null, false, true),
            new MagicPaintingVariant.Layer("deckchair", null, null, false, true),
            new MagicPaintingVariant.Layer("palm_tree", null, null, false, true),
            new MagicPaintingVariant.Layer("frame", null, null, false, false)
        ));
        register(context, MagicPaintingVariants.MUSIC_IN_THE_MIRE, "Music in the Mire", "TripleHeadedSheep", 4, 3, MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, List.of(
            new MagicPaintingVariant.Layer("bl_sky", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 88, 56), null, false, true),
            new MagicPaintingVariant.Layer("bl_roots", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 84, 52), null, true, true),
            new MagicPaintingVariant.Layer("bl_lightning", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 78, 52), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.LIGHTNING, 1.0F, false, 0.0F, 1.0F), false, true),
            new MagicPaintingVariant.Layer("bl_mid", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 76, 52), null, false, true),
            new MagicPaintingVariant.Layer("bl_mid_lightning", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 76, 52), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.LIGHTNING, 1.0F / 5.0F, false, 0.0F, 1.0F), false, true),
                new MagicPaintingVariant.Layer("bl_fog", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 76, 52), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.DAY_TIME, 0.25F, false, 0.0F, 1.0F, 22331, 2000), false, true),
            new MagicPaintingVariant.Layer("bl_foreground", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 74, 48), null, false, true),
            new MagicPaintingVariant.Layer("bl_foreground_lightning", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 74, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.LIGHTNING, 1.0F / 6.0F, false, 0.0F, 1.0F), false, true),
            new MagicPaintingVariant.Layer("bl_greeblings", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 74, 48), null, false, true),
            new MagicPaintingVariant.Layer("bl_greeblings_lightning", new MagicPaintingVariant.Layer.Parallax(MagicPaintingVariant.Layer.Parallax.Type.VIEW_ANGLE, 0.015F, 74, 48), new MagicPaintingVariant.Layer.OpacityModifier(MagicPaintingVariant.Layer.OpacityModifier.Type.LIGHTNING, 1.0F / 7.0F, false, 0.0F, 1.0F), false, true),
            new MagicPaintingVariant.Layer("bl_frame", null, null, false, false)
        ));
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(BootstrapContext<MagicPaintingVariant> context, ResourceKey<MagicPaintingVariant> key, String title, String author, int width, int height, Identifier backSprite, List<MagicPaintingVariant.Layer> layers) {
        Component titleComponent = Component.translatable(key.identifier().toLanguageKey("magic_painting", "title"));
        Component authorComponent = Component.translatable(key.identifier().toLanguageKey("magic_painting", "author"));
        MagicPaintingVariant variant = new MagicPaintingVariant(width * 16, height * 16, layers, titleComponent, authorComponent, backSprite);
        MagicPaintingVariants.MAGIC_PAINTING_ATLAS_HELPER.put(key.identifier(), variant);
        MagicPaintingVariants.MAGIC_PAINTING_LANG_HELPER.put(key.identifier(), Pair.of(title, author));
        context.register(key, variant);
    }

}
