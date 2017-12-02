package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFLapisBlock extends Block implements ModelRegisterCallback {
    protected BlockTFLapisBlock() {
        super(Material.IRON);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TFItems.creativeTab);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
    }
}