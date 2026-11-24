package twilightforest.datagen.data.world;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import twilightforest.init.TFTrimMaterials;

public class TFTrimMaterialGenerator {

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, TFTrimMaterials.IRONWOOD, TFTrimMaterials.IRONWOOD_PALETTE, Style.EMPTY.withColor(7037281));
        register(context, TFTrimMaterials.STEELEAF, TFTrimMaterials.STEELEAF_PALETTE, Style.EMPTY.withColor(4814643));
        register(context, TFTrimMaterials.KNIGHTMETAL, TFTrimMaterials.KNIGHTMETAL_PALETTE, Style.EMPTY.withColor(8424562));
        register(context, TFTrimMaterials.FIERY, TFTrimMaterials.FIERY_PALETTE, Style.EMPTY.withColor(16758076));
        register(context, TFTrimMaterials.NAGA_SCALE, TFTrimMaterials.NAGA_SCALE_PALETTE, Style.EMPTY.withColor(2381586));
        register(context, TFTrimMaterials.CARMINITE, TFTrimMaterials.CARMINITE_PALETTE, Style.EMPTY.withColor(10092544));
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Identifier paletteId, Style color) {
        String descriptionId = Util.makeDescriptionId("trim_material", trimKey.identifier());
        Component materialComponent = Component.translatable(descriptionId).withStyle(color);
        TrimMaterial material = new TrimMaterial(paletteId, materialComponent);
        context.register(trimKey, material);
    }
}
