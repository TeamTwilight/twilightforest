package twilightforest.util;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;

public interface IMapColorSupplier {
    default MapColor supplyMapColor() {
        return supplyPlankColor().getMapColor();
    }

    BlockPlanks.EnumType supplyPlankColor();
}
