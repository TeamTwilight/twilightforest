package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFPressurePlate extends BlockPressurePlate implements ModelRegisterCallback {
    public BlockTFPressurePlate(Material wood, BlockPressurePlate.Sensitivity sensitivity) {
        super(wood, sensitivity);
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
