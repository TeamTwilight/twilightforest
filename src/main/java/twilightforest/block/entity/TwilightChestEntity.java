package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TwilightChestEntity extends ChestBlockEntity {
    public TwilightChestEntity(BlockPos pos, BlockState state) {
        super(TFBlockEntities.TF_CHEST.get(), pos, state);
    }
}
