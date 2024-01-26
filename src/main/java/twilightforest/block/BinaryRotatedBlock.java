package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * For blocks where their texture looks different based on horizontal axis; X or Z.
 * Rotated if Z axis, not rotated if X axis. Hence, "Binary".
 * <br>
 * - Drull
 */
public class BinaryRotatedBlock extends Block {
    public static final MapCodec<BinaryRotatedBlock> CODEC = simpleCodec(BinaryRotatedBlock::new);
    public static final BooleanProperty ROTATED = BooleanProperty.create("rotated");

    @Override
    public MapCodec<BinaryRotatedBlock> codec() {
        return CODEC;
    }

    public BinaryRotatedBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATED, context.getHorizontalDirection().getAxis() == Direction.Axis.Z);
    }
}
