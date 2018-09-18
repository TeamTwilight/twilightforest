package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFTrapDoor extends BlockTrapDoor implements ModelRegisterCallback {
    protected BlockTFTrapDoor(Material materialIn) {
        super(materialIn);
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
