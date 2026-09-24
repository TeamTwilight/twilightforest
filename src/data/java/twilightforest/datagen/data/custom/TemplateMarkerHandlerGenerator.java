package twilightforest.datagen.data.custom;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.structures.markerhandler.*;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Map;

public class TemplateMarkerHandlerGenerator {

    public static void bootstrap(BootstrapContext<TemplateMarkerHandlerList> context) {
        BlockPlaceMarkerHandler campfireSeat = new BlockPlaceMarkerHandler(new WeightedStateProvider(WeightedList.<BlockState>builder()
            .add(TFBlocks.TWILIGHT_OAK_SLAB.value().defaultBlockState(), 1)
            .add(Blocks.AIR.defaultBlockState(), 3)
            .build()));

        DryingRackMarkerHandler armorRack = new DryingRackMarkerHandler(SimpleStateProvider.simple(TFBlocks.CANOPY_DRYING_RACK.value()), TFLootTables.CAMP_ARMOR_RACK);

        DryingRackMarkerHandler birchDryingRack = new DryingRackMarkerHandler(SimpleStateProvider.simple(TFBlocks.BIRCH_DRYING_RACK.value()), TFLootTables.CAMP_DRYING_RACK);

        PaintingMarkerHandler painting = new PaintingMarkerHandler(PaintingVariantTags.PLACEABLE);

        LootMarkerHandler tentPot = new LootMarkerHandler(BlockStateProvider.simple(Blocks.DECORATED_POT), TFLootTables.CAMP_POT);

        Map<String, Holder<TemplateMarkerHandler>> keyedHandlers = Map.of(
            "twilight_oak_slab", Holder.direct(campfireSeat),
            "camp_armor_rack", Holder.direct(armorRack),
            "birch_drying_rack", Holder.direct(birchDryingRack),
            "painting", Holder.direct(painting),
            "tent_pot",  Holder.direct(tentPot)
        );
        context.register(TemplateMarkerHandlers.CAMP_MARKER_HANDLERS, TemplateMarkerHandlerList.of(
            new RotationMarkerHandler(Holder.direct(new SwitchMarkerHandler(keyedHandlers)))
        ));
    }

}
