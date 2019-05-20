package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFFence extends BlockFence implements ModelRegisterCallback {
    public BlockTFFence(Material materialIn, MapColor mapColorIn) {
        super(materialIn, mapColorIn);
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
