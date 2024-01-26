package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 * Just a dummy subclass to register the extra blockstate property.
 * See the comments on the superclass.
 */
public class ReappearingBlock extends VanishingBlock {

	public ReappearingBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false).setValue(VANISHED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(VANISHED);
	}
}
