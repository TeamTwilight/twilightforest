package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFDoor extends BlockDoor implements ModelRegisterCallback {
    protected BlockTFDoor(Material materialIn) {
        super(materialIn);
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
