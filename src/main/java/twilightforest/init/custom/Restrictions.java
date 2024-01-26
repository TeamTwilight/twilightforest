package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFStructures;
import twilightforest.util.Enforcement;
import twilightforest.util.PlayerHelper;
import twilightforest.util.Restriction;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Restrictions {
    public static final Codec<Holder<Restriction>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC, false);

    public static final ResourceKey<Restriction> DARK_FOREST = makeKey(TFBiomes.DARK_FOREST.location());
    public static final ResourceKey<Restriction> DARK_FOREST_CENTER = makeKey(TFBiomes.DARK_FOREST_CENTER.location());
    public static final ResourceKey<Restriction> FINAL_PLATEAU = makeKey(TFBiomes.FINAL_PLATEAU.location());
    public static final ResourceKey<Restriction> FIRE_SWAMP = makeKey(TFBiomes.FIRE_SWAMP.location());
    public static final ResourceKey<Restriction> GLACIER = makeKey(TFBiomes.GLACIER.location());
    public static final ResourceKey<Restriction> HIGHLANDS = makeKey(TFBiomes.HIGHLANDS.location());
    public static final ResourceKey<Restriction> SNOWY_FOREST = makeKey(TFBiomes.SNOWY_FOREST.location());
    public static final ResourceKey<Restriction> SWAMP = makeKey(TFBiomes.SWAMP.location());
    public static final ResourceKey<Restriction> THORNLANDS = makeKey(TFBiomes.THORNLANDS.location());

    private static ResourceKey<Restriction> makeKey(ResourceLocation name) {
        return ResourceKey.create(TFRegistries.Keys.RESTRICTIONS, name);
    }

    public static void bootstrap(BootstapContext<Restriction> context) {
        context.register(DARK_FOREST, new Restriction(TFStructures.KNIGHT_STRONGHOLD, Enforcements.DARKNESS.getKey(), 0.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(DARK_FOREST_CENTER, new Restriction(TFStructures.DARK_TOWER, Enforcements.DARKNESS.getKey(), 0.0F, asStack(TFBlocks.KNIGHT_PHANTOM_TROPHY), List.of(TwilightForestMod.prefix("progress_knights"))));
        context.register(FINAL_PLATEAU, new Restriction(TFStructures.FINAL_CASTLE, Enforcements.ACID_RAIN.getKey(), 1.5F, asStack(TFItems.LAMP_OF_CINDERS), List.of(TwilightForestMod.prefix("progress_troll"))));
        context.register(FIRE_SWAMP, new Restriction(TFStructures.HYDRA_LAIR, Enforcements.FIRE.getKey(), 8.0F, asStack(TFItems.MEEF_STROGANOFF), List.of(TwilightForestMod.prefix("progress_labyrinth"))));
        context.register(GLACIER, new Restriction(TFStructures.AURORA_PALACE, Enforcements.FROST.getKey(), 1.0F, asStack(TFItems.ALPHA_YETI_FUR), List.of(TwilightForestMod.prefix("progress_yeti"))));
        context.register(HIGHLANDS, new Restriction(TFStructures.TROLL_CAVE, Enforcements.ACID_RAIN.getKey(), 0.5F, asStack(TFBlocks.UBEROUS_SOIL), List.of(TwilightForestMod.prefix("progress_merge"))));
        context.register(SNOWY_FOREST, new Restriction(TFStructures.YETI_CAVE, Enforcements.FROST.getKey(), 0.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(SWAMP, new Restriction(TFStructures.LABYRINTH, Enforcements.HUNGER.getKey(), 1.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(THORNLANDS, new Restriction(TFStructures.FINAL_CASTLE, Enforcements.ACID_RAIN.getKey(), 1.0F, asStack(TFItems.LAMP_OF_CINDERS), List.of(TwilightForestMod.prefix("progress_troll"))));
    }

    public static ItemStack asStack(ItemLike itemLike) {
        return new ItemStack(itemLike);
    }
}
