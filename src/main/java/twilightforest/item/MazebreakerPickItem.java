package twilightforest.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.tags.TFBlockTags;

public class MazebreakerPickItem extends Item {
	public MazebreakerPickItem(Properties properties) {
		super(properties);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		return state.is(TFBlockTags.MAZEBREAKER_ACCELERATED) ? destroySpeed * 16.0F : destroySpeed;
	}
}