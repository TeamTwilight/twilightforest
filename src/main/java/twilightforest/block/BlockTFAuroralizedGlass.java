package twilightforest.block;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFAuroralizedGlass extends BlockGlass implements ModelRegisterCallback {
    public BlockTFAuroralizedGlass() {
        super(Material.ICE, false);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
