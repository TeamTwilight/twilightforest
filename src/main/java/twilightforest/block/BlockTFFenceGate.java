package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFFenceGate extends BlockFenceGate implements ModelRegisterCallback {
    public BlockTFFenceGate(BlockPlanks.EnumType plankEnum) {
        super(plankEnum);
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }
}
