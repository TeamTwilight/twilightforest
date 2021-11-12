package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.block.entity.TwilightChestEntity;

public class TwilightChest extends ChestBlock {
    public TwilightChest(Properties properties) {
        super(properties, TFBlockEntities.TF_CHEST::get);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TwilightChestEntity(pos, state);
    }
}
