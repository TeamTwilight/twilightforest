package twilightforest.datagen.data.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFStructures;
import twilightforest.init.custom.Enforcements;
import twilightforest.init.custom.Restrictions;
import twilightforest.util.Restriction;

import java.util.List;

public class RestrictionGenerator {

    public static void bootstrap(BootstrapContext<Restriction> context) {
        context.register(Restrictions.DARK_FOREST, new Restriction(TFStructures.KNIGHT_STRONGHOLD, Enforcements.DARKNESS.getKey(), 0.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(Restrictions.DARK_FOREST_CENTER, new Restriction(TFStructures.DARK_TOWER, Enforcements.DARKNESS.getKey(), 0.0F, asStack(TFBlocks.KNIGHT_PHANTOM_TROPHY), List.of(TwilightForestMod.prefix("progress_knights"))));
        context.register(Restrictions.FINAL_PLATEAU, new Restriction(TFStructures.FINAL_CASTLE, Enforcements.ACID_RAIN.getKey(), 1.5F, asStack(TFItems.LAMP_OF_CINDERS), List.of(TwilightForestMod.prefix("progress_troll"))));
        context.register(Restrictions.FIRE_SWAMP, new Restriction(TFStructures.HYDRA_LAIR, Enforcements.FIRE.getKey(), 8.0F, asStack(TFItems.MEEF_STROGANOFF), List.of(TwilightForestMod.prefix("progress_labyrinth"))));
        context.register(Restrictions.GLACIER, new Restriction(TFStructures.AURORA_PALACE, Enforcements.FROST.getKey(), 1.0F, asStack(TFItems.ALPHA_YETI_FUR), List.of(TwilightForestMod.prefix("progress_yeti"))));
        context.register(Restrictions.HIGHLANDS, new Restriction(TFStructures.TROLL_CAVE, Enforcements.ACID_RAIN.getKey(), 0.5F, asStack(TFBlocks.UBEROUS_SOIL), List.of(TwilightForestMod.prefix("progress_merge"))));
        context.register(Restrictions.SNOWY_FOREST, new Restriction(TFStructures.YETI_CAVE, Enforcements.FROST.getKey(), 0.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(Restrictions.SWAMP, new Restriction(TFStructures.LABYRINTH, Enforcements.HUNGER.getKey(), 1.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(Restrictions.THORNLANDS, new Restriction(TFStructures.FINAL_CASTLE, Enforcements.ACID_RAIN.getKey(), 1.0F, asStack(TFItems.LAMP_OF_CINDERS), List.of(TwilightForestMod.prefix("progress_troll"))));
    }

    private static ItemStackTemplate asStack(DeferredBlock<? extends Block> blockHolder) {
        return new ItemStackTemplate(blockHolder.asItem());
    }

    private static ItemStackTemplate asStack(DeferredItem<? extends Item> itemHolder) {
        return new ItemStackTemplate(itemHolder);
    }

}
