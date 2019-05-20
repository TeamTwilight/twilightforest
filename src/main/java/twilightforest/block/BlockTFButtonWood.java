package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFButtonWood extends BlockButtonWood implements ModelRegisterCallback {
    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
