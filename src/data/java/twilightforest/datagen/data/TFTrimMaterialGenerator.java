package twilightforest.datagen.data;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import twilightforest.init.TFMaterialAssetGroup;
import twilightforest.init.TFTrimMaterials;

public class TFTrimMaterialGenerator {

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, TFTrimMaterials.IRONWOOD, TFMaterialAssetGroup.IRONWOOD, Style.EMPTY.withColor(7037281));
        register(context, TFTrimMaterials.STEELEAF, TFMaterialAssetGroup.STEELEAF, Style.EMPTY.withColor(4814643));
        register(context, TFTrimMaterials.KNIGHTMETAL, TFMaterialAssetGroup.KNIGHTMETAL, Style.EMPTY.withColor(8424562));
        register(context, TFTrimMaterials.FIERY, TFMaterialAssetGroup.FIERY, Style.EMPTY.withColor(16758076));
        register(context, TFTrimMaterials.NAGA_SCALE, TFMaterialAssetGroup.NAGA_SCALE, Style.EMPTY.withColor(2381586));
        register(context, TFTrimMaterials.CARMINITE, TFMaterialAssetGroup.CARMINITE, Style.EMPTY.withColor(10092544));
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, MaterialAssetGroup group, Style color) {
        String descriptionId = Util.makeDescriptionId("trim_material", trimKey.identifier());
        Component materialComponent = Component.translatable(descriptionId).withStyle(color);
        TrimMaterial material = new TrimMaterial(group, materialComponent);
        context.register(trimKey, material);
    }

}
