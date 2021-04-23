package twilightforest.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.BlockState;

public class BlockTFNagastoneStairs extends StairsBlock {

	BlockTFNagastoneStairs(BlockState state) {
		super(() -> state, Properties.create(state.getMaterial()).setRequiresTool().hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));
	}

	//TODO: Reimplement
//	@Override
//	@Deprecated
//	public BlockState mirror(BlockState state, Mirror mirrorIn) {
//		return super.mirror(state, mirrorIn).with(DIRECTION, state.get(DIRECTION) == LeftRight.LEFT ? LeftRight.RIGHT : LeftRight.LEFT);
//	}
}
